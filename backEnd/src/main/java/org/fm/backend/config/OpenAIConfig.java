package org.fm.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAIConfig {

    @Value("${OPENAI_API_KEY:${OPENAI_API_KEY:}}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
