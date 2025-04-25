<template>
  <div class="fitness-plan-container">
    <div class="button-group">
      <slot name="before-buttons"></slot>
      
      <el-button @click="handleGeneratePlan" class="button" type="primary">
        生成健身计划
      </el-button>

      <el-popover placement="bottom" :width="400" trigger="click">
        <template #reference>
          <el-button class="map" type="primary">查看我的健身进度</el-button>
        </template>
        <el-calendar :range="[startDate, endDate]">
          <template #date-cell="{ data }">
            <div :class="getCellStyle(data.day)"></div>
          </template>
        </el-calendar>
        <p>本月计划已完成<span class="bold-text">{{ completedWorkoutsCount }}</span>天</p>
      </el-popover>
      
      <slot name="after-buttons"></slot>
    </div>

    <el-collapse class="list" v-model="activeName">
      <el-collapse-item
        v-for="(week, index) in planData"
        :key="index"
        :title="titles[index]"
        :name="index"
        class="custom-collapse-item"
      >
        <el-timeline v-if="week && week.length" class="line">
          <el-timeline-item
            v-for="(item, index2) in week"
            :key="index2"
            :timestamp="item.timestamp"
            class="custom-timeline-item"
            placement="top"
          >
            <el-container>
              <el-popover placement="right" :width="500" trigger="click">
                <template #reference>
                  <el-card class="card" shadow="hover">
                    <img :src="item.coverUrl || ''" alt="Event Image" class="event-image" />
                    <template #footer>{{ item.workoutName || '未命名训练' }}</template>
                  </el-card>
                </template>
                <div style="padding-left: 0">
                  <el-table 
                    :data="item.exercises || []" 
                    :row-style="{height:50+'px'}" 
                    stripe 
                    class="table"
                  >
                    <el-table-column prop="exerciseName" label="动作" width="150" />
                    <el-table-column prop="count" label="组数" />
                    <el-table-column prop="time" label="时间" />
                    <el-table-column label="演示">
                      <template #default="{ row }">
                        <div>
                          <img 
                            @click="openExerciseDetail(row)" 
                            :src="row.coverUrl || ''" 
                            alt="演示" 
                            class="gif" 
                          />
                        </div>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <el-button 
                  @click="completeWorkout(index, index2)" 
                  class="over" 
                  type="primary" 
                  :disabled="isWorkoutCompleted(index, index2)"
                >
                  {{ isWorkoutCompleted(index, index2) ? '今日计划已完成' : '完成计划' }}
                </el-button>
              </el-popover>
            </el-container>
          </el-timeline-item>
        </el-timeline>
        <div v-else class="empty-week">该周暂无计划</div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, defineProps } from 'vue';
import { ElMessageBox, ElNotification } from 'element-plus';
import dayjs from 'dayjs';
import { fitnessPlanApi } from '../api/services';
import { handleSpecialApiResponse } from '../api/apiUtils';
import type { Exercise, WorkoutDay } from '../types/api';

// 定义props
defineProps({
  showTestButton: {
    type: Boolean,
    default: true
  }
});

// 健身计划数据
const planData = ref<WorkoutDay[][]>([]);
const completedWorkouts = ref<Record<string, boolean>>({});
const loading = ref(false);

// UI 状态
const activeName = ref([0]);
const titles = ["第一周", "第二周", "第三周", "第四周"];

// 日历相关
const startDate = ref(dayjs().startOf('month').toDate());
const endDate = ref(dayjs().endOf('month').toDate());

// 计算已完成的锻炼天数
const completedWorkoutsCount = computed(() => {
  return Object.keys(completedWorkouts.value).filter(key => completedWorkouts.value[key]).length;
});

/**
 * 生成工作日键名
 */
function getWorkoutKey(weekIndex: number, dayIndex: number): string {
  return `week${weekIndex}-day${dayIndex}`;
}

/**
 * 获取日历单元格样式
 */
function getCellStyle(day: string): string {
  const date = dayjs(day);
  // 检查是否有完成该日期的锻炼
  // 这里简化处理，实际应用需要更精确的日期匹配逻辑
  for (let i = 0; i < planData.value.length; i++) {
    const week = planData.value[i];
    if (!week) continue;
    
    for (let j = 0; j < week.length; j++) {
      const workout = week[j];
      if (!workout) continue;
      
      // 如果时间戳包含当前日期，且已完成，则返回空样式
      if (workout.timestamp.includes(date.format('MM-DD')) && 
          isWorkoutCompleted(i, j)) {
        return '';
      }
    }
  }
  return 'calendar-day';
}

/**
 * 检查锻炼是否已完成
 */
function isWorkoutCompleted(weekIndex: number, dayIndex: number): boolean {
  const key = getWorkoutKey(weekIndex, dayIndex);
  
  // 首先检查本地状态
  if (completedWorkouts.value[key]) {
    return true;
  }
  
  // 然后检查后端数据
  if (planData.value[weekIndex] && 
      planData.value[weekIndex][dayIndex]) {
    const isCompleted = planData.value[weekIndex][dayIndex].isCompleted === "true";
    // 更新本地状态
    if (isCompleted) {
      completedWorkouts.value[key] = true;
    }
    return isCompleted;
  }
  
  return false;
}

/**
 * 完成锻炼计划
 */
function completeWorkout(weekIndex: number, dayIndex: number) {
  const key = getWorkoutKey(weekIndex, dayIndex);
  completedWorkouts.value[key] = true;
  
  // 计算实际索引
  const workoutIndex = weekIndex * 7 + dayIndex;
  
  // 调用接口更新完成状态
  const token = localStorage.getItem('token');
  fetch(`http://localhost:8080/api/Achievement/UpdateFitnessPlanAchievement?token=${token}&workoutIndex=${workoutIndex}`)
    .then(response => response.json())
    .then(() => {
      ElNotification({
        message: '已完成今日计划',
        type: 'success',
        duration: 2000
      });
      // 刷新数据
      loadWeeksData(false);
    })
    .catch(error => {
      console.error('完成计划失败:', error);
      ElNotification({
        message: '完成计划失败，请重试',
        type: 'error',
        duration: 2000
      });
    });
}

