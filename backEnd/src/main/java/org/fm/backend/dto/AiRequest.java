package org.fm.backend.dto;

import lombok.Data;
import lombok.Builder;
import java.util.Map;

@Data
@Builder
public class AiRequest {
  /** 业务类型：fitness | meal | post | … */
  private String type;
  /** 额外参数（如 userId、recordId、title、content…） */
  private Map<String, Object> params;

  public Map<String, Object> toMap() {
    // 可以在这里注入 type 进 params 方便 service 分流
    params.put("type", type);
    return params;
  }
}
