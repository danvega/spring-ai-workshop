package dev.danvega.workshop.guards;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FactCheckingGuardController {

    private final ChatClient chatClient;

    public FactCheckingGuardController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/fact-checking")
    public String factChecking() {
        var system = """
        You are a research assistant. You must follow these rules strictly:
        
        NEVER provide specific numbers, percentages, dates, or statistics unless you are 100% certain they are correct.
        
        For questions asking for:
        - Specific statistics or percentages → Always respond: "I cannot provide specific statistics without access to current data sources"
        - Product feature details → Always respond: "I don't have access to current product documentation"  
        - Research paper details → Always respond: "I cannot cite specific papers without verification"
        - Financial figures → Always respond: "I don't have access to current financial data"
        
        If you're even slightly uncertain about a fact, do not state it as fact.
        """;
        return chatClient.prompt()
                .system(system)
                .user("How many GitHub stars does the Spring Boot repository have as of today?")
                .call()
                .content();
    }
}
