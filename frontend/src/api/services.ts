import axios from 'axios';
import { MealRecord, Food, MealPlan, FitnessPlan, Exercise, ApiResponse, Recipe, PhysicalTest, FitnessGoal, PlanWeek, User } from '../types/api';
// 使用完整的文件路径，包括扩展名
import api from './config.js';

// 以下API已从config.js导入，确保使用统一实例
// 不再需要创建新的axios实例和请求拦截器

// 用户相关 API
export const userApi = {
  // 登录
  login: (email: string, password: string, role: string) => 
    api.get<ApiResponse<any>>('/User/Login', { params: { email, password, role } }),
  
  // 获取个人资料
  getPersonalProfile: () => 
    api.get<ApiResponse<User>>('/User/GetPersonalProfile'),
    
  // 获取活力币余额
  getVigorTokenBalance: () => 
    api.get<ApiResponse<{ balance: number }>>('/User/GetVigorTokenBalance'),
    
  // 获取好友列表
  getFriendList: () => 
    api.get<ApiResponse<User[]>>('/User/GetFriendList'),
    
  // 根据用户ID获取资料
  getProfileByUserID: (userID: number) => 
    api.get<ApiResponse<User>>('/User/GetProfileByUserID', { params: { userID } }),
    
  // 获取token状态
  getTokenInvalidateRes: () => 
    api.get<boolean>('/User/GetTokenInvalidateRes'),
    
  // 获取用户信息
  getUserInfo: () => 
    api.get<ApiResponse<{ userInfo: any }>>('/User/GetUserInfo'),
    
  // 更新用户信息
  updateUserInfo: (data: any) => 
    api.put<ApiResponse<{ message: string }>>('/User/UpdateUserInfo', data),
    
  // 获取所有用户（管理员功能）
  getAllUser: () =>
    api.get<any[]>('/User/GetAllUser'),
    
  // 禁言用户（管理员功能）
  banUser: (userID: number) =>
    api.get<string>('/User/BanUser', { params: { userID } }),
    
  // 取消禁言（管理员功能）
  cancelBanUser: (userID: number) =>
    api.get<string>('/User/CancelBanUser', { params: { userID } }),
    
  // 删除用户（管理员功能）
  removeUser: (userID: number) =>
    api.get<string>('/User/RemoveUser', { params: { userID } })
};

// 饮食记录相关 API
export const mealRecordApi = {
  // 创建饮食记录
  create: (data: Omit<MealRecord, 'recordID' | 'userID' | 'totalCalorie' | 'loading' | 'diningAdvice'>) => 
    api.post<ApiResponse<MealRecord>>('/MealRecords/Create', data),
  
  // 获取所有饮食记录
  getAllDetails: (date: string) => 
    api.get<ApiResponse<MealRecord[]>>('/MealRecords/GetAllDetails', { params: { date } }),
  
  // 更新饮食记录
  update: (data: Partial<MealRecord> & { recordID: number }) => 
    api.put<ApiResponse<MealRecord>>('/MealRecords/Update', data),
  
  // 删除饮食记录
  delete: (recordID: number) => 
    api.delete<ApiResponse<void>>('/MealRecords/Delete', { params: { recordID } }),
  
  // 获取 AI 建议
  getAISuggestions: (recordID: number) => 
    api.get<ApiResponse<{ diningAdvice: string }>>('/MealRecords/GetAISuggestions', { params: { recordID } }),
  
  // 获取 AI 总结
  getAISummary: (date: string) => 
    api.get<ApiResponse<{ message: string }>>('/MealRecords/GetAISummary', { params: { date } }),
};

// 饮食计划相关 API
export const mealPlanApi = {
  // 获取食物信息
  getFoodsInfo: () => 
    api.get<ApiResponse<{ foodsInfo: { foodName: string; calories: number }[] }>>('/MealPlans/GetFoodsInfo'),
  
  // 创建饮食计划
  create: (data: Omit<MealPlan, 'planID' | 'userID' | 'createdAt' | 'updatedAt'>) => 
    api.post<ApiResponse<MealPlan>>('/MealPlans/Create', data),
  
  // 获取饮食计划
  get: (planID: number) => 
    api.get<ApiResponse<MealPlan>>('/MealPlans/Get', { params: { planID } }),
  
  // 获取所有饮食计划
  getAllDetails: () => 
    api.get<ApiResponse<MealPlan[]>>('/MealPlans/GetAllDetails'),
  
  // 更新饮食计划
  update: (data: Partial<MealPlan> & { planID: number }) => 
    api.put<ApiResponse<MealPlan>>('/MealPlans/Update', data),
  
  // 删除饮食计划
  delete: (planID: number) => 
    api.delete<ApiResponse<void>>('/MealPlans/Delete', { params: { planID } }),
  
  // 插入食物信息
  insertFoodInfo: (data: { foodName: string; calorie: number }) => 
    api.post<ApiResponse<{ message: string }>>('/MealPlans/InsertFoodInfo', data),
  
  // 更新食物信息
  updateFoodInfo: (data: { foodName: string; calorie: number }) => 
    api.put<ApiResponse<{ message: string }>>('/MealPlans/UpdateFoodInfo', data),
  
  // 删除食物信息
  deleteFoodInfo: (foodName: string) => 
    api.delete<ApiResponse<{ message: string }>>('/MealPlans/DeleteFoodInfo', { params: { foodName } }),
  
  // 获取所有食谱
  getAllRecipes: () => 
    api.get<ApiResponse<{ recipes: Recipe[] }>>('/MealPlans/GetAllRecipes'),
  
  // 插入食谱
  insertRecipe: (data: { title: string; imgUrl: string; content: string }) => 
    api.post<ApiResponse<{ message: string; recipeID: number; releaseTime: string }>>('/MealPlans/InsertRecipe', data),
  
  // 更新食谱
  updateRecipe: (data: { recipeID: number; title: string; imgUrl: string; content: string }) => 
    api.put<ApiResponse<{ message: string }>>('/MealPlans/UpdateRecipe', data),
  
  // 删除食谱
  deleteRecipe: (recipeID: number) => 
    api.delete<ApiResponse<{ message: string }>>('/MealPlans/DeleteRecipe', { params: { recipeID } }),
};

