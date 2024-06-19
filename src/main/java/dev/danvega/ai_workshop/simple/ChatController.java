package dev.danvega.ai_workshop.simple;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    /**
     * A basic example of how to use the chat client to pass a message and call the LLM
     * @param message
     * @return
     */
    @GetMapping("/")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about Banks") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

    /**
     * Take in a topic as a request parameter and use that param in the user message
     * @param topic
     * @return
     */
    @GetMapping("/jokes-by-topic")
    public String jokesByTopic(@RequestParam String topic) {
        return chatClient.prompt()
                .user(u -> u.text("Tell me a joke about {topic}").param("topic",topic))
                .call()
                .content();
    }

    /**
     * What if you didn't want to get a String back, and you wanted the whole response?
     * @param message
     * @return
     */
    @GetMapping("jokes-with-response")
    public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about computers") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

}
