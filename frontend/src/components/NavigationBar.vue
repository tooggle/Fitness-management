<template>
    <div :class="['head_con', navBarFixed ? 'navBarWrap' : '']">
        <div class="logo-container">
            <img src="../assets/images/logo.png" alt="FitFit" class="logo">
        </div>
        <div class="wrapper">
            <nav>
                <!-- 导航栏选项 -->
                <input type="radio" name="tab" id="home" :checked="$route.path === '/home' || $route.path === '/'">
                <input type="radio" name="tab" id="equipment" :checked="$route.path === '/equipment'">
                <input type="radio" name="tab" id="aifit" :checked="$route.path === '/aifit'">
                <input type="radio" name="tab" id="forum" :checked="$route.path === '/forum'">
                <input type="radio" name="tab" id="achievement" :checked="$route.path === '/achievements'">
                <input type="radio" name="tab" id="course" :checked="$route.path === '/course'">
                <input type="radio" name="tab" id="plan" :checked="$route.path === '/fitnessplan'">
                <input type="radio" name="tab" id="chat" :checked="$route.path === '/chat'">
                <input type="radio" name="tab" id="healthyDiet"
                    :checked="$route.path === '/healthyDiet' || $route.path === '/MealPlanner' || $route.path === '/MealRecord'">

                <!-- 导航链接 -->
                <label for="home" class="home" @click="delayedNavigation('/home')">
                    <router-link to="/home" class="nav-link">
                        <el-icon><House /></el-icon>
                        首页
                    </router-link>
                </label>
                <label for="plan" class="plan" @click="delayedNavigation('/fitnessplan')">
                    <router-link to="/fitnessplan" class="nav-link">
                        <el-icon><Finished /></el-icon>
                        健身计划
                    </router-link>
                </label>
                <label for="aifit" class="aifit" @click="delayedNavigation('/aifit')">
                    <router-link to="/aifit" class="nav-link">
                        <el-icon><Picture /></el-icon>
                        健身指导
                    </router-link>
                </label>
                <label for="course" class="course" @click="delayedNavigation('/course')">
                    <router-link to="/course" class="nav-link">
                        <el-icon><Notebook /></el-icon>
                        健身课程
                    </router-link>
                </label>
                <label for="equipment" class="equipment" @click="delayedNavigation('/equipment')">
                    <router-link to="/equipment" class="nav-link">
                        <el-icon><Basketball /></el-icon>
                        健身器材
                    </router-link>
                </label>
                <label for="healthyDiet" class="healthyDiet" @click="delayedNavigation('/healthyDiet')">
                    <router-link to="/healthyDiet" class="nav-link">
                        <el-icon><Apple /></el-icon>
                        健康饮食
                    </router-link>
                </label>
                <label for="forum" class="forum" @click="delayedNavigation('/forum')">
                    <router-link to="/forum" class="nav-link">
                        <el-icon><ChatDotRound /></el-icon>
                        健身论坛
                    </router-link>
                </label>
                <!-- 动态指示条 -->
                <div class="tab"></div>
            </nav>
        </div>
        <div class="avatar-container">
            <!-- 用户头像和下拉菜单 -->
            <template v-if="token">
                <el-dropdown>
                    <img :src="iconUrl" alt="User" class="dropdownlink">
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="navigateToUserProfile">
                                <el-icon><Setting /></el-icon>
                                个人资料
                            </el-dropdown-item>
                            <el-dropdown-item v-if="currentUser === 'admin'" @click="navigateToAdminPanel">
                                <el-icon><Tools /></el-icon>
                                管理界面
                            </el-dropdown-item>
                            <el-dropdown-item @click="navigateToLoginOut">
                                <el-icon><Switch /></el-icon>
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </template>
            <template v-else>
                <el-button type="primary" class="round-button" @click="navigateToLoginOut">登录/注册</el-button>
            </template>
        </div>
    </div>
</template>

<script>
import router from "../router/index.js";
import axios from "axios";
import { ElNotification } from "element-plus";
import { commonMixin } from '../mixins/checkLoginState';
import { Basketball } from "@element-plus/icons-vue";

