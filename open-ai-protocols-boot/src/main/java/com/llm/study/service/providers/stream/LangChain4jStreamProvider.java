package com.llm.study.service.providers.stream;


import com.llm.study.config.SiliconFlowLLMConfig;
import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.entity.ProtocolContext;
import com.llm.study.service.frames.LangChain4jServiceImpl;
import com.llm.study.service.handlers.DefaultStreamingChatResponseHandler;
import com.llm.study.service.providers.AbstractLLMProvider;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

@Service
public class LangChain4jStreamProvider extends AbstractLLMProvider {
    @Autowired
    private SiliconFlowLLMConfig siliconFlowLLMConfig;
    @Autowired
    private LangChain4jServiceImpl langChain4jService;

    @Override
    public <T> T chatStream(LLMRequest llmRequest, LLMContext llmContext) {
        // 创建一个用于流式传输的Sink
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        DefaultStreamingChatResponseHandler handler = new DefaultStreamingChatResponseHandler(sink);
        ProtocolContext protocolContext = ProtocolContext.builder().handler(handler).build();
        ChatRequest chatRequest = buildStreamBody(llmRequest, llmContext);
        langChain4jService.chatStream(
                siliconFlowLLMConfig.getMeta().getEndpointUrl(),
                siliconFlowLLMConfig.getCredential().getApiKey(),
                chatRequest,
                protocolContext
        );
        return (T) sink.asFlux();
    }

    @Override
    public <T> T buildStreamBody(LLMRequest llmRequest, LLMContext llmContext) {
        List<ChatMessage> messages = buildMessageHistory(llmRequest, llmContext);
        ChatRequest chatRequest = ChatRequest.builder()
                .modelName(getModelName(llmRequest, llmContext))
                .temperature(getTemperature())
                .topP(getTopP())
                .frequencyPenalty(getFrequencyPenalty())
                .messages(messages)
                .build();
        return (T) chatRequest;
    }

    @Override
    public <T> T buildMessageHistory(LLMRequest llmRequest, LLMContext llmContext) {
        String systemPrompt = buildSystemPrompt(llmRequest, llmContext);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.systemMessage(systemPrompt));
        messages.add(UserMessage.userMessage(llmRequest.getQuery()));
        return (T) messages;
    }

    @Override
    public String buildSystemPrompt(LLMRequest llmRequest, LLMContext llmContext) {
        return siliconFlowLLMConfig.getMeta().getSystemPrompt();
    }

    @Override
    public Double getTemperature() {
        return siliconFlowLLMConfig.getMeta().getTemperature();
    }

    @Override
    public Double getTopP() {
        return siliconFlowLLMConfig.getMeta().getTopP();
    }

    @Override
    public long getMaxCompletionTokens() {
        return siliconFlowLLMConfig.getMeta().getMaxCompletionTokens();
    }

    @Override
    public Double getFrequencyPenalty() {
        return siliconFlowLLMConfig.getMeta().getFrequencyPenalty();
    }

    @Override
    public String getModelName(LLMRequest llmRequest, LLMContext llmContext) {
        return siliconFlowLLMConfig.getMeta().getModel();
    }

}