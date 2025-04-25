// API 响应通用结构
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

// 用户相关接口
export interface User {
  userID: number;
  username: string;
  nickname?: string;
  avatar?: string;
  email?: string;
  phone?: string;
  vigorTokenBalance?: number;
}

// 饮食记录相关接口
export interface MealRecord {
  recordID: number;
  userID: number;
  mealType: string; // 早餐、午餐、晚餐、加餐
  foods: string[]; // 食物列表
  calories: number; // 总卡路里
  date: string; // 格式 YYYY-MM-DD
  diningAdvice?: string; // AI建议
  loading?: boolean; // 前端状态标记
  totalCalorie?: number; // 总卡路里
}

// 食物信息接口
export interface Food {
  foodName: string;
  calorie: number; // 每100g卡路里
}

// 食谱接口
export interface Recipe {
  recipeID: number;
  title: string;
  imgUrl: string;
  content: string;
  releaseTime: string; // ISO日期字符串
}

// 饮食计划接口
export interface MealPlan {
  planID: number;
  userID: number;
  title: string;
  description: string;
  mealItems: MealItem[];
  totalCalories: number;
  createdAt: string;
  updatedAt: string;
}

export interface MealItem {
  itemID: number;
  mealType: string;
  foods: Food[];
  calories: number;
}

// 健身计划相关接口
export interface Exercise {
  exerciseID?: number;
  exerciseName: string;
  category: string;
  gifUrl?: string;
  coverUrl?: string;
  explanation?: string;
  count?: string; // 组数
  time?: string; // 时间
}

export interface WorkoutDay {
  timestamp: string;
  workoutName: string;
  coverUrl: string;
  isCompleted: string; // "true" 或 "false"
  exercises: Exercise[];
  date?: string; // 格式 YYYY-MM-DD
}

export interface PlanWeek {
  plan: WorkoutDay[][];
  message?: string;
}

export interface FitnessPlan {
  planID: number;
  userID: number;
  title: string;
  description: string;
  workoutDays: WorkoutDay[];
  createdAt: string;
  updatedAt: string;
}

// 体测数据接口
export interface PhysicalTest {
  height: number; // 身高(cm)
  weight: number; // 体重(kg)
  age: number;
  gender: string; // "male" 或 "female"
  bodyFatPercentage?: number; // 体脂率
  muscleMass?: number; // 肌肉量
  restingHeartRate?: number; // 静息心率
}

// 健身目标接口
export interface FitnessGoal {
  goalID: number;
  userID: number;
  goalType: string; // "weight_loss", "muscle_gain", "endurance", etc.
  targetValue: number;
  currentValue: number;
  unit: string; // "kg", "percentage", etc.
  deadline: string;
  createdAt: string;
}

// 成就接口
export interface Achievement {
  achievementID: number;
  userID: number;
  title: string;
  description: string;
  icon: string;
  unlockDate: string;
  category: string; // "fitness", "diet", etc.
}

// 通知接口
export interface Notification {
  notificationID: number;
  userID: number;
  title: string;
  content: string;
  isRead: boolean;
  createdAt: string;
  type: string; // "achievement", "reminder", etc.
} 