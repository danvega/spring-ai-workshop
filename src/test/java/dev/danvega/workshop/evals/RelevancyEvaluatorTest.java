package dev.danvega.workshop.evals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RelevancyEvaluatorTest {

    RelevancyEvaluator relevancyEvaluator;

    @BeforeEach
    void setUp(@Autowired ChatClient.Builder builder) {
        this.relevancyEvaluator = new RelevancyEvaluator(builder);
    }

    @Test
    void testRelevantResponse() {
        String userQuery = "What is the capital of France?";
        List<String> context = List.of(
                "France is a country in Western Europe known for its culture and cuisine.",
                "The capital city of France is Paris, which is also its largest city.",
                "French is the official language spoken in France."
        );

        String llmResponse = "The capital of France is Paris.";
        EvaluationRequest request = new EvaluationRequest(userQuery, contextStringsToDocuments(context), llmResponse);
        EvaluationResponse response = relevancyEvaluator.evaluate(request);
        assertTrue(response.isPass(), "Response should be relevant. Feedback: " + response.getFeedback());
    }

    @Test
    void testIrrelevantResponse() {
        String userQuery = "What is the capital of France?";
        List<String> context = List.of(
                "France is a country in Western Europe known for its culture and cuisine.",
                "The capital city of France is Paris, which is also its largest city.",
                "French is the official language spoken in France."
        );
        String llmResponse = "I enjoy eating Italian pasta."; // Clearly irrelevant
        EvaluationRequest request = new EvaluationRequest(userQuery, contextStringsToDocuments(context), llmResponse);
        EvaluationResponse response = relevancyEvaluator.evaluate(request);

        assertFalse(response.isPass(), "Response should be irrelevant. Feedback: " + response.getFeedback());
    }

    private List<Document> contextStringsToDocuments(List<String> contextStrings) {
        return contextStrings.stream()
                .map(Document::new)  // Assumes Document has a constructor that takes a String
                .toList();
    }

}
