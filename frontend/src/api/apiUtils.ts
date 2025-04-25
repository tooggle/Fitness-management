import { ElNotification } from 'element-plus';
import { ApiResponse } from '../types/api';

/**
 * 处理API响应的标准方法
 * @param promise API请求Promise
 * @param options 配置项
 * @returns 处理后的Promise
 */
export function handleApiResponse<T>(
  promise: Promise<{ data: ApiResponse<T> }>,
  options: {
    successMessage?: string;
    errorMessage?: string;
    showSuccessNotification?: boolean;
    showErrorNotification?: boolean;
  } = {}
): Promise<T> {
  const {
    successMessage = '操作成功',
    errorMessage = '操作失败',
    showSuccessNotification = true,
    showErrorNotification = true
  } = options;

  return promise
    .then(response => {
      const { data } = response;
      if (data.code === 200 || data.code === 0) {
        if (showSuccessNotification) {
          ElNotification({
            message: data.message || successMessage,
            type: 'success',
            duration: 2000
          });
        }
        return data.data;
      } else {
        if (showErrorNotification) {
          ElNotification({
            message: data.message || errorMessage,
            type: 'error',
            duration: 2000
          });
        }
        return Promise.reject(new Error(data.message || errorMessage));
      }
    })
    .catch(error => {
      if (showErrorNotification) {
        ElNotification({
          message: error.message || errorMessage,
          type: 'error',
          duration: 2000
        });
      }
      return Promise.reject(error);
    });
}

/**
 * 处理特殊API响应（非标准ApiResponse格式）
 * @param promise API请求Promise
 * @param options 配置项
 * @returns 处理后的Promise
 */
export function handleSpecialApiResponse<T>(
  promise: Promise<{ data: T }>,
  options: {
    successCondition?: (data: T) => boolean;
    successMessage?: string;
    errorMessage?: string;
    showSuccessNotification?: boolean;
    showErrorNotification?: boolean;
  } = {}
): Promise<T> {
  const {
    successCondition = () => true,
    successMessage = '操作成功',
    errorMessage = '操作失败',
    showSuccessNotification = true,
    showErrorNotification = true
  } = options;

  return promise
    .then(response => {
      const { data } = response;
      if (successCondition(data)) {
        if (showSuccessNotification) {
          ElNotification({
            message: successMessage,
            type: 'success',
            duration: 2000
          });
        }
        return data;
      } else {
        if (showErrorNotification) {
          ElNotification({
            message: errorMessage,
            type: 'error',
            duration: 2000
          });
        }
        return Promise.reject(new Error(errorMessage));
      }
    })
    .catch(error => {
      if (showErrorNotification) {
        ElNotification({
          message: error.message || errorMessage,
          type: 'error',
          duration: 2000
        });
      }
      return Promise.reject(error);
    });
} 