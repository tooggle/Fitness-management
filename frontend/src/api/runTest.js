import { testConnection, testEndpoint } from './testConnection.js';

// 测试基本连接
async function runBasicTest() {
  console.log('正在测试与后端的基本连接...');
  const result = await testConnection();
  console.log('测试结果:', result);
  return result;
}

// 测试特定API端点
async function runEndpointTest(endpoint = '/User/Login', method = 'get', data = null, params = { email: 'test@example.com', password: 'password', role: 'user' }) {
  console.log(`正在测试API端点: ${endpoint}，方法: ${method}`);
  const result = await testEndpoint(endpoint, method, data, params);
  console.log('测试结果:', result);
  return result;
}

// 运行所有测试
async function runAllTests() {
  try {
    // 测试基本连接
    const basicResult = await runBasicTest();
    
    // 如果基本连接成功，继续测试具体端点
    if (basicResult.success) {
      console.log('\n基本连接成功，继续测试具体API端点...\n');
      
      // 测试登录API
      await runEndpointTest('/User/Login', 'get', null, { email: 'test@example.com', password: 'password', role: 'user' });
      
      // 可以添加更多端点测试
      
    } else {
      console.log('\n基本连接失败，停止后续测试。\n');
    }
    
    console.log('\n所有测试完成。\n');
  } catch (error) {
    console.error('测试过程中发生错误:', error);
  }
}

// 运行测试
runAllTests(); 