# Evaluating AI Output

How do you test code whose output is not deterministic? These tests show three approaches. They all call the live OpenAI API, so they need `OPENAI_API_KEY` and can flake on rare occasions.

## The tests

- `StructuredOutputTest` sidesteps nondeterminism by asserting on structure, not content. The model returns an `Itinerary` record and the test checks the shape.
- `SentimentAnalysisTest` uses a classification task with a known answer. `ReviewService` maps a review onto the `Sentiment` enum, and the test asserts the expected value.
- `RelevancyEvaluatorTest` uses Spring AI's `RelevancyEvaluator`, an AI judge that scores whether a response is relevant to the question and context.

## Run them

```bash
./mvnw test
```

Spring AI also ships a `FactCheckingEvaluator` for catching hallucinations against reference documents. It works best with the specialized `bespoke-minicheck` model on a local Ollama. That model is a 4.7GB download, so this workshop skips it and sticks to tests anyone can run with just an OpenAI key.
