package com.web.webhook.dto.requestDto;

public class AutoReplyRequestDto {

    private String ruleName;

    private String keyword;

    private String replyText;

    private String status;

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}