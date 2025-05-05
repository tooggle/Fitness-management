package org.fm.backend.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.fm.backend.config.OpenAIConfig;
import org.springframework.stereotype.Component;

@Component
public class OpenAIUtil {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final String apiKey;

    public OpenAIUtil(OpenAIConfig config) {
        this.client = new OkHttpClient();
        this.apiKey = config.getApiKey();
    }

    public String getAISuggestion(String prompt) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenAI API key is not configured");
        }

        String bodyJson = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": ["
                + "    {\"role\": \"user\", \"content\": \"" + escapeJson(prompt) + "\"}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(bodyJson, JSON);
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(
                    String.format("OpenAI API Error: %d %s", response.code(), response.message())
                );
            }
            return response.body().string();
        }
    }

    // json 字符串转义
    private String escapeJson(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }

    public static class AIResBuilder {
        private String suggestion;
        private boolean success;

        public AIResBuilder suggestion(String suggestion) {
            this.suggestion = suggestion;
            return this;
        }

        public AIResBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AIRes build() {
            return new AIRes(success, suggestion);
        }
    }

    public static class AIRes {
        private final boolean success;
        private final String suggestion;

        public AIRes(boolean success, String suggestion) {
            this.success = success;
            this.suggestion = suggestion;
        }
    }
}

