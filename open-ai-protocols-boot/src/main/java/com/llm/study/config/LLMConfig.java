package com.llm.study.config;

import lombok.Data;

@Data
public class LLMConfig {
    private Meta meta = new Meta();
    private Credential credential = new Credential();

    private Policy policy = new Policy();
    private Timeout timeout = new Timeout();

    @Data
    public static class Meta {
        private String systemPrompt = "你是一个AI智能助手，可以友善的回答用户的问题，回答不超过100字。";
        private String endpointUrl = "https://api.openai.com/v1";
        private String model = "gpt-3.5-turbo";
        private double temperature = 0.7;
        private long maxCompletionTokens = 8192;
        private double topP = 0.8;
        private double frequencyPenalty = 0.25;
        private double presencePenalty = 0;
    }

    @Data
    public static class Credential {
        private String apiKey;
    }

    @Data
    public static class Policy {
        private long maxRetries;
        private int historyUserMessage;
        private int historyAssistantMessage;
    }

    @Data
    public static class Timeout {
        private long connectTimeoutMs;
        private long readTimeoutMs;
        private long writeTimeoutMs;
    }
}