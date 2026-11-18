package dev.danvega.workshop.talks;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Lab 2: one small endpoint that uses all three ideas together.
//   1. A system message that tells the model who it is and how to judge
//   2. A user prompt template with {title} and {description} filled in by param()
//   3. Structured output that maps the answer to the TalkReview record
@RestController
public class TalkReviewController {

    private final ChatClient chatClient;

    public TalkReviewController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
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
                    u.text("""
                            Review this conference talk submission.

                            Title: {title}

                            Description:
                            {description}
                            """);
                    u.param("title", talk.title());
                    u.param("description", talk.description());
                })
                .call()
                .entity(TalkReview.class);
    }

}
