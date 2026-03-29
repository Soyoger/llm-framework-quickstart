package com.llm.study.entity;

import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yongjie.su
 * @date 2026/3/26 13:40
 * @description
 * @modified 2026/3/26 13:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolContext {
    private StreamingChatResponseHandler handler;
}
