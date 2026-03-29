package com.llm.study.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Sinks;

/**
 * @author yongjie.su
 * @date 2026/3/26 13:02
 * @description
 * @modified 2026/3/26 13:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LLMContext {
    private String provider;
    private Sinks.Many<String> sink;
}
