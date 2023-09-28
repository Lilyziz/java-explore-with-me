package ru.practicum.main.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.practicum.client.BaseClient;
import ru.practicum.client.HttpClient;
import ru.practicum.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class StatsServer {
    private static final HttpClient httpClient = new HttpClient();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String dateTime = LocalDateTime.now().format(dateTimeFormatter);
    private final ConfigClient configClient;

    public StatsServer(ConfigClient configClient) {
        this.configClient = configClient;
    }

    public void saveHit(HttpServletRequest request) throws IOException, InterruptedException {
        String host = configClient.getStatServerUrl();
        String uri = request.getRequestURI();
        String ip = request.getHeader("host").split(":")[0];
        EndpointHit endpointHit = new EndpointHit(null, "ewm-main-service", uri, ip, dateTime);

        httpClient.postHit(host, "{\"app\":\"ewm-main-service\"," +
                "\"uri\":\"" + uri + "\"," +
                "\"ip\":\"" + ip + "\"," +
                "\"timestamp\":\"" + dateTime + "\"}");
    }

    public Integer requeryViews(String uris) throws IOException, InterruptedException {
        String start = LocalDateTime.now().minusYears(1).format(dateTimeFormatter);
        String end = LocalDateTime.now().plusYears(1).format(dateTimeFormatter);

        String host = configClient.getStatServerUrl();

        HttpResponse<String> httpResponse1 = BaseClient.http(start, end, host, uris + "&unique=true");
        String jsonString = httpResponse1.body();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> map = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {
        });

        String jsonString2 = map.get(0).toString();
        int index = jsonString2.indexOf("hits=");

        return Integer.parseInt(jsonString2.substring(index + 5, jsonString2.length() - 1));
    }
}
