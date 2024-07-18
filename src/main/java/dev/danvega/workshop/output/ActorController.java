package dev.danvega.workshop.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ActorController {

    private final ChatClient chatClient;

    public ActorController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/films-string")
    public String getActorFilmsString() {
        return chatClient.prompt()
                .user("Generate a filmography for a Anthony Hopkins for the year 2010.")
                .call()
                .content();
    }

    @GetMapping("/films")
    public ActorFilms getActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for a Anthony Hopkins.")
                .call()
                .entity(ActorFilms.class);
    }

    @GetMapping("/films-list")
    public List<ActorFilms> listActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for the actors Denzel Washington, Leonardo DiCaprio and Tom Hanks")
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/films-by-actor")
    public ActorFilms getActorFilmsByName(@RequestParam String actor) {
        return chatClient.prompt()
                .user(u -> u.text("Generate a filmography for the actor {actor}").param("actor",actor))
                .call()
                .entity(ActorFilms.class);
    }

}
