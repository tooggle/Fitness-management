package org.fm.backend.service.ai;

public enum AiType {
  FITNESS("fitness"),
  MEAL("meal"),
  POST("post");

  private final String code;
  AiType(String code){ this.code = code; }
  public String code(){ return code; }
}
