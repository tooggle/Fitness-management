<template>
    <el-upload
      class="upload-demo"
      action="https://jsonplaceholder.typicode.com/posts/"
      :on-change="handleChange"
      :before-upload="beforeUpload"
    >
      <el-icon style="font-size: 20px;"><PictureFilled /></el-icon>
    </el-upload>
  </template>
  
  <script>
  import {PictureFilled} from '@element-plus/icons-vue';
  import store from "../store/index.js";
  import { messageApi } from "../api/services";
 
  export default {
    name: 'ImageUpload',
    data() {
      return {
        hasChanged: false // 标志位，初始为false
      };
    },
    components: {
      PictureFilled,
    },
    methods: {
      
      async handleChange(file, fileList) {
        console.log('用户确认选择该图片', file);
        this.hasChanged = !this.hasChanged;
        if (this.hasChanged) return;

        try {
          const reader = new FileReader();
          reader.readAsDataURL(file.raw);

          reader.onload = async () => {
            const base64Image = reader.result;
            const currentTime = new Date().toLocaleString();
            const imageURL = URL.createObjectURL(file.raw);

            const msg = {
              targetName: store.state.targetInfomation.name,
              targetId: store.state.targetInfomation.targetId,
              list: {
                is_me: true,
                time: currentTime,
                message: imageURL,
                messageType: 'image',
              }
            };
            store.commit('addMessage', msg);

            const messageData = {
              receiverID: store.state.targetInfomation.id,
              senderID: store.state.myInformation.id,
              time: currentTime,
              messageType: 'image',
              content: base64Image,
            };

            const response = await messageApi.sendMessage(messageData);
            console.log('Message with image sent successfully:', response.data);
          };

          reader.onerror = (error) => {
            console.error('Error reading file:', error);
          };
        } catch (error) {
          console.error('Error handling image upload:', error);
        }
      },
      beforeUpload(file) {
        const isImage = file.type.startsWith('image/');
        if (!isImage) {
          this.$message.error('只能上传图片格式的文件');
        }
        return isImage;
      }
    }
  }
  </script>
  
  <style scoped>
.upload-demo {
  display: inline-block; /* 使上传组件适应内容 */
  width :13px;
  height:24px;
}
.upload-button{
  width :13px;
  height:24px;
}

  </style>
  