<template>
  <common-layout />
  <div class="bg">
    <div class="user-profile">
      <div class="profile-container">
        <div class="back-button-container">
          <el-button
            @click="goBack"
            circle
            style="font-size: 24px; width: 50px; height: 50px"
          >
            <el-icon>
              <arrow-left />
            </el-icon>
          </el-button>
        </div>
        <aside class="profile-sidebar">
          <div class="avatar-wrapper" @click="showLargeImage">
            <img
              :src="imagePreview || profile.iconURL || defaultAvatar"
              alt="avatar"
              class="avatar"
            />
            <span class="edit-icon" @click.stop="triggerFileInput"
              >&#9998;</span
            >
          </div>
          <h2>{{ profile.userName }}</h2>
          <div class="tags">
            <span
              v-for="(tag, index) in tags"
              :key="index"
              :style="{ backgroundColor: colors[index % colors.length] }"
              class="tag"
            >
              {{ tag }}
              <span class="remove-tag" @click="removeTag(index)">×</span>
            </span>
            <span v-if="addingTag" class="tag-input">
              <input
                type="text"
                v-model="newTag"
                @blur="addTag"
                @keyup.enter="addTag"
              />
            </span>
            <span class="add-tag" @click="addingTag = true">+</span>
          </div>
          <input
            type="file"
            @change="handleFileUpload"
            ref="fileInput"
            class="file-input"
          />
        </aside>

        <main class="profile-main">
          <!-- 第一行：用户ID、邮箱、注册时间 -->
          <section class="profile-info">
            <div class="info-row">
              <div class="profile-viewer">
                <label>用户ID</label>
                <p>{{ profile.userID }}</p>
              </div>
              <div class="profile-viewer">
                <label>邮箱</label>
                <p>{{ profile.email }}</p>
              </div>
              <div class="profile-viewer">
                <label>注册时间</label>
                <p>{{ formatDate(profile.registrationTime) }}</p>
              </div>
            </div>
          </section>

          <!-- 第三行：昵称、年龄、性别 -->
          <section class="profile-info">
            <div class="info-row uniform-row">
              <EditableField
                label="昵称"
                :value="profile.userName"
                type="input"
                @save="profile.userName = $event"
              />
              <EditableField
                label="年龄"
                :value="profile.age"
                type="input"
                @save="profile.age = $event"
              />
              <div class="profile-editor">
                <label>性别</label>
                <select v-model="profile.gender">
                  <option value="男">男</option>
                  <option value="女">女</option>
                </select>
              </div>
            </div>
          </section>

          <!-- 第四行：类型、体重 -->
          <section class="profile-info">
            <div class="info-row uniform-row">
              <EditableField
                label="健身目标类型"
                :value="profile.goalType"
                type="input"
                @save="profile.goalType = $event"
              />
              <EditableField
                label="体重 / kg"
                :value="profile.goalWeight"
                type="input"
                @save="profile.goalWeight = $event"
              />
            </div>
          </section>

          <!-- 第五行：会员状态 -->
          <section class="profile-editor">
            <label>会员状态:</label>
            <p>{{ profile.isMember ? "会员" : "普通用户" }}</p>
          </section>

          <!-- 第六行：个性签名 -->
          <section class="profile-info">
            <div class="signature-editor signature-textarea">
              <label>个性签名</label>
              <textarea
                v-model="profile.introduction"
                @blur="emitSave('introduction')"
                placeholder="写下你的个性签名..."
                class="enhanced-textarea"
              ></textarea>
            </div>
          </section>

          <!-- 保存和取消按钮 -->
          <div class="profile-actions">
            <button @click="cancelEdit" class="cancel-button">取消</button>
            <button @click="saveProfile" class="save-button">保存</button>
          </div>

          <!-- 帖子列表部分 -->
          <div class="button-container">
            <button @click="togglePostList" class="toggle-button">
              {{ postListVisible ? "收起已发布帖子" : "查看已发布帖子" }}
            </button>
          </div>
          <section class="post-list">
            <div v-if="postListVisible">
              <div v-if="posts.length === 0">该用户尚未发布任何帖子。</div>
              <ul v-else>
                <!--li v-for="post in posts" :key="post.postID" class="post-item">
                                    <small>{{ new Date(post.postTime).toLocaleString() }}</small>
                                    <h4>{{ post.postTitle }}</h4>
                                    <p>{{ post.postContent }}</p>
                                </li-->
                <!-- 帖子列表部分 -->
                <div v-for="post in posts" :key="post.postID">
                  <small>{{ new Date(post.postTime).toLocaleString() }}</small>
                  <div class="post-item">
                    <div class="post-content">
                      <h3 class="post-title" @click="viewPost(post.postID)">
                        {{ post.postTitle }}
                        <span class="category-tag">{{
                          post.postCategory
                        }}</span>
                      </h3>
                      <!-- 图片展示 -->
                      <div v-if="post.imgUrl != `null`" class="post-image">
                        <img
                          :src="post.imgUrl"
                          alt="Post Image"
                          class="image"
                        />
                      </div>
                      <!-- 使用 v-html 直接渲染保存的内容 -->
                      <p
                        class="post-snippet"
                        v-html="renderContent(post.postContent)"
                      ></p>
                    </div>
                    <div class="post-footer">
                      <span class="post-actions">
                        <span class="icon-with-text no-click">
                          <LikeOutlined />
                          <span>{{ post.likesCount }}</span>
                        </span>
                        <span class="icon-with-text no-click">
                          <MessageOutlined />
                          <span>{{ post.commentsCount }}</span>
                        </span>
                        <span class="icon-with-text no-click">
                          <ShareAltOutlined />
                          <span>{{ post.forwardCount }}</span>
                        </span>
                        <span
                          class="icon-with-text-delete"
                          @click="deletePost(post.postID)"
                        >
                          <DeleteOutlined />
                        </span>
                      </span>
                    </div>
                  </div>
                </div>
              </ul>
            </div>
          </section>

          <!-- 活力币余额模块 -->
          <div class="button-container">
            <button @click="toggleBalanceModule" class="toggle-button">
              {{ balanceModuleVisible ? "收起活力币余额" : "查看活力币余额" }}
            </button>
          </div>
          <section class="vitality-balance">
            <div v-if="balanceModuleVisible">
              <h3>活力币余额</h3>
              <div class="balance-display">
                <p class="balance-amount">¥ {{ vigorTokenBalance }}</p>
              </div>
              <h3>余额变动记录</h3>
              <div class="balance-records-container">
                <table class="balance-records">
                  <thead>
                    <tr>
                      <th>记录ID</th>
                      <th>变动原因</th>
                      <th>变动量</th>
                      <th>余额</th>
                      <th>创建时间</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="record in vigorTokenRecords"
                      :key="record.recordID"
                    >
                      <td>{{ record.recordID }}</td>
                      <td>{{ record.reason }}</td>
                      <td
                        :class="{
                          positive: record.change > 0,
                          negative: record.change < 0,
                        }"
                      >
                        {{ record.change > 0 ? "￥+" : "￥"
                        }}{{ record.change }}
                      </td>
                      <td>{{ "￥" }}{{ record.balance }}</td>
                      <td>
                        {{ new Date(record.createTime).toLocaleString() }}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </section>
          <!-- 成就模块 -->
          <div class="button-container">
            <button @click="toggleAchievements" class="toggle-button">
              {{ achievementsVisible ? "收起成就进度表" : "查看成就进度表" }}
            </button>
          </div>
          <section class="achievement-section">
            <div v-if="achievementsVisible">
              <div class="achievement-container">
                <!-- 使用 v-for 循环生成成就图片 -->
                <div
                  v-for="(achievement, index) in achievements"
                  :key="index"
                  class="achievement-item-wrapper"
                >
                  <div
                    class="achievement-item"
                    :style="{ '--progress': getProgress(achievement) }"
                    @mouseover="showTooltip(index)"
                    @click="
                      showPopup(achievement.achievementId, achievement.target)
                    "
                    @mouseleave="hideTooltip"
                  >
                    <img
                      :src="getImagePath(achievement.achievementId)"
                      alt="Achievement Badge"
                    />
                  </div>
                  <!-- Popup Window -->
                  <div
                    v-if="isPopupVisible"
                    class="popup-overlay"
                    @click.self="hidePopup"
                  >
                    <div class="popup-content">
                      <RankingList :achievementId="currentAchievementId" />
                      <button @click="hidePopup" class="close-btn">×</button>
                    </div>
                  </div>
                  <div v-if="tooltipVisibleIndex === index" class="tooltip">
                    <h4>{{ achievement.name }}</h4>
                    <div class="achievement-content">
                      <span :style="{ fontSize: '15px', color: '#ffffff' }"
                        >目标: {{ achievement.target }}</span
                      >
                    </div>
                    <div class="achievement-content">
                      <span :style="{ fontSize: '15px', color: '#ffffff' }"
                        >完成情况:
                        {{
                          (
                            (achievement.progress * 100) /
                            achievement.target
                          ).toFixed(2)
                        }}%</span
                      >
                    </div>
                    <div class="achievement-content">
                      <span :style="{ fontSize: '15px', color: '#ffffff' }"
                        >状态:
                        {{ achievement.isAchieved ? "已完成" : "未完成" }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </main>
      </div>

      <!-- 模态框显示大图 -->
      <div v-if="isLargeImageVisible" class="modal" @click="hideLargeImage">
        <img
          :src="imagePreview || profile.iconURL || defaultAvatar"
          alt="Large avatar"
          class="large-avatar"
        />
      </div>
    </div>
  </div>
</template>

<script>
import RankingList from "../components/RankingList.vue"; // 确保路径正确

import defaultAvatar from "../assets/images/default-avatar.png";
import EditableField from "../views/EditableField.vue";
import axios from "axios";
import { ElNotification } from "element-plus";
import { mapState } from "vuex";
import Achievement_1 from "../assets/badges/Achievement_1.png";
import Achievement_2 from "../assets/badges/Achievement_2.png";
import Achievement_3 from "../assets/badges/Achievement_3.png";
import Achievement_4 from "../assets/badges/Achievement_4.png";
import Achievement_5 from "../assets/badges/Achievement_5.png";
import Achievement_6 from "../assets/badges/Achievement_6.png";
import Achievement_7 from "../assets/badges/Achievement_7.png";
import Achievement_8 from "../assets/badges/Achievement_8.png";
import { commonMixin } from "../mixins/checkLoginState";
import store from "../store";
import {
  LikeOutlined,
  MessageOutlined,
  ShareAltOutlined,
  DeleteOutlined,
} from "@ant-design/icons-vue";
import { postApi, userApi } from "../api/services";

export default {
  mixins: [commonMixin],
  components: {
    EditableField,
    LikeOutlined,
    MessageOutlined,
    ShareAltOutlined,
    DeleteOutlined,
    RankingList,
  },
  data() {
    return {
      currentAchievementId: null, // 用于存储当前的 achievementId
      currentTarget: null,
      isPopupVisible: false, // 控制弹窗是否可见
      profile: {
        userID: -1,
        userName: "",
        password: "",
        salt: "",
        email: "",
        registrationTime: "",
        iconURL: "",
        age: -1,
        gender: "",
        tags: "",
        introduction: "",
        goalType: "",
        goalWeight: null,
        isMember: null,
      },
      tags: [],
      originalTags: [], // 保存初始状态的标签
      posts: [],
      colors: [
        "#e57373",
        "#81c784",
        "#64b5f6",
        "#ffb74d",
        "#ba68c8",
        "#4db6ac",
      ],
      addingTag: false,
      newTag: "",
      originalProfile: null,
      defaultAvatar,
      imagePreview: null,
      originalImagePreview: null, // 保存初始状态的头像
      isLargeImageVisible: false,
      vigorTokenBalance: 0,
      vigorTokenRecords: [],
      postListVisible: false,
      balanceModuleVisible: false,
      achievementsVisible: false,
      achievements: [],
      tooltipVisibleIndex: null,
      achievementImages: {
        1: Achievement_1,
        2: Achievement_2,
        3: Achievement_3,
        4: Achievement_4,
        5: Achievement_5,
        6: Achievement_6,
        7: Achievement_7,
        8: Achievement_8,
      },
    };
  },
  computed: {
    ...mapState(["token", "userID"]),
  },
  created() {
    this.checkAvailable();
    const userID = this.$route.params.userID;
    this.fetchUserProfile(userID);
    this.fetchUserPosts();
    this.getVigorTokenBalance();
    this.getVigorTokenRecordsFromDB();
  },
  mounted() {
    this.fetchAchievements();
  },
  methods: {
    showPopup(achievementId, target) {
      console.log("Popup show");
      this.isPopupVisible = true;
      this.currentAchievementId = parseInt(achievementId, 10); // 存储当前的 achievementId
      console.log("Current Achievement ID:", this.currentAchievementId);

      this.currentTarget = parseInt(target, 10); // 存储当前的 achievementId
      console.log("Current Target:", this.currentTarget);
    },
    hidePopup() {
      this.isPopupVisible = false;
      this.currentAchievementId = null;
      this.currentTarget = null; // 隐藏弹窗时清空当前的 achievementId
    },
    viewPost(postID) {
      this.$router.push({ name: "PostDetail", params: { postID: postID } });
    },
    renderContent(content) {
      // 这里可以进一步处理内容，例如对其他 HTML 标签的处理
      const plainText = content.replace(/<[^>]+>/g, ""); // 移除所有HTML标签
      return plainText.length > 40 ? plainText.slice(0, 50) + "..." : plainText;
    },
    goBack() {
      this.$router.back();
    },
    togglePostList() {
      this.postListVisible = !this.postListVisible;
    },
    toggleBalanceModule() {
      this.balanceModuleVisible = !this.balanceModuleVisible;
    },
    toggleAchievements() {
      this.achievementsVisible = !this.achievementsVisible;
    },
    showTooltip(index) {
      console.log("Tooltip show for index:", index);
      this.tooltipVisibleIndex = index;
    },
    hideTooltip() {
      console.log("Tooltip hide");
      this.tooltipVisibleIndex = null;
    },
    getProgress(achievement) {
      let progress = achievement.progress / achievement.target;
      return Math.min(1, Math.max(0, progress));
    },
    getImagePath(achievementId) {
      return this.achievementImages[achievementId] || "";
    },
    // 得到格式化的注册时间
    formatDate(date) {
      //profile.registrationTime
      if (!(date instanceof Date)) {
        console.log("格式错误");
        return "";
      }
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      const seconds = date.getSeconds().toString().padStart(2, "0");
      return `${year}-${month}-${day}  ${hours}:${minutes}:${seconds}`; // 格式化为 yyyy-mm-dd
    },

    async fetchUserProfile(userID) {
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(
          `http://localhost:8080/api/User/GetProfile`,
          {
            params: { token, userID },
          }
        );
        const date = new Date(response.data.registrationTime);
        console.log(date);
        this.profile = response.data;
        this.profile.registrationTime = date;
        this.originalProfile = JSON.parse(JSON.stringify(this.profile));
        this.tags = this.profile.tags
          ? this.profile.tags.split("#").filter(Boolean)
          : [];
        this.originalTags = [...this.tags]; // 保存初始状态的标签
        this.imagePreview = this.profile.iconURL; // 设置头像预览
        this.originalImagePreview = this.profile.iconURL; // 保存初始状态的头像
        console.log("用户资料：", this.profile);
        console.log("获取用户资料成功");
      } catch (error) {
        ElNotification({
          title: "错误",
          message: "获取用户资料失败",
          type: "error",
        });
      }
    },
    async fetchUserPosts() {
      try {
        const response = await postApi.getPostByUserID(this.$route.params.userID);
        this.posts = response.data;
        console.log("获取用户帖子成功：", this.posts);
      } catch (error) {
        ElNotification({
          title: "错误",
          message: "获取用户帖子失败",
          type: "error",
        });
      }
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.beforeAvatarUpload(file);
      }
    },
    beforeAvatarUpload(file) {
      this.imagePreview = "";
      const isJPGorPNG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGorPNG) {
        ElNotification({
          title: "错误",
          message: "上传头像图片格式只能为JPEG或PNG",
          type: "error",
        });
        return false;
      }
      if (!isLt2M) {
        ElNotification({
          title: "错误",
          message: "上传头像图片大小不能超过2MB!",
          type: "error",
        });
        return false;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imagePreview = reader.result;
        this.profile.iconURL = this.imagePreview;
      };
      return false;
    },
    async fetchAchievements() {
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(
          `http://localhost:8080/api/Achievement/GetAchievement?token=${token}`,
          {
            params: {
              userID: this.$route.params.userID,
            },
          }
        );
        const data = response.data;
        this.achievements = data.achievements.map((achievement) => ({
          achievementId: achievement.achievementId,
          name: achievement.name,
          target: parseInt(achievement.target, 10),
          progress: parseInt(achievement.progress, 10),
          isAchieved: achievement.isAchieved === "true",
        }));

        console.log("获取成就表成功：", this.achievements);
      } catch (error) {
        ElNotification({
          title: "错误",
          message: "成就表获取失败:",
          type: "error",
        });
      }
    },
    emitSave(field) {
      console.log(`${field} updated:`, this.profile[field]);
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    showLargeImage() {
      this.isLargeImageVisible = true;
    },
    hideLargeImage() {
      this.isLargeImageVisible = false;
    },
    addTag() {
      if (this.newTag.trim()) {
        this.tags.push(this.newTag.trim());
        this.newTag = "";
        this.addingTag = false;
      }
    },
    removeTag(index) {
      this.tags.splice(index, 1);
    },
    cancelEdit() {
      this.profile = JSON.parse(JSON.stringify(this.originalProfile));
      this.tags = [...this.originalTags]; // 恢复初始状态的标签
      this.imagePreview = this.originalImagePreview; // 恢复初始状态的头像
      ElNotification({
        title: "提示",
        message: "编辑已取消",
        type: "info",
      });
    },
    async saveProfile() {
      console.log(this.userID);

      console.log(this.profile.userID);
      if (this.profile.userID != this.userID) {
        // 检查 userID 是否匹配
        this.profile = JSON.parse(JSON.stringify(this.originalProfile)); // 恢复原始数据
        this.tags = [...this.originalTags]; // 恢复初始状态的标签
        this.imagePreview = this.originalImagePreview; // 恢复初始状态的头像
        ElNotification({
          title: "错误",
          message: "无法保存：您没有操作权限，信息已恢复。",
          type: "error",
        });
        return;
      }

      try {
        const formattedTags = this.tags.join("#") + "#";
        const token = localStorage.getItem("token");
        const postData = {
          userID: this.profile.userID,
          userName: this.profile.userName,
          iconURL: this.profile.iconURL,
          age: this.profile.age,
          gender: this.profile.gender,
          tags: formattedTags,
          introduction: this.profile.introduction,
          isMember: this.profile.isMember,
          goalType: this.profile.goalType,
          goalWeight: this.profile.goalWeight,
        };
        console.log("上传资料：", postData);

        const response = await axios.post(
          `http://localhost:8080/api/User/UpdateProfile?token=${token}`,
          postData
        );
        console.log("上传响应：", response.data);

        this.originalProfile = JSON.parse(JSON.stringify(this.profile));
        this.originalTags = [...this.tags]; // 保存更新后的标签
        this.originalImagePreview = this.imagePreview; // 保存更新后的头像
        if (response.data === "更新成功") {
          ElNotification({
            title: "成功",
            message: "保存成功！",
            type: "success",
          });
          store.commit("setIconUrl", this.profile.iconURL);
        }
      } catch (error) {
        console.log(error);
        ElNotification({
          title: "错误",
          message: "保存资料失败",
          type: "error",
        });
      }
    },
    async getVigorTokenBalance() {
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(
          `http://localhost:8080/api/User/GetVigorTokenBalance?token=${token}`,
          {
            params: {
              userID: this.$route.params.userID,
            },
          }
        );
        this.vigorTokenBalance = response.data.balance;
        console.log("获取活力币余额成功：", this.vigorTokenBalance);
      } catch (error) {
        ElNotification({
          title: "错误",
          message: "获取活力币余额失败:",
          type: "error",
        });
      }
    },
    async getVigorTokenRecordsFromDB() {
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(
          `http://localhost:8080/api/User/GetVigorTokenReacords?token=${token}`,
          {
            params: {
              userID: this.$route.params.userID,
            },
          }
        );
        this.vigorTokenRecords = response.data.records.map((item) => ({
          recordID: item.recordID,
          reason: item.reason,
          change: item.change,
          balance: item.balance,
          createTime: item.createTime,
        }));

        this.vigorTokenRecords.sort((a, b) => b.recordID - a.recordID);
        console.log("获取活力币变动记录成功：", this.vigorTokenRecords);
      } catch (error) {
        ElNotification({
          title: "错误",
          message: "获取活力币变动记录失败",
          type: "error",
        });
      }
    },
    deletePost(postID) {
      const token = localStorage.getItem("token");
      axios
        .delete(`http://localhost:8080/api/Post/DeletePostByPostID`, {
          params: {
            token: token,
            postID: postID,
            postOwnerID: this.profile.userID,
          },
        })
        .then((response) => {
          console.log(response.data);
          if (response.data.message === "删除帖子成功") {
            ElNotification({
              title: "成功",
              message: "帖子删除成功",
              type: "success",
            });
            this.fetchUserPosts();
          } else {
            ElNotification({
              title: "错误",
              message: "删除帖子失败",
              type: "error",
            });
          }
        })
        .catch((error) => {
          ElNotification({
            title: "错误",
            message: "删除帖子时发生错误",
            type: "error",
          });
        });
    },
  },
};
</script>

