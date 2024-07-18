package dev.danvega.ai_workshop.stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/olympics")
public class Olympics {

    private static final Logger log = LoggerFactory.getLogger(Olympics.class);
    private final ChatClient chatClient;
    @Value("classpath:/docs/olympic-sports.txt")
    private Resource docsToStuffResource;
    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olympicSportsResource;

    public Olympics(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/2024")
    public String get2024OlympicSports(
            @RequestParam(value = "message", defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
            @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit
    ) throws IOException {
        String sports = docsToStuffResource.getContentAsString(Charset.defaultCharset());
        return chatClient.prompt()
                .user(u -> u
                        .text(olympicSportsResource)
                        .param("question", message)
                        .param("context", stuffit ? sports : "" ))
                .call()
                .content();
    }
}
