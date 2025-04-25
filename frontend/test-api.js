// 使用Jest或其他测试框架会更好，但为了简单起见，我们使用Node的fetch API
import axios from 'axios';

async function testBackendConnection() {
  try {
    console.log('正在测试与后端的连接...');
    
    // 测试基本连接 - 健康检查端点
    try {
      const healthResponse = await axios.get('http://localhost:8080/api/health', {
        timeout: 3000
      });
      console.log('健康检查成功:', healthResponse.data);
    } catch (error) {
      console.log('健康检查失败:', error.message);
      // 健康检查失败不代表整个后端不可用，继续测试其他端点
    }
    
    // 测试用户登录端点
    try {
      const loginResponse = await axios.get('http://localhost:8080/api/User/Login', {
        params: {
          email: 'test@example.com',
          password: 'password',
          role: 'user'
        }
      });
      console.log('登录API测试成功:', loginResponse.data);
    } catch (error) {
      console.log('登录API测试失败:', error.message);
    }
    
    // 测试获取食物信息端点
    try {
      const foodResponse = await axios.get('http://localhost:8080/api/MealPlans/GetFoodsInfo');
      console.log('食物信息API测试成功:', foodResponse.data);
    } catch (error) {
      console.log('食物信息API测试失败:', error.message);
    }
    
    console.log('API测试完成');
  } catch (error) {
    console.error('测试过程中发生错误:', error);
  }
}

// 运行测试
testBackendConnection(); 