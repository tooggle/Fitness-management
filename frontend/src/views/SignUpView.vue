<template>
    <div class="background">
        <div class="background-image"></div>
        <div class="login-container">
            <div class="content">
                <div class="title-img"></div>
                <el-card class="login-card">
                    <h2 class="login-title">用户注册</h2>
                    <el-form :model="SignUpForm" :rules="rules" label-position="left" label-width="80px">
                        <el-form-item label="用户昵称" prop="name">
                            <el-input v-model="SignUpForm.name" />
                        </el-form-item>
                        <el-form-item label="用户类型" prop="userType">
                            <el-radio-group v-model="SignUpForm.userType">
                                <el-radio label="user">普通用户</el-radio>
                                <el-radio label="coach">教练</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="SignUpForm.password" type="password" show-password />
                        </el-form-item>
                        <el-form-item label="确认密码" prop="verifyPwd">
                            <el-input v-model="SignUpForm.verifyPwd" type="password" show-password />
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="SignUpForm.email" />
                        </el-form-item>
                        <el-row justify="center">
                            <el-col>
                                <el-form-item>
                                    <el-button type="primary" class="login-button" @click="SignUp">完成注册</el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row justify="end">
                            <el-col :span="10">
                                <el-link type="primary" @click="returnLoginView">已有账号？返回登录</el-link>
                            </el-col>
                        </el-row>
                    </el-form>
                </el-card>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { ElNotification } from "element-plus";

export default {
    data() {
        return {
            SignUpForm: {
                name: '',
                password: '',
                verifyPwd: '',
                email: '',
                userType: 'user',
            },
            rules: {
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 16, message: '密码长度为6-16位', trigger: 'blur' },
                ],
                verifyPwd: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    {
                        trigger: 'blur',
                        validator: (rule, value, callback) => {
                            if (value === '') callback(new Error("请确认密码"));
                            else {
                                if (value !== this.SignUpForm.password) callback(new Error("密码不一致"));
                                else callback();
                            }
                        }
                    }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    {
                        trigger: 'blur',
                        validator: (rule, value, callback) => {
                            const emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                            if (!value) callback();
                            else {
                                if (emailReg.test(value)) callback();
                                else callback(new Error("邮箱地址非法"));
                            }
                        }
                    }
                ],
                name: [
                    { required: true, message: '请输入昵称', trigger: 'blur' },
                    { min: 0, max: 20, message: '昵称过长', trigger: 'blur' },
                ],
                userType: [
                    { required: true, message: '请选择用户类型', trigger: 'change' }
                ],
            },
        }
    },

    methods: {
        async SignUp() {
            try {
                const requestData = {
                    email: this.SignUpForm.email,
                    password: this.SignUpForm.password,
                    accountName: this.SignUpForm.name,
                    role: this.SignUpForm.userType === 'user' ? 'user' : 'coach',
                    coachName: this.SignUpForm.name,
                };

                console.log(requestData);

                const response = await axios.post(`http://localhost:8080/api/User/Register`, requestData);

                if (response.data.message === "成功注册") {
                    ElNotification({
                        message: "注册成功",
                        type: 'success',
                        duration: 2000
                    });
                    this.$router.push({ name: 'LoginView' });
                } else if (response.data.message === "注册失败：邮箱已存在") {
                    ElNotification({
                        message: "注册失败：邮箱已存在，请重新填写",
                        type: 'error',
                        duration: 2000
                    });
                }

            } catch (error) {
                ElNotification({
                    message: '注册失败，请稍后再试',
                    type: 'error',
                    duration: 2000
                });
                console.error('Error registering:', error);
            }
        },

        returnLoginView() {
            this.$router.push({ name: 'LoginView' });
        },
    }
}
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
    max-width: 500px;
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
