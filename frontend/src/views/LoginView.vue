<template>
    <div class="background">
        <div class="background-image"></div>
        <div class="login-container">
            <div class="content">
                <div class="title-img"></div>
                <el-card class="login-card">
                    <h2 class="login-title">登录</h2>
                    <el-tabs v-model="activeName" class="tabs">
                        <el-tab-pane label="用户登录" name="user">
                            <el-form :model="LogInForm" :rules="rules" label-position="left" label-width="70px">
                                <el-form-item label="身份" prop="role" required>
                                    <el-radio-group v-model="LogInForm.role">
                                        <el-radio label="user">普通用户</el-radio>
                                        <el-radio label="coach">教练</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="LogInForm.email" />
                                </el-form-item>
                                <el-form-item label="密码" prop="password">
                                    <el-input v-model="LogInForm.password" type="password" show-password />
                                </el-form-item>
                                <el-row justify="center">
                                    <el-col>
                                        <el-form-item>
                                            <el-button type="primary" class="login-button"
                                                @click="submitForm">登录</el-button>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row justify="end">
                                    <el-col :span="10">
                                        <el-link type="primary" @click="signUp">没有账号？注册一个</el-link>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </el-tab-pane>
                        <el-tab-pane label="管理员登录" name="admin">
                            <el-form :model="LogInForm" :rules="rules" label-position="left" label-width="70px">
                                <el-form-item label="邮箱" prop="email">
                                    <el-input v-model="LogInForm.email" />
                                </el-form-item>
                                <el-form-item label="密码" prop="password">
                                    <el-input v-model="LogInForm.password" type="password" show-password />
                                </el-form-item>
                                <el-row justify="center">
                                    <el-col>
                                        <el-form-item>
                                            <el-button type="primary" class="login-button"
                                                @click="submitForm('admin')">登录</el-button>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </el-tab-pane>
                    </el-tabs>
                </el-card>
            </div>
        </div>
    </div>
</template>

<script>
import store from "../store/index.js";
import { ElNotification } from "element-plus";
import axios from "axios";