export default {
    name: "NavigationBar",
    mixins: [commonMixin],
    data() {
        return {
            navBarFixed: false, // 控制导航栏是否固定
            token: localStorage.getItem('token'), // 用户登录状态
            iconUrl: this.$store.state.iconUrl, // 用户头像
            checkLoginInterval: null, // 登录状态检查定时器
            currentUser: localStorage.getItem('role') // 当前用户角色
        };
    },
    props: {
        showMenu: {
            type: Boolean,
            default: true,
        }
    },
    methods: {
        navigateToAdminPanel() {
            this.router().push('/admin'); // 跳转到管理界面
        },
        router() {
            return router;
        },
        delayedNavigation(target) {
            setTimeout(() => {
                this.router().push(target); // 延迟跳转
            }, 500);
        },
        navigateToUserProfile() {
            const userID = this.$store.state.userID;
            this.router().push(`/user/${userID}`); // 跳转到用户个人资料
        },
        navigateToLoginOut() {
            this.router().push('/login'); // 跳转到登录页
            localStorage.removeItem('iconUrl');
            localStorage.removeItem('token');
        },
        watchScroll() {
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            if (scrollTop > 90) {
                this.navBarFixed = true; // 滚动时固定导航栏
            } else {
                this.navBarFixed = false;
            }
        },
        checkLoginStatus() {
            const currentPath = this.$route.path;
            if (currentPath === '/home' || currentPath === '/') {
                return;
            }
            this.checkAvailable(); // 检查登录状态
        }
    },
    mounted() {
        window.addEventListener("scroll", this.watchScroll); // 监听滚动事件
        this.checkLoginInterval = setInterval(this.checkLoginStatus, 20000); // 定时检查登录状态
    },
    beforeUnmount() {
        if (this.checkLoginInterval) {
            clearInterval(this.checkLoginInterval); // 清理定时器
        }
        window.removeEventListener("scroll", this.watchScroll); // 移除滚动监听
    }
};
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    text-align: center;
    background: #fff;
}

.head_con {
    display: flex;
    align-items: center;
    position: absolute;
    top: 3%;
    left: 0;
    width: 100%;
    padding-left: 5%;
    padding-right: 5%;
    transition: all 0.3s ease;
}

.logo-container {
    margin-right: 0px;
}

.logo {
    height: 50px;
    display: block;
}

.wrapper,
.avatar-container {
    height: 50px;
    display: flex;
    align-items: center;
    margin-left: auto;
}

.wrapper {
    width: 60vw;
    line-height: 50px;
    background-color: rgba(255, 255, 255, 0.9); /* 半透明白色背景 */
    border-radius: 50px;
    transition: all 0.3s ease;
}

.wrapper nav {
    display: flex;
    flex: 1;
    position: relative;
}

.wrapper nav label {
    flex: 1;
    width: 100%;
    position: relative;
    z-index: 1;
    cursor: pointer;
}

.wrapper nav label a {
    position: relative;
    z-index: -1;
    color: #333; /* 默认文字颜色 */
    font-size: 1vw;
    font-weight: 500;
    text-decoration: none;
    transition: all 0.3s ease;
}

.wrapper nav label a:hover {
    color: #6a11cb; /* 悬停时文字颜色变化 */
    transform: scale(1.05);
}

.wrapper nav input {
    display: none;
}

.wrapper nav .tab {
    position: absolute;
    height: 100%;
    width: 14.3%;
    left: 0px;
    bottom: 0px;
    background: linear-gradient(to right, #6a11cb, #2575fc);
    border-radius: 50px;
    transition: 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.wrapper nav #home:checked~label.home a,
.wrapper nav #equipment:checked~label.equipment a,
.wrapper nav #aifit:checked~label.aifit a,
.wrapper nav #forum:checked~label.forum a,
.wrapper nav #course:checked~label.course a,
.wrapper nav #plan:checked~label.plan a,
.wrapper nav #healthyDiet:checked~label.healthyDiet a {
    color: #fff;
    transition: 0.6s;
}

.wrapper nav #equipment:checked~.tab {
    left: 57.5%;
}

.wrapper nav #aifit:checked~.tab {
    left: 28.5%;
}

.wrapper nav #forum:checked~.tab {
    left: 85.7%;
}

.wrapper nav #course:checked~.tab {
    left: 42.9%;
}

.wrapper nav #plan:checked~.tab {
    left: 14.3%;
}

.wrapper nav #healthyDiet:checked~.tab {
    left: 71.6%;
}

.avatar-container {
    position: relative;
    display: flex;
    align-items: center;
    height: 50px;
    border: none;
}

.dropdownlink {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    cursor: pointer;
    border: 5px solid #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.20);
    transition: all 0.3s ease;
}

.dropdownlink:hover {
    transform: scale(1.1);
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
}

.nav-link {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px;
    transition: all 0.3s ease;
}

.nav-link:hover {
    color: #6a11cb;
    background: rgba(106, 17, 203, 0.1);
    border-radius: 10px;
}

.equip-icon {
    width: 19px;
    height: 19px;
    vertical-align: middle;
    transition: all 0.3s ease;
}

.equip-icon:hover {
    transform: scale(1.1);
}

.round-button {
    width: 100px;
    height: 50px;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    color: #fff;
    border: none;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.3s ease;
}

.round-button:hover {
    background-color: #6a11cb;
    transform: scale(1.05);
}

.navBarWrap {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 999;
    background-color: rgba(255, 255, 255, 0.9); /* 半透明白色背景 */
    transition: all 0.3s ease;
}

@media (min-width: 1024px) {
    #app {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
}
</style>