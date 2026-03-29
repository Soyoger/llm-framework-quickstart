package com.llm.study.service.providers.stream;

import com.llm.study.config.OpenAILLMConfig;
import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.entity.ProtocolContext;
import com.llm.study.service.frames.OpenAIServiceImpl;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIStreamProvider extends StreamLLMProvider {

    @Autowired
    private OpenAILLMConfig openAILLMConfig;

    @Autowired
    private OpenAIServiceImpl openAiService;

    @Override
    public <T> T chatStream(LLMRequest llmRequest, LLMContext llmContext) {
        return (T) super.chatStream(llmRequest, llmContext);
    }

    @Override
    public <T> T eventStream(LLMRequest llmRequest, LLMContext llmContext) {
        ChatCompletionCreateParams chatCompletionCreateParams = buildStreamBody(llmRequest, llmContext);
        ProtocolContext protocolContext = ProtocolContext.builder().build();
        return (T) openAiService.chatStream(openAILLMConfig.getMeta().getEndpointUrl(),
                openAILLMConfig.getCredential().getApiKey(), chatCompletionCreateParams, protocolContext);
    }

    @Override
    public <T> T buildStreamBody(LLMRequest llmRequest, LLMContext llmContext) {
        List<ChatCompletionMessageParam> messages = buildMessageHistory(llmRequest, llmContext);
        ChatCompletionCreateParams chatCompletionCreateParams = ChatCompletionCreateParams.builder()
                .model(getModelName(llmRequest, llmContext))
                .temperature(getTemperature())
                .topP(getTopP())
                .frequencyPenalty(getFrequencyPenalty())
                .maxCompletionTokens(getMaxCompletionTokens())
                .messages(messages)
                .build();
        return (T) chatCompletionCreateParams;
    }

    @Override
    public <T> T buildMessageHistory(LLMRequest llmRequest, LLMContext llmContext) {
        String systemPrompt = buildSystemPrompt(llmRequest, llmContext);
        List<ChatCompletionMessageParam> messages = new ArrayList<>();
        messages.add(ChatCompletionMessageParam.ofSystem(
                ChatCompletionSystemMessageParam.builder()
                        .content(systemPrompt)
                        .build()
        ));
        messages.add(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder()
                        .content(llmRequest.getQuery())
                        .build()
        ));
        return (T) messages;
    }

    @Override
    public String buildSystemPrompt(LLMRequest llmRequest, LLMContext llmContext) {
        return openAILLMConfig.getMeta().getSystemPrompt();
    }

    @Override
    public Double getTemperature() {
        return openAILLMConfig.getMeta().getTemperature();
    }

    @Override
    public Double getTopP() {
        return openAILLMConfig.getMeta().getTopP();
    }

    @Override
    public long getMaxCompletionTokens() {
        return openAILLMConfig.getMeta().getMaxCompletionTokens();
    }

    @Override
    public Double getFrequencyPenalty() {
        return openAILLMConfig.getMeta().getFrequencyPenalty();
    }

    @Override
    public String getModelName(LLMRequest llmRequest, LLMContext llmContext) {
        return openAILLMConfig.getMeta().getModel();
    }
}