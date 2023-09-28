package ru.practicum.client;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient {
    String port = "9090";

    public HttpResponse<String> postHit(String host, EndpointHit hit) throws IOException, InterruptedException {

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(host + "/hit")
                .build();
        URI uriPost = uriComponents.expand(host, port).toUri();

        HttpRequest requestEvent = HttpRequest.newBuilder()
                .uri(uriPost)
                .headers(HttpHeaders.CONTENT_TYPE, "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(hit.toString()))
                .build();

        return java.net.http.HttpClient.newBuilder()
                .build()
                .send(requestEvent, HttpResponse.BodyHandlers.ofString());
    }
}