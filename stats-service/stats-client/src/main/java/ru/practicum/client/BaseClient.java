package ru.practicum.client;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseClient {
    public static HttpResponse<String> http(String start, String end, String host1, String uris)
            throws IOException, InterruptedException {

        String host = host1.split(":")[1].substring(2);//"localhost";
        String port = host1.split(":")[2]; //"9090";
        String uris1 = "/" + uris;
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(host1 + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .build();

        URI uriGet = uriComponents.expand(host, port).toUri();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriGet)
                .header("content-type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        return client.send(request, handler);
    }
}