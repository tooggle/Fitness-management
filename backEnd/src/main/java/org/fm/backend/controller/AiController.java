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
}
