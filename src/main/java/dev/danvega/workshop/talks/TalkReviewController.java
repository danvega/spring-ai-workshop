package dev.danvega.workshop.talks;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Lab 2: one small endpoint that uses all three ideas together.
//   1. A user prompt template loaded from src/main/resources/prompts/talk-review.st
//   2. A system message that tells the model who it is and how to judge
//   3. Structured output that maps the answer to the TalkReview record
@RestController
public class TalkReviewController {

    private final ChatClient chatClient;
    private final Resource reviewPrompt;

    public TalkReviewController(ChatClient.Builder builder,
                                @Value("classpath:/prompts/talk-review.st") Resource reviewPrompt) {
        this.chatClient = builder.build();
        this.reviewPrompt = reviewPrompt;
    }

    @PostMapping("/talks/review")
    public TalkReview review(@RequestBody Talk talk) {
        var system = """
                You are a strict but fair reviewer on the QCon program committee.
                Score each talk from 1 to 10. Most talks score between 4 and 7.
                Be specific. Every improvement should be something the speaker can act on.
                """;

        return chatClient.prompt()
                .system(system)
                .user(u -> {
                    u.text(reviewPrompt);
                    u.param("title", talk.title());
                    u.param("description", talk.description());
                })
                .call()
                .entity(TalkReview.class);
    }

}
