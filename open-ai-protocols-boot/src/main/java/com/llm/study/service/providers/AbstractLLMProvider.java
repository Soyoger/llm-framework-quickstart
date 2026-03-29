package com.llm.study.service.providers;

import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbstractLLMProvider implements LLMProvider {
    @Override
    public boolean provider(LLMRequest llmRequest, LLMContext llmContext) {
        return false;
    }

    @Override
    public boolean support(LLMRequest llmRequest, LLMContext llmContext) {
        return false;
    }

    @Override
    public boolean isStream(LLMRequest llmRequest, LLMContext llmContext) {
        return false;
    }

    @Override
    public Double getTemperature() {
        return 0.8;
    }

    @Override
    public Double getTopP() {
        return 0.6;
    }

    @Override
    public long getMaxCompletionTokens() {
        return 8192;
    }

    @Override
    public Double getFrequencyPenalty() {
        return 0.25;
    }

    @Override
    public String getModelName(LLMRequest llmRequest, LLMContext llmContext) {
        return "";
    }

    @Override
    public String buildSystemPrompt(LLMRequest llmRequest, LLMContext llmContext) {
        return "";
    }

    @Override
    public <T> T buildMessageHistory(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }

    @Override
    public LLMRequest preProcessRequest(LLMRequest llmRequest, LLMContext llmContext) {
        return llmRequest;
    }

    @Override
    public <T> T buildStreamBody(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }

    @Override
    public <T> T chatStream(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }

    @Override
    public <T> T chatResponse(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }

    @Override
    public <T> T postProcessResponse(LLMRequest llmRequest, LLMContext llmContext, T response) {
        return response;
    }
}