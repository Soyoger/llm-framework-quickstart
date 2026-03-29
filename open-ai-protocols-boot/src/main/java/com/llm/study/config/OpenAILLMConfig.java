package com.llm.study.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yongjie.su
 * @date 2026/3/26 17:09
 * @description
 * @modified 2026/3/26 17:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
@ConfigurationProperties(prefix = "custom.openai")
public class OpenAILLMConfig extends LLMConfig {
}
