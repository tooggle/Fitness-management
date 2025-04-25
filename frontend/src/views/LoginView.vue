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
import { userApi } from "../api/services";

export default {
    data() {
        return {
            LogInForm: {
                email: '',
                password: '',
                role: 'user'
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

                //测试使用：
                // 预设的万能账号信息
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

                //以上测试使用
            try {
                if (role === 'admin') {
                    this.LogInForm.role = 'admin';
                }
                const requestData = {
                    email: this.LogInForm.email,
                    password: this.LogInForm.password,
                    role: this.LogInForm.role
                };

                console.log("发送请求的数据: ", requestData);

                const response = await userApi.login(requestData.email, requestData.password, requestData.role);
                console.log("收到响应的数据: ", response.data.message);

                const response1 = await userApi.getPersonalProfile();

                const message = response.data.message;
                let notificationType = 'info';

                if (message === '身份权限不符') {
                    notificationType = 'error';
                }

                if (message === '登录成功') {
                    notificationType = 'success';

                    store.commit('setRole', requestData.role);
                    store.commit('setToken', response.data.token);
                    store.commit('setUserID', response1.data.userID);
                    store.commit('setName', response1.data.userName);
                    store.commit('setIconUrl', response1.data.iconURL);
                    store.commit('setEmail', response1.data.email);
                    store.commit('setIntroduction', response1.data.introduction);
                    store.commit('setIsPost', response1.data.isPost);

                    store.dispatch('pollIsPost');

                    if (requestData.role === 'admin') {
                        this.$router.push({ path: '/admin' });
                    } else {
                        this.$router.push({ name: 'home' });
                    }
                } else if (message === '邮箱不存在或错误' || message === '密码错误') {
                    notificationType = 'error';
                }

                ElNotification({
                    message: message,
                    type: notificationType,
                    duration: 2000
                });

            } catch (error) {
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
.background {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: hidden;
}

.background-image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    background-size: cover;
    z-index: -1;
}

.login-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
}

.content {
    width: 100%;
    max-width: 460px;
    padding: 2rem;
    transform: translateY(-2%);
    position: relative;
    z-index: 1;
}

.title-img {
    height: 70px;
    margin-bottom: 20px;
    background-image: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNDAgNjAiPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBkb21pbmFudC1iYXNlbGluZT0ibWlkZGxlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtd2VpZ2h0PSI3MDAiIGZvbnQtc2l6ZT0iMjgiIGZpbGw9IiNmZmYiPkZpdG5lc3MgU3lzdGVtPC90ZXh0Pjwvc3ZnPg==');
    background-repeat: no-repeat;
    background-position: center;
    background-size: contain;
}

.login-card {
    width: 100%;
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
    background-color: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    overflow: visible;
    transition: all 0.3s ease;
}

.login-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
}

.login-card ::v-deep .el-card__body {
    padding: 2rem;
}

.login-title {
    text-align: center;
    margin-bottom: 1.5rem;
    color: #333;
    font-size: 1.8rem;
    font-weight: 600;
}

.tabs ::v-deep .el-tabs__nav-wrap::after {
    height: 1px;
    background-color: rgba(0, 0, 0, 0.05);
}

.tabs ::v-deep .el-tabs__item {
    font-size: 1rem;
    padding: 0 1.5rem;
    height: 48px;
    line-height: 48px;
    transition: all 0.2s;
}

.tabs ::v-deep .el-tabs__item.is-active {
    color: #764ba2;
    font-weight: 600;
}

.tabs ::v-deep .el-tabs__active-bar {
    background-color: #764ba2;
    height: 3px;
    border-radius: 3px;
}

.tabs ::v-deep .el-tabs__header {
    margin-bottom: 20px;
}

.tabs ::v-deep .el-tabs__content {
    overflow: visible;
}

.el-form-item {
    margin-bottom: 1.5rem !important;
    width: 100%;
}

.el-input {
    display: block;
    width: 100%;
}

/* 重新添加输入框样式但避免嵌套问题 */
:deep(.el-input__inner) {
    height: 42px;
    font-size: 0.95rem;
    border-radius: 8px;
    transition: all 0.3s;
    padding: 0 15px;
    background-color: #fff;
    width: 100%;
    box-sizing: border-box;
}

:deep(.el-input__inner:focus) {
    border-color: #764ba2;
    box-shadow: 0 0 0 2px rgba(118, 75, 162, 0.2);
}

:deep(.el-input__suffix) {
    right: 10px;
}

.el-input ::v-deep .el-input__inner:focus {
    border-color: #764ba2;
    box-shadow: 0 0 0 2px rgba(118, 75, 162, 0.2);
}

.login-button {
    width: 100%;
    height: 42px;
    font-size: 1rem;
    font-weight: 500;
    border-radius: 8px;
    background: linear-gradient(to right, #667eea, #764ba2);
    border: none;
    color: white;
    transition: all 0.3s;
}

.login-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(118, 75, 162, 0.4);
    opacity: 0.9;
}

.login-button:active {
    transform: translateY(0);
}

.el-link {
    font-size: 0.9rem;
    color: #764ba2 !important;
    transition: all 0.2s;
}

.el-link:hover {
    text-decoration: underline;
    color: #667eea !important;
}

.el-radio ::v-deep .el-radio__input.is-checked .el-radio__inner {
    background-color: #764ba2;
    border-color: #764ba2;
}

.el-radio ::v-deep .el-radio__input.is-checked + .el-radio__label {
    color: #764ba2;
}

.el-radio ::v-deep .el-radio__inner {
    border-color: rgba(0, 0, 0, 0.2);
}

.el-form ::v-deep .el-form-item__label {
    font-size: 0.95rem;
    color: #333;
    line-height: 1.4;
    padding-bottom: 6px;
}

.el-form ::v-deep .el-form-item__content {
    line-height: 1.4;
    display: block;
    width: 100%;
    position: relative;
}

.el-radio-group {
    display: flex;
    gap: 16px;
}

@media (max-width: 480px) {
    .content {
        padding: 1rem;
        max-width: 100%;
    }
    
    .login-card ::v-deep .el-card__body {
        padding: 1.5rem;
    }
    
    .login-title {
        font-size: 1.5rem;
        margin-bottom: 1rem;
    }
    
    :deep(.el-input__inner) {
        height: 38px;
        font-size: 0.9rem;
    }
}
</style>