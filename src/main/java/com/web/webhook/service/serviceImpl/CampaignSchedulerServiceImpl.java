package com.web.webhook.service.serviceImpl;
import com.web.webhook.dto.requestDto.RescheduleCampaignRequestDto;
import com.web.webhook.job.CampaignSchedulerJob;
import com.web.webhook.dto.requestDto.ScheduleCampaignRequestDto;
import com.web.webhook.dto.responseDto.ScheduleCampaignResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.entity.ScheduledCampaign;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.ScheduledCampaignRepository;
import com.web.webhook.service.CampaignSchedulerService;
import org.quartz.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignSchedulerServiceImpl
        implements CampaignSchedulerService {

    private final Scheduler scheduler;
    private final CampaignRepository campaignRepository;
    private final ScheduledCampaignRepository scheduledCampaignRepository;

    public CampaignSchedulerServiceImpl(
            Scheduler scheduler,
            CampaignRepository campaignRepository,
            ScheduledCampaignRepository scheduledCampaignRepository) {

        this.scheduler = scheduler;
        this.campaignRepository = campaignRepository;
        this.scheduledCampaignRepository = scheduledCampaignRepository;
    }

    @Override
    public ScheduleCampaignResponseDto scheduleCampaign(
            ScheduleCampaignRequestDto requestDto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String createdBy = authentication.getName();

        Campaign campaign = campaignRepository.findById(
                        requestDto.getCampaignId())
                .orElseThrow(() ->
                        new RuntimeException("Campaign Not Found"));

        try {

            String jobName =
                    "CAMPAIGN_" + campaign.getId();

            String jobGroup =
                    "WHATSAPP_CAMPAIGN";

            JobDetail jobDetail =
                    JobBuilder.newJob(
                                    CampaignSchedulerJob.class)
                            .withIdentity(
                                    jobName,
                                    jobGroup
                            )
                            .usingJobData(
                                    "campaignId",
                                    campaign.getId()
                            )
                            .build();

            Trigger trigger =
                    TriggerBuilder.newTrigger()
                            .withIdentity(
                                    "TRIGGER_" + campaign.getId(),
                                    jobGroup
                            )
                            .startAt(
                                    java.util.Date.from(
                                            requestDto.getScheduledTime()
                                                    .atZone(
                                                            ZoneId.systemDefault())
                                                    .toInstant()
                                    )
                            )
                            .build();

            scheduler.scheduleJob(jobDetail, trigger);

            ScheduledCampaign scheduledCampaign =
                    new ScheduledCampaign();

            scheduledCampaign.setCampaignId(
                    campaign.getId());

            scheduledCampaign.setJobName(
                    jobName);

            scheduledCampaign.setJobGroup(
                    jobGroup);

            scheduledCampaign.setScheduledTime(
                    requestDto.getScheduledTime());

            scheduledCampaign.setStatus(
                    "SCHEDULED");

            scheduledCampaign.setCreatedBy(
                    createdBy);

            scheduledCampaign.setCreatedAt(
                    LocalDateTime.now());

            scheduledCampaign.setUpdatedAt(
                    LocalDateTime.now());

            ScheduledCampaign saved =
                    scheduledCampaignRepository.save(
                            scheduledCampaign);

            campaign.setStatus("SCHEDULED");
            campaignRepository.save(campaign);

            return map(saved);

        } catch (SchedulerException e) {

            throw new RuntimeException(
                    e.getMessage());
        }

    }

    @Override
    public List<ScheduleCampaignResponseDto> getAllScheduledCampaigns() {

        String createdBy =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        return scheduledCampaignRepository
                .findByCreatedBy(createdBy)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());

    }

    @Override
    public ScheduleCampaignResponseDto getScheduledCampaign(
            Long id) {

        ScheduledCampaign campaign =
                scheduledCampaignRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Scheduled Campaign Not Found"));

        return map(campaign);

    }

    @Override
    public void cancelScheduledCampaign(
            Long id) {

        ScheduledCampaign scheduledCampaign =
                scheduledCampaignRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Scheduled Campaign Not Found"));

        try {

            scheduler.deleteJob(
                    JobKey.jobKey(
                            scheduledCampaign.getJobName(),
                            scheduledCampaign.getJobGroup()
                    )
            );

            scheduledCampaign.setStatus(
                    "CANCELLED");

            scheduledCampaign.setUpdatedAt(
                    LocalDateTime.now());

            scheduledCampaignRepository.save(
                    scheduledCampaign);

            Campaign campaign =
                    campaignRepository.findById(
                                    scheduledCampaign.getCampaignId())
                            .orElseThrow();

            campaign.setStatus(
                    "CANCELLED");

            campaignRepository.save(campaign);

        } catch (SchedulerException e) {

            throw new RuntimeException(
                    e.getMessage());

        }

    }

    private ScheduleCampaignResponseDto map(
            ScheduledCampaign campaign) {

        ScheduleCampaignResponseDto dto =
                new ScheduleCampaignResponseDto();

        dto.setId(campaign.getId());

        dto.setCampaignId(
                campaign.getCampaignId());

        dto.setJobName(
                campaign.getJobName());

        dto.setJobGroup(
                campaign.getJobGroup());

        dto.setScheduledTime(
                campaign.getScheduledTime());

        dto.setStatus(
                campaign.getStatus());

        return dto;

    }

    @Override
    public void rescheduleCampaign(
            Long id,
            RescheduleCampaignRequestDto requestDto) {

        ScheduledCampaign scheduledCampaign =
                scheduledCampaignRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Scheduled Campaign Not Found"));

        try {

            scheduler.deleteJob(
                    JobKey.jobKey(
                            scheduledCampaign.getJobName(),
                            scheduledCampaign.getJobGroup()
                    )
            );

            JobDetail jobDetail =
                    JobBuilder.newJob(CampaignSchedulerJob.class)
                            .withIdentity(
                                    scheduledCampaign.getJobName(),
                                    scheduledCampaign.getJobGroup()
                            )
                            .usingJobData(
                                    "campaignId",
                                    scheduledCampaign.getCampaignId()
                            )
                            .build();

            Trigger trigger =
                    TriggerBuilder.newTrigger()
                            .withIdentity(
                                    "TRIGGER_" + scheduledCampaign.getCampaignId(),
                                    scheduledCampaign.getJobGroup()
                            )
                            .startAt(
                                    java.util.Date.from(
                                            requestDto.getScheduledTime()
                                                    .atZone(
                                                            java.time.ZoneId.systemDefault())
                                                    .toInstant()
                                    )
                            )
                            .build();

            scheduler.scheduleJob(
                    jobDetail,
                    trigger
            );

            scheduledCampaign.setScheduledTime(
                    requestDto.getScheduledTime()
            );

            scheduledCampaign.setUpdatedAt(
                    LocalDateTime.now()
            );

            scheduledCampaign.setStatus(
                    "RESCHEDULED"
            );

            scheduledCampaignRepository.save(
                    scheduledCampaign
            );

        } catch (SchedulerException e) {

            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }

}
