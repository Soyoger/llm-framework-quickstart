package com.llm.study.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMResponse {
    private String content;
}