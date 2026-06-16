package com.web.webhook.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookRequestDto {

    private String object;
    private List<Entry> entry;

    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }

    public List<Entry> getEntry() { return entry; }
    public void setEntry(List<Entry> entry) { this.entry = entry; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        private String id;
        private List<Change> changes;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public List<Change> getChanges() { return changes; }
        public void setChanges(List<Change> changes) { this.changes = changes; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Change {
        private Value value;
        private String field;

        public Value getValue() { return value; }
        public void setValue(Value value) { this.value = value; }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        private List<Status> statuses;
        private List<Message> messages;

        public List<Status> getStatuses() { return statuses; }
        public void setStatuses(List<Status> statuses) { this.statuses = statuses; }

        public List<Message> getMessages() { return messages; }
        public void setMessages(List<Message> messages) { this.messages = messages; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {

        private String id;

        private String status;

        @JsonProperty("recipient_id")
        private String recipientId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(String recipientId) {
            this.recipientId = recipientId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String from;
        private String id;
        private String type;

        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}
