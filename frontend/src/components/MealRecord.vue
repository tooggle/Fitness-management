<template>
  <el-row class="row-bg" justify="space-between">
    <el-col :span="17">
      <div class="recordContainer">
        <div class="container">
          <!--日期选择和上传按键-->
          <el-row class="row-bg" justify="space-between">
            <el-col :span="7">
              <el-date-picker size="large" :style="{ fontSize: '18px' }" v-model="selectedDate" type="date"
                placeholder="选择日期" :shortcuts="shortcuts" />
            </el-col>
            <el-col :span="5">
              <el-button type="primary" @click="handleCellClick" size="large" :style="{ fontSize: '18px' }">
                上传新记录<el-icon class="el-icon--right">
                  <Upload />
                </el-icon>
              </el-button>
            </el-col>
          </el-row>
          <!--记录展示-->
          <div class="cards-container">
            <el-row v-for="(record, index) in recordsForSelectedDate" :key="index" :gutter="20">
              <template v-if="index % 2 === 0">
                <el-col :span="8">
                  <div class="grid-content">
                    <img :src="record.mealPhoto" style="width: 80%; aspect-ratio: 1 / 1; " fit="fill" />
                  </div>
                </el-col>
                <el-col :span="16">
                  <div class="grid-content meal-details">
                    <h3 :style="{ fontSize: '24px !important' }">
                      <el-icon v-if="record.mealTime.getHours() >= 6 && record.mealTime.getHours() <= 18">
                        <Sunny />
                      </el-icon>
                      <el-icon v-else>
                        <Moon />
                      </el-icon>
                      {{ record.mealTime.getFullYear() }}年{{ record.mealTime.getMonth() + 1 }}月{{
                        record.mealTime.getDate()
                      }}日
                      {{ record.mealTime.getHours().toString().padStart(2, '0') }}:{{
                        record.mealTime.getMinutes().toString().padStart(2, '0') }}
                    </h3>
                    <el-table :data="record.foods" height="200" style="width: 100%"
                      :style="{ fontSize: '18px !important' }">
                      <el-table-column prop="foodName" label="食物名称" />
                      <el-table-column prop="quantity" label="食物数量/g" />
                    </el-table>
                    <div class="ai-suggestion-container">
                      <div class="ai-suggestion-header">
                        <h4 :style="{ fontSize: '20px !important' }">AI建议：</h4>
                      </div>
                      <el-skeleton v-if="record.loading" :rows="1" animated>
                        <template #template>
                          <el-skeleton-item style="width: 100%; height: 20px;" />
                        </template>
                      </el-skeleton>
                      <!-- 显示实际内容 -->
                      <div v-else class="content-container">
                        <p v-html="record.diningAdvice" class="left-align"
                          :style="{ height: '200px', overflowY: 'auto', fontSize: '16px !important' }"></p>
                      </div>
                    </div>
                  </div>
                </el-col>

                <el-col :span="18"></el-col>
                <el-button type="primary" @click="showPlan(record)" :style="{ fontSize: '17px !important' }">
                  修改<el-icon class="el-icon--right">
                    <Edit />
                  </el-icon>
                </el-button>
                <el-button type="danger" @click="deleteRecord(record)" :style="{ fontSize: '17px !important' }">
                  删除<el-icon class="el-icon--right">
                    <Delete />
                  </el-icon>
                </el-button>

                <el-divider border-style="dashed" style="margin-left: 15px;margin-right: 15px;" />
              </template>
              <template v-else>
                <el-col :span="16">
                  <div class="grid-content meal-details">
                    <h3 :style="{ fontSize: '24px !important' }">
                      <el-icon v-if="record.mealTime.getHours() >= 6 && record.mealTime.getHours() <= 18">
                        <Sunny />
                      </el-icon>
                      <el-icon v-else>
                        <Moon />
                      </el-icon>
                      {{ record.mealTime.getFullYear() }}年{{ record.mealTime.getMonth() + 1 }}月{{
                        record.mealTime.getDate()
                      }}日
                      {{ record.mealTime.getHours().toString().padStart(2, '0') }}:{{
                        record.mealTime.getMinutes().toString().padStart(2, '0') }}
                    </h3>
                    <el-table :data="record.foods" height="200" style="width: 100%"
                      :style="{ fontSize: '18px !important' }">
                      <el-table-column prop="foodName" label="食物名称" />
                      <el-table-column prop="quantity" label="食物数量/g" />
                    </el-table>
                    <div class="ai-suggestion-container">
                      <div class="ai-suggestion-header">
                        <h4 :style="{ fontSize: '20px !important' }">AI建议：</h4>
                      </div>
                      <el-skeleton v-if="record.loading" :rows="1" animated>
                        <template #template>
                          <el-skeleton-item style="width: 100%; height: 20px;" />
                        </template>
                      </el-skeleton>
                      <!-- 显示实际内容 -->
                      <div v-else class="content-container">
                        <p v-html="record.diningAdvice" class="left-align"
                          :style="{ height: '200px', overflowY: 'auto', fontSize: '18px !important' }"></p>
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content">
                    <img :src="record.mealPhoto" style="width: 80%; aspect-ratio: 1 / 1; " fit="fill" />
                  </div>
                </el-col>

                <el-col :span="18"></el-col>
                <el-button type="primary" @click="showPlan(record)" :style="{ fontSize: '17px !important' }">
                  修改<el-icon class="el-icon--right">
                    <Edit />
                  </el-icon>
                </el-button>
                <el-button type="danger" @click="deleteRecord(record)" :style="{ fontSize: '17px !important' }">
                  删除<el-icon class="el-icon--right">
                    <Delete />
                  </el-icon>
                </el-button>

                <el-divider border-style="dashed" style="margin-left: 15px;margin-right: 15px;" />
              </template>
            </el-row>
          </div>
        </div>
        <!--上传部分展示-->
        <el-dialog v-model="dialogVisible">
          <el-form ref="form">
            <h2>{{ this.formatDate(this.selectedDate) }} 饮食记录</h2>
            <h4>注意：输入自定义食物可能会导致热量计算不准确噢！</h4>
            <br>
            <el-upload class="avatar-uploader" :show-file-list="false" :before-upload="beforeAvatarUpload">
              <img v-if="imageUrl" :src="imageUrl" class="meal-avatar" />
              <el-icon v-else class="meal-avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
            <div class="flex gap-2">
              <div class="tags-container">
                <el-tag v-for="tag in dynamicTags" class="tag-block" :key="tag" closable :disable-transitions="false"
                  size="large" @close="handleClose(tag)">
                  {{ tag }}
                  <el-input-number v-model="tagQuantities[tag]" :min=0 :step=50 class="numInput" />
                  <span> g</span>
                </el-tag>
              </div>
              <div class="custom-select">
                <el-select v-if="inputVisible" ref="selectRef" v-model="inputValue" @change="handleInputConfirm"
                  @blur="handleInputConfirm" placeholder="输入/选择食物" filterable allow-create default-first-option
                  :style="{ width: '300px', height: '40px' }">
                  <el-option v-for="item in food" :key="item.value" :label="item.label" :value="item.value"
                    :disabled="isOptionDisabled(item.value)"></el-option>
                </el-select>
                <el-button v-else class="button-new-tag" :disabled="!canAdd" @click="showInput">
                  + 点击添加食物
                </el-button>
              </div>
            </div>
            <div style="margin: 20px 0" />
            <div style="display: flex; justify-content: center;">
              <el-form-item>
                <el-button type="primary" @click="saveEvent" :style="{ fontSize: '18px' }">保存</el-button>
              </el-form-item>
            </div>
          </el-form>
        </el-dialog>
        <!--修改部分展示-->
        <el-dialog v-model="modiVisible">
          <el-form ref="form">
            <el-upload class="avatar-uploader" :show-file-list="false" :before-upload="beforeAvatarUpload">
              <img v-if="currentRecord.mealPhoto" :src="currentRecord.mealPhoto" class="meal-avatar" />
              <el-icon v-else class="meal-avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
            <div class="flex gap-2">
              <div class="tags-container">
                <el-tag v-for="tag in this.selectedFoods" class="tag-block" :key="tag" closable size="large"
                  :disable-transitions="false" @close="handleClose(tag)">
                  {{ tag }}
                  <el-input-number v-model="tagQuantities[tag]" :min=0 :step=50 class="numInput" />
                  <span> g</span>
                </el-tag>
              </div>
              <div class="custom-select">
                <el-select v-if="inputVisible" ref="selectRef" v-model="inputValue" @change="handleInputConfirm"
                  @blur="handleInputConfirm" placeholder="输入/选择食物" filterable allow-create default-first-option
                  :style="{ width: '300px', height: '40px' }">
                  <el-option v-for="item in food" :key="item.value" :label="item.label" :value="item.value"
                    :disabled="isOptionDisabled(item.value)"></el-option>
                </el-select>
                <el-button v-else class="button-new-tag" :disabled="!canAdd" @click="showInput">
                  + 点击添加食物
                </el-button>
              </div>
            </div>
            <div style="margin: 20px 0" />
            <div style="display: flex; justify-content: center;">
              <el-form-item>
                <el-button type="primary" @click="saveModi">保存</el-button>
              </el-form-item>
            </div>
          </el-form>
        </el-dialog>
      </div>
    </el-col>
    <el-col :span="7">
      <div>
        <el-card class="ana-container">
          <template #header>
            <div class="card-header">
              <h2 :style="{ fontSize: '26px', color: 'white' }">每日饮食情况总结</h2>
            </div>
          </template>
          <div class="center-content">
            <button v-if="!anaLoading" class="button-custom" @click="getAna()">点击分析</button>
            <div v-else>
              <div v-if="!getAnalysis" class="loading-container">
                <div class="loader"></div>
                <div class="loading-text">
                  <p class="fade-in" style="color:black">日期：{{ this.formatDate(this.selectedDate) }}</p>
                  <p class="fade-in" style="animation-delay: 1s; color:black;">使用模型：通义千问</p>
                  <p class="fade-in" style="animation-delay: 2s; color:black;">当日摄取卡路里总量：{{ this.countCarlorie() }} kCal</p>
                  <p class="fade-in" style="animation-delay: 3s; color:black;">正在生成总结，请稍等...</p>
                </div>
              </div>
              <div v-else>
                <p v-html="this.AIanalysis" class="ana-style"></p>
              </div>
            </div>
          </div>
        </el-card>

      </div>
    </el-col>
  </el-row>
