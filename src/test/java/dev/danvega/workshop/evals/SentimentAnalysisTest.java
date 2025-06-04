package dev.danvega.workshop.evals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SentimentAnalysisTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void testPositiveSentiment() {
        String positiveReview = "I absolutely love this product! It's fantastic, and I highly recommend it to everyone.";
        Sentiment result = reviewService.classifySentiment(positiveReview);
        assertEquals(Sentiment.POSITIVE, result, "The sentiment should be classified as POSITIVE.");
    }

    @Test
    void testNegativeSentiment() {
        String negativeReview = "This is the worst experience I've ever had. The product is terrible and broke immediately.";
        Sentiment result = reviewService.classifySentiment(negativeReview);
        assertEquals(Sentiment.NEGATIVE, result, "The sentiment should be classified as NEGATIVE.");
    }

    @Test
    void testNeutralSentiment() {
        String neutralReview = "The product is okay. It does what it says but nothing more.";
        Sentiment result = reviewService.classifySentiment(neutralReview);
        assertEquals(Sentiment.NEUTRAL, result, "The sentiment should be classified as NEUTRAL.");
    }

}
