package com.llm.study.service.frames;

import com.llm.study.entity.ProtocolContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.stereotype.Service;

/**
 * @author yongjie.su
 * @date 2026/3/28 7:36
 * @description
 * @modified 2026/3/28 7:36
 */
@Service
public class LangChain4jServiceImpl {
    public <T> T chatStream(String baseUrl, String apiKey, T chatRequest, ProtocolContext protocolContext) {
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder().apiKey(apiKey).baseUrl(baseUrl).build();
        model.chat((ChatRequest) chatRequest, protocolContext.getHandler());
        return null;
    }

    public <T> T chatResponse(String baseUrl, String apiKey, T chatRequest, ProtocolContext protocolContext) {
        OpenAiChatModel model = OpenAiChatModel.builder().apiKey(apiKey).baseUrl(baseUrl).build();
        ChatResponse chatResponse = model.chat((ChatRequest) chatRequest);
        return (T) chatResponse;
    }
}
