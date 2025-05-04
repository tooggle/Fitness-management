package org.fm.backend.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AiResponse {
  private boolean success;
  private String text;
  private String type;
  // （可选）时间戳、requestId…
}
