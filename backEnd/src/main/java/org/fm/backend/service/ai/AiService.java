package org.fm.backend.service.ai;

import java.util.Map;

public interface AiService {
  /**
   * @param params 包含 type + 业务参数
   * @return AI 返回的原始文本
   */
  String generate(Map<String, Object> params);
}
