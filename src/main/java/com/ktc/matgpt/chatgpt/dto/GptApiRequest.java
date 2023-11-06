package com.ktc.matgpt.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static com.ktc.matgpt.chatgpt.dto.GptApiResponse.Choice.Message;

/**
 * n – can be specified if we want to increase the number of responses to generate. The default value is 1.
 * temperature – controls the randomness of the response. The default value is 1 (most random).
 * max_tokens – is used to limit the maximum number of tokens in the response. The default value is infinity which means that the response will be as long as the model can generate. Generally, it would be a good idea to set this value to a reasonable number to avoid generating very long responses and incurring a high cost.
 */
public record GptApiRequest(@JsonProperty("model") String modelType,
                            @JsonProperty("messages") List<Message> messages,
                            @JsonProperty("n") int numberOfResponses,
                            @JsonProperty("temperature") double randomnessOfResponse,
                            @JsonProperty("max_tokens") int maxTokens) {

    private static final int NUMBER_OF_RESPONSES = 1;
    private static final double RANDOMNESS_OF_RESPONSE = 1.0;
    private static final int MAX_TOKENS = 150;
    private static final String MESSAGE_ROLE = "user";
    private static final String MODEL_TYPE = "gpt-3.5-turbo";

    public GptApiRequest(String content) {
        this(MODEL_TYPE,
                new ArrayList<>(List.of(new Message(MESSAGE_ROLE, content))),
                NUMBER_OF_RESPONSES,
                RANDOMNESS_OF_RESPONSE,
                MAX_TOKENS);
    }
}
