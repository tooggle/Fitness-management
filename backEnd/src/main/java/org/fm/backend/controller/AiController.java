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
  public AiResponse generate(@RequestBody AiRequest req) {
    String result = aiService.generate(req.toMap());
    return AiResponse.builder()
                     .success(true)
                     .text(result)
                     .type(req.getType())
                     .build();
  }

  @PostMapping("/analyzeExercise")
  public AiResponse analyzeExercise(@RequestBody AiRequest req) {
    String base64Img = (String) req.getParams().get("base64Img");
    String exerciseName = (String) req.getParams().get("exerciseName");

    if (base64Img == null || exerciseName == null) {
      return AiResponse.builder()
                       .success(false)
                       .text("缺少 base64Img 或 exerciseName 参数")
                       .build();
    }

    // 调用分析功能
    String result = aiService.analyzeExercise(base64Img, exerciseName);
    return AiResponse.builder()
                     .success(true)
                     .text(result)
                     .type(req.getType())
                     .build();
  }
}