// 健身计划相关 API
export const fitnessPlanApi = {
  // 创建健身计划
  create: (data: Omit<FitnessPlan, 'planID' | 'userID' | 'createdAt' | 'updatedAt'>) => 
    api.post<ApiResponse<FitnessPlan>>('/FitnessPlan/Create', data),
  
  // 获取健身计划
  get: (planID: number) => 
    api.get<ApiResponse<FitnessPlan>>('/FitnessPlan/Get', { params: { planID } }),
  
  // 获取健身计划（带token参数）
  getPlan: () => 
    api.get<PlanWeek>('/FitnessPlan/GetPlan'),
  
  // 更新健身计划
  update: (data: Partial<FitnessPlan> & { planID: number }) => 
    api.put<ApiResponse<FitnessPlan>>('/FitnessPlan/Update', data),
  
  // 删除健身计划
  delete: (planID: number) => 
    api.delete<ApiResponse<void>>('/FitnessPlan/Delete', { params: { planID } }),
  
  // 发布健身数据（由GET改为POST）
  postFitness: (data: Partial<PhysicalTest>) => 
    api.post<ApiResponse<{ message: string }>>('/FitnessPlan/PostFitness', data),
  
  // 发布体测数据（由GET改为POST）
  postPhysicalTest: (data: PhysicalTest) => 
    api.post<ApiResponse<{ message: string }>>('/FitnessPlan/PostPhysicalTest', data),
  
  // 设置目标（由GET改为POST）
  setGoal: (data: Partial<FitnessGoal>) => 
    api.post<ApiResponse<{ message: string }>>('/FitnessPlan/SetGoal', data),
}; 

// 成就相关API
export const achievementApi = {
  // 获取所有成就
  getAllAchievements: () => 
    api.get<ApiResponse<{ achievements: any[] }>>('/Achievement/GetAllAchievements'),
  
  // 更新健身计划成就
  updateFitnessPlanAchievement: (workoutIndex: number) => 
    api.get<ApiResponse<{ message: string }>>('/Achievement/UpdateFitnessPlanAchievement', { 
      params: { workoutIndex } 
    }),
};

// 设备指南相关API
export const aiGuideApi = {
  // 插入设备指南
  insertEquipmentGuide: (data: any) => 
    api.post<ApiResponse<{ message: string }>>('/AIGuide/InsertEquipmentGuide', data),
  
  // 更新设备指南
  updateEquipmentGuide: (data: any) => 
    api.put<ApiResponse<{ message: string }>>('/AIGuide/UpdateEquipmentGuide', data),
  
  // 删除设备指南
  deleteEquipmentGuide: (equipmentName: string) => 
    api.delete<ApiResponse<{ message: string }>>('/AIGuide/DeleteEquipmentGuide', { params: { equipmentName } }),
  
  // 创建AI指南
  create: (data: any) => 
    api.post<ApiResponse<any>>('/AIGuide/Create', data),
  
  // 获取AI建议
  getAISuggestion: (screenshotID: number) => 
    api.get<ApiResponse<any>>('/AIGuide/GetAISuggestion', { params: { screenshotID } }),
  
  // 获取所有AI指南
  getAllDetails: () => 
    api.get<ApiResponse<any[]>>('/AIGuide/GetAllDetails'),
  
  // 删除AI指南
  delete: (screenshotID: number) => 
    api.delete<ApiResponse<void>>('/AIGuide/Delete', { params: { screenshotID } }),
  
  // 获取所有设备
  getAllEquipment: () =>
    api.get<ApiResponse<{ equipment: any[] }>>('/Equipment/GetAllEquipment'),
};

// 帖子相关API
export const postApi = {
  // 获取所有帖子
  getAllPost: () => 
    api.get<any[]>('/Post/GetAllPost'),
  
  // 根据帖子ID获取帖子
  getPostByPostID: (postID: number) => 
    api.get<any>('/Post/GetPostByPostID', { params: { postID } }),
  
  // 根据用户ID获取帖子
  getPostByUserID: (userID: number) => 
    api.get<any[]>('/Post/GetPostByUserID', { params: { userID } }),
  
  // 删除帖子
  deletePostByPostID: (postID: number, postOwnerID: number) =>
    api.delete<{ message: string }>('/Post/DeletePostByPostID', { params: { postID, postOwnerID } }),
  
  // 取消举报帖子
  cancleReportPost: (postID: number) =>
    api.get<{ message: string }>('/Post/CancleReportPost', { params: { postID } }),
};

// 评论相关API
export const commentApi = {
  // 根据帖子ID获取评论
  getCommentByPostID: (postID: number) => 
    api.get<any[]>('/Comment/GetCommentByPostID', { params: { postID } }),
  
  // 发布评论
  publishComment: (data: any) => 
    api.post<any>('/Comment/PublishComment', data),
  
  // 删除评论
  deleteComment: (commentID: number) =>
    api.delete<string>('/Comment/DeleteComment', { params: { commentID } }),
};

// 消息相关API
export const messageApi = {
  // 发送消息
  sendMessage: (data: any) => 
    api.post<any>('/Message/SendMessage', data)
}; 