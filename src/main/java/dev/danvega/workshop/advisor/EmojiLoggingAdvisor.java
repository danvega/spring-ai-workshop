package dev.danvega.workshop.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class EmojiLoggingAdvisor implements CallAdvisor {

    public static final Function<ChatClientRequest, String> DEFAULT_REQUEST_TO_STRING = ChatClientRequest::toString;
    public static final Function<ChatResponse, String> DEFAULT_RESPONSE_TO_STRING = ModelOptionsUtils::toJsonStringPrettyPrinter;
    private static final Logger logger = LoggerFactory.getLogger(EmojiLoggingAdvisor.class);
    private final Function<ChatClientRequest, String> requestToString;
    private final Function<ChatResponse, String> responseToString;
    private final int order;

    public EmojiLoggingAdvisor() {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, 0);
    }

    public EmojiLoggingAdvisor(int order) {
        this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, order);
    }

    public EmojiLoggingAdvisor(@Nullable Function<ChatClientRequest, String> requestToString, @Nullable Function<ChatResponse, String> responseToString, int order) {
        this.requestToString = requestToString != null ? requestToString : DEFAULT_REQUEST_TO_STRING;
        this.responseToString = responseToString != null ? responseToString : DEFAULT_RESPONSE_TO_STRING;
        this.order = order;
    }

    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.logRequest(chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        this.logResponse(chatClientResponse);
        return chatClientResponse;
    }

    private void logRequest(ChatClientRequest request) {
        logger.debug("\uD83E\uDD16 Request: {}", this.requestToString.apply(request));
    }

    private void logResponse(ChatClientResponse chatClientResponse) {
        logger.debug("\uD83E\uDD16 Response: {}", this.responseToString.apply(chatClientResponse.chatResponse()));
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return this.order;
    }

}
