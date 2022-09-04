package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Message {
    private final Instant time;
    private final String name;
    private final String message;

    public Message(String name, String message) {
        this.time = Instant.now();
        this.name = name;
        this.message = message;
    }

    public Message(@JsonProperty("time") Instant time, @JsonProperty("name") String name,
                   @JsonProperty("message") String message) {
        this.time = time;
        this.name = name;
        this.message = message;
    }

    public Instant getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return time.toString() + " - " + name + ": " + message;
    }
}
