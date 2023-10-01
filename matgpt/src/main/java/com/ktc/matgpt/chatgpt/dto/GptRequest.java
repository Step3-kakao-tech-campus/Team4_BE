package com.ktc.matgpt.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static com.ktc.matgpt.chatgpt.dto.GptResponse.Choice.Message;

/**
 * n – can be specified if we want to increase the number of responses to generate. The default value is 1.
 * temperature – controls the randomness of the response. The default value is 1 (most random).
 * max_tokens – is used to limit the maximum number of tokens in the response. The default value is infinity which means that the response will be as long as the model can generate. Generally, it would be a good idea to set this value to a reasonable number to avoid generating very long responses and incurring a high cost.
 */
public record GptRequest(@JsonProperty("model") String modelType,
                         List<Message> messages,
                         @JsonProperty("n") int numberOfResponses,
                         @JsonProperty("temperature") double randomnessOfResponse,
                         @JsonProperty("max_tokens") int maxTokens) {

    public static final int NUMBER_OF_RESPONSES = 1;
    public static final double RANDOMNESS_OF_RESPONSE = 1.0;
    public static final int MAX_TOKENS = 100;
    public static final String MESSAGE_ROLE = "user";

    public GptRequest(String modelType, String content) {
        this(modelType,
                new ArrayList<>(List.of(new Message(MESSAGE_ROLE, content))),
                NUMBER_OF_RESPONSES,
                RANDOMNESS_OF_RESPONSE,
                MAX_TOKENS);
    }
}
