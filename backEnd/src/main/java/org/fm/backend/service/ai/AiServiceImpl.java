package org.fm.backend.service.ai;

import org.fm.backend.dto.AiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Service("generalAiService")
public class AiServiceImpl implements AiService {

    private final WebClient webClient;

    @Autowired
    public AiServiceImpl(WebClient openAiWebClient) {
        this.webClient = openAiWebClient;
    }

    @Override
    public String generate(Map<String, Object> params) {
        String prompt = (String) params.get("prompt");
        return callOpenAI(prompt);
    }

    @Override
    public String analyzeExercise(String base64Img, String exerciseName) {
        String prompt = String.format(
            "请扮演健身教练，根据以下信息给出专业的动作指导和纠正建议：\n" +
            "- 动作名称：%s\n" +
            "- 图像（Base64 编码）：%s\n" +
            "请输出要点清晰、要点分明的文本。",
            exerciseName,
            base64Img
        );
        return callOpenAI(prompt);
    }

    private String callOpenAI(String prompt) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
    
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(message));
    
        Mono<Map> responseMono = webClient.post()
                                          .bodyValue(body)
                                          .retrieve()
                                          .bodyToMono(Map.class);
    
        Map<String, Object> response = responseMono.block();
        if (response == null || !response.containsKey("choices")) {
            throw new RuntimeException("调用 OpenAI 失败");
        }
    
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> first = choices.get(0);
        Map<String, Object> messageObj = (Map<String, Object>) first.get("message");
        String generatedText = (String) messageObj.get("content");
    
        // 返回给调用者
        return generatedText;
    }    
}
