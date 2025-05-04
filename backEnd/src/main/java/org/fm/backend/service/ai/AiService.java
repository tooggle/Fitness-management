package org.fm.backend.service.ai;

import java.util.Map;

public interface AiService {
  /**
   * @param params 包含 type + 业务参数
   * @return AI 返回的原始文本
   */
  String generate(Map<String, Object> params);

  /**
   * 用于处理健身分析的 AI 请求
   * @param base64Img 图像 Base64 编码
   * @param exerciseName 动作名称
   * @return AI 分析结果文本
   */
  String analyzeExercise(String base64Img, String exerciseName);
}
