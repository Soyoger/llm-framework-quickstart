package com.llm.study.service.frames;

import com.llm.study.entity.ProtocolContext;
import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.core.Timeout;
import com.openai.core.http.AsyncStreamResponse;
import com.openai.credential.BearerTokenCredential;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author yongjie.su
 * @date 2026/3/26 13:45
 * @description
 * @modified 2026/3/26 13:45
 */
@Slf4j
@Service
public class OpenAIServiceImpl {

    public <T> T chatStream(String baseUrl, String apiKey, T chatRequest, ProtocolContext protocolContext) {
        OpenAIClientAsync openAIClientAsync = OpenAIOkHttpClientAsync.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
        ChatCompletionCreateParams chatCompletionCreateParams = (ChatCompletionCreateParams) chatRequest;
        AsyncStreamResponse<ChatCompletionChunk> chatCompletionChunkAsyncStreamResponse = openAIClientAsync.chat().completions().createStreaming(chatCompletionCreateParams);
        return (T) chatCompletionChunkAsyncStreamResponse;
    }

    public <T> T chatResponse(String baseUrl, String apiKey, T chatRequest, ProtocolContext protocolContext) {
        OpenAIClient openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(baseUrl)
                .credential(BearerTokenCredential.create(apiKey))
                .timeout(Timeout.builder().connect(Duration.ofMillis(10000)).read(Duration.ofMillis(10000)).write(Duration.ofMillis(10000)).build())
                .build();
        ChatCompletionCreateParams chatCompletionCreateParams = (ChatCompletionCreateParams) chatRequest;
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(chatCompletionCreateParams);
        return (T) chatCompletion;
    }
}
