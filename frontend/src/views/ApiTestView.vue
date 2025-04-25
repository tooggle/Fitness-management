<template>
  <div>
    <el-header class="header">
      <navigation-bar />
    </el-header>
    <el-main>
      <el-card class="api-test-card">
        <template #header>
          <div class="card-header">
            <h2>API连接测试</h2>
            <el-button type="primary" @click="testBasicConnection">
              测试基本连接
            </el-button>
          </div>
        </template>
        
        <div class="test-result" v-if="basicTestResult">
          <el-alert
            :title="basicTestResult.message"
            :type="basicTestResult.success ? 'success' : 'error'"
            :closable="false"
            show-icon
          />
          <div class="result-details" v-if="basicTestResult.data">
            <pre>{{ JSON.stringify(basicTestResult.data, null, 2) }}</pre>
          </div>
        </div>
        
        <el-divider content-position="center">自定义API测试</el-divider>
        
        <el-form :model="customTest" label-width="120px">
          <el-form-item label="API端点">
            <el-input v-model="customTest.endpoint" placeholder="/User/Login" />
          </el-form-item>
          
          <el-form-item label="请求方法">
            <el-select v-model="customTest.method">
              <el-option label="GET" value="get" />
              <el-option label="POST" value="post" />
              <el-option label="PUT" value="put" />
              <el-option label="DELETE" value="delete" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="请求参数" v-if="customTest.method === 'get' || customTest.method === 'delete'">
            <el-input
              v-model="customTest.paramsString"
              type="textarea"
              placeholder='{ "param1": "value1", "param2": "value2" }'
              :rows="4"
            />
          </el-form-item>
          
          <el-form-item label="请求数据" v-if="customTest.method === 'post' || customTest.method === 'put'">
            <el-input
              v-model="customTest.dataString"
              type="textarea"
              placeholder='{ "field1": "value1", "field2": "value2" }'
              :rows="4"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="testCustomEndpoint">
              测试
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="test-result" v-if="customTestResult">
          <el-alert
            :title="customTestResult.message"
            :type="customTestResult.success ? 'success' : 'error'"
            :closable="false"
            show-icon
          />
          <div class="result-details" v-if="customTestResult.data">
            <pre>{{ JSON.stringify(customTestResult.data, null, 2) }}</pre>
          </div>
        </div>
      </el-card>
    </el-main>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import NavigationBar from '../components/NavigationBar.vue';
import { testConnection, testEndpoint } from '../api/testConnection';

export default defineComponent({
  components: {
    NavigationBar
  },
  setup() {
    const basicTestResult = ref(null);
    const customTestResult = ref(null);
    
    const customTest = ref({
      endpoint: '',
      method: 'get',
      paramsString: '',
      dataString: ''
    });
    
    const testBasicConnection = async () => {
      basicTestResult.value = await testConnection();
    };
    
    const testCustomEndpoint = async () => {
      try {
        let params = null;
        if (customTest.value.paramsString) {
          params = JSON.parse(customTest.value.paramsString);
        }
        
        let data = null;
        if (customTest.value.dataString) {
          data = JSON.parse(customTest.value.dataString);
        }
        
        customTestResult.value = await testEndpoint(
          customTest.value.endpoint,
          customTest.value.method,
          data,
          params
        );
      } catch (e) {
        customTestResult.value = {
          success: false,
          message: `参数解析错误: ${e.message}`,
          error: e
        };
      }
    };
    
    return {
      basicTestResult,
      customTestResult,
      customTest,
      testBasicConnection,
      testCustomEndpoint
    };
  }
});
</script>

<style scoped>
.header {
  height: 80px;
}

.api-test-card {
  max-width: 800px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.test-result {
  margin: 20px 0;
}

.result-details {
  margin-top: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  overflow-x: auto;
}

pre {
  margin: 0;
  white-space: pre-wrap;
}
</style> 