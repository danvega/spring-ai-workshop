package dev.danvega.workshop.evals;

import dev.danvega.workshop.output.Itinerary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(ChatClientTestConfig.class)
public class StructuredOutputTest {

    private ChatClient chatClient;

    @BeforeEach
    void setUp(@Autowired ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Test
    public void testGetItinerary() {
        String destination = "Cleveland, OH";
        var result = chatClient.prompt()
                .user(u -> {
                    u.text("What's a good vacation plan while I'm in {destination} for 3 days?");
                    u.param("destination", destination);
                })
                .call()
                .entity(Itinerary.class);

        assertNotNull(result, "The result should not be null.");
        assertNotNull(result.itinerary(), "Itinerary should not be null.");
        assertFalse(result.itinerary().isEmpty(), "Itinerary activities should not be empty.");
    }

}
