package com.llm.study.service.providers.stream;

import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.service.providers.AbstractLLMProvider;
import com.openai.core.http.AsyncStreamResponse;
import com.openai.models.chat.completions.ChatCompletionChunk;

/**
 * @author yongjie.su
 * @date 2026/3/28 5:28
 * @description
 * @modified 2026/3/28 5:28
 */
public class StreamLLMProvider extends AbstractLLMProvider {

    @Override
    public boolean isStream(LLMRequest llmRequest, LLMContext llmContext) {
        return true;
    }

    @Override
    public <T> T chatStream(LLMRequest llmRequest, LLMContext llmContext) {
        LLMRequest preLLMRequest = preProcessRequest(llmRequest, llmContext);
        AsyncStreamResponse<ChatCompletionChunk> streamResponse = eventStream(preLLMRequest, llmContext);
        AsyncStreamResponse<ChatCompletionChunk> postStreamResponse = postProcessResponse(preLLMRequest, llmContext,
                streamResponse);
        return (T) postStreamResponse;
    }


    public <T> T eventStream(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }
}
