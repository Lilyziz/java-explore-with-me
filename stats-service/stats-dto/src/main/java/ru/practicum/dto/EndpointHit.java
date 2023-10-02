package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    String app;
    String uri;
    String ip;
    String timestamp;

    @Override
    public String toString() {
        return String.format("{\"app\":\"%s\"," +
                "\"uri\":\"%s\"," +
                "\"ip\":\"%s\"," +
                "\"timestamp\":\"%s\"}",
                app, uri, ip, timestamp);
    }
}
