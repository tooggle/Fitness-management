<template>
  <div class="icon-course-container">
    <div class="icon-container">
      <el-icon>
        <ChatSquare />
      </el-icon>
      <span class="hot-text"><b class="bolder">HOT</b></span>
    </div>

    <el-card class="my-card">
      <template #header>
        <div class="card-header">
          <div class="title-left">
            <b class="bolder">教学课单</b>
            <b class="title-right">{{ editForm.courseName }}</b>
            <span class="learning-status">教学中</span>
          </div>
          <div class="icoin-container">
            <el-icon
              :style="{ fontSize: '24px', marginRight: '20px' }"
              @click="handleDocumentClick"
            >
              <Document />
            </el-icon>
            <el-icon
              :style="{ fontSize: '24px', marginRight: '20px' }"
              @click="showModal = true"
            >
              <Edit />
            </el-icon>
            <el-icon :style="{ fontSize: '24px' }" @click="handleDeleteClick">
              <Delete />
            </el-icon>
          </div>
        </div>
      </template>
      <div class="card-firstrow">
        <el-icon>
          <CaretRight />
        </el-icon>
        <b class="bolder">教学进度</b>
        <!-- <div class="course-progress" :style="progressStyle">
          {{ courseProgress }}
        </div> -->
      </div>
      <div class="card-secondrow">
        <el-icon>
          <CaretRight />
        </el-icon>
        <b class="bolder">课程时间</b>
        <div class="course-time" :style="timeStyle">
          {{ editForm.courseStartTime }} - {{ editForm.courseEndTime }}
        </div>
      </div>
    </el-card>
    <el-card class="continue-learn">
      <!-- <template #header class="header2"></template> -->
       <br/>
      <div class="continue-btn" @click="showCourseComments">查看评论</div>
      <br/>
      <div class="continue-btn" @click="showTraniees">查看学员</div>
    </el-card>
  </div>

  <!-- 课程模态框 -->
  <CourseModal
    v-if="showModall"
    :isVisible="showModall"
    :thecourse="processedForm"
    @close="showModall = false"
  />

  <!-- 编辑课程模态框 -->
  <el-dialog v-model="showModal" title="编辑课程" width="50%">
    <el-form :model="editForm">
      <el-form-item label="课程名称">
        <el-input
          v-model="editForm.courseName"
          placeholder="例如:30到45分钟核心训练"
        ></el-input>
      </el-form-item>
      <el-form-item label="课程图片">
        <el-upload
          class="upload-demo"
          :file-list="fileList"
          :on-change="handleFileUpload"
          :before-upload="beforeUpload"
          :show-file-list="false"
        >
          <div class="image-upload-container">
            <el-image
              v-if="editForm.coursePhotoUrl"
              :src="editForm.coursePhotoUrl"
              fit="cover"
            ></el-image>
            <div v-else class="upload-placeholder">
              <el-icon><plus /></el-icon>
              <span>点击上传</span>
            </div>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="课程描述">
        <el-input
          v-model="editForm.courseDescription"
          type="textarea"
          placeholder="核心肌群是身体的中心力量，对于维持姿势、提高运动表现和预防受伤至关重要。"
        ></el-input>
      </el-form-item>
      <el-form-item label="课程容量">
        <el-input
          v-model="editForm.capacity"
          placeholder="请填入课程容量"
        ></el-input>
      </el-form-item>
      <el-form-item label="课程开始时间">
        <el-date-picker
          v-model="editForm.courseStartTime"
          type="date"
          placeholder="选择日期"
        />
      </el-form-item>
      <el-form-item label="课程结束时间">
        <el-date-picker
          v-model="editForm.courseEndTime"
          type="date"
          placeholder="选择日期"
        />
      </el-form-item>
      <!-- 每周上课时间段 -->
      <el-form-item label="每周上课时间段">
        <div v-for="(schedule, index) in editForm.schedules" :key="index" style="margin-bottom: 10px;">
          <el-select
            v-model="schedule.dayOfWeek"
            placeholder="选择星期几"
            style="width: 120px;"
          >
            <el-option v-for="(day, i) in weekDays" :key="i" :label="day.label" :value="day.value"></el-option>
          </el-select>
          <el-time-picker
            v-model="schedule.classTime"
            is-range
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="HH:mm"
            value-format="HH:mm"
            style="width: 220px; margin-left: 10px;"
          ></el-time-picker>
          <el-button type="danger" @click="removeSchedule(index)" style="margin-left: 10px;">删除</el-button>
        </div>
        <el-form-item>
          <el-select v-model="newSchedule.dayOfWeek" placeholder="选择星期几" style="width: 120px;">
            <el-option v-for="(day, i) in weekDays" :key="i" :label="day.label" :value="day.value"></el-option>
          </el-select>
          <el-time-picker
            v-model="newSchedule.classTime"
            is-range
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="HH:mm"
            value-format="HH:mm"
            style="width: 220px; margin-left: 10px;"
          ></el-time-picker>
          <el-button type="primary" @click="addSchedule" style="margin-left: 10px;">添加时间段</el-button>
        </el-form-item>
      </el-form-item>

      <el-form-item label="课程难度">
        <el-radio-group v-model="editForm.courseGrade">
          <el-radio :label="1">1</el-radio>
          <el-radio :label="2">2</el-radio>
          <el-radio :label="3">3</el-radio>
          <el-radio :label="4">4</el-radio>
          <el-radio :label="5">5</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="课程价格">
        <el-input-number
          v-model="editForm.coursePrice"
          :min="0"
          :max="3000"
          :step="1"
          placeholder="请填入一个数字"
        />
      </el-form-item>
      <el-form-item label="课程特征">
        <div>
          <el-tag
            v-for="(feature, index) in editForm.features"
            :key="index"
            closable
            @close="removeFeature(index)"
            style="margin-right: 8px"
          >
            {{ feature }}
          </el-tag>
          <el-input
            v-model="inputFeature"
            placeholder="输入并按回车,如 [力量, 增肌]"
            @keyup.enter.native="addFeature"
            style="width: 200px"
          ></el-input>
        </div>
      </el-form-item>
      <el-form-item label="课程分类">
        <el-select v-model="editForm.typeID" placeholder="请选择课程分类">
          <el-option label="高强度间歇" value="1"></el-option>
          <el-option label="儿童趣味课" value="3"></el-option>
          <el-option label="低强度塑形" value="2"></el-option>
          <el-option label="有氧训练" value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitEdit">提交</el-button>
        <el-button @click="showModal = false">取消</el-button>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="showModal = false">取消</el-button>
      <el-button type="primary" @click="submitEdit">保存修改</el-button>
    </template>
  </el-dialog>

  <!-- 评论模态框 -->
  <el-button v-if="isCommentVisible" @click="isCommentVisible = false"
    >收起评论</el-button
  >
  <Comment
    v-if="isCommentVisible"
    :myName="this.userName"
    :myHeader="this.userIcon"
    :comments="comments"
  />
