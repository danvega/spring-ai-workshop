package dev.danvega.workshop.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VacationPlan {

    private final ChatClient chatClient;

    public VacationPlan(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/vacation/unstructured")
    public String vacationUnstructured() {
        return chatClient.prompt()
                .user("What's a good vacation plan while I'm in Montreal CA for 4 days?")
                .call()
                .content();
    }

    @GetMapping("/vacation/structured")
    public Itinerary vacationStructured() {
        return chatClient.prompt()
                .user("What's a good vacation plan while I'm in Montreal CA for 4 days?")
                .call()
                .entity(Itinerary.class);
    }

    record Activity(String activity, String location, String day, String time){}
    record Itinerary(List<Activity> itinerary) {}
}
