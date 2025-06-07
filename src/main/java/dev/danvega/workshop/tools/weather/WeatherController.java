package dev.danvega.workshop.tools.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final ChatClient chatClient;
    private final WeatherService weatherService;

    public WeatherController(ChatClient.Builder builder, WeatherService weatherService) {
        this.chatClient = builder.build();
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/alerts")
    public String getAlerts(@RequestParam String message) {
        return chatClient.prompt()
                .tools(weatherService)
                .user(message)
                .call()
                .content();
    }
}
