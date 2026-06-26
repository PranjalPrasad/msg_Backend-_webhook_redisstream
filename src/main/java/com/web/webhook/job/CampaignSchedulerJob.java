package com.web.webhook.job;

import com.web.webhook.entity.Campaign;
import com.web.webhook.repository.CampaignRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class CampaignSchedulerJob implements Job {

    private final CampaignRepository campaignRepository;

    public CampaignSchedulerJob(
            CampaignRepository campaignRepository) {

        this.campaignRepository = campaignRepository;
    }

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        Long campaignId =
                context.getMergedJobDataMap()
                        .getLong("campaignId");

        Campaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException("Campaign Not Found"));

        /*
            Meta API Integration yaha hoga.

            Future Flow:

            1. Get Campaign
            2. Get Template
            3. Get Contact Group
            4. Get Contacts
            5. Redis Queue
            6. Meta Cloud API
            7. Update Status
        */

        campaign.setStatus("RUNNING");

        campaignRepository.save(campaign);

    }

}
