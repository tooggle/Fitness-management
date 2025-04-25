// 为config.js模块提供全局类型声明
declare module '../api/config.js' {
  import { AxiosInstance } from 'axios';
  const api: AxiosInstance;
  export default api;
} 