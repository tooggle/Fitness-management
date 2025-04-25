import axios from 'axios';

// 创建axios实例
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.params = {
                ...config.params,
                token: token
            };
        }
        return config;
    },
    error => {
        console.error('请求错误', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
api.interceptors.response.use(
    response => {
        // 如果后端返回了自定义的成功/失败状态码，可以在这里统一处理
        if (response.data && response.data.code !== undefined) {
            if (response.data.code !== 200) {
                console.warn('后端业务逻辑错误', response.data);
                // 可以在这里统一处理业务逻辑错误
            }
        }
        return response;
    },
    error => {
        const { response } = error;
        
        if (response) {
            switch (response.status) {
                case 400:
                    console.error('请求参数错误', response.data);
                    break;
                case 401:
                    console.error('未授权，请重新登录', response.data);
                    // 处理token过期
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                    break;
                case 403:
                    console.error('拒绝访问', response.data);
                    break;
                case 404:
                    console.error('请求的资源不存在', response.data);
                    break;
                case 500:
                    console.error('服务器内部错误', response.data);
                    break;
                default:
                    console.error(`未知错误 ${response.status}`, response.data);
            }
        } else if (error.request) {
            // 请求已发出，但没有收到响应
            console.error('网络错误，无法连接到服务器', error.request);
        } else {
            // 请求配置发生错误
            console.error('请求配置错误', error.message);
        }
        
        return Promise.reject(error);
    }
);

export default api; 