/**
 * 打开锻炼详情
 */
function openExerciseDetail(exercise: Exercise) {
  if (!exercise.gifUrl) {
    ElNotification({
      message: '暂无演示视频',
      type: 'info',
      duration: 2000
    });
    return;
  }
  
  ElMessageBox.alert(
    `<div class="video-div"><video controls autoplay width="90%" src="${exercise.gifUrl}"></video></div>`,
    exercise.explanation || '锻炼详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确定',
      customStyle: {
        'max-width': '45%',
        height: '90%'
      }
    }
  );
}

/**
 * 处理生成计划按钮点击
 */
function handleGeneratePlan(event: MouseEvent) {
  loadWeeksData(true);
}

/**
 * 加载健身计划数据
 */
function loadWeeksData(showNotification = true) {
  if (loading.value) return;
  
  loading.value = true;
  
  fetch(`http://localhost:8080/api/FitnessPlan/GetPlan?token=${localStorage.getItem('token')}`)
    .then(response => response.json())
    .then(data => {
      if (data && typeof data === 'object') {
        // 确保数据结构完整
        if (!data.plan || !Array.isArray(data.plan)) {
          data.plan = [[], [], [], []];
        }
        
        // 设置数据
        planData.value = data.plan;
        
        // 更新完成状态
        updateCompletedStatus();
        
        if (showNotification) {
          if (data.message !== "fail") {
            ElNotification({
              message: "健身计划成功生成！",
              type: 'success',
              duration: 2000
            });
          } else {
            ElNotification({
              message: "请先填写体测表！",
              type: 'error',
              duration: 2000
            });
          }
        }
      } else {
        ElNotification({
          message: "数据格式错误",
          type: 'error',
          duration: 2000
        });
      }
    })
    .catch(error => {
      console.error('获取健身计划失败:', error);
      ElNotification({
        message: "获取健身计划失败",
        type: 'error',
        duration: 2000
      });
    })
    .finally(() => {
      loading.value = false;
    });
}

/**
 * 更新完成状态
 */
function updateCompletedStatus() {
  // 重置状态
  completedWorkouts.value = {};
  
  // 遍历计划，更新完成状态
  planData.value.forEach((week, weekIndex) => {
    if (!week) return;
    
    week.forEach((workout, dayIndex) => {
      if (!workout) return;
      
      if (workout.isCompleted === "true") {
        const key = getWorkoutKey(weekIndex, dayIndex);
        completedWorkouts.value[key] = true;
      }
    });
  });
}

// 组件挂载时加载数据
onMounted(() => {
  loadWeeksData(false);
});
</script>

<style scoped>
.fitness-plan-container {
  position: relative;
  padding: 20px;
}

.button-group {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  gap: 20px;
}

.bold-text {
  font-size: 30px;
  font-weight: bold;
  color: red;
}

.button {
  position: relative;
  width: 120px;
  height: 40px;
}

.map {
  position: relative;
  width: 150px;
  height: 40px;
}

.list {
  width: 80%;
  margin: 0 auto;
  margin-top: 20px;
}

.line {
  position: relative;
  left: 10px;
  top: 10px;
}

.over {
  position: relative;
  margin-top: 10px;
  left: 38%;
}

.empty-week {
  text-align: center;
  padding: 20px;
  color: #999;
}

.card {
  max-width: 300px;
  height: 200px;
}

.event-image {
  width: 100%;
  height: auto;
  max-height: 166px;
  object-fit: cover;
}

.table {
  width: 100%;
  max-height: 325px;
  overflow-y: auto;
}

.gif {
  width: 50px;
  height: 50px;
  object-fit: cover;
  cursor: pointer;
}

.gif:hover {
  content: url(../assets/images/strength.png);
}

.calendar-day {
  height: 30px;
  width: 30px;
  background-image: url('../assets/images/complete.png');
  background-size: contain;
  background-repeat: no-repeat;
}

.el-container {
  width: 300px;
  height: 200px;
}

/* Deep selectors for Element Plus components */
:deep(.custom-timeline-item .el-timeline-item__timestamp) {
  font-size: 16px;
  font-weight: bold;
  color: black;
  padding: 5px 10px;
  border-radius: 5px;
  text-align: left;
}

:deep(.custom-timeline-item .el-timeline-item__tail) {
  border-color: red;
}

:deep(.custom-timeline-item .el-timeline-item__node) {
  background-color: rgb(72, 147, 255);
}

:deep(.el-card:hover) {
  box-shadow: 5px 10px 16px #0ff !important;
}

:deep(.el-card:active) {
  box-shadow: 5px 10px 16px #ff5858 !important;
}

:deep(.el-calendar__header) {
  display: none;
}

:deep(.el-calendar-table thead th) {
  display: none;
}

:deep(.el-calendar-table .el-calendar-day:hover) {
  background-color: inherit;
}

:deep(.el-card__body) {
  height: 166px;
}

:deep(.el-card .el-card__footer) {
  padding-top: 0;
  color: white;
  font-size: 1.2em;
  font-weight: bold;
  background-color: darkblue;
}

:deep(.el-calendar-table .el-calendar-day) {
  width: 60px;
  height: 40px;
}

:deep(.el-calendar) {
  width: 350px;
  height: 200px;
  padding-bottom: 0;
}
</style>
