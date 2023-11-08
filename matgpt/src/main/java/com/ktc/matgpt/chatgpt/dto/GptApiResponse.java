package com.ktc.matgpt.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktc.matgpt.chatgpt.utils.UnixTimeConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public record GptApiResponse(@JsonProperty("created") int created,
                             @JsonProperty("choices") List<Choice> choices,
                             @JsonProperty("usage") Usage usage,
                             @JsonProperty("id") String id,
                             @JsonProperty("model") String model,
                             @JsonProperty("object") String object) {

    public record Choice(@JsonProperty("finish_reason") FinishReason finishReason,
                         @JsonProperty("index") int index,
                         @JsonProperty("message") Message message) {

        public record Message(@JsonProperty("role") String role,
                              @JsonProperty("content") String content) {}
    }

    public record Usage(@JsonProperty("prompt_tokens") int promptTokens,
                        @JsonProperty("completion_tokens") int completionTokens,
                        @JsonProperty("total_tokens") int totalTokens) {}

    public String getContent() {
        return this.choices().get(0).message().content();
    }

    public FinishReason getFinishReason() {
        return this.choices().get(0).finishReason();
    }

    public void log() {
        log.info("[ChatGPT API] Response CreatedAt: {}", UnixTimeConverter.toLocalDateTime(this.created()));
        log.info("[ChatGPT API] Response Content: {}", this.getContent());
    }
}
