package dev.danvega.workshop.tools.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final ChatClient chatClient;
    private final WeatherService weatherService;

    public WeatherController(ChatClient.Builder builder, WeatherService weatherService) {
        this.chatClient = builder.build();
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/alerts/{state}")
    public String getAlerts(@PathVariable String state) {
        return weatherService.getAlerts(state);
    }
}
