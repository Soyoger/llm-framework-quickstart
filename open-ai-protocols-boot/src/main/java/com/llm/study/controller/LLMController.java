package com.llm.study.controller;

import com.llm.study.entity.LLMContext;
import com.llm.study.entity.LLMRequest;
import com.llm.study.entity.LLMResponse;
import com.llm.study.service.providers.response.LangChain4jResponseProvider;
import com.llm.study.service.providers.response.OpenAIResponseProvider;
import com.llm.study.service.providers.response.SpringAIResponseProvider;
import com.llm.study.service.providers.stream.LangChain4jStreamProvider;
import com.llm.study.service.providers.stream.OpenAIStreamProvider;
import com.llm.study.service.providers.stream.SpringAIStreamProvider;
import com.llm.study.utils.StreamUtil;
import com.openai.core.http.AsyncStreamResponse;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import dev.langchain4j.model.chat.response.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "LLM API", description = "LLM Service API with multiple providers (OpenAI, LangChain4j, Spring AI)")
public class LLMController {
    @Autowired
    private OpenAIResponseProvider openAIResponseProvider;

    @Autowired
    private OpenAIStreamProvider openAIStreamProvider;

    @Autowired
    private LangChain4jResponseProvider langChain4jResponseProvider;

    @Autowired
    private LangChain4jStreamProvider langChain4jStreamProvider;

    @Autowired
    private SpringAIResponseProvider springAIResponseProvider;

    @Autowired
    private SpringAIStreamProvider springAIStreamProvider;

    @Operation(
            summary = "OpenAI Chat Completion",
            description = "Send a chat request to OpenAI and get a response"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LLMResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/openAI/chatResponse")
    public LLMResponse chatResponse(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        LLMContext llmContext = LLMContext.builder().provider("openai").build();
        ChatCompletion chatCompletion = openAIResponseProvider.chatResponse(request, llmContext);
        return LLMResponse.builder().content(chatCompletion.choices().get(0).message().content().orElse("error")).build();
    }

    @Operation(
            summary = "OpenAI Chat Stream",
            description = "Send a chat request to OpenAI and get a stream response using Server-Sent Events (SSE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful stream response",
                    content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/openAI/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sseStream(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        // 正确包装成 Spring Flux（官方标准适配方式）
        LLMContext llmContext = LLMContext.builder().provider("openai").build();
        AsyncStreamResponse<ChatCompletionChunk> streamResponse = openAIStreamProvider.chatStream(request, llmContext);
        Flux<ChatCompletionChunk> fromAsyncStream = StreamUtil.fromAsyncStream(streamResponse);
        // 1. 先过滤 choices 为空或 null 的情况
        return fromAsyncStream
                .filter(chunk -> chunk.choices() != null && !chunk.choices().isEmpty())
                // 2. 诊断：记录chunk信息
                .doOnNext(chunk -> log.debug("Received chunk: finishReason={}, content={}",
                        chunk.choices().get(0).finishReason(),
                        chunk.choices().get(0).delta().content().orElse("")))
                // 3. 取出内容并处理
                .map(chunk -> chunk.choices().get(0).delta().content().orElse("111"))
                // 4. 【关键】只过滤掉null，不过滤空字符串
                .filter(content -> content != null && !content.isEmpty())
                // 5. 标准 SSE 格式：data:内容 + 两个换行
                .map(content -> "data: " + content);
    }

    @Operation(
            summary = "LangChain4j Chat Completion",
            description = "Send a chat request to LangChain4j and get a response"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LLMResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/langChain4j/chatResponse")
    public LLMResponse chatLcResponse(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        LLMContext llmContext = LLMContext.builder().provider("langChain4j").build();
        ChatResponse chatResponse = langChain4jResponseProvider.chatResponse(request, llmContext);
        return LLMResponse.builder().content(chatResponse.aiMessage().text().trim()).build();
    }

    @Operation(
            summary = "LangChain4j Chat Stream",
            description = "Send a chat request to LangChain4j and get a stream response using Server-Sent Events (SSE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful stream response",
                    content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/langChain4j/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sseLcStream(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        // 正确包装成 Spring Flux（官方标准适配方式）
        LLMContext llmContext = LLMContext.builder().provider("langChain4j").build();
        Flux<String> streamResponse = langChain4jStreamProvider.chatStream(request, llmContext);

        // 处理流式响应并转换为SSE格式
        return streamResponse
                // 过滤掉null和空字符串
                .filter(content -> content != null && !content.isEmpty())
                // 标准 SSE 格式：data:内容 + 两个换行
                .map(content -> "data: " + content)
                // 在出现错误时发送错误事件
                .onErrorReturn("data: [ERROR] Stream failed\n\n");
    }

    @Operation(
            summary = "Spring AI Chat Completion",
            description = "Send a chat request to Spring AI (ZhiPu) and get a response"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LLMResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/springAI/chatResponse")
    public LLMResponse chatSpringAIResponse(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        LLMContext llmContext = LLMContext.builder().provider("zhiPu").build();
        String content = springAIResponseProvider.chatResponse(request,
                llmContext);
        return LLMResponse.builder().content(content.trim()).build();
    }

    @Operation(
            summary = "Spring AI Chat Stream",
            description = "Send a chat request to Spring AI (ZhiPu) and get a stream response using Server-Sent Events (SSE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful stream response",
                    content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/springAI/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sseSpringAIStream(
            @Parameter(description = "LLM request containing the query and extra parameters")
            @RequestBody LLMRequest request) {
        LLMContext llmContext = LLMContext.builder().provider("zhiPu").build();
        Flux<String> output = springAIStreamProvider.chatStream(request,
                llmContext);
        return output.filter(content -> content != null && !content.isEmpty())
                .map(content -> "data: " + content);

    }
}