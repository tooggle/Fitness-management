package org.fm.backend.service.ai;

import org.fm.backend.util.OpenAIUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service("generalAiService")
@RequiredArgsConstructor
public class GeneralAiService implements AiService {
  
  private final OpenAIUtil openAIUtil;

  @Override
  public String generate(Map<String, Object> params) {
    String type = (String) params.get("type");
    String prompt;
    switch (type) {
      case "fitness":
        // e.g. params: userId, recordId
        prompt = buildFitnessPrompt(params);
        break;
      case "meal":
        prompt = buildMealPrompt(params);
        break;
      case "post":
        prompt = buildPostPrompt(params);
        break;
      default:
        throw new IllegalArgumentException("Unsupported AI type: " + type);
    }
    return openAIUtil.getAISuggestion(prompt);
  }

  private String buildFitnessPrompt(Map<String,Object> p) { /* … */ }
  private String buildMealPrompt(Map<String,Object> p)    { /* … */ }
  private String buildPostPrompt(Map<String,Object> p)    { /* … */ }
}
