package org.fm.backend.controller;

import org.fm.backend.dto.AiRequest;
import org.fm.backend.dto.AiResponse;
import org.fm.backend.service.ai.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

  @Qualifier("generalAiService")
  private final AiService aiService;

  @PostMapping("/generate")
  public AiResponse generate(
      @RequestHeader(value = "Authorization", required = false) String authorization,
      @RequestBody AiRequest req) {
    AiResponse authFail = checkAuth(authorization);
    if (authFail != null) {
      return authFail;
    }

    String result = aiService.generate(req.toMap());
    return AiResponse.builder()
                     .success(true)
                     .text(result)
                     .type(req.getType())
                     .build();
  }

  @PostMapping("/analyzeExercise")
  public AiResponse analyzeExercise(
      @RequestHeader(value = "Authorization", required = false) String authorization,
      @RequestBody AiRequest req) {
    // 校验 Token
    AiResponse authFail = checkAuth(authorization);
    if (authFail != null) {
      return authFail;
    }

    String base64Img = (String) req.getParams().get("base64Img");
    String exerciseName = (String) req.getParams().get("exerciseName");

    if (base64Img == null || exerciseName == null) {
      return AiResponse.builder()
                       .success(false)
                       .text("缺少 base64Img 或 exerciseName 参数")
                       .build();
    }

    String result = aiService.analyzeExercise(base64Img, exerciseName);
    return AiResponse.builder()
                     .success(true)
                     .text(result)
                     .type(req.getType())
                     .build();
  }

  private AiResponse checkAuth(String authorization) {
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return AiResponse.builder()
                       .success(false)
                       .text("缺少或无效的 Authorization token")
                       .build();
    }
    String token = authorization.substring(7);
    if (!isValidToken(token)) {
      return AiResponse.builder()
                       .success(false)
                       .text("无效的 token")
                       .build();
    }
    return null;
  }

  private boolean isValidToken(String token) {
    return true;
  }
}
