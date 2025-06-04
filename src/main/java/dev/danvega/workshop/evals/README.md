# Spring AI Evaluation Examples

This package provides examples of different evaluation techniques for AI models using the Spring AI framework. These tests demonstrate how to assess the quality and correctness of AI-generated responses in various scenarios.

## Prerequisites

Before running these tests, ensure you have the following installed and configured:

- Java (JDK 17 or later recommended)
- Maven or Gradle (to build the project and run tests)
- Docker Desktop (for running local LLM instances via Testcontainers)
- You will also need to pull the necessary LLM models into your local Ollama instance. Open your terminal and run:bash
  - ollama pull bespokeai/minicheck

(If you haven't used Ollama before, you might need to install it first and ensure it's running.)

## Overview of Tests

This project includes the following evaluation test examples:

1.  **Sentiment Analysis Test:** Demonstrates traditional unit testing for deterministic AI tasks.
2.  **Relevancy Evaluator Test:** Shows how to use an LLM to evaluate if an AI's response is relevant to the query and context.
3.  **Fact Checking Evaluator Test:** Illustrates using an LLM to verify the factual accuracy of an AI's claims against provided documents.

## Test Details

### 1. Sentiment Analysis Test

*   **Purpose:** This test evaluates an AI model's ability to perform sentiment classification (e.g., POSITIVE, NEGATIVE, NEUTRAL) on a given text. It's an example of testing deterministic AI tasks where expected outcomes are known and consistent.
*   **How it Works:**
    *   It uses the Spring AI `ChatClient` to send a text input to an LLM.
    *   The LLM is prompted to classify the sentiment and return a specific keyword (e.g., "POSITIVE").
    *   Spring AI's `.entity(Sentiment.class)` feature is used to automatically map the LLM's string response to a predefined Java `Sentiment` enum.
    *   The test asserts that the classified sentiment matches the expected sentiment for various inputs.
    *   A low `temperature` setting is typically used for the LLM to ensure consistent and predictable outputs for this type of task.
*   **How to Use/Run:**
    *   Locate the `SentimentAnalysisTest.java` file (or similarly named file).
    *   This is a standard JUnit 5 test. You can run it from your IDE or using your build tool (e.g., `mvn test`).
    *   The test methods will call a service method (e.g., `classifySentiment(String review)`) which interacts with the LLM.
    *   Assertions check if the returned `Sentiment` enum matches the expected value.

### 2. Relevancy Evaluator Test

*   **Purpose:** This test assesses whether an AI model's response is relevant to a user's query and any provided contextual information. This is particularly useful for evaluating Retrieval Augmented Generation (RAG) systems.
*   **How it Works:**
    *   It utilizes Spring AI's `RelevancyEvaluator`.
    *   The `RelevancyEvaluator` internally uses another LLM (the "evaluator model," e.g., `mistral` in this example) to judge the relevance.
    *   Testcontainers is used to automatically start and manage a local Ollama instance running the `mistral` model.
    *   An `EvaluationRequest` is created, containing the user query, a list of context documents, and the AI-generated response to be evaluated.
    *   The `RelevancyEvaluator` processes this request and returns an `EvaluationResponse`. The `isPass()` method of the response indicates if the AI's answer was deemed relevant.
*   **How to Use/Run:**
    *   Locate the `RelevancyEvaluatorDemoTest.java` file (or similarly named file).
    *   Ensure Docker Desktop is running.
    *   This JUnit 5 test uses `@Testcontainers`. When you run the test, it will automatically start an Ollama container.
    *   The test sets up a `RelevancyEvaluator` configured to use the Ollama-hosted `mistral` model.
    *   Test methods provide sample queries, contexts, and AI responses. Assertions check the `isPass()` status of the `EvaluationResponse`.

### 3. Fact Checking Evaluator Test

*   **Purpose:** This test verifies the factual accuracy of claims made in an AI's response against a set of provided reference documents. It helps detect factual inaccuracies and hallucinations.
*   **How it Works:**
    *   It employs Spring AI's `FactCheckingEvaluator`.
    *   This evaluator uses a specialized LLM (the "evaluator model," e.g., `bespokeai/minicheck` in this example) designed for fact-checking tasks.
    *   Testcontainers manages a local Ollama instance running the `bespokeai/minicheck` model.
    *   An `EvaluationRequest` is constructed, containing the reference document(s) (as context) and the AI-generated claim (as the response to be evaluated). The original user query can also be included.
    *   The `FactCheckingEvaluator` determines if the claim is factually supported by the provided documents, returning an `EvaluationResponse`. The `isPass()` method indicates factual consistency.
*   **How to Use/Run:**
    *   Locate the `FactCheckingEvaluatorDemoTest.java` file (or similarly named file).
    *   Ensure Docker Desktop is running.
    *   This JUnit 5 test also uses `@Testcontainers` to manage the Ollama instance with the `bespokeai/minicheck` model.
    *   The test sets up a `FactCheckingEvaluator` configured to use this specialized model.
    *   Test methods provide sample context documents and AI claims. Assertions check the `isPass()` status of the `EvaluationResponse`.

## Running All Tests

You can run all tests in the project using your IDE's test runner or via the command line:

*   **Maven:** `mvn test`
*   **Gradle:** `./gradlew test`

Ensure Docker Desktop is running before executing tests that use Testcontainers (Relevancy and Fact Checking tests).

## Key Technologies Used

*   Spring AI
*   JUnit 5
*   Testcontainers
*   Ollama

## Customization

These examples use default configurations and prompts for the evaluators. For real-world applications, you can customize:

*   The prompts used by the evaluators to better suit your specific criteria.
*   The LLM models used for generation and evaluation.
*   The `ChatOptions` (e.g., temperature, max tokens) for LLM interactions.

Refer to the Spring AI documentation for more details on customization.