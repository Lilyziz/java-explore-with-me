package ru.practicum.main.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigClient {
    private final String statServerUrl;

    public ConfigClient(@Value("${stats-server.url}") String statServerUrl) {
        this.statServerUrl = statServerUrl;
    }

    public String getStatServerUrl() {
        return statServerUrl;
    }
}