</template>

<script>
import CourseModal from "../components/CourseModal.vue";
import axios from "axios";
import { ElNotification } from "element-plus";
import Comment from "../components/Comment.vue";
export default {
  components: {
    CourseModal,
    Comment,
  },
  name: "TeachCard",
  props: {
    //作为传入的老师创建的教学课程的相关参数
    editForm: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      processedForm: {},
      weekDays: [
        { label: '周日', value: 0 },
        { label: '周一', value: 1 },
        { label: '周二', value: 2 },
        { label: '周三', value: 3 },
        { label: '周四', value: 4 },
        { label: '周五', value: 5 },
        { label: '周六', value: 6 }
      ], // 星期几选项
      newSchedule: { classID:-1,dayOfWeek: 0, classTime: [] }, // 新添加的时间段
      inputFeature: "",
      showModal: false, //编辑课程的视窗
      showModall: false, //查看课程的视窗
      showComments: false, //查看课程评论的视窗
      showDialog: false,
      isCommentVisible: false,
      comments: [], // 评论数组，初始为空
    };
  },
  created() {
    // 删除生成随机评论的方法调用
  },

  mounted() {
    // 确保 editForm 被正确传入
    console.log(this.editForm.courseName);
    this.userName = localStorage.getItem("name");
    this.email = localStorage.getItem("email");
    this.userIcon = localStorage.getItem("iconUrl");
    console.log("格式化前的数据",this.editForm);
    this.processedForm = this.formatForm;
    console.log("格式化后的数据",this.processedForm)

  },
  methods: {
    addSchedule() {
      if (this.newSchedule.dayOfWeek !== null && this.newSchedule.classTime.length === 2) {
        this.editForm.schedules.push({ ...this.newSchedule });
        this.newSchedule = { classID:-1,dayOfWeek: 0, classTime:[] }; // 重置新添加的时间段
      } else {
        this.$message.error('请完整填写时间段');
      }
    },
    removeSchedule(index) {
      this.editForm.schedules.splice(index, 1);
    },
    // 删除 generateRandomComments 方法

    showCourseComments() {
      this.isCommentVisible = !this.isCommentVisible;
      axios.get('http://localhost:8080/api/Course/GetCourseCommentByClassID',{
        params:{
          token:localStorage.getItem("token"),
          classID: this.editForm.schedules[0].classID
        }
      })
      .then((response) => {
        this.comments = response.data;
        if(this.comments.length>0){
          this.showComments = true;
        }
        else{
          ElNotification({
            title: "提示",
            message: "当前暂无课程评价",
            type: "info",
          });
        }
      })
    },
    addFeature() {
      const feature = this.inputFeature.trim();
      if (feature && !this.editForm.features.includes(feature)) {
        this.editForm.features.push(feature);
      }
      this.inputFeature = ""; // 清空输入框
    },
    removeFeature(index) {
      this.editForm.features.splice(index, 1);
    },
    //教练查看课程
    handleDocumentClick() {
      this.showModall = true;
    },

    //教练删除课程
    handleDeleteClick() {
      console.log("删除课程ID", this.editForm.schedules[0].classID);

      axios
        .delete("http://localhost:8080/api/Course/DeleteCourseByClassID", {
          params: {
            token: localStorage.getItem("token"),
            classID: this.editForm.schedules[0].classID,
          },
        })
        .then((response) => {
          console.log("课程删除成功:", response.data);
          ElNotification({
            title: "成功",
            message: "成功删除已发布课程",
            type: "success",
          });
          this.$emit("delete-teachcourse", this.editForm.courseName);
        })
        .catch((err) => {
          console.log("课程删除失败:", err);
        });
    },

    handleFileUpload(file) {
      this.editForm.coursePhotoUrl = URL.createObjectURL(file.raw);
    },
    beforeUpload(file) {
      this.imagePreview = "";
      const isJPGorPNG =
        file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGorPNG) {
        this.$message.error("上传头像图片只能是 JPG 或 PNG 格式!");
        return false;
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
        return false;
      }
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imagePreview = reader.result;
        this.editForm.coursePhotoUrl = this.imagePreview;
      };
      return false;
    },
    formatClassTime(schedule) {
      console.log(schedule);

      if (Array.isArray(schedule.classTime) && schedule.classTime.length === 2) {
        // 将 classTime 数组转换为 "08:00-10:00" 格式的字符串
        schedule.classTime = `${schedule.classTime[0]}-${schedule.classTime[1]}`;
      }
      return schedule;
    },
    getCourseValue(courseName) {
      switch (courseName) {
        case "高强度间歇":
          return 1;
        case "低强度塑形":
          return 2;
        case "儿童趣味课":
          return 3;
        case "有氧训练":
          return 4;
        default:
          return 0; // 如果输入的课程名称不在列表中，返回0或其他默认值
      }
    },
    //-------------------------------------- API接口------------------------------------------------------
    //教练修改课程API(完结版)
    submitEdit() {
      const token = localStorage.getItem("token");
      // 转换日期字符串为 Date 对象
      var startDate = new Date(this.editForm.courseStartTime);
      var endDate = new Date(this.editForm.courseEndTime);
      // 计算时间差（以毫秒为单位）
      var timeDiff = endDate - startDate;
      // 将时间差转换为天数
      var daysDiff = timeDiff / (1000 * 3600 * 24);
      console.log("是否为数组",Array.isArray(this.editForm.schedules));
      // 应用转换函数到每个元素
      let schedules=this.editForm.schedules;
      // console.log("转换前的课程时间为:",schedules);
      schedules = schedules.map(this.formatClassTime);
      // schedules.forEach(element => {
      //   console.log("转换后的课程时间为:",element);
      // });

      this.showModal = false;
      const postData = {
        course: {
          classID: schedules[0].classID,
          typeID: parseInt(this.editForm.courseType),
          courseName: this.editForm.courseName,
          capacity: this.editForm.capacity,
          courseDescription: this.editForm.courseDescription,
          coursePrice: this.editForm.coursePrice,
          courseStartTime: this.editForm.courseStartTime,
          courseEndTime: this.editForm.courseEndTime,
          courseLastDays: daysDiff,
          courseGrade: this.editForm.courseGrade,
          coursePhotoUrl: this.editForm.coursePhotoUrl,
          courseVideoUrl: "null",
          features: this.editForm.features.join("#"),
        },
        courseSchedules:schedules
      };
      console.log("上传的数据为",postData);
      axios
        .post(
          `http://localhost:8080/api/Course/ModifyCourse?token=${token}`,postData
        )
        .then((response) => {
          console.log("课程修改成功:", response.data);
          this.showModal = false;
          //!!!!不要清数据
          ElNotification({
            title: "成功",
            message: "课程修改成功！",
            type: "success",
          });
        })
        .catch((error) => {
          console.log("课程修改错误:", error);
          ElNotification({
            title: "错误",
            message: "修改课程时发生错误，请稍后再试。",
            type: "error",
          });
        });
    },

    //教练删除课程的API(完结版)
    deleteCourseByClassID() {
      const token = localStorage.getItem("token");
      const classID = this.editForm.classID;
      axios
        .delete(`http://localhost:8080/api/Course/DeleteCourseByClassID`, {
          params: {
            token: token,
            classID: classID,
          },
        })
        .then((response) => {
          console.log("教练删除课程成功:", response.data);
        })
        .catch((error) => {
          console.error("教练删除课程失败:", error);
        });
    },
  },

  computed: {
    formatForm() {
      // 复制 editForm 的所有字段
      let formattedForm = { ...this.editForm };

      // 处理 schedules 字段中的 classTime
      formattedForm.schedules = this.editForm.schedules.map(schedule => {
        if (Array.isArray(schedule.classTime) && schedule.classTime.length === 2) {
          return {
            ...schedule, // 保留原 schedule 的其他字段
            classTime: schedule.classTime.join('-') // 转换为 '8:00-10:00' 形式
          };
        }
        return schedule;
      });

      // 将结果赋值给 processedForm 变量
      this.processedForm = formattedForm;

      // 返回处理后的表单，方便在模板中使用
      return formattedForm;
    },
    //课程卡片上的静态字体样式
    progressStyle() {
      return {
        color: "#337ecc",
        fontWeight: "bold",
        marginLeft: "10px",
      };
    },
    timeStyle() {
      return {
        color: "#337ecc",
        fontWeight: "bold",
        marginLeft: "10px",
      };
    },
  },
};
</script>