<style scoped>
.bg {
  background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.2)), url("../assets/images/forum-bg.jpg");
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  width: 100%;
  min-height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  padding-top: 40px;
  padding-bottom: 40px;
}

.user-profile {
  display: flex;
  justify-content: center;
  align-items: center;
  height: fit-content;
  width: 100%;
  background-color: transparent;
}

.profile-container {
  display: flex;
  width: 100%;
  max-width: 80vw;
  margin: auto;
  background-color: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(15px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  border-radius: 20px;
  justify-content: space-between;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.profile-sidebar {
  width: 22%;
  padding: 25px;
  text-align: center;
  display: flex;
  flex-direction: column;
  position: relative;
  top: 130px;
  background-color: transparent;
  border: none;
}

.profile-main {
  width: 78%;
  margin-left: 0px;
  padding: 30px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: transparent;
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
  margin-bottom: 15px;
  transition: transform 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.03);
}

.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  border: 5px solid rgba(255, 255, 255, 0.7);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.edit-icon {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background-color: #42b983;
  color: white;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
  padding: 8px 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease;
}

.edit-icon:hover {
  background-color: #3aa876;
  transform: scale(1.1);
}

.file-input {
  display: none;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 15px;
  justify-content: center;
}

.tag {
  border-radius: 20px;
  padding: 6px 12px;
  font-size: 14px;
  color: white;
  margin: 3px;
  display: inline-block;
  position: relative;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.remove-tag {
  margin-left: 8px;
  cursor: pointer;
  font-weight: bold;
  transition: color 0.2s;
}

.remove-tag:hover {
  color: #ff4d4d;
}

.tag-input input {
  padding: 6px 12px;
  border-radius: 20px;
  border: 1px solid #ddd;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
  font-size: 14px;
}

.add-tag {
  background-color: #42b983;
  color: white;
  border-radius: 50%;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 18px;
  line-height: 1;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease;
}

.add-tag:hover {
  background-color: #3aa876;
  transform: scale(1.1);
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
  font-size: 20px;
  color: #333;
  text-align: left;
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  transition: all 0.3s ease;
}

.uniform-row > .editable-field,
.uniform-row > .profile-editor {
  flex: 1;
}

p {
  height: 50px;
  margin: 5px 0;
  padding: 10px 12px;
  border: 1px solid rgba(204, 204, 204, 0.5);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.4);
  text-align: left;
  color: #333;
  overflow: auto;
  transition: all 0.3s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.profile-editor,
.profile-viewer {
  display: flex;
  flex-direction: column;
  width: 32%;
  margin-bottom: 15px;
  font-size: 20px;
  color: #333;
  text-align: left;
}

label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
  font-size: 16px;
}

input,
select {
  padding: 10px 12px;
  border: 1px solid rgba(204, 204, 204, 0.8);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.4);
  height: 50px;
  font-size: 16px;
  width: 100%;
  transition: all 0.3s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

input:focus,
select:focus {
  outline: none;
  border-color: #42b983;
  box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
}

.profile-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 25px;
  font-size: 20px;
}

