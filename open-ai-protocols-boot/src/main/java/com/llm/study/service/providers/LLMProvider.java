package com.llm.study.service.providers;

import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;

public interface LLMProvider {
    /**
     * 检查供应商是否支持该请求
     */
    boolean provider(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 检查是否支持该请求
     */
    boolean support(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 检查是否流式
     */
    boolean isStream(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 获取温度参数
     */
    Double getTemperature();

    /**
     * 获取topP参数
     */
    Double getTopP();

    /**
     * 获取最大完成token数
     */
    long getMaxCompletionTokens();

    /**
     * 获取频率惩罚参数
     */
    Double getFrequencyPenalty();

    /**
     * 获取模型名称
     */
    String getModelName(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 构建系统提示词
     */
    String buildSystemPrompt(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 构建用户消息历史
     */
    <T> T buildMessageHistory(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 预处理请求
     */
    LLMRequest preProcessRequest(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 构建流式消息体
     */
    <T> T buildStreamBody(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 处理流式请求
     */
    <T> T chatStream(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 处理响应请求
     */
    <T> T chatResponse(LLMRequest llmRequest, LLMContext llmContext);

    /**
     * 后处理响应
     */
    <T> T postProcessResponse(LLMRequest llmRequest, LLMContext llmContext, T response);
}