<style scoped>
.bolder {
  font-weight: bold;
}

.icon-course-container {
  display: flex;
  align-items: center;
}

.icon-container {
  position: relative;
  display: inline-flex;
  font-size: 30px;
  color: red;
  margin-right: -10px;
  margin-top: -60px;
}

.hot-text {
  position: absolute;
  top: 5px;
  left: 3px;
  font-size: 10px;
  color: red;
  font-weight: bold;
}

.my-card {
  width: 500px;
  height: 130px;
}

.card-header {
  display: flex;
  align-items: center;
  height: 10px;
  justify-content: space-between;
  width: 470px;
}

.title-left {
  display: flex;
  align-items: center;
  font-size: smaller;
  font-weight: bold;
}

.title-right {
  font-weight: bold;
  font-size: 1.2rem;
}

.title-left .bolder,
.title-left .title-right {
  margin-right: 10px;
}

.learning-status {
  padding: 2px 6px;
  background-color: white;
  color: orange;
  font-size: 12px;
  border: 2px solid orange;
  border-radius: 3px;
  margin-left: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.icoin-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.card-firstrow {
  display: flex;
  align-items: center;
  margin-top: 5px;
  margin-bottom: 7px;
}

.card-secondrow {
  display: flex;
  align-items: center;
}

.continue-learn {
  margin-top: 1px;
  height: 134px;
  padding: 20px;
}

.continue-btn {
  display: block;
  text-align: center;
  padding: 5px;
  width: 120px;
  background-color: orange;
  color: white;
  font-weight: bold;
  cursor: pointer;
  user-select: none;
  border-radius: 3px;
  margin-top: -10px;
}
</style>