</template>

<script lang="ts">
import axios from 'axios';
import { marked } from 'marked';
import { ElNotification } from 'element-plus';
import anaBG from '../assets/images/anaBG.png';
import { onMounted, ref, defineComponent, watch } from 'vue';
import { mealRecordApi, userApi, mealPlanApi } from "../api/services";
import { MealRecord, Food } from "../types/api";
import { Upload, Edit, Delete, Plus, Sunny, Moon } from '@element-plus/icons-vue';

export default defineComponent({
  name: 'Record',
  components: {
    Upload,
    Edit,
    Delete,
    Plus,
    Sunny,
    Moon
  },
  data() {
    return {
      vigorTokenBalance: 0,
      selectedDate: new Date(),
      dialogVisible: false,
      modiVisible: false,
      imageUrl: '',
      currentRecord: {
        userID: 0,
        recordID: -1,
        mealTime: new Date(),
        foods: [],
        mealPhoto: "",
        totalNumOfFoods: 0,
        totalCalorie: 0,
        loading: true,
        diningAdvice: '',
      } as MealRecord,
      oneDayRecord: {} as Record<string, MealRecord[]>,
      canAdd: true,
      inputValue: '',
      dynamicTags: [] as string[],
      inputVisible: false,
      tagQuantities: {} as Record<string, number>,
      selectedFoods: [] as string[],
      food: [
        { value: '燕麦', label: '燕麦' },
        { value: '牛奶', label: '牛奶' },
        { value: '鱼', label: '鱼' },
      ],
      shortcuts: [
        {
          text: '今天',
          value: new Date(),
        },
        {
          text: '昨天',
          value: () => {
            const date = new Date();
            date.setDate(date.getDate() - 1);
            return date;
          },
        },
        {
          text: '一周前',
          value: () => {
            const date = new Date();
            date.setDate(date.getDate() - 7);
            return date;
          },
        },
      ],
      cardStyle: {
        backgroundImage: anaBG,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      },
      anaLoading: false,
      getAnalysis: false,
      AIanalysis: ""
    };
  },
  computed: {
    recordsForSelectedDate(): MealRecord[] {
      const formattedDate = this.formatDate(this.selectedDate);
      return this.oneDayRecord[formattedDate] || [];
    }
  },
  methods: {
    getAna() {
      const needToAna = this.formatDate(this.selectedDate);
      if (!this.oneDayRecord[needToAna]) {
        ElNotification({
          title: '注意',
          message: '当日记录不存在，无法生成总结!',
          type: 'warning',
          duration: 2000
        })
      }
      else {
        if (this.vigorTokenBalance < 30) {
          ElNotification({
            title: '注意',
            message: `本功能需要耗费30活力币，您的余额为${this.vigorTokenBalance}，余额不足!`,
            type: 'warning',
            duration: 2000
          })
          return
        }
        else {
          ElNotification({
            title: '注意',
            message: `本次消费30活力币，您的余额为${this.vigorTokenBalance - 30}`,
            type: 'success',
            duration: 2000
          })
        }
        this.anaLoading = true;
        this.getAIAnalysis();
      }
    },
    countCarlorie() {
      const needToCount = this.formatDate(this.selectedDate);
      let count = 0;
      for (let i = 0; i < this.oneDayRecord[needToCount].length; i++) {
        count += this.oneDayRecord[needToCount][i].totalCalorie;
      }
      return count;
    },
    handleCellClick(): void {
      this.currentRecord = {
        userID: 0,
        recordID: -1,
        mealTime: new Date(),
        foods: [],
        mealPhoto: "",
        totalNumOfFoods: 0,
        totalCalorie: 0,
        loading: true,
        diningAdvice: '',
      };
      this.imageUrl = '';
      this.dynamicTags = [];
      this.tagQuantities = {};
      this.selectedFoods = [];
      this.dialogVisible = false;
      this.selectedDate = new Date();
      this.dialogVisible = true;
    },
    showInput(): void {
      this.inputVisible = true;
    },
    handleInputConfirm(): void {
      if (this.inputValue && this.currentRecord.totalNumOfFoods < 5) {
        const existingItem = this.food.find(item => item.value === this.inputValue);
        if (!existingItem) {
          this.food.push({ value: this.inputValue, label: this.inputValue });
        }

        this.currentRecord.totalNumOfFoods++;
        this.dynamicTags.push(this.inputValue);
        this.tagQuantities[this.inputValue] = 0;
        this.selectedFoods.push(this.inputValue);

        this.inputVisible = false;
        this.inputValue = '';
        if (this.currentRecord.totalNumOfFoods === 5) {
          this.canAdd = false;
        }
      }
    },
    handleClose(tag: string): void {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      this.currentRecord.foods.splice(this.currentRecord.foods.indexOf(tag), 1);
      if (this.tagQuantities.hasOwnProperty(tag)) {
        delete this.tagQuantities[tag];
      }
      this.currentRecord.totalNumOfFoods--;
      if (this.currentRecord.totalNumOfFoods < 5) {
        this.canAdd = true;
      }
      this.selectedFoods = this.selectedFoods.filter(food => food !== tag);
    },
    isOptionDisabled(value: string): boolean {
      return this.selectedFoods.includes(value);
    },
    beforeAvatarUpload(file: File): boolean {
      this.imageUrl = '';
      const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGorPNG) {
        ElNotification({
          title: '错误',
          message: '上传图片只能是 JPG 或 PNG 格式!',
          type: 'error',
        });
        return false;
      }
      if (!isLt2M) {
        ElNotification({
          title: '错误',
          message: '上传图片大小不能超过2MB!',
          type: 'error',
        });
        return false;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageUrl = reader.result as string;
        this.currentRecord.mealPhoto = this.imageUrl;
      };
      return false;
    },
    formatDate(date: Date): string {
      if (!(date instanceof Date)) {
        return '';
      }
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    formatTime(date: Date): string {
      const hours = date.getHours().toString().padStart(2, '0');
      return `${hours}`;
    },
    saveEvent(): void {
      if (this.currentRecord.mealPhoto) {
        const now = new Date();
        this.currentRecord.mealTime = now;
        const currentDay = this.formatDate(now);
        if (!this.oneDayRecord[currentDay]) {
          this.oneDayRecord[currentDay] = [];
        }
        for (let i = 0; i < this.selectedFoods.length; i++) {
          if (this.tagQuantities[this.selectedFoods[i]] == 0) {
            this.currentRecord.foods = [];
            return;
          }
          const newFood = { foodName: this.selectedFoods[i], quantity: this.tagQuantities[this.selectedFoods[i]] };
          this.currentRecord.foods.push(newFood);
        }
        this.oneDayRecord[currentDay].push(this.currentRecord);
        this.sendRecordToDB();

        this.currentRecord = {
          userID: 0,
          recordID: -1,
          mealTime: new Date(),
          foods: [],
          mealPhoto: "",
          totalNumOfFoods: 0,
          totalCalorie: 0,
          loading: true,
          diningAdvice: '',
        };
        this.imageUrl = '';
        this.dynamicTags = [];
        this.tagQuantities = {};
        this.selectedFoods = [];
        this.dialogVisible = false;
      } else {
        ElNotification({
          title: '警告',
          message: '记录图片不可为空',
          type: 'warning',
          duration: 2000
        });
      }
    },
    deleteRecord(record: MealRecord): void {
      const needToDelete = this.formatDate(this.selectedDate);
      for (let i = 0; i < this.oneDayRecord[needToDelete].length; i++) {
        if (record === this.oneDayRecord[needToDelete][i]) {
          this.deleteRecordInDB(record.recordID);
          this.oneDayRecord[needToDelete].splice(i, 1);
          i--;
          break;
        }
      }
      if (this.oneDayRecord[needToDelete].length === 0) {
        delete this.oneDayRecord[needToDelete];
      }
    },
    showPlan(record: MealRecord): void {
      this.modiVisible = true;
      this.currentRecord = record;
      this.tagQuantities = {};
      this.selectedFoods = [];
      for (let i = 0; i < this.currentRecord.totalNumOfFoods; i++) {
        this.tagQuantities[this.currentRecord.foods[i].foodName] = this.currentRecord.foods[i].quantity;
        this.selectedFoods.push(this.currentRecord.foods[i].foodName);
      }
    },
    saveModi(): void {
      if (this.currentRecord.mealPhoto) {
        const needToModi = this.formatDate(this.selectedDate);
        if (!this.oneDayRecord[needToModi]) {
          this.oneDayRecord[needToModi] = [];
        }
        let i = -1;
        for (i = 0; i < this.oneDayRecord[needToModi].length; i++) {
          if (this.oneDayRecord[needToModi][i].recordID === this.currentRecord.recordID) {
            break;
          }
        }
        this.currentRecord.foods = [];
        for (let i = 0; i < this.selectedFoods.length; i++) {
          if (this.tagQuantities[this.selectedFoods[i]] == 0) {
            this.currentRecord.foods = [];
            return;
          }
          const newFood = { foodName: this.selectedFoods[i], quantity: this.tagQuantities[this.selectedFoods[i]] };
          this.currentRecord.foods.push(newFood);
        }
        this.currentRecord.loading = true;
        this.oneDayRecord[needToModi][i] = this.currentRecord;
        this.updateRecordToDB(this.currentRecord);
        this.modiVisible = false;
        this.currentRecord = {
          userID: 0,
          recordID: -1,
          mealTime: new Date(),
          foods: [],
          mealPhoto: "",
          totalNumOfFoods: 0,
          totalCalorie: 0,
          loading: true,
          diningAdvice: '',
        };
        this.imageUrl = '';
        this.dynamicTags = [];
        this.tagQuantities = {};
        this.selectedFoods = [];
        this.modiVisible = false;
      }
    },
    // 获取活力币余额
    getVigorTokenBalance(): void {
      userApi.getVigorTokenBalance()
        .then(response => {
          this.vigorTokenBalance = response.data.data.balance;
        }).catch(error => {
          this.vigorTokenBalance = 0;
          console.error("Error fetching vigorTokenBalance:", error);
        });
    },
    // 发送记录
    sendRecordToDB(): void {
      const requestData = {
        mealTime: this.currentRecord.mealTime,
        mealPhoto: this.currentRecord.mealPhoto,
        foods: this.currentRecord.foods.map(food => ({
          foodName: food.foodName,
          quantity: food.quantity
        }))
      };
      mealRecordApi.create(requestData)
        .then(response => {
          console.log("创建 ", requestData);
          console.log(response.data.message);
          ElNotification({
            message: response.data.message,
            type: 'success',
            duration: 2000
          });
          const check = this.formatDate(this.selectedDate);
          if (!this.oneDayRecord[check]) {
            this.oneDayRecord[check] = [];
          }
          for (let i = 0; i < this.oneDayRecord[check].length; i++) {
            if (this.oneDayRecord[check][i].mealTime === requestData.mealTime) {
              this.oneDayRecord[check][i].recordID = response.data.data.recordID;
              this.oneDayRecord[check][i].totalCalorie = response.data.data.totalCalorie/100.0;
              this.getAISuggestions(this.oneDayRecord[check][i].recordID);
              break;
            }
          }
        })
        .catch(error => {
          console.error("Error creating record:", error);
        });
    },
    getAISuggestions(recordID: number): void {
      console.log("获取AI建议", recordID);
      const maxAttempts = 20;

      const pollForAISuggestions = (attempts = 0) => {
        console.log("获取AI建议", recordID);
        mealRecordApi.getAISuggestions(recordID)
          .then(response => {
            console.log("轮询尝试次数:", attempts, "AI建议:", response.data);
            if (response.data && response.data.data.diningAdvice) {
              console.log("AI建议:", response.data.data.diningAdvice);
              const check = this.formatDate(this.selectedDate);
              for (let i = 0; i < this.oneDayRecord[check].length; i++) {
                if (this.oneDayRecord[check][i].recordID === recordID) {
                  this.oneDayRecord[check][i].diningAdvice = marked(response.data.data.diningAdvice);
                  this.oneDayRecord[check][i].loading = false;
                }
              }
              this.getVigorTokenBalance();
            } else if (attempts < maxAttempts) {
              setTimeout(() => pollForAISuggestions(attempts + 1), 1000);
            } else {
              console.error("AI suggestions could not be retrieved after 10 attempts.");
              ElNotification({
                message: "AI建议获取失败，请稍后重试",
                type: 'error',
                duration: 2000
              });
            }
          })
          .catch(error => {
            console.error("Error getting AI suggestions:", error);
          });
      };

      pollForAISuggestions();
    },
    getAIAnalysis(): void {
      const date = this.formatDate(this.selectedDate);
      console.log(date);
      mealRecordApi.getAISummary(date)
        .then(response => {
          console.log("AI建议:", response.data.message);
          this.getAnalysis = true;
          this.AIanalysis = marked(response.data.data.message);
        })
        .catch(error => {
          console.error("Error getting AI suggestions:", error);
        });
    },
    getFoodFromDB(): void {
      mealPlanApi.getFoodsInfo()
        .then(response => {
          console.log(response.data.data.foodsInfo);
          this.food = response.data.data.foodsInfo.map(item => ({
            value: item.foodName,
            label: item.foodName
          }));
        });
    },
    getRecordFromDB(date: Date): void {
      const formattedDate = this.formatDate(date);
      console.log(formattedDate);
      mealRecordApi.getAllDetails(formattedDate)
        .then(response => {
          this.oneDayRecord[formattedDate] = [];
          response.data.data.records.forEach(item => {
            console.log(response.data.data.records);
            const getrecordID = item.recordID;
            const foods = item.foods.map(foodItem => ({
              foodName: foodItem.foodName,
              quantity: foodItem.quantity
            }));
            const length = foods.length;
            this.oneDayRecord[formattedDate].push({
              userID: item.userID,
              recordID: getrecordID,
              mealTime: new Date(item.mealTime),
              foods: foods,
              mealPhoto: item.mealPhoto,
              totalCalorie: item.totalCalorie,
              totalNumOfFoods: length,
              diningAdvice: marked(item.diningAdvice),
              loading: false,
            });
          });
        });
    },
    updateRecordToDB(planContent: MealRecord): void {
      const requestData = {
        foods: planContent.foods.map(food => ({
          foodName: food.foodName,
          quantity: food.quantity
        })),
        mealPhoto: planContent.mealPhoto,
        mealTime: planContent.mealTime,
        recordID: planContent.recordID
      };
      console.log("更新", requestData);
      mealRecordApi.update(requestData)
        .then(response => {
          console.log(response.data.message);
          ElNotification({
            message: response.data.message,
            type: 'success',
            duration: 2000
          });
          const check = this.formatDate(planContent.mealTime);
          if (!this.oneDayRecord[check]) {
            this.oneDayRecord[check] = [];
          }
          for (let i = 0; i < this.oneDayRecord[check].length; i++) {
            if (this.oneDayRecord[check][i].recordID === planContent.recordID) {
              this.getAISuggestions(this.oneDayRecord[check][i].recordID);
            }
          }
        });
    },
    deleteRecordInDB(recordID: number): void {
      mealRecordApi.delete(recordID)
        .then(response => {
          console.log(response.data.message);
          ElNotification({
            message: response.data.message,
            type: 'success',
            duration: 2000
          });
        });
    },
  },
  mounted() {
    this.selectedDate = new Date();
  },
  created() {
    this.getFoodFromDB();
    this.selectedDate = new Date();
    this.getRecordFromDB(this.selectedDate);
    this.getVigorTokenBalance();
  },
  watch: {
    selectedDate(newDate: Date) {
      this.anaLoading = false;
      this.getAnalysis = false;
      this.AIanalysis = "";
      this.getRecordFromDB(newDate);
    }
  },
  setup() {
    const glowButton = ref<HTMLElement | null>(null);

    const handleMouseMove = (e: MouseEvent) => {
      const button = glowButton.value;
      if (!button) return;

      const rect = button.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;

      button.style.setProperty('--x', `${x}px`);
      button.style.setProperty('--y', `${y}px`);
    };

    onMounted(() => {
      if (glowButton.value) {
        glowButton.value.addEventListener('mousemove', handleMouseMove);
      }
    });

    return {
      glowButton,
    };
  },
});
</script>

