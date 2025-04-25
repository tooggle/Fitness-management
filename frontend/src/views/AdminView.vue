<template>
  <div class="admin-dashboard">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="admin-header">
        <div class="logo-container">
          <img src="../assets/images/logo.png" alt="Fitness" class="logo" />
          <span class="logo-text">Fitness</span>
        </div>
        <div class="user-container">
          <el-dropdown trigger="click">
            <div class="user-profile">
              <img :src="iconUrl" alt="User" class="avatar" />
              <span class="username">{{ store.state.userName || '管理员' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="dropdown-menu">
                <el-dropdown-item @click="goPage('/home')">
                  <el-icon><House /></el-icon>
                  <span>首页</span>
                </el-dropdown-item>
                <el-dropdown-item @click="openUser(userID)">
                  <el-icon><UserFilled /></el-icon>
                  <span>个人资料</span>
                </el-dropdown-item>
                <el-dropdown-item @click="navigateToLoginOut">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-container class="main-container">
        <!-- 侧边栏 -->
        <el-aside width="220px" class="admin-sidebar">
          <div class="sidebar-header">
            <el-icon><Setting /></el-icon>
            <span>管理控制台</span>
          </div>
          <el-menu
            :default-active="active.toString()"
            class="sidebar-menu"
            @open="handleOpen"
            @close="handleClose"
            text-color="#f0f0f0"
            active-text-color="#ffffff"
            background-color="transparent"
          >
            <el-menu-item index="1" @click="active = 1" class="menu-item">
              <el-icon><IconMenu /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="2" @click="active = 2" class="menu-item">
              <el-icon><Setting /></el-icon>
              <span>内容管理</span>
            </el-menu-item>
            <el-menu-item index="3" @click="active = 3" class="menu-item">
              <el-icon><Dish /></el-icon>
              <span>饮食管理</span>
            </el-menu-item>
            <el-menu-item index="4" @click="active = 4" class="menu-item">
              <el-icon><Basketball /></el-icon>
              <span>器械管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <!-- 主内容区域 -->
        <el-main class="admin-main">
          <!-- 用户管理界面 -->
          <div v-if="active == 1" class="content-section">
            <div class="section-header">
              <h2 class="section-title">用户管理</h2>
              <el-input 
                v-model="searchQuery" 
                placeholder="搜索用户ID或用户名" 
                clearable 
                class="search-box"
                prefix-icon="Search"
              />
            </div>
            
            <el-card class="content-card">
              <el-table 
                :data="filteredUsers" 
                style="width: 100%"
                border
                stripe
                class="data-table"
              >
                <el-table-column prop="email" label="邮箱地址"></el-table-column>
                <el-table-column prop="userName" label="用户名"></el-table-column>
                <el-table-column prop="userID" label="用户ID"></el-table-column>
                <el-table-column prop="registrationTime" label="注册时间">
                  <template #default="{ row }">
                    {{ formatDate(row.registrationTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="isMember" label="是否会员">
                  <template #default="{ row }">
                    <el-tag size="small" :type="row.isMember === 1 ? 'success' : 'info'">
                      {{ row.isMember === 1 ? '是' : '否' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态">
                  <template #default="{ row }">
                    <el-tag :type="getTagType(row)" :style="getTagStyle(row)" size="small">
                      {{ row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="250">
                  <template #default="{ row }">
                    <el-button 
                      size="small" 
                      :type="row.status === '禁言' ? 'primary' : 'danger'"
                      @click="row.status === '正常' ? restrictUser(row) : cancelBanUser(row)"
                      :disabled="row.status === '删除'"
                      class="action-button"
                    >
                      {{ row.status === '禁言' ? '取消禁言' : '限制言论' }}
                    </el-button>
                    <el-button 
                      size="small" 
                      type="warning" 
                      @click="deactivateUser(row)"
                      :disabled="row.status === '删除'"
                      class="action-button"
                    >
                      删除用户
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 内容管理界面 -->
          <div v-if="active == 2" class="content-section">
            <div class="section-header">
              <h2 class="section-title">内容管理</h2>
            </div>
            
            <el-card class="content-card">
              <el-table 
                :data="contentList" 
                style="width: 100%"
                border
                stripe
                class="data-table"
              >
                <el-table-column prop="postTitle" label="标题"></el-table-column>
                <el-table-column prop="userName" label="作者"></el-table-column>
                <el-table-column prop="postCategory" label="类型">
                  <template #default="{ row }">
                    <el-tag size="small" :type="row.postID ? 'info' : 'warning'">
                      {{ row.postID ? '帖子' : '评论' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="postTime" label="发布时间">
                  <template #default="{ row }">
                    {{ formatDate(row.postTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态">
                  <template #default="{ row }">
                    <el-tag :type="getTagType(row)" :style="getTagStyle(row)" size="small">
                      {{ row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="250">
                  <template #default="{ row }">
                    <el-button 
                      size="small" 
                      type="danger" 
                      @click="deleteContent(row)"
                      :disabled="row.status === '删除'"
                      class="action-button"
                    >
                      删除
                    </el-button>
                    <el-button 
                      size="small"
                      type="info" 
                      @click="navigateToPost(row)"
                      :disabled="row.status === '删除'"
                      class="action-button"
                    >
                      查看评论
                    </el-button>
                    <el-button 
                      size="small" 
                      type="primary" 
                      @click="cancelReport(row)"
                      :disabled="row.isReported === 0 || row.status === '删除'"
                      class="action-button"
                    >
                      取消举报
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!--饮食管理界面-->
          <div v-if="active === 3" class="content-section">
            <div class="section-header">
              <h2 class="section-title">饮食管理</h2>
            </div>
            
            <el-tabs type="border-card">
              <el-tab-pane label="食物管理">
                <div style="padding: 20px; max-width: 1200px; margin: 0 auto;">
                  <el-skeleton :rows="8" animated v-if="isLoading" />
                  <div v-else>
                    <el-table :data="foodData" style="width: 100%" border>
                      <el-table-column prop="foodName" label="食物名称" />
                      <el-table-column prop="calorie" label="热量(千卡)" />
                      <el-table-column label="操作" width="180">
                        <template #default="scope">
                          <el-button size="small" @click="editFood(scope.row, scope.$index)">编辑</el-button>
                          <el-button size="small" type="danger" @click="deleteFood(scope.row, scope.$index)">删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                    <div style="margin-top: 20px;">
                      <el-button type="primary" @click="addNewFood">添加食物</el-button>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="饮食分享">
                <div style="padding: 20px; max-width: 1200px; margin: 0 auto;">
                  <el-skeleton :rows="8" animated v-if="isLoading" />
                  <div v-else>
                    <el-card v-for="(item, index) in dietData" :key="index" style="margin-bottom: 15px;">
                      <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                          <span>{{ item.title }}</span>
                          <div>
                            <el-button size="small" text @click="editDiet(item, index)">编辑</el-button>
                            <el-button size="small" text type="danger" @click="deleteDiet(item, index)">删除</el-button>
                          </div>
                        </div>
                      </template>
                      <div style="display: flex; gap: 20px;">
                        <div style="width: 120px; height: 120px; background-color: #eee;"></div>
                        <div>{{ item.content }}</div>
                      </div>
                    </el-card>
                    <div style="margin-top: 20px;">
                      <el-button type="primary" @click="addNewDiet">添加饮食分享</el-button>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
            
            <!-- 食物编辑对话框 -->
            <el-dialog v-model="foodDialogVisible" :title="foodDialogTitle" width="30%">
              <el-form :model="currentFood" label-width="80px">
                <el-form-item label="食物名称">
                  <el-input v-model="currentFood.foodName" placeholder="请输入食物名称"></el-input>
                </el-form-item>
                <el-form-item label="食物热量">
                  <el-input-number v-model="currentFood.calorie" :min="1" :max="10000" label="热量(千卡)"></el-input-number>
                </el-form-item>
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="foodDialogVisible = false">取消</el-button>
                  <el-button type="primary" @click="saveFood">保存</el-button>
                </span>
              </template>
            </el-dialog>
            
            <!-- 饮食分享编辑对话框 -->
            <el-dialog v-model="dietDialogVisible" :title="dietDialogTitle" width="50%">
              <el-form :model="currentDiet" label-width="80px">
                <el-form-item label="标题">
                  <el-input v-model="currentDiet.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="内容">
                  <el-input v-model="currentDiet.content" type="textarea" :rows="5" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dietDialogVisible = false">取消</el-button>
                  <el-button type="primary" @click="saveDiet">保存</el-button>
                </span>
              </template>
            </el-dialog>
          </div>
          
          <!-- 器械管理界面 -->
          <div v-if="active === 4" class="content-section">
            <div class="section-header">
              <h2 class="section-title">器械管理</h2>
            </div>
            <!-- 集成器械管理组件 -->
            <adminEquipment />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive, shallowRef } from 'vue';
import store from '../store/index.js';
import { useRouter } from "vue-router";
import axios from 'axios';
import { ElNotification, ElMessageBox } from 'element-plus';
import { 
  House, 
  UserFilled, 
  Setting, 
  SwitchButton, 
  Basketball, 
  Dish, 
  Food, 
  Notebook, 
  Plus, 
  CirclePlusFilled,
  Search,
  Delete,
  Edit,
  DocumentAdd,
  DataAnalysis
} from '@element-plus/icons-vue';
import { IconMenu } from '@arco-design/web-vue/es/icon';
import AddFood from '../components/AddFood.vue';
import AddDiet from '../components/AddDiet.vue';
import adminEquipment from '../components/adminEquipment.vue';
import { userApi, mealPlanApi } from '../api/services.ts';
import { handleApiResponse, handleSpecialApiResponse } from '../api/apiUtils.ts';
import api from '../api/config.js';

const router = useRouter();
let active = ref(1);
let searchQuery = ref('');
let dialogVisible = ref(false);
let article = ref({
  content: '',
  title: '',
  comments: []
});
const userID = store.state.userID;

// 加载状态
const isLoading = ref(true);

// 食物数据
const foodData = ref([
  { foodName: '鸡胸肉', calorie: 165 },
  { foodName: '牛排', calorie: 271 },
  { foodName: '三文鱼', calorie: 208 },
  { foodName: '虾', calorie: 99 },
  { foodName: '鳄梨', calorie: 160 },
  { foodName: '香蕉', calorie: 89 },
  { foodName: '全麦面包', calorie: 81 },
  { foodName: '蓝莓', calorie: 57 }
]);

// 饮食分享数据
const dietData = ref([
  { title: '健康早餐食谱', content: '一天之计在于晨，健康的早餐可以为一天提供足够的能量。推荐搭配全麦面包、鸡蛋、牛奶和水果。' },
  { title: '低脂午餐方案', content: '午餐应该营养均衡但不宜过重。可选择鸡胸肉、糙米饭和蔬菜沙拉。控制油脂摄入，确保蛋白质充足。' },
  { title: '运动后补充能量', content: '剧烈运动后30分钟内是补充营养的黄金时间。建议摄入优质蛋白质如鸡蛋、酸奶，以及适量碳水化合物。' }
]);

// 器械数据
const equipmentData = ref([
  { name: '哑铃', description: '适合力量训练的基础器材', guide: '使用哑铃时需要保持正确姿势，避免关节受伤。' },
  { name: '跑步机', description: '室内有氧训练的理想选择', guide: '使用前应进行热身，从低速开始逐渐增加强度。' },
  { name: '卧推架', description: '锻炼胸肌的专业设备', guide: '使用前应调整高度和角度，确保舒适安全。' },
  { name: '引体向上器', description: '训练背部和手臂的有效设备', guide: '初学者可使用辅助装置，逐步增加训练强度。' },
  { name: '划船机', description: '全身性有氧训练器材', guide: '保持良好姿势，避免腰部受力过大。' },
  { name: '健身球', description: '提高核心稳定性的辅助器材', guide: '选择适合自己身高的尺寸，确保使用时安全稳定。' }
]);

// 食物管理对话框
const foodDialogVisible = ref(false);
const foodDialogTitle = ref('');
const currentFood = reactive({
  foodName: '',
  calorie: 0
});
const currentFoodIndex = ref(-1);
const isEditingFood = ref(false);

// 饮食分享对话框
const dietDialogVisible = ref(false);
const dietDialogTitle = ref('');
const currentDiet = reactive({
  title: '',
  content: ''
});
const currentDietIndex = ref(-1);
const isEditingDiet = ref(false);

// 器械管理对话框
const equipmentDialogVisible = ref(false);
const equipmentDialogTitle = ref('');
const currentEquipment = reactive({
  name: '',
  description: '',
  guide: ''
});
const currentEquipmentIndex = ref(-1);
const isEditingEquipment = ref(false);

// ===== 与SpringBoot后端交互的API方法 =====

// 食物管理API方法
async function fetchFoodsFromAPI() {
  try {
    isLoading.value = true;
    const response = await handleSpecialApiResponse(
      mealPlanApi.getFoodsInfo(),
      {
        showSuccessNotification: false
      }
    );
    
    if (response && response.foodsInfo) {
      foodData.value = response.foodsInfo.map(item => ({
        foodName: item.foodName,
        calorie: item.calories
      }));
    }
  } catch (error) {
    console.error('获取食物数据失败', error);
    ElNotification({
      title: '错误',
      message: '获取食物数据失败，显示本地模拟数据',
      type: 'error'
    });
  } finally {
    isLoading.value = false;
  }
}

async function saveFoodToAPI(food) {
  try {
    if (isEditingFood.value) {
      // 更新食物
      await handleApiResponse(
        mealPlanApi.updateFoodInfo({
          foodName: food.foodName,
          calorie: food.calorie
        }),
        {
          successMessage: `食物"${food.foodName}"已成功更新`
        }
      );
    } else {
      // 添加食物
      await handleApiResponse(
        mealPlanApi.insertFoodInfo({
          foodName: food.foodName,
          calorie: food.calorie
        }),
        {
          successMessage: `食物"${food.foodName}"已成功添加`
        }
      );
    }
    // 重新获取食物列表
    await fetchFoodsFromAPI();
  } catch (error) {
    console.error('保存食物失败', error);
  }
}

async function deleteFoodFromAPI(foodName) {
  try {
    await handleApiResponse(
      mealPlanApi.deleteFoodInfo(foodName),
      {
        successMessage: `食物"${foodName}"已成功删除`
      }
    );
    // 重新获取食物列表
    await fetchFoodsFromAPI();
  } catch (error) {
    console.error('删除食物失败', error);
  }
}

// 饮食分享API方法
async function fetchRecipesFromAPI() {
  try {
    isLoading.value = true;
    const response = await handleSpecialApiResponse(
      mealPlanApi.getAllRecipes(),
      {
        showSuccessNotification: false
      }
    );
    
    if (response && response.recipes) {
      dietData.value = response.recipes.map(item => ({
        recipeID: item.recipeID,
        title: item.title,
        content: item.content,
        imgUrl: item.imgUrl || '',
        releaseTime: new Date(item.releaseTime)
      }));
    }
  } catch (error) {
    console.error('获取饮食分享数据失败', error);
    ElNotification({
      title: '错误',
      message: '获取饮食分享数据失败，显示本地模拟数据',
      type: 'error'
    });
  } finally {
    isLoading.value = false;
  }
}

async function saveRecipeToAPI(recipe) {
  try {
    if (isEditingDiet.value && recipe.recipeID) {
      // 更新饮食分享
      await handleApiResponse(
        mealPlanApi.updateRecipe({
          recipeID: recipe.recipeID,
          title: recipe.title,
          content: recipe.content,
          imgUrl: recipe.imgUrl || ''
        }),
        {
          successMessage: `饮食分享"${recipe.title}"已成功更新`
        }
      );
    } else {
      // 添加饮食分享
      await handleApiResponse(
        mealPlanApi.insertRecipe({
          title: recipe.title,
          content: recipe.content,
          imgUrl: recipe.imgUrl || ''
        }),
        {
          successMessage: `饮食分享"${recipe.title}"已成功添加`
        }
      );
    }
    // 重新获取饮食分享列表
    await fetchRecipesFromAPI();
  } catch (error) {
    console.error('保存饮食分享失败', error);
  }
}

async function deleteRecipeFromAPI(recipeID) {
  try {
    if (!recipeID) {
      throw new Error('缺少饮食分享ID');
    }
    await handleApiResponse(
      mealPlanApi.deleteRecipe(recipeID),
      {
        successMessage: '饮食分享已成功删除'
      }
    );
    // 重新获取饮食分享列表
    await fetchRecipesFromAPI();
  } catch (error) {
    console.error('删除饮食分享失败', error);
  }
}

// 设备管理API方法 (假设后端有这些接口)
async function fetchEquipmentFromAPI() {
  try {
    isLoading.value = true;
    // 使用统一的api实例替代createAxiosWithToken
    const response = await api.get('/Equipment/GetAllEquipment');
    if (response.data && response.data.data && response.data.data.equipment) {
      equipmentData.value = response.data.data.equipment;
    }
  } catch (error) {
    console.error('获取器材数据失败', error);
    ElNotification({
      title: '错误',
      message: '获取器材数据失败，显示本地模拟数据',
      type: 'error'
    });
  } finally {
    isLoading.value = false;
  }
}

// 前端方法与后端API结合

// 食物管理方法
function addNewFood() {
  foodDialogTitle.value = '添加新食物';
  currentFood.foodName = '';
  currentFood.calorie = 0;
  currentFoodIndex.value = -1;
  isEditingFood.value = false;
  foodDialogVisible.value = true;
}

function editFood(food, index) {
  foodDialogTitle.value = '编辑食物';
  currentFood.foodName = food.foodName;
  currentFood.calorie = food.calorie;
  currentFoodIndex.value = index;
  isEditingFood.value = true;
  foodDialogVisible.value = true;
}

function deleteFood(food, index) {
  ElMessageBox.confirm(`确定要删除食物"${food.foodName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 尝试调用API删除
    deleteFoodFromAPI(food.foodName).catch(() => {
      // 如果API调用失败，则在本地数据中删除
      foodData.value.splice(index, 1);
    });
  }).catch(() => {});
}

function saveFood() {
  if (!currentFood.foodName.trim()) {
    ElNotification({
      title: '错误',
      message: '食物名称不能为空',
      type: 'error'
    });
    return;
  }
  
  if (currentFood.calorie <= 0) {
    ElNotification({
      title: '错误',
      message: '热量必须大于0',
      type: 'error'
    });
    return;
  }
  
  // 尝试调用API保存
  saveFoodToAPI({...currentFood}).catch(() => {
    // 如果API调用失败，则在本地数据中保存
    if (isEditingFood.value) {
      foodData.value[currentFoodIndex.value] = { ...currentFood };
    } else {
      foodData.value.push({ ...currentFood });
    }
  });
  
  foodDialogVisible.value = false;
}

// 饮食分享方法
function addNewDiet() {
  dietDialogTitle.value = '添加饮食分享';
  currentDiet.title = '';
  currentDiet.content = '';
  currentDiet.imgUrl = '';
  currentDietIndex.value = -1;
  isEditingDiet.value = false;
  dietDialogVisible.value = true;
}

function editDiet(diet, index) {
  dietDialogTitle.value = '编辑饮食分享';
  currentDiet.title = diet.title;
  currentDiet.content = diet.content;
  currentDiet.imgUrl = diet.imgUrl || '';
  currentDiet.recipeID = diet.recipeID;
  currentDietIndex.value = index;
  isEditingDiet.value = true;
  dietDialogVisible.value = true;
}

function deleteDiet(diet, index) {
  ElMessageBox.confirm(`确定要删除"${diet.title}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 尝试调用API删除
    if (diet.recipeID) {
      deleteRecipeFromAPI(diet.recipeID).catch(() => {
        // 如果API调用失败，则在本地数据中删除
        dietData.value.splice(index, 1);
      });
    } else {
      // 如果没有recipeID，就只在本地删除
      dietData.value.splice(index, 1);
      ElNotification({
        title: '成功',
        message: `已删除饮食分享：${diet.title}`,
        type: 'success'
      });
    }
  }).catch(() => {});
}

function saveDiet() {
  if (!currentDiet.title.trim()) {
    ElNotification({
      title: '错误',
      message: '标题不能为空',
      type: 'error'
    });
    return;
  }
  
  if (!currentDiet.content.trim()) {
    ElNotification({
      title: '错误',
      message: '内容不能为空',
      type: 'error'
    });
    return;
  }
  
  // 尝试调用API保存
  saveRecipeToAPI({...currentDiet}).catch(() => {
    // 如果API调用失败，则在本地数据中保存
    if (isEditingDiet.value) {
      dietData.value[currentDietIndex.value] = { ...currentDiet };
    } else {
      dietData.value.push({ ...currentDiet });
    }
  });
  
  dietDialogVisible.value = false;
}

// 器械管理方法
function addNewEquipment() {
  equipmentDialogTitle.value = '添加新器械';
  currentEquipment.name = '';
  currentEquipment.description = '';
  currentEquipment.guide = '';
  currentEquipmentIndex.value = -1;
  isEditingEquipment.value = false;
  equipmentDialogVisible.value = true;
}

function editEquipment(equipment, index) {
  equipmentDialogTitle.value = '编辑器械';
  currentEquipment.name = equipment.name;
  currentEquipment.description = equipment.description;
  currentEquipment.guide = equipment.guide || '';
  currentEquipmentIndex.value = index;
  isEditingEquipment.value = true;
  equipmentDialogVisible.value = true;
}

function deleteEquipment(equipment, index) {
  ElMessageBox.confirm(`确定要删除器械"${equipment.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    equipmentData.value.splice(index, 1);
    ElNotification({
      title: '成功',
      message: `已删除器械：${equipment.name}`,
      type: 'success'
    });
  }).catch(() => {});
}

function saveEquipment() {
  if (!currentEquipment.name.trim()) {
    ElNotification({
      title: '错误',
      message: '器械名称不能为空',
      type: 'error'
    });
    return;
  }
  
  if (!currentEquipment.description.trim()) {
    ElNotification({
      title: '错误',
      message: '器械描述不能为空',
      type: 'error'
    });
    return;
  }
  
  if (isEditingEquipment.value) {
    // 编辑现有器械
    equipmentData.value[currentEquipmentIndex.value] = { ...currentEquipment };
    ElNotification({
      title: '成功',
      message: `器械"${currentEquipment.name}"已更新`,
      type: 'success'
    });
  } else {
    // 添加新器械
    equipmentData.value.push({ ...currentEquipment });
    ElNotification({
      title: '成功',
      message: `器械"${currentEquipment.name}"已添加`,
      type: 'success'
    });
  }
  
  equipmentDialogVisible.value = false;
}

checkAvailable();
// 用户信息数据结构
const users = ref([]);
const iconUrl = store.state.iconUrl;
// 帖子数据结构
const contentList = ref([]);

// 在页面加载后初始化数据
onMounted(() => {
  checkAvailable();
  fetchUsers();
  fetchPosts();
  
  // 尝试从API获取数据
  Promise.all([
    fetchFoodsFromAPI().catch(() => console.log('使用本地食物数据')),
    fetchRecipesFromAPI().catch(() => console.log('使用本地饮食分享数据')),
    fetchEquipmentFromAPI().catch(() => console.log('使用本地器械数据'))
  ]).finally(() => {
    isLoading.value = false;
  });
});

// 过滤用户数据
const filteredUsers = computed(() => {
  if (searchQuery.value === '') {
    return users.value;
  }
  return users.value.filter(user => user.userName.includes(searchQuery.value) || String(user.userID).includes(searchQuery.value));
});

// 获取用户信息
async function fetchUsers() {
  try {
    const response = await api.get(`/User/GetAllUser`);
    users.value = response.data;
    users.value.sort((a, b) => new Date(a.registrationTime) - new Date(b.registrationTime));

    users.value.forEach(user => {
      if (user.isDelete === 1) {
        user.status = '删除';
      } else if (user.isPost === 0) {
        user.status = '禁言';
      } else {
        user.status = '正常';
      }
    });
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '获取用户信息失败，请稍后再试。',
      type: 'error',
    });
  }
}

// 获取帖子信息
async function fetchPosts() {
  try {
    const response = await api.get(`/Post/GetAllPost`);
    contentList.value = response.data;
    
    // 判断帖子是否被举报
    contentList.value.forEach(post => {
      if (post.isReported === 1) {
        post.status = '被举报';
      } else {
        post.status = '正常';
      }
    });

    // 按照发布时间从小到大排序
    contentList.value.sort((a, b) => new Date(a.postTime) - new Date(b.postTime));
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '获取帖子信息失败，请稍后再试。',
      type: 'error',
    });
  }
}

// 取消举报操作
async function cancelReport(post) {
  try {
    const response = await api.get('/Post/CancleReportPost', {
      params: {
        postID: post.postID,
      }
    });
    if (response.data.message === '成功取消举报') {
      post.isReported = 0;
      post.status = '正常';
      ElNotification({
        title: '成功',
        message: `已成功取消举报该帖子：${post.postTitle}`,
        type: 'success',
      });
    }
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '取消举报失败，请稍后再试。',
      type: 'error',
    });
  }
}

// 获取标签样式
function getTagStyle(row) {
  if (row.status === '正常') return { backgroundColor: 'green', color: 'white' };
  if (row.status === '被举报') return { backgroundColor: 'orange', color: 'white' };
  if (row.status === '删除') return { backgroundColor: 'red', color: 'white' };
  return {};
}

// 获取标签类型
function getTagType(row) {
  if (row.status === '正常') return 'success';
  if (row.status === '被举报') return 'warning';
  if (row.status === '删除') return 'danger';
  return 'info';
}

function checkAvailable() {
  let token = localStorage.getItem('token');
  if (token == null) {
    ElNotification({
      title: '提示',
      message: '请先登录',
      type: 'warning',
      duration: 2000
    })
    router.push('/login')
    return;
  };
  
  api.get(`/User/GetTokenInvalidateRes`)
    .then(response => {
      console.log("登录状态:", response.data);
      if (!response.data) {
        ElNotification({
          title: '提示',
          message: '登录已过期，请重新登录',
          type: 'warning',
          duration: 2000
        });
        localStorage.removeItem('token');
        router.push('/login');
      }
    }).catch(error => {
      ElNotification({
        title: '错误',
        message: '获取用户信息失败',
        type: 'error',
      });
    });
}

// 页面加载时获取数据
onMounted(() => {
  checkAvailable();
  fetchUsers();
  fetchPosts();
  
  // 模拟数据加载
  setTimeout(() => {
    isLoading.value = false;
  }, 1000);
});

// 格式化日期
function formatDate(date) {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 用户管理操作
async function restrictUser(user) {
  try {
    const response = await api.get('/User/BanUser', {
      params: {
        userID: user.userID,
      }
    });
    if (response.data === '禁言成功') {
      user.status = '禁言';
      ElNotification({
        title: '成功',
        message: `用户 ${user.userName} 已成功禁言。`,
        type: 'success',
      });
    }
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '限制用户言论失败，请稍后再试。',
      type: 'error',
    });
  }
}

async function cancelBanUser(user) {
  try {
    const response = await api.get('/User/CancelBanUser', {
      params: {
        userID: user.userID,
      }
    });
    if (response.data === '取消禁言成功') {
      user.status = '正常';
      ElNotification({
        title: '成功',
        message: `用户 ${user.userName} 已成功取消禁言。`,
        type: 'success',
      });
    }
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '取消禁言失败，请稍后再试。',
      type: 'error',
    });
  }
}

async function deactivateUser(user) {
  try {
    const response = await api.get('/User/RemoveUser', {
      params: {
        userID: user.userID,
      }
    });
    if (response.data === '删除成功') {
      user.status = '删除';
      ElNotification({
        title: '成功',
        message: `用户 ${user.userName} 已成功删除。`,
        type: 'success',
      });
    }
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '删除用户失败，请稍后再试。',
      type: 'error',
    });
  }
}

function navigateToLoginOut() {
  goPage('/login')

  localStorage.removeItem('token')
}

// 内容管理操作
async function deleteContent(content) {
  try {
    let response;
    if (content.postID) {
      response = await api.delete('/Post/DeletePostByPostID', {
        params: {
          postID: content.postID,
          postOwnerID: content.userID
        }
      });
      if (response.data.message === '删除帖子成功') {
        content.status = '删除';
        ElNotification({
          title: '成功',
          message: `帖子 ${content.postTitle} 已成功删除。`,
          type: 'success',
        });
      }
    } else if (content.commentID) {
      response = await api.delete('/Comment/DeleteComment', {
        params: {
          commentID: content.commentID,
        }
      })
      if (response.data === '评论删除成功') {
        content.status = '删除';
        ElNotification({
          title: '成功',
          message: '评论已成功删除。',
          type: 'success',
        });
      }
    }
  } catch (error) {
    ElNotification({
      title: '错误',
      message: '删除内容失败，请稍后再试。',
      type: 'error',
    });
  }
}

// 跳转到帖子操作
function navigateToPost(content) {
  if (content.postID) {
    router.push({
      path: `/post/${content.postID}`,
    });
  }
}

// 导航操作
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};

const handleClose = (key, keyPath) => {
  console.log(key, keyPath);
};

const openUser = (userID) => {
  userID = userID || store.state.userID;
  router.push({
    path: `/user/${userID}`,
  });
};

const goPage = (path) => {
  router.push({
    path,
  });
};

// 创建组件引用
const foodManagementComponent = shallowRef(AddFood);
const dietSharingComponent = shallowRef(AddDiet);
const equipmentManagementComponent = shallowRef(adminEquipment);
</script>

<style>
#app {
  max-width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
  width: 100% !important;
  overflow-x: hidden !important;
}

body {
  margin: 0 !important;
  padding: 0 !important;
  overflow-x: hidden !important;
  width: 100% !important;
}

.admin-dashboard {
  width: 100% !important;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow-x: hidden !important;
  margin: 0 !important;
  padding: 0 !important;
}

.el-container {
  width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
}

/* 顶部导航栏样式 */
.admin-header {
  height: 60px;
  padding: 0 20px;
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 10;
  width: 100% !important;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  margin-left: 10px;
  background: linear-gradient(90deg, #42b883, #35495e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.user-container {
  display: flex;
  align-items: center;
}

.user-profile {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.user-profile:hover {
  background-color: #f5f7fa;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #42b883;
}

.username {
  margin-left: 8px;
  font-weight: 500;
  color: #606266;
}

.dropdown-menu {
  margin-top: 10px;
  border-radius: 8px;
  overflow: hidden;
}

.dropdown-menu .el-dropdown-menu__item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
}

.dropdown-menu .el-dropdown-menu__item i {
  margin-right: 10px;
}

/* 侧边栏样式 */
.admin-sidebar {
  background: linear-gradient(180deg, #35495e, #2c3e50);
  color: #ffffff;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 5;
  width: 220px !important;
}

.sidebar-header {
  height: 60px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  font-size: 1.1rem;
  font-weight: 600;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header .el-icon {
  margin-right: 10px;
}

.sidebar-menu {
  padding: 20px 0;
  border: none;
}

.menu-item {
  margin: 8px 0;
  border-radius: 0 30px 30px 0;
  margin-right: 16px;
  transition: all 0.3s ease;
}

.menu-item.is-active {
  background: rgba(66, 184, 131, 0.8) !important;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.menu-item i {
  margin-right: 10px;
}

/* 主内容区域样式 */
.main-container {
  height: calc(100vh - 60px);
  width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
}

.admin-main {
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
  width: 100% !important;
}

.content-section {
  margin-bottom: 30px;
  width: 100% !important;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #303133;
  margin: 0;
  position: relative;
  padding-left: 12px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  height: 1.2em;
  width: 4px;
  background-color: #42b883;
  border-radius: 2px;
}

.search-box {
  width: 300px;
}

.content-card {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.data-table {
  width: 100%;
}

.data-table th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

.action-button {
  margin: 4px 0;
  padding: 6px 12px;
  border-radius: 4px;
}

/* 饮食管理和器械管理样式优化 */
.diet-management {
  padding: 0 !important;
  width: 100%;
}

.diet-management .management-card {
  height: 100%;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.diet-management .el-table {
  margin-top: 15px;
  width: 100%;
}

.diet-management .el-col {
  padding: 10px;
}

.diet-management .card-header {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  border-bottom: 1px solid #ebeef5;
}

.diet-management .card-title {
  margin-left: 10px;
  font-weight: 600;
  color: #303133;
}

.diet-management .management-form {
  padding: 15px;
  overflow: auto;
  max-height: 600px;
}

/* 修复器械管理样式 */
.content-section .equipment-container {
  margin-bottom: 15px;
}

.content-section .card-content {
  display: flex;
  cursor: pointer;
}

.content-section .card-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.content-section .card-text {
  flex: 1;
}

.content-section .card-text h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #303133;
}

.content-section .card-text p {
  color: #606266;
  margin: 0;
}

.content-section .pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 上传组件样式 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.dialog-image {
  max-width: 100%;
  margin: 10px 0;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .admin-sidebar {
    width: 60px !important;
    overflow: hidden;
  }
  
  .sidebar-header span, 
  .menu-item span {
    display: none;
  }
  
  .menu-item {
    display: flex;
    justify-content: center;
    margin-right: 0;
  }
  
  .menu-item i {
    margin-right: 0;
  }
  
  .diet-management .el-col {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-box {
    width: 100%;
    margin-top: 10px;
  }
  
  .action-button {
    padding: 4px 8px;
    font-size: 12px;
  }
}

/* 美化滚动条 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #909399;
}
</style>
