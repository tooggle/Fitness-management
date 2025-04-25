import api from './config';

/**
 * 测试与后端的连接
 * 该函数尝试向后端发送一个简单的请求，用于验证连接是否正常
 * @returns {Promise<{success: boolean, message: string}>} 测试结果
 */
export const testConnection = async () => {
  try {
    // 尝试请求一个简单的API端点，如果Spring Boot后端有健康检查接口，最好使用该接口
    const response = await api.get('/health', { timeout: 3000 });
    
    return {
      success: true,
      message: '与后端连接成功',
      data: response.data
    };
  } catch (error) {
    console.error('后端连接测试失败:', error);
    
    return {
      success: false,
      message: `与后端连接失败: ${error.message}`,
      error
    };
  }
};

/**
 * 测试特定API端点
 * @param {string} endpoint - API端点
 * @param {string} method - 请求方法 (get, post, put, delete)
 * @param {Object} data - 请求数据 (用于POST/PUT请求)
 * @param {Object} params - URL参数 (用于GET/DELETE请求)
 * @returns {Promise<{success: boolean, message: string}>} 测试结果
 */
export const testEndpoint = async (endpoint, method = 'get', data = null, params = null) => {
  try {
    const config = {};
    if (params) {
      config.params = params;
    }
    
    let response;
    switch (method.toLowerCase()) {
      case 'post':
        response = await api.post(endpoint, data, config);
        break;
      case 'put':
        response = await api.put(endpoint, data, config);
        break;
      case 'delete':
        response = await api.delete(endpoint, config);
        break;
      case 'get':
      default:
        response = await api.get(endpoint, config);
        break;
    }
    
    return {
      success: true,
      message: `API调用成功: ${endpoint}`,
      data: response.data
    };
  } catch (error) {
    console.error(`API调用失败 ${endpoint}:`, error);
    
    return {
      success: false,
      message: `API调用失败 ${endpoint}: ${error.message}`,
      error
    };
  }
};

export default {
  testConnection,
  testEndpoint
}; 