<style scoped>
.recordContainer {
  background-color: rgb(247, 251, 254);
  width: 100%
}

.meal-item {
  margin-bottom: 20px;
}

.avatar-uploader .meal-avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.meal-avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.cards-container {
  margin-top: 10px;
  width: 100%;
}

.grid-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.meal-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
}

.ai-suggestion-container {
  width: 100%;
}

.ai-suggestion-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.ai-suggestion-header h4 {
  margin: 0;
}

.ai-suggestion-content {
  width: 100%;
}

.button-grid {
  justify-content: right;
}

.statisticsContainer {
  flex: 1;
  padding: 20px;
}

.left-align {
  text-align: left;
  margin: 0px;
  padding: 0;
}

.ana-container {
  height: 83vh;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-image: url('../assets/images/anaBG.png') !important;
  display: flex;
  /*width: 100%;*/
  flex-direction: column;

}

.ana-style {
  height: 65vh;
  width: 23vw;
  overflow-y: auto;
  font-size: 14px;
  color: rgb(30, 29, 29);
  padding-left: 30px !important;
}

.loading-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  position: relative;
}

.card-header {
  flex-shrink: 0;
}

.center-content {
  display: flex;
  flex-grow: 1;
  justify-content: center;
  align-items: center;
  margin: 20px;
}

