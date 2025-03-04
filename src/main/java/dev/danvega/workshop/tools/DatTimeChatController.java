package dev.danvega.workshop.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatTimeChatController {

    private final ChatClient chatClient;

    public DatTimeChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/tools")
    public String tools() {
        return chatClient.prompt("What day is tomorrow?")
                .tools(new DatTimeTools())
                .call()
                .content();
    }

}
