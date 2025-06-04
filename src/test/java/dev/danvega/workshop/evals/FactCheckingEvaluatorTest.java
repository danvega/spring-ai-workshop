package dev.danvega.workshop.evals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class FactCheckingEvaluatorTest {

    private static final String OLLAMA_IMAGE = "ollama/ollama:0.1.48";

    // Boot will expose http://<host>:<mappedPort>
    @Container
    @ServiceConnection(name = "spring.ai.ollama.base-url")
    static final OllamaContainer ollama = new OllamaContainer(OLLAMA_IMAGE);

    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    void setUp(@Autowired ChatClient.Builder builder) {
        factCheckingEvaluator = new FactCheckingEvaluator(builder);
    }

    @Test
    void passes_when_claim_is_true() {
        String contextDocument = """
                The Eiffel Tower is a wrought-iron lattice tower located on the Champ de Mars in Paris, France. \
                It was designed and built by Gustave Eiffel's company and completed in 1889 as the entrance to the \
                1889 World's Fair.""";
        String aiClaim = "The Eiffel Tower was completed in 1889.";

        // For FactCheckingEvaluator:
        // - userText: Can be the original user query, or the claim itself for simplicity if no original query.
        // - dataList: The list of context documents against which the claim is checked.
        // - responseContent: The AI's claim that needs fact-checking.
        EvaluationRequest request = new EvaluationRequest(
                "When was the Eiffel Tower completed?", // Example original user query
                List.of(new Document(contextDocument)),
                aiClaim
        );
        EvaluationResponse response = factCheckingEvaluator.evaluate(request);

        assertTrue(response.isPass(), "Claim should be factually correct. Feedback: " + response.getFeedback());
    }

    @Test
    void fails_when_claim_is_false() {
        String contextDocument = """
                The Eiffel Tower is a wrought-iron lattice tower located on the Champ de Mars in Paris, France. \
                It was designed and built by Gustave Eiffel's company and completed in 1889 as the entrance to the \
                1889 World's Fair.""";
        String aiClaim = "The Eiffel Tower is located in Berlin, Germany.";

        EvaluationRequest request = new EvaluationRequest(
                "Where is the Eiffel Tower?", // Example original user query
                List.of(new Document(contextDocument)),
                aiClaim
        );
        EvaluationResponse response = factCheckingEvaluator.evaluate(request);

        assertFalse(response.isPass(), "Claim should be factually incorrect. Feedback: " + response.getFeedback());
    }

}