.glow-button {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 100px;
  background-color: rgb(128, 152, 8);
  border-radius: 50%;
  position: relative;
  color: #999;
  font-size: 1.5em;
  text-decoration: none;
  overflow: hidden;
  transition: 0.5s;
}

.glow-button:hover {
  color: var(--clr);
  text-shadow: 0 0 15px var(--clr), 0 0 15px var(--clr);
}

.glow-button span {
  position: relative;
  z-index: 1;
  letter-spacing: 0.2em;
}

.glow-button::before {
  content: '';
  position: absolute;
  top: var(--y);
  left: var(--x);
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  opacity: 0;
  transition: 0.5s, top 0s, left 0s;
}

.glow-button:hover::before {
  opacity: 1;
}

.glow-button::after {
  content: '';
  position: absolute;
  background-color: rgba(45, 45, 45, 0.8);
  inset: 2px;
  border-radius: 48px;
}

.loader {
  margin-top: 100px;
  width: 108px;
  height: 60px;
  color: #269af2;
  --c: radial-gradient(farthest-side, currentColor 96%, #0000);
  background:
    var(--c) 100% 100% /30% 60%,
    var(--c) 70% 0 /50% 100%,
    var(--c) 0 100% /36% 68%,
    var(--c) 27% 18% /26% 40%,
    linear-gradient(currentColor 0 0) bottom/67% 58%;
  background-repeat: no-repeat;
  position: relative;
}

.loader:after {
  content: "";
  position: absolute;
  inset: 0;
  background: inherit;
  opacity: 0.4;
  animation: l7 1s infinite;
}

@keyframes l7 {
  to {
    transform: scale(1.8);
    opacity: 0
  }
}

.button-custom {
  margin-top: 100px;
  align-items: center;
  border: 0;
  border-radius: 50%;
  box-shadow: rgba(151, 65, 252, 0.2) 0 15px 30px -5px;
  box-sizing: border-box;
  color: #FFFFFF;
  display: flex;
  font-size: 26px;
  justify-content: center;
  line-height: 1em;
  width: 120px;
  height: 120px;
  text-decoration: none;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  white-space: nowrap;
  cursor: pointer;
  position: relative;
  z-index: 0;
}

.button-custom:before {
  content: "";
  background: linear-gradient(45deg,
      #B08FAF,
      #B1D1CE,
      #A3C3E0,
      #B1D1CE,
      #B08FAF);
  position: absolute;
  top: -2px;
  left: -2px;
  background-size: 400%;
  z-index: -1;
  filter: blur(10px);
  width: calc(100% + 4px);
  height: calc(100% + 4px);
  animation: glowing-button-85 20s linear infinite;
  transition: opacity 0.3s ease-in-out;
  border-radius: 50%;
}

@keyframes glowing-button-85 {
  0% {
    background-position: 0 0;
  }

  50% {
    background-position: 400% 0;
  }

  100% {
    background-position: 0 0;
  }
}

.button-custom:after {
  z-index: -1;
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(144deg, #5B42F3 0%, #00DDEB);
  left: 0;
  top: 0;
  border-radius: 50%;
}

.loading-text p {
  font-size: 19px;
  color: black !important;
  text-align: center;
  margin-top: 20px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

.fade-in {
  opacity: 0;
  animation: fadeIn 1s forwards;
}

.ana-style {
  text-align: left;
  margin: 0;
  margin-top: 10px;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.4);
}

.tags-container {
  display: flex;
  align-items: center;
  flex-direction: column;
  font-size: 16px;
}

.custom-select {
  font-size: 16px;
}

.numInput input {
  font-size: 16px;
}

.button-new-tag {
  font-size: 16px;
}

.tag-block {
  font-size: 16px;
  margin-top: 5px;
}
</style>