package org.test.editor.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageParser {

    private final ObjectMapper objectMapper;

    public MessageParser() {
        this.objectMapper = new ObjectMapper();
    }

    public Message<?> parseMessage(String json) throws IOException {
        return objectMapper.readValue(json, Message.class);
    }
    public String parseMessage(Message messageObject) throws IOException {
        return objectMapper.writeValueAsString(messageObject);
    }
    public <T> Message<T> parseMessage(String json, Class<T> clazz) throws IOException {
        Message<?> rawMessage = objectMapper.readValue(json, Message.class);
        T data = objectMapper.convertValue(rawMessage.getData(), clazz);
        return new Message<>(rawMessage.getAction(), rawMessage.getTimestamp(), data);
    }

}