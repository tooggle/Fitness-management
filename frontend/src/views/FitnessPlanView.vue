<template>
  <navigation-bar/>
  <common-layout />
  <body class="container">
    <div class="fitness-content">
      <TimeThread>
        <template #before-buttons>
          <el-button @click="dialogFormVisible = true" class="action-button" type="primary">
            填写我的体测表
          </el-button>
        </template>
      </TimeThread>
    </div>
    
    <el-dialog v-model="dialogFormVisible" title="体测信息" width="400">
      <el-form :model="form" :rules="rules" ref="ruleFormRef">
        <el-form-item label="身高(cm)" :label-width="formLabelWidth" prop="height">
          <el-input-number v-model.number="form.height" autocomplete="off" :precision="2" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="体重(kg)" :label-width="formLabelWidth" prop="weight">
          <el-input-number v-model.number="form.weight" autocomplete="off" :precision="2" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="BMI" :label-width="formLabelWidth" prop="BMI">
          <el-input-number v-model.number="form.BMI" autocomplete="off" :precision="1" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="体脂率" :label-width="formLabelWidth" prop="bodyFatRate">
          <el-input-number v-model.number="form.bodyFatRate" autocomplete="off" :precision="1" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="俯卧撑个数" :label-width="formLabelWidth" prop="pushups">
          <el-input-number v-model.number="form.pushups" autocomplete="off" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="深蹲个数" :label-width="formLabelWidth" prop="squats">
          <el-input-number v-model.number="form.squats" autocomplete="off" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="仰卧起坐个数" :label-width="formLabelWidth" prop="situps">
          <el-input-number v-model.number="form.situps" autocomplete="off" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="引体向上个数" :label-width="formLabelWidth" prop="pullup">
          <el-input-number v-model.number="form.pullup" autocomplete="off" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="一千米时间(s)" :label-width="formLabelWidth" prop="longDistance">
          <el-input-number v-model.number="form.longDistance" autocomplete="off" :controls="false"></el-input-number>
        </el-form-item>
        <el-form-item label="健身目标" :label-width="formLabelWidth" prop="goal">
          <el-select v-model="form.goal" placeholder="请选择一个健身目标">
            <el-option label="减脂" value="loseWeight" />
            <el-option label="增肌" value="buildMuscle" />
            <el-option label="塑型" value="bodySculpting" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button  type="primary" @click="submitForm(ruleFormRef)">
            创建
          </el-button>
        </div>
      </template>
    </el-dialog>
  </body>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted } from 'vue';
import { ElNotification } from 'element-plus';
import { fitnessPlanApi, userApi } from "../api/services";
import { useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';
import { FitnessPlan, Exercise, WorkoutDay } from "../types/api";
import NavigationBar from '../components/NavigationBar.vue';
import CommonLayout from '../components/CommonLayout.vue';
import TimeThread from '../components/TimeThread.vue';
import axios from 'axios';

interface RuleForm {
  height: number;
  weight: number;
  BMI: number;
  bodyFatRate: number;
  pushups: number;
  squats: number;
  situps: number;
  pullup: number;
  longDistance: number;
  goal: string;
}

export default defineComponent({
  name: 'FitnessPlanView',
  components: {
    NavigationBar,
    CommonLayout,
    TimeThread
  },
  data() {
    return {
      inputValue: '',
      dynamicTags: [] as string[],
      inputVisible: false,
      calendarRef: null,
      value: new Date(),
      dialogVisible: false,
      showCurrentPlan: false,
      currentFormData: {
        userID: 0,
        planID: 0,
        fitnessPlanID: -1,
        title: '',
        description: '',
        workoutDays: [] as WorkoutDay[],
        date: new Date(),
        exercises: [] as Exercise[],
        state: false,
        numOfTypes: 0,
        createdAt: '',
        updatedAt: ''
      } as FitnessPlan,
      formDataStore: {} as Record<string, FitnessPlan[]>,
      exercise: [
        { value: 'Running', label: 'Running' },
        { value: 'Swimming', label: 'Swimming' },
        { value: 'Cycling', label: 'Cycling' },
      ],
      canAdd: true,
      Complete: ['○', '●'],
      tagQuantities: {} as Record<string, number>,
      selectedExercises: [] as string[],
    };
  },
  setup() {
    const router = useRouter();
    const dialogFormVisible = ref(false);
    const formLabelWidth = '110px';
    const imagePath = 'src/assets/images/background.jpg';
    const ruleFormRef = ref<FormInstance>();
    const form = reactive<RuleForm>({
      height: 0,
      weight: 0,
      BMI: 0,
      bodyFatRate: 0,
      pushups: 0,
      squats: 0,
      situps: 0,
      pullup: 0,
      longDistance: 0,
      goal: '',
    });
    const rules = reactive<FormRules<RuleForm>>({
      height: [
        { required: true, message: '请输入身高', trigger: 'blur' },
        { type: 'number', message: '请输入数字', trigger: 'blur' },
      ],
      weight: [
        { required: true, message: '请输入体重', trigger: 'blur' },
        { type: 'number', message: '请输入数字', trigger: 'blur' },
      ],
      BMI: [
        { required: true, message: '请输入BMI', trigger: 'blur' },
        { type: 'number', message: '请输入数字', trigger: 'blur' },
      ],
      bodyFatRate: [
        { required: true, message: '请输入体脂率', trigger: 'blur' },
        { type: 'number', message: '请输入数字', trigger: 'blur' },
      ],
      pushups: [
        { required: true, message: '请输入俯卧撑个数', trigger: 'blur' },
        { type: 'integer', message: '请输入整数', trigger: 'blur' },
      ],
      squats: [
        { required: true, message: '请输入深蹲个数', trigger: 'blur' },
        { type: 'integer', message: '请输入整数', trigger: 'blur' },
      ],
      situps: [
        { required: true, message: '请输入仰卧起坐个数', trigger: 'blur' },
        { type: 'integer', message: '请输入整数', trigger: 'blur' },
      ],
      pullup: [
        { required: true, message: '请输入引体向上个数', trigger: 'blur' },
        { type: 'integer', message: '请输入整数', trigger: 'blur' },
      ],
      longDistance: [
        { required: true, message: '请输入一千米时间', trigger: 'blur' },
        { type: 'integer', message: '请输入整数', trigger: 'blur' },
      ],
      goal: [
        { required: true, message: '请选择健身目标', trigger: 'blur' },
      ],
    });
    const backgroundStyle = {
      'background': `${imagePath} no-repeat center/cover`,
      'height': '100vh'
    };
    const loading = ref(true);
    const activeName = ref(1);

    const submitForm = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          const formData = {
            title: `${form.goal}健身计划`,
            description: `基于您的体测数据创建的${form.goal}健身计划`,
            workoutDays: [
              {
                timestamp: new Date().toISOString(),
                workoutName: "健身日1",
                coverUrl: "",
                isCompleted: "false",
                exercises: [],
                date: new Date().toISOString().split('T')[0]
              }
            ] as WorkoutDay[],
            height: form.height,
            weight: form.weight,
            BMI: form.BMI,
            bodyFatRate: form.bodyFatRate,
            pushups: form.pushups,
            squats: form.squats,
            situps: form.situps,
            pullup: form.pullup,
            longDistance: form.longDistance,
            goal: form.goal
          };
          fitnessPlanApi.create(formData)
            .then(response => {
              console.log(response.data.message);
              ElNotification({
                message: response.data.message,
                type: 'success',
                duration: 2000
              });
              dialogFormVisible.value = false;
              router.push('/fitness-plan');
            })
            .catch(error => {
              console.error('Error:', error);
              ElNotification({
                message: '创建失败，请重试',
                type: 'error',
                duration: 2000
              });
            });
        } else {
          console.log('error submit!', fields);
        }
      });
    };

    function checkAvailable() {
      let token = localStorage.getItem('token');
      if (token == null) {
        ElNotification({
          title: '提示',
          message: '请先登录',
          type: 'warning',
          duration: 2000
        });
        router.push('/login');
        return;
      }
      
      axios.get(`http://localhost:8080/api/User/GetTokenInvalidateRes`, {
        params: {
          token: token
        }
      })
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
        })
        .catch(error => {
          ElNotification({
            title: '错误',
            message: '获取用户信息失败',
            type: 'error',
          });
        });
    }

    onMounted(() => {
      checkAvailable();
    });

    return {
      dialogFormVisible,
      formLabelWidth,
      imagePath,
      ruleFormRef,
      form,
      rules,
      backgroundStyle,
      loading,
      activeName,
      submitForm,
      checkAvailable,
    };
  }
});
</script>



<style scoped>
/* 未解决的背景问题 */
.container {
  background-image: url();
  background-size: cover;
  background-position: center;
  width: 100%;
  position: absolute;
  background-attachment: fixed;
  top: 12vh;
  min-height: 100vh;
  left: 0;
}

.fitness-content {
  position: relative;
  width: 100%;
  padding-top: 20px;
}

.action-button {
  width: 150px;
  height: 40px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-bottom: 17px;
  padding-top: 17px;
}
/* 自定义 el-collapse-item 的样式 */
:deep(.custom-collapse-item .el-collapse-item__header) {
  font-size: 18px;
  font-weight: bold;
  color: #1f2d3d;
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 10px;
  border-bottom: 1px solid #ebeef5;
  transition: all 0.3s ease;
  background-color: transparent;
}

/* 为标题添加图标 */
:deep(.custom-collapse-item .el-collapse-item__header::before) {
  content: '📅';
  margin-right: 10px;
}

/* 鼠标悬停时标题的样式 */
:deep(.custom-collapse-item .el-collapse-item__header:hover) {
  background-color: #e6f7ff;
  color: #409eff;
}
</style>

