package com.llm.study.utils;

import com.openai.core.http.AsyncStreamResponse;
import com.openai.models.chat.completions.ChatCompletionChunk;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Optional;

/**
 * @author yongjie.su
 * @date 2026/3/27 23:25
 * @description
 * @modified 2026/3/27 23:25
 */
@Slf4j
public class StreamUtil {
    // ==========================================
    // 工具方法：OpenAI AsyncStreamResponse → Flux
    // ==========================================
    public static Flux<ChatCompletionChunk> fromAsyncStream(AsyncStreamResponse<ChatCompletionChunk> asyncStreamResponse) {
        // 创建一个流式发射器
        Sinks.Many<ChatCompletionChunk> sink = Sinks.many().unicast().onBackpressureBuffer();

        asyncStreamResponse.subscribe(new AsyncStreamResponse.Handler<ChatCompletionChunk>() {
            @Override
            public void onComplete(@NotNull Optional<Throwable> error) {
                if (error.isPresent()) {
                    log.error("Stream completed with error", error.get());
                } else {
                    log.debug("Stream completed successfully");
                }
                sink.tryEmitComplete();
            }

            @Override
            public void onNext(ChatCompletionChunk chunk) {
                sink.tryEmitNext(chunk);
            }
        });
        // 返回 Flux
        return sink.asFlux();
    }
}
