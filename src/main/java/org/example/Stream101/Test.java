package org.example.Stream101;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class Test {

  public static void main(String[] args) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(
        PropertyNamingStrategy.SNAKE_CASE);
    String json = "{ \"event_category\" : \"Account\"}";
    Message m = objectMapper.readValue(json, Message.class);
    System.out.println(m.getEventCategory());
  }
}
