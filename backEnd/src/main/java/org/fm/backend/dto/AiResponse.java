package org.fm.backend.dto;

import lombok.Data;

import java.util.Map;

import lombok.Builder;

@Data
@Builder
public class AiResponse {
  private boolean success;
  private String text;
  private String type;
  private Map<String, Object> usage;
}
