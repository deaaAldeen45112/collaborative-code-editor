package org.test.editor.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public class Message<T> {

    @JsonProperty("action")
    private String action;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("data")
    private T data;


    public Message() {
    }

    public Message(String action, String timestamp, T data) {
        this.action = action;
        this.timestamp = timestamp;
        this.data = data;
    }
    public Message(String action,T data) {
        this.action = action;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}