.cancel-button,
.save-button {
  padding: 10px 24px;
  margin-left: 15px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.cancel-button {
  background-color: #f56c6c;
  color: white;
}

.cancel-button:hover {
  background-color: #e64942;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.save-button {
  background-color: #42b983;
  color: white;
}

.save-button:hover {
  background-color: #3aa876;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
  animation: modalFadeIn 0.3s ease-out;
}

@keyframes modalFadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.large-avatar {
  max-width: 90%;
  max-height: 90%;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  animation: avatarZoomIn 0.4s ease-out;
}

@keyframes avatarZoomIn {
  from { transform: scale(0.8); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.signature-editor {
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-bottom: 10px;
}

.signature-textarea textarea {
  height: 80px;
  font-size: 18px;
  line-height: 1.5;
  width: 100%;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.4);
  resize: none;
  border: 1px solid #ccc;
  padding: 12px 15px;
  transition: all 0.3s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.signature-textarea textarea:focus {
  outline: none;
  border-color: #42b983;
  box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
}

.enhanced-textarea::placeholder {
  color: #aaa;
  font-style: italic;
}

.button-container {
  text-align: left;
  margin: 20px 0 10px;
}

.toggle-button {
  background-color: #42b983;
  color: #ffffff;
  border: none;
  padding: 12px 24px;
  margin-bottom: 15px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.toggle-button:hover {
  background-color: #3aa876;
  transform: translateY(-2px);
  box-shadow: 0 5px 12px rgba(0, 0, 0, 0.15);
}

.toggle-button:active {
  transform: scale(0.98);
}

.toggle-button::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}

.toggle-button:focus:not(:active)::after {
  animation: ripple 1s ease-out;
}

@keyframes ripple {
  0% {
    transform: scale(0, 0);
    opacity: 0.5;
  }
  100% {
    transform: scale(30, 30);
    opacity: 0;
  }
}

.post-list,
.vitality-balance,
.achievement-section {
  margin-bottom: 30px;
  max-height: 500px;
  overflow-y: auto;
  border-radius: 12px;
  scrollbar-width: thin;
  scrollbar-color: #42b983 transparent;
}

.post-list::-webkit-scrollbar,
.vitality-balance::-webkit-scrollbar,
.achievement-section::-webkit-scrollbar {
  width: 6px;
}

.post-list::-webkit-scrollbar-track,
.vitality-balance::-webkit-scrollbar-track,
.achievement-section::-webkit-scrollbar-track {
  background: transparent;
}

.post-list::-webkit-scrollbar-thumb,
.vitality-balance::-webkit-scrollbar-thumb,
.achievement-section::-webkit-scrollbar-thumb {
  background-color: #42b983;
  border-radius: 6px;
}

.post-item {
  background-color: rgba(255, 255, 255, 0.8);
  color: #000;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 12px;
  border: 1px solid rgba(230, 230, 230, 0.8);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.post-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
}

.post-content {
  text-align: left;
}

.post-title {
  font-size: 18px;
  color: #007bff;
  margin-bottom: 10px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.post-title:hover {
  color: #0056b3;
  text-decoration: underline;
}

.post-title .category-tag {
  background-color: #f0f0f0;
  border-radius: 50px;
  padding: 3px 10px;
  font-size: 12px;
  color: #555;
  margin-left: 10px;
  transition: all 0.2s ease;
}

.post-title:hover .category-tag {
  background-color: #e6e6e6;
}

.post-snippet {
  font-size: 16px;
  color: #555;
  line-height: 1.5;
  margin: 10px 0;
}

.post-footer {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 14px;
  color: #888;
  gap: 20px;
}

.post-actions {
  color: #555;
  display: flex;
  gap: 20px;
  align-items: center;
}

.icon-with-text {
  display: flex;
  align-items: center;
  gap: 5px;
  transition: color 0.2s ease;
}

.icon-with-text-delete {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #f56c6c;
  transition: all 0.2s ease;
}

.icon-with-text-delete:hover {
  color: #e64942;
  transform: scale(1.1);
}

.post-image {
  text-align: left;
  margin: 15px 0;
}

.post-image .image {
  width: 120px;
  height: auto;
  border-radius: 8px;
  display: inline-block;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.post-image .image:hover {
  transform: scale(1.05);
}

.balance-display {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px 0;
}

.balance-amount {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
  line-height: 1.5;
  background: none;
  border: none;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.balance-records-container {
  width: 100%;
  max-height: 60vh;
  overflow-y: auto;
  margin-top: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.balance-records {
  width: 100%;
  border-collapse: collapse;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  overflow: hidden;
}

.balance-records th,
.balance-records td {
  padding: 12px 15px;
  text-align: center;
  font-size: 16px;
  border: none;
}

.balance-records th {
  background-color: #42b983;
  color: white;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 14px;
}

.balance-records tr {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  transition: background-color 0.2s ease;
}

.balance-records tr:last-child {
  border-bottom: none;
}

.balance-records tr:nth-child(even) {
  background-color: rgba(242, 242, 242, 0.5);
}

.balance-records tr:hover {
  background-color: rgba(66, 185, 131, 0.1);
}

.positive {
  color: #67C23A;
  font-weight: 600;
  font-size: 16px !important;
}

.negative {
  color: #F56C6C;
  font-weight: 600;
  font-size: 16px !important;
}

h3 {
  font-size: 22px;
  color: #2c3e50;
  text-align: left;
  margin: 25px 0 15px;
  position: relative;
  padding-bottom: 10px;
}

h3::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 50px;
  height: 3px;
  background-color: #42b983;
  border-radius: 3px;
}

.achievement-container {
  margin-top: 30px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  padding: 10px;
}

.achievement-item-wrapper {
  position: relative;
  width: 18%;
  aspect-ratio: 1 / 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.achievement-item {
  position: relative;
  width: 100%;
  aspect-ratio: 1 / 1;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 50%;
  background-color: transparent;
  cursor: pointer;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.achievement-item:hover {
  transform: scale(1.05) translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.25);
}

.achievement-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.achievement-item::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  z-index: 2;
  transform-origin: bottom;
  transform: scaleY(calc(1 - var(--progress)));
  transition: transform 0.5s ease;
}

.achievement-item:hover::before {
  transform: scaleY(0);
}

.tooltip {
  position: absolute;
  bottom: 0%;
  left: 100%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.85);
  color: #fff;
  padding: 15px;
  border-radius: 8px;
  white-space: nowrap;
  z-index: 1000;
  display: block;
  min-width: 200px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateX(-50%) translateY(10px); }
  to { opacity: 1; transform: translateX(-50%) translateY(0); }
}

.tooltip h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding-bottom: 8px;
}

.achievement-content {
  height: auto;
  min-height: 40px;
  margin: 10px 0;
  padding: 8px 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 6px;
  background-color: rgba(255, 255, 255, 0.1);
  text-align: left;
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.75);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
  backdrop-filter: blur(8px);
  animation: fadeIn 0.3s forwards ease-in-out;
}

.popup-content {
  background: white;
  padding: 30px;
  border-radius: 15px;
  position: relative;
  width: 80%;
  max-width: 800px;
  height: auto;
  max-height: 80vh;
  overflow-y: auto;
  transform: scale(0.9);
  opacity: 0;
  animation: popupZoomIn 0.4s forwards ease-out;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.popup-content button {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.popup-content button:hover {
  background-color: #e64942;
  transform: scale(1.1);
}

@keyframes popupZoomIn {
  0% {
    transform: scale(0.9);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.back-button-container {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 1000;
}

.el-button.is-circle {
  transition: all 0.3s ease;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
}

.el-button.is-circle:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 12px rgba(0, 0, 0, 0.2);
}

.popup-content .close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 20px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 0;
}

.popup-content .close-btn:hover {
  background-color: #e64942;
  transform: scale(1.1);
}
</style>