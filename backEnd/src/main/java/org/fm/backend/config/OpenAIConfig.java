package org.fm.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAIConfig {

    //@Value("${openai.api.key}") // 从配置文件中加载 API 密钥
    private String apiKey;

    // Getter 方法
    public String getApiKey() {
        return apiKey;
    }
}