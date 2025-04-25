// 使用Node.js内置的fetch API测试后端连接
console.log('正在测试与后端的连接...');

// 测试健康检查端点
async function testHealth() {
  try {
    const response = await fetch('http://localhost:8080/api/health', {
      method: 'GET'
    });
    const data = await response.json();
    console.log('健康检查成功:', data);
    return { success: true, data };
  } catch (error) {
    console.log('健康检查失败:', error.message);
    return { success: false, error: error.message };
  }
}

// 测试登录端点
async function testLogin() {
  try {
    const url = new URL('http://localhost:8080/api/User/Login');
    url.searchParams.append('email', 'test@example.com');
    url.searchParams.append('password', 'password');
    url.searchParams.append('role', 'user');
    
    const response = await fetch(url, {
      method: 'GET'
    });
    const data = await response.json();
    console.log('登录API测试成功:', data);
    return { success: true, data };
  } catch (error) {
    console.log('登录API测试失败:', error.message);
    return { success: false, error: error.message };
  }
}

// 运行所有测试
async function runTests() {
  const healthResult = await testHealth();
  const loginResult = await testLogin();
  
  console.log('========================');
  console.log('测试结果摘要:');
  console.log('健康检查:', healthResult.success ? '成功' : '失败');
  console.log('登录API:', loginResult.success ? '成功' : '失败');
  console.log('========================');
}

runTests(); 