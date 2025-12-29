package dev.danvega.workshop.evals;


public class SentimentResponse {
    private Sentiment sentiment;

    public SentimentResponse() {}

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }
}