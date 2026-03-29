package com.llm.study.service.handlers;


import dev.langchain4j.model.chat.response.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author yongjie.su
 * @date 2025/12/27 0:11
 * @description
 * @modified 2025/12/27 0:11
 */
@Slf4j
@Service
public class DefaultStreamingChatResponseHandler implements StreamingChatResponseHandler {
    @Getter
    private final Sinks.Many<String> sink;
    private final AtomicBoolean completed = new AtomicBoolean(false);

    public DefaultStreamingChatResponseHandler() {
        this.sink = null;
    }

    public DefaultStreamingChatResponseHandler(Sinks.Many<String> sink) {
        this.sink = sink;
    }

    @Override
    public void onPartialResponse(String partialResponse) {
        if (sink != null && !completed.get()) {
            sink.tryEmitNext(partialResponse);
        }
        StreamingChatResponseHandler.super.onPartialResponse(partialResponse);
    }

    @Override
    public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
        String chunkText = partialResponse.text();
        if (sink != null && !completed.get() && chunkText != null && !chunkText.isEmpty()) {
            sink.tryEmitNext(chunkText);
        }
    }

    @Override
    public void onPartialThinking(PartialThinking partialThinking) {
        StreamingChatResponseHandler.super.onPartialThinking(partialThinking);
    }

    @Override
    public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
        StreamingChatResponseHandler.super.onPartialThinking(partialThinking, context);
    }

    @Override
    public void onPartialToolCall(PartialToolCall partialToolCall) {
        StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall);
    }

    @Override
    public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
        StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall, context);
    }

    @Override
    public void onCompleteToolCall(CompleteToolCall completeToolCall) {
        StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
    }

    @Override
    public void onCompleteResponse(ChatResponse chatResponse) {
        String resultText = chatResponse.aiMessage().text();
        log.debug("resultText: {}", resultText);
        if (sink != null && !completed.getAndSet(true)) {
            sink.tryEmitComplete();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("onError: throwable: {}", throwable);
        if (sink != null && !completed.getAndSet(true)) {
            sink.tryEmitError(throwable);
        }
    }

}
