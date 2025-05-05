package org.fm.backend.dto;

import lombok.Data;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class AiRequest {
  private String prompt;
  /** 业务类型：fitness | meal | post | … */
  private String type;
  
  /** 额外参数（如 userId、recordId、title、content…） */
  private Map<String, Object> params;

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
        map.put("prompt", this.prompt);
        map.put("type", this.type);
        if (params != null) {
            map.putAll(params); // 将额外的参数加入请求
        }
        return map;
  }
}