export default
    {
        data() {
            return {
                LogInForm:
                {
                    email: '',
                    password: '',
                    role: 'user' // 默认值设为用户登录
                },
                rules: {
                    email: [
                        { required: true, message: '请输入邮箱', trigger: 'blur' },
                        {
                            trigger: 'blur',
                            validator: (rule, value, callback) => {
                                const emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                                if (!value) callback();
                                else if (emailReg.test(value)) callback();
                                else callback(new Error("邮箱地址非法"));
                            }
                        }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 5, max: 16, message: '密码长度为5-16位', trigger: 'blur' }
                    ]
                },
                activeName: 'user',
            };
        },

        methods: {
            async submitForm(role) {
                // //测试使用：
                // // 预设的万能账号信息
                // const superUserEmail = 'superuser@example.com';
                // const superUserPassword = 'admin123';

                // // 检查是否是万能账号
                // if (
                // this.LogInForm.email === superUserEmail &&
                // this.LogInForm.password === superUserPassword
                // ) {
                // // 直接设置 Vuex 状态
                // store.commit('setRole', 'admin'); // 假设万能账号是管理员角色
                // store.commit('setToken', 'superuser-token'); // 设置一个模拟的 token
                // store.commit('setUserID', 'superuser-id'); // 设置一个模拟的用户 ID
                // store.commit('setName', 'Super User'); // 设置用户名
                // store.commit('setIconUrl', '/path/to/superuser/icon.jpg'); // 设置用户头像路径
                // store.commit('setEmail', superUserEmail); // 设置邮箱
                // store.commit('setIntroduction', 'This is a super user.'); // 设置用户简介
                // store.commit('setIsPost', true); // 假设万能账号有发帖权限

                // // 跳转到管理员页面
                // this.$router.push({ path: '/admin' });

                // // 显示通知
                // ElNotification({
                //     message: '登录成功（万能账号）',
                //     type: 'success',
                //     duration: 2000,
                // });

                // return; // 退出方法，不再执行后续代码
                // }

                // //以上测试使用
                try {
                    if (role === 'admin') {
                        this.LogInForm.role = 'admin';
                    }
                    const requestData = {
                        email: this.LogInForm.email,  // 这里假设 email 字段用于用户名
                        password: this.LogInForm.password,
                        role: this.LogInForm.role  // 传递用户类型到后端，可能是 'user' 或 'admin'
                    };

                    console.log("发送请求的数据: ", requestData);

                    const response = await axios.get(`http://localhost:8080/api/User/Login`, {
                        params: {
                            email: requestData.email,
                            password: requestData.password,
                            role: requestData.role
                        }
                    });
                    console.log("收到响应的数据: ", response.data.message);

                    const response1 = await axios.get(`http://localhost:8080/api/User/GetPersonalProfile?token=${response.data.token}`);

                    const message = response.data.message;
                    let notificationType = 'info';  // 默认类型为 'info'

                    if (message === '身份权限不符') {
                        notificationType = 'error';
                    }

                    if (message === '登录成功') {
                        notificationType = 'success';

                        // 只有在登录成功时才进行角色存储和页面跳转
                        store.commit('setRole', requestData.role);
                        store.commit('setToken', response.data.token);
                        // 存储用户信息
                        store.commit('setUserID', response1.data.userID);
                        store.commit('setName', response1.data.userName);
                        store.commit('setIconUrl', response1.data.iconURL)
                        store.commit('setEmail', response1.data.email);
                        store.commit('setIntroduction', response1.data.introduction)
                        // 存储当前用户发帖权限
                        store.commit('setIsPost', response1.data.isPost);

                        store.dispatch('pollIsPost');  // 开启轮询，更新发帖权限

                        if (requestData.role === 'admin') {
                            this.$router.push({ path: '/admin' });
                        } else {
                            //this.$router.push({ name: 'UserProfile' });
                            this.$router.push({ name: 'home' });
                        }
                    } else if (message === '邮箱不存在或错误' || message === '密码错误') {
                        notificationType = 'error';
                    }

                    // 无论成功与否，都显示通知
                    ElNotification({
                        message: message,
                        type: notificationType,
                        duration: 2000
                    });

                } catch (error) {
                    // 捕获请求异常并显示错误信息
                    ElNotification({
                        message: '用户名或密码错误',
                        type: 'error',
                        duration: 2000
                    });
                    console.error('Error login', error);
                }
            },

            signUp() {
                this.$router.push("/SignUp");
            }
        },
    };
</script>

<style scoped>
/* 新增背景容器 */
.login-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #f5f7fa;
    display: flex;
    align-items: center;
    justify-content: center;
}

.login-wrapper {
    width: 100%;
    max-width: 500px; /* 缩小登录框宽度 */
    padding: 2.5rem;
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    transform: translateY(-10%); /* 微调垂直位置 */
}

.header {
    margin-bottom: 1.5rem; /* 缩小头部间距 */
    text-align: center;
}


 /* .logo { */
    /* font-size: 2rem; 缩小logo大小 */
    /* font-weight: 600; */
    /* color: #2c3e50; */
    /* margin-bottom: 0.3rem; */
/* } */


/* .slogan { */
    /* font-size: 0.9rem; 缩小标语字号 */
/* } */

.login-card {
    width: 130%;
    border-radius: 10px; /* 调小圆角 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 减弱阴影 */
    background-color: #ffffff;
}

.login-card ::v-deep .el-card__body {
    padding: 3.5rem; /* 缩小内边距 */
}

.tabs ::v-deep .el-tabs__item {
    font-size: 0.95rem; /* 调小标签字号 */
    padding: 0 1rem; /* 调小标签内边距 */
    height: 42px;
    line-height: 42px;
}

.el-form-item {
    margin-bottom: 1rem !important; /* 缩小表单项间距 */
}

.el-input ::v-deep .el-input__inner {
    height: 38px; /* 缩小输入框高度 */
    font-size: 0.9rem;
    border-radius: 5px;
}

.login-button {
    height: 38px; /* 缩小按钮高度 */
    font-size: 0.95rem;
    border-radius: 6px;
}

/* 其他元素微调 */
.el-link {
    font-size: 0.82rem;
}

.el-radio ::v-deep .el-radio__label {
    font-size: 0.85rem;
}
</style>