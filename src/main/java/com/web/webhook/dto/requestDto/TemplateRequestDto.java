package com.web.webhook.dto.requestDto;

public class TemplateRequestDto {

    private String templateName;

    private String templateType;

    private String templateBody;

    private String status;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateBody() {
        return templateBody;
    }

    public void setTemplateBody(String templateBody) {
        this.templateBody = templateBody;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}