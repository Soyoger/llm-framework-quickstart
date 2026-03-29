package com.llm.study.service.providers.response;

import com.llm.study.config.ZhiPuLLMConfig;
import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.entity.ProtocolContext;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpringAIResponseProvider extends ResponseLLMProvider {
    @Autowired
    private ZhiPuLLMConfig zhiPuLLMConfig;

    @Override
    public <T> T chatResponse(LLMRequest llmRequest, LLMContext llmContext) {
        OpenAiChatOptions chatOptions = buildStreamBody(llmRequest, llmContext);
        ProtocolContext protocolContext = ProtocolContext.builder().build();
        OpenAiApi openAiApi = new OpenAiApi(
                zhiPuLLMConfig.getMeta().getEndpointUrl(),
                zhiPuLLMConfig.getCredential().getApiKey()
        );

        String systemPrompt = buildSystemPrompt(llmRequest, llmContext);
        List<Message> messages = buildMessageHistory(llmRequest, llmContext);
        OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApi, chatOptions);
        String chatResponse =
                ChatClient.builder(openAiChatModel).build()
                        .prompt(systemPrompt)
                        .messages(messages)
                        .user(llmRequest.getQuery())
                        .call()
                        .content();
        return (T) chatResponse;
    }

    @Override
    public <T> T buildStreamBody(LLMRequest llmRequest, LLMContext llmContext) {
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model(getModelName(llmRequest, llmContext))
                .temperature(getTemperature())
                .topP(getTopP())
                .frequencyPenalty(getFrequencyPenalty())
                .build();
        return (T) chatOptions;
    }

    @Override
    public <T> T buildMessageHistory(LLMRequest llmRequest, LLMContext llmContext) {
        List<Message> messages = new ArrayList<>();
        return (T) messages;
    }

    @Override
    public String buildSystemPrompt(LLMRequest llmRequest, LLMContext llmContext) {
        return zhiPuLLMConfig.getMeta().getSystemPrompt();
    }

    @Override
    public Double getTemperature() {
        return zhiPuLLMConfig.getMeta().getTemperature();
    }

    @Override
    public Double getTopP() {
        return zhiPuLLMConfig.getMeta().getTopP();
    }

    @Override
    public long getMaxCompletionTokens() {
        return zhiPuLLMConfig.getMeta().getMaxCompletionTokens();
    }

    @Override
    public Double getFrequencyPenalty() {
        return zhiPuLLMConfig.getMeta().getFrequencyPenalty();
    }

    @Override
    public String getModelName(LLMRequest llmRequest, LLMContext llmContext) {
        return zhiPuLLMConfig.getMeta().getModel();
    }
}