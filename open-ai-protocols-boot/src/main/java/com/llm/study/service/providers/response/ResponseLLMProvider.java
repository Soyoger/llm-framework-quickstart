package com.llm.study.service.providers.response;

import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.service.providers.AbstractLLMProvider;

/**
 * @author yongjie.su
 * @date 2026/3/28 5:29
 * @description
 * @modified 2026/3/28 5:29
 */
public class ResponseLLMProvider extends AbstractLLMProvider {
    @Override
    public boolean isStream(LLMRequest llmRequest, LLMContext llmContext) {
        return false;
    }

    @Override
    public <T> T chatResponse(LLMRequest llmRequest, LLMContext llmContext) {
        this.preProcessRequest(llmRequest, llmContext);
        Object output = this.eventResponse(llmRequest, llmContext);
        this.postProcessResponse(llmRequest, llmContext, output);
        return (T) output;
    }

    public <T> T eventResponse(LLMRequest llmRequest, LLMContext llmContext) {
        return null;
    }
}
