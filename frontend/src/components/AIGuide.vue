<template>
  <div class="container">
    <div class="title">
      <span>健身动作指导</span>
    </div>
    <div class="content">
      <div v-if="!isAnalyzing">
        <el-steps :active="active" align-center class="steps">
          <el-step>
            <template #title>
              <span style="font-size: 20px; font-weight: bold;">上传健身截图</span>
            </template>
          </el-step>
          <el-step>
            <template #title>
              <span style="font-size: 20px; font-weight: bold;">输入健身动作类型</span>
            </template>
          </el-step>
          <el-step>
            <template #title>
              <span style="font-size: 20px; font-weight: bold;">AI分析</span>
            </template>
          </el-step>
        </el-steps>
        <div class="before-tracking">
          <div class="content-left">
            <div class="before-upload" v-if="!isUpload">
              <el-upload action="#" drag class="uploader" list-type="picture" :auto-upload="false"
                :show-file-list="false" :on-change="imgPreview">
                <div class="uploader-icon">
                  <el-icon icon="el-icon-upload"><upload/></el-icon>
                </div>
                <div class="uploader-text">请将图像拖到此处或点击上传</div>
              </el-upload>
            </div>
            <div class="after-upload" v-else>
              <img :src="screenShotUrl" alt="" class="upload-img" />
              <span class="actions">
                <!-- 放大 -->
                <span class="item">
                  <el-icon class="el-icon-zoom-in" @click="beforeImgDialogVisible = true"><ZoomIn/></el-icon>
                </span>
                <!-- 删除 -->
                <span class="item">
                  <el-icon class="el-icon-delete" @click="del"><Delete/></el-icon>
                </span>
              </span>
            </div>
          </div>
          <div class="content-right">
            <el-card v-if="active === 1" shadow="always" class="card">
              <div slot="header" class="clearfix">
                 <span class="upload-title">上传健身截图</span>
              </div>
              <div v-if="!isUpload" class="step1_before_upload">
                <div class="loading-icon">
                  <el-icon class="el-icon-camera-solid" @click="imgCapDialogVisible = true"><camera/></el-icon>
                </div>
                <p>未检测到图像上传，请先在 <b>左侧</b> 上传图像</p>
                <p>或点击
                  <el-icon class="el-icon-camera-solid"><camera/></el-icon>
                  进行拍照
                </p>
              </div>
              <div v-else>
                {{ active++ }}

              </div>
            </el-card>
            <el-card v-if="active === 2" shadow="always" class="card">
              <div slot="header" class="clearfix">
                <span class="upload-title">输入健身动作类型</span>
              </div>
              <el-form class="type-form">
                <el-form-item   >
                  <el-input v-model="screenshotsCurrent.exerciseName"
                    style="width: 300px; font-size: 20px; color: #000 !important; font-weight: bold; margin-top: 5px;">
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="img-tip-step2">
                点击 <b>下一步</b> 开始AI分析
              </div>
              <!-- <el-button type="primary" round class="img-button" @click="active--">上一步</el-button> -->
              <el-button type="primary" round class="img-button" @click="step2Next">下一步</el-button>
            </el-card>
            <el-card v-if="active === 3" shadow="always" class="card step3">
              <div slot="header" class="clearfix">
                <span class="upload-title">AI分析</span>
              </div>
              <div class="step3">
                <div class="img-info-item">
                  {{ '文件名: ' + this.screenShot.name }}
                </div>
                <div class="img-info-item">
                  {{ '类型: ' + this.screenShot.type }}
                </div>
                <div class="img-info-item">
                  {{ '文件大小: ' + this.convertFileSize(this.screenShot.size) }}
                </div>
                <div class="img-info-item">
                  {{ '健身动作类型: ' + this.screenshotsCurrent.exerciseName }}
                </div>
              </div>
              <div class="img-info-step3">
                请确认无误后，点击 <b>AI分析</b> 进行分析
              </div>
              <el-button type="primary" round class="img-button" @click="active--">上一步</el-button>
              <el-button type="primary" round class="img-button" @click="uploadImg">AI分析</el-button>
            </el-card>
          </div>
        </div>
      </div>
      <div class="after-tracking" v-else>
        <div class="content-left">
          <div class="after-success-tracking">
            <img :src="screenShotUrl" alt="" class="upload-img" />
            <span class="actions">
              <!-- 放大 -->
              <span class="item">
                <el-icon class="el-icon-zoom-in" @click="zoomin"><ZoomIn/></el-icon>
              </span>
            </span>
          </div>
        </div>
        <div class="content-right">
          <el-card shadow="always" class="card">
            <div slot="header" class="clearfix">
              <el-tag class="tag" v-if="analysisStatue === 0" type="success">分析成功</el-tag>
              <el-tag class="tag" v-if="analysisStatue === 1">AI分析中</el-tag>
              <el-tag class="tag" v-if="analysisStatue === 2" type="danger">分析失败</el-tag>
            </div>
            <div class="before-success-tracking" v-if="!successAnalyze">
              <div class="tracking" v-if="analysisStatue === 1">
                <div>
                  <el-progress class="progress" type="circle" :percentage="analysisPercentage" :width="300" />
                </div>
                <el-button type="primary" round class="cancel-btn" @click="cancelTrack">取消分析</el-button>
              </div>
              <div class="track-error" v-if="analysisStatue === 2">
                <div>
                  <el-progress class="progress" type="circle" :percentage="analysisPercentage" status="exception" />
                </div>
                <el-button type="primary" round class="cancel-btn" @click="tryAgain">重新分析</el-button>
              </div>
            </div>
            <!-- 在这里展示 后端返回的分析结果，Markdown 文本 -->
            <div class="after-success-tracking" v-else>
              <div class="markdown-content-container">
                <div class="markdown-content-res" v-html="markdownToHtml(markdownText)"></div>
              </div>
              <el-button type="primary" round class="img-info-finish" @click="retrain">重新分析</el-button>
            </div>
          </el-card>
        </div>
      </div>

    </div>
    <!-- 图片显示对话框 -->
    <el-dialog v-model="beforeImgDialogVisible" :modal-append-to-body="false" top="5vh" :show-close="false" class="dialog">
      <img :src="screenShotUrl" alt="" class="dialog-img"/>
    </el-dialog>



    <!--    拍照对话框-->
    <div v-show="imgCapDialogVisible" class="img-cap-dialog">
      <div class="img-cap-dialog-content">
        <p class="img-cap-dialog-content-title">拍照上传</p>
        <div v-show="!onCamara" class="before-open-camara">
          <el-form class="info-form">
            <el-form-item label="开启镜像:">
              <el-switch v-model="mirror" active-color="#262626">
              </el-switch>
            </el-form-item>
          </el-form>
          <el-button type="info" class="setting-btn" @click="imgCapDialogVisible = false">取消拍照</el-button>
          <el-button type="info" class="setting-btn" @click="openCamara">开启摄像头</el-button>
        </div>
        <div v-show="onCamara" class="after-open-camara">
          <div class="media-container">
            <video id="tracking-video" autoplay v-show="!trackFrame" class="cap-img" />
            <img :src="trackFrame" alt="" v-show="trackFrame" class="cap-img" />
          </div>
          <div class="setting-btn-group">
            <!--            <el-button type="info" class="setting-btn" @click="imgCapDialogVisible = false">取消拍照</el-button>-->
            <el-button type="info" class="setting-btn" @click="stopCamara">关闭摄像头</el-button>
            <el-button type="info" class="setting-btn" v-show="!trackFrame" @click="captureImage">拍照</el-button>
            <el-button type="info" class="setting-btn" v-show="trackFrame" @click="reCapture">重新拍照</el-button>
            <el-button type="info" class="setting-btn" v-show="trackFrame" @click="useCapture">使用图片</el-button>
          </div>
        </div>
      </div>
    </div>




    <!-- 显示上传的图片 -->
    <div v-if="uploadedScreenshots.length" class="screenshot-gallery">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(screenshot, index) in filteredScreenshots" :key="index">
          <el-card @click.native="openDialog(screenshot)"
            :body-style="{ padding: '10px', position: 'relative', height: '400px' }">
            <!-- 删除按钮 -->
            <el-button @click.stop="deleteScreenshot(screenshot)" type="danger"  circle
              style="position: absolute; bottom: 10px; right: 10px;z-index: 10">
              <el-icon class="el-icon-delete"><Delete/></el-icon>
            </el-button>
            <img :src="screenshot.screenshotUrl" alt="Screenshot" class="uploaded-image"
              style="max-height: 380px;height:90%; object-fit: contain; display: block; margin-bottom: 20px;" />
            <div style="text-align: center;">{{ screenshot.exerciseName }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 模态窗口展示详情 -->
    <el-dialog v-model="dialogVisible" width="60%">
      <div class="title-container">
        <div class="markdown-content">
          <strong>运动分析详情</strong>
        </div>
      </div>
      <hr class="divider">
      <div class="markdown-content">
        <strong>运动名称:</strong> <span v-html="markdownToHtml(screenshotForShow.exerciseName)"></span>
      </div>
      <hr class="divider">
      <div class="markdown-content">
        <strong>AI建议:</strong>
        <div class="markdown-content-container-sub">
          <span v-html="markdownToHtml(screenshotForShow.AIsuggestion)"></span>
        </div>
      </div>
      <hr class="divider">
      <p class="info">创建时间: {{ screenshotForShow.createTime }}</p>
      <hr class="divider">
      <img :src="screenshotForShow.screenshotUrl" alt="运动图片" class="dialog-image">
    </el-dialog>
  </div>
</template>


<script>
import ElementPlus from 'element-plus'
import axios from 'axios'
import MarkdownIt from 'markdown-it'
import { ElNotification } from 'element-plus';
const md = new MarkdownIt()
import { Upload,ZoomIn,Delete,Camera,Picture,Clock  } from '@element-plus/icons-vue'

export default {
  name: 'FitnessGuide',
  data() {
    return {
      searchQuery: '',
      dialogVisible: false,
      active: 1,
      isUpload: false, // false
      screenShot: '', // 上传图片File
      screenShotUrl: '', // 上传图片url
      isAnalyzing: false, // 是否开始分析
      beforeImgDialogVisible: false,
      imgCapDialogVisible: false,
      successAnalyze: false, // false
      analysisStatue: 1, // 检测状态（0：成功，1：检测中，2：失败）
      analysisPercentage: 0, // 检测进度
      stopProgress: false, // 终止进度条
      deviceId: '', // 选中设备id
      videoContain: null, // 摄像头视图容器
      mirror: false, // 摄像头镜像
      onCamara: false, // 是否打开摄像头
      videoStream: null, // 摄像头视频流
      trackFrame: '', // 跟踪完成的帧，
      uploadedBase64: '',
      // space
      userID: 0, // 暂时固定的用户ID
      vigorTokenBalance:0,
      uploadForm: {
        userID: 0,
        exerciseName: '',
        screenshot: null
      },
      screenshotsCurrent:
      {
        exerciseName: '',
        screenshotID: 0,
        screenshotUrl: '',
        AIsuggestion: '',
        createTime: ''
      },
      screenshotForShow:
      {
        exerciseName: '',
        screenshotID: 0,
        screenshotUrl: '',
        AIsuggestion: '',
        createTime: ''
      },
      uploadedScreenshots: [],
      markdownText: '# 标题'
    }
  },
  created() {
    this.getScreenshotFromDB()
    this.getVigorTokenBalance()
  },
  computed: {
    filteredScreenshots() {
      if (!this.searchQuery) {
        return this.uploadedScreenshots
      }
      return this.uploadedScreenshots.filter(screenshot => {
        return screenshot.exerciseName.toLowerCase().includes(this.searchQuery.toLowerCase())
      })
    }
  },
  watch: {
    mirror(isMirror) {
      if (isMirror) {
        this.videoContain.style.transform = 'rotateY(180deg)'
      } else {
        this.videoContain.style.transform = 'rotateY(0)'
      }
    }
  },
  mounted() {
    this.videoContain = document.getElementById('tracking-video')
  },
  beforeDestroy() {
    if (this.$store.state.cancelAxios.cancelAxios !== null) {
      this.$store.state.cancelAxios.cancelAxios()
      this.$store.dispatch('delReqUrl', true)
    }
  },
  components: {
    Upload,
    ZoomIn,
    Delete,
    Camera,
    Picture,
    Clock,
  },
  methods: {
    zoomin() {
    console.log('Icon clicked');
    this.beforeImgDialogVisible = true;
  },
    // 图片缩略图
    imgPreview(file) {
      console.log('file is', file)
      if (file.raw.type.split('/')[0] === 'image') {
        this.screenShot = file.raw
        this.screenShotUrl = URL.createObjectURL(file.raw)
        this.isUpload = true
        // 添加读取文件为 Base64 的代码
        const reader = new FileReader()
        reader.readAsDataURL(file.raw)
        reader.onload = () => {
          this.screenshotsCurrent.screenshotUrl = reader.result
          console.log('Base64 string:', this.screenshotsCurrent.screenshotUrl) // 这里可以查看 Base64 字符串
        }
      } else {
        ElNotification({
          title: '注意',
          message: `请上传正确的图像格式！`,
          type: 'warning',
          duration: 2000
        })
      }
    },
    // 删除图片
    del() {
      this.$confirm('此操作将删除该图像, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.screenShotUrl = ''
        this.isUpload = false
        this.active = 1
        ElNotification({
          title: '提示',
          message: `删除成功！`,
          type: 'success',
          duration: 2000
        })
      })
    },
    // 上传图片
    async uploadImg() {
      if (this.vigorTokenBalance < 50) {
        ElNotification({
          title: '注意',
          message: `本功能需要耗费50活力币，您的余额为${this.vigorTokenBalance}，余额不足!`,
          type: 'warning',
          duration: 2000
        })
        return
      }
      else {
        ElNotification({
          title: '注意',
          message: `本次消费50活力币，您的余额为${this.vigorTokenBalance-50}`,
          type: 'warning',
          duration: 2000
        })
      }
      this.isAnalyzing = true
      this.analysisStatue = 1
      this.analysisPercentage = 0
      if (this.screenShotUrl === '') {
        ElNotification({
          title: '注意',
          message: `请先上传图片或检查图片是否上传成功`,
          type: 'error',
          duration: 2000
        })
        return
      }
      console.log('开始分析')
      const token = localStorage.getItem('token');
      const requestData = {
        exerciseName: this.screenshotsCurrent.exerciseName,
        screenshotUrl: this.screenshotsCurrent.screenshotUrl
      }
      // 生成随机的更新间隔，例如1到5秒之间
      let randomTimeout = Math.floor(Math.random() * 250) + 50
      this.refreshProgress(randomTimeout)
      axios.post(`http://localhost:8080/api/AIGuide/Create?token=${token}`, requestData)
        .then(response => {
          this.screenshotsCurrent.screenshotID = response.data.screenshotID
          this.screenshotsCurrent.createTime = new Date(response.data.createTime)
          this.screenshotsCurrent.screenshotUrl = response.data.screenshotUrl
          console.log(response.data.message)
          this.getAISuggestions(response.data.screenshotID)
        }).catch(error => {
          console.error('创建失败：', error)
        })
    },
    // getAISuggestions(screenshotID) {
    //   console.log('获取AI建议:', screenshotID)
    //   axios.get(`http://localhost:8080/api/AIGuide/GetAISuggestion/`, {
    //     params: {
    //       screenshotID: screenshotID
    //     }
    //   })
    //     .then(response => {
    //       this.markdownText = response.data.message
    //       this.analysisPercentage = 100
    //       this.successAnalyze = true
    //       this.analysisStatue = 0
    //       console.log('AI建议:', response.data)
    //       this.screenshotsCurrent.AIsuggestion = response.data.message
    //       const screenshot = {
    //         exerciseName: this.screenshotsCurrent.exerciseName,
    //         screenshotID: this.screenshotsCurrent.screenshotID,
    //         screenshotUrl: this.screenshotsCurrent.screenshotUrl,
    //         AIsuggestion: this.screenshotsCurrent.AIsuggestion,
    //         createTime: this.screenshotsCurrent.createTime
    //       }
    //       console.log('imgUrl:', this.screenshotsCurrent.screenshotUrl)
    //       this.uploadedScreenshots.push(screenshot)
    //       this.getVigorTokenBalance()
    //     })
    //     .catch(error => {
    //       console.error('Error getting AI suggestions:', error)
    //     })
    // },
    getAISuggestions(screenshotID) {
      console.log('获取AI建议:', screenshotID);
      let attempts = 0;
      const maxAttempts = 15;
      const interval = 1000; // 1秒

      const poll = () => {
        if (attempts >= maxAttempts) {
          console.error('最大请求次数已达，请求终止。');
          return;
        }

        axios.get(`http://localhost:8080/api/AIGuide/GetAISuggestion/`, {
          params: {
            screenshotID: screenshotID
          }
        })
          .then(response => {
            if (response.data.message !== "") {
              console.log('AI建议:', response.data.message)
              this.markdownText = response.data.message;
              this.analysisPercentage = 100;
              this.successAnalyze = true;
              this.analysisStatue = 0;
              console.log('AI建议:', response.data);
              this.screenshotsCurrent.AIsuggestion = response.data.message;
              const screenshot = {
                exerciseName: this.screenshotsCurrent.exerciseName,
                screenshotID: this.screenshotsCurrent.screenshotID,
                screenshotUrl: this.screenshotsCurrent.screenshotUrl,
                AIsuggestion: this.screenshotsCurrent.AIsuggestion,
                createTime: this.screenshotsCurrent.createTime
              };
              console.log('imgUrl:', this.screenshotsCurrent.screenshotUrl);
              this.uploadedScreenshots.push(screenshot);
              this.getVigorTokenBalance();
              this.screenshotsCurrent.exerciseName = "";
              clearInterval(pollingInterval); // 停止轮询
            } else {
              attempts++;
            }
          })
          .catch(error => {
            console.error('Error getting AI suggestions:', error);
            attempts++;
          });
      };

      const pollingInterval = setInterval(poll, interval);
    },
    getScreenshotFromDB() {
      const token = localStorage.getItem('token');
      axios.get(`http://localhost:8080/api/AIGuide/GetAllDetails?token=${token}`)
        .then(response => {
          console.log(response.data.suggestions)
          response.data.suggestions.forEach(item => {
            const screenshot = {
              exerciseName: item.exerciseName,
              screenshotID: item.screenshotID,
              screenshotUrl: item.screenshotUrl,
              AIsuggestion: item.suggestions,
              createTime: item.createTime
            }
            this.uploadedScreenshots.push(screenshot)
          })
        })
    },
    deleteScreenshot(screenshot) {
      // 查找并删除匹配项
      axios.delete(`http://localhost:8080/api/AIGuide/Delete`, {
        params: {
          screenshotID: screenshot.screenshotID
        }
      })
        .then(response => {
          console.log(response.data.message)

          // 从列表中移除该项
          this.uploadedScreenshots = this.uploadedScreenshots.filter(item => item !== screenshot)
          ElNotification({
          title: '提示',
          message: `删除成功！`,
          type: 'success',
          duration: 2000
        })
        })
        .catch(error => {
          console.error('删除失败', error)
        })
    },
    markdownToHtml(text) {
      return md.render(text)
    },
    // 获取活力币余额
    getVigorTokenBalance() {
      const token = localStorage.getItem('token');
      axios.get(`http://localhost:8080/api/User/GetVigorTokenBalance?token=${token}`,
          {params:{
            userID:-1
            }})
        .then(response => {
          this.vigorTokenBalance = response.data.balance;
          })
    },
    // 重新检测
    retrain() {
      this.$confirm('重新分析将清空本次分析结果, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isUpload = false
        this.isAnalyzing = false
        this.successAnalyze = false
        this.screenShotUrl = ''
        this.active = 1
        ElNotification({
          title: '提示',
          message: `操作成功`,
          type: 'success',
          duration: 2000
        })
      })
    },
    // 再试一次
    tryAgain() {
      this.isAnalyzing = false
      this.successAnalyze = false
      this.active = 3
    },
    // 开启摄像头
    openCamara() {
      this.$confirm('即将开启摄像头，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.onCamara = true
        const constraints = {
          video: {
            deviceId: this.deviceId ? this.deviceId : undefined,
            width: { min: 640, ideal: 1280, max: 1920 },
            height: { min: 480, ideal: 720, max: 1080 }
          }
        }
        navigator.mediaDevices.getUserMedia(constraints).then(stream => {
          this.videoContain.srcObject = stream
          this.videoStream = stream
        })
      })
    },
    // 拍照
    captureImage() {
      const canvas = document.createElement('canvas')
      // console.log(video.videoHeight)
      canvas.width = this.videoContain.videoWidth
      canvas.height = this.videoContain.videoHeight
      let ctx = canvas.getContext('2d')
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      if (this.mirror) {
        ctx.translate(canvas.width, 0)
        ctx.scale(-1, 1)
      }
      ctx.drawImage(this.videoContain, 250, 35, 800, 650)
      canvas.toBlob(blob => {
        this.trackFrame = URL.createObjectURL(blob)
        this.screenShot = new File([blob], 'capture.png', { type: 'image/png' })
        this.screenShotUrl = this.trackFrame
      })
    },
    // 重新拍照
    reCapture() {
      this.trackFrame = ''
      this.screenShot = ''
    },
    // 关闭摄像头
    stopCamara() {
      this.videoStream.getTracks()[0].stop()
      this.videoStream = null
      this.videoContain.srcObject = null
      this.onCamara = false
    },
    // 使用照片
    useCapture() {
      this.screenShot = this.trackFrame
      this.trackFrame = ''
      this.imgCapDialogVisible = false
      this.isUpload = true
      this.videoStream.getTracks()[0].stop()
      this.videoStream = null
      this.videoContain.srcObject = null
      this.onCamara = false
    },
    // 文件大小类型换算
    convertFileSize(size) {
      if (size / 1024 > 1) {
        size /= 1024
        if (size / 1024 > 1) {
          size /= 1024
          if (size / 1024 > 1) {
            size /= 1024
            return size.toFixed(2) + ' GB'
          }
          return size.toFixed(2) + ' MB'
        }
        return size.toFixed(2) + ' KB'
      }
      return size.toFixed(2) + ' B'
    },
    // 判断检测类型是否为空
    step2Next() {
      this.active++
    },
    // 进度条更新
    refreshProgress(timeout) {
      let interval = setInterval(() => {
        this.analysisPercentage++
        if (this.analysisPercentage >= 99 || this.stopProgress) {
          this.stopProgress = false
          clearInterval(interval)
        }
      }, timeout)
    },
    // 取消分析
    cancelTrack() {
      this.$confirm('确认取消分析?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isAnalyzing = false
        this.active = 3
        this.$store.state.cancelAxios.cancelAxios()
        this.$store.dispatch('delReqUrl', true)
      })
    },
    // 在这里实现函数
    handleFileChange(event) {
      this.uploadForm.screenshot = event.target.files[0]
    },
    openDialog(screenshot) {
      console.log('hello', screenshot) // 添加这一行来调试
      this.screenshotForShow = {
        exerciseName: screenshot.exerciseName,
        screenshotUrl: screenshot.screenshotUrl,
        AIsuggestion: screenshot.AIsuggestion,
        createTime: screenshot.createTime
      }
      this.dialogVisible = true
    }
  }
}
</script>

<style scoped>
.title {
  font-size: 3vw;
  color: #3498db;
  text-shadow: 1px 1px 3px rgba(0,0,0,0.2);
  margin-bottom: 20px;
  font-weight: bold;
}

.title span {
  letter-spacing: 1vw;
}

.container {
  background: linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url("../assets/images/bg5.jpg") no-repeat center;
  background-size: cover;
  width: 200vw;
  margin-top: 3%;
  height: 85vh;
  position: relative;
  overflow-y: auto;
}

.steps {
  margin-bottom: 30px;
}

:deep(.el-step__title) {
  color: #f8f8f8 !important;
}

:deep(.el-step__head.is-process) {
  color: #3498db !important;
  border-color: #3498db !important;
}

:deep(.el-step__head.is-finish) {
  color: #2ecc71 !important;
  border-color: #2ecc71 !important;
}

:deep(.el-step__line) {
  background-color: rgba(255,255,255,0.5) !important;
}

.img-cap {
  border: none;
  background-color: transparent;
  font-size: 2.3vh;
  color: #3498db;
  transition: all 0.3s;
}

.img-cap:hover {
  font-weight: bold;
  color: #2980b9;
  transform: scale(1.05);
}

.img-cap-dialog {
  background-color: rgba(0, 0, 0, .8);
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.img-cap-dialog-content {
  width: 80%;
  height: 90vh;
  background-color: rgba(255, 255, 255, .9);
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.img-cap-dialog-content-title {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: bold;
  position: relative;
}

.img-cap-dialog-content-title:after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100px;
  height: 3px;
  background-color: #3498db;
}

.before-open-camara .info-form {
  margin: 40px auto;
  max-width: 500px;
}

.before-open-camara .info-form :deep(.el-form-item) {
  margin: 20px 0;
}

.before-open-camara .info-form :deep(.el-form-item__label) {
  font-size: 18px;
  color: #2c3e50;
}

.before-open-camara .setting-btn {
  padding: 10px 20px;
  margin: 10px;
  font-size: 16px;
  border-radius: 30px;
  background: #3498db;
  border-color: #3498db;
  transition: all 0.3s;
}

.before-open-camara .setting-btn:hover {
  background: #2980b9;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.media-container {
  margin: 20px auto;
  width: 80%;
  height: 55vh;
  border-radius: 15px;
  background-color: rgba(44, 62, 80, 0.1);
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.cap-img {
  width: 100%;
  height: 100%;
  border-radius: 15px;
  object-fit: contain;
}

.after-open-camara .setting-btn {
  padding: 10px 15px;
  margin: 10px;
  font-size: 16px;
  border-radius: 30px;
  background: #3498db;
  border-color: #3498db;
  transition: all 0.3s;
}

.after-open-camara .setting-btn:hover {
  background: #2980b9;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.setting-btn-group {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 20px;
}

.actions {
  position: absolute;
  border-radius: 15px;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  cursor: default;
  text-align: center;
  color: #fff;
  opacity: 0;
  background-color: rgba(0, 0, 0, .6);
  transition: all 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.actions:hover {
  opacity: 1;
}

.actions .item {
  margin: 0 15px;
  cursor: pointer;
  font-size: 30px;
  background-color: rgba(255,255,255,0.2);
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.actions .item:hover {
  background-color: rgba(255,255,255,0.4);
  transform: scale(1.1);
}

:deep(.uploader .el-upload-dragger) {
  width: 100%;
  height: 45vh;
  border-radius: 15px;
  border: 2px dashed rgba(255,255,255,0.5);
  background-color: rgba(255, 255, 255, 0.1);
  transition: all 0.3s;
}

:deep(.uploader .el-upload-dragger:hover) {
  border-color: #3498db;
  background-color: rgba(255, 255, 255, 0.2);
}

.uploader-icon {
  font-size: 50px !important;
  color: #ecf0f1;
  margin-bottom: 15px;
  transition: all 0.5s;
}

:deep(.uploader:hover .uploader-icon) {
  color: #3498db;
  transform: scale(1.2);
}

.uploader-text {
  margin-top: 15px !important;
  font-size: 24px !important;
  color: #ecf0f1;
}

.title-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.screenshot-gallery {
  margin-top: 30px;
  width: 90%;
  margin-left: 5%;
}

:deep(.el-card) {
  border-radius: 15px;
  overflow: hidden;
  transition: all 0.3s;
  margin-bottom: 20px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1) !important;
  background-color: rgba(255,255,255,0.85) !important;
}

:deep(.el-card:hover) {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.15) !important;
}

.uploaded-image {
  width: 100%;
  height: auto;
  max-height: 280px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease-in-out;
}

.uploaded-image:hover {
  transform: scale(1.03);
}

:deep(.el-dialog) {
  border-radius: 15px;
  overflow: hidden;
  background-color: rgba(255,255,255,0.95);
}

:deep(.el-dialog__header) {
  background-color: #3498db;
  padding: 15px 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: bold;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

.dialog-image {
  width: 100%;
  max-width: 50%;
  border-radius: 8px;
  display: block;
  margin: 0 auto;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.info {
  color: #34495e;
  font-size: 18px;
  margin: 10px 0;
}

:deep(.el-dialog__body p),
:deep(.el-dialog__body div) {
  color: #34495e;
  font-size: 16px;
}

.markdown-content-container {
  max-height: 40vh;
  min-height: 40vh;
  overflow-y: auto;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 15px;
  margin-top: 15px;
  background-color: #f9f9f9;
}

.markdown-content-container-sub {
  max-height: 200px;
  overflow-y: auto;
  padding: 15px;
  text-align: left;
  font-size: 16px !important;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.markdown-content {
  color: #2c3e50;
  font-size: 18px !important;
  text-align: left;
  margin-bottom: 15px;
  margin-left: 5%;
}

.markdown-content-res {
  color: #2c3e50;
  font-size: 16px !important;
  text-align: left;
  margin-bottom: 10px;
  line-height: 1.6;
}

.divider {
  border: 0;
  height: 1px;
  background: #e0e0e0;
  margin: 20px 0;
}

.search-input {
  width: 400px;
  height: 40px;
  margin-left: -60%;
  border-radius: 20px;
  font-size: 20px;
  background-color: #929191;
}



.type-form {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 30px 0;
}

:deep(.type-form .el-input) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.type-form .el-input__inner) {
  height: 45px;
  font-size: 18px;
  border: 1px solid #dcdfe6;
  box-shadow: none;
}

.content {
  margin: 1.5% auto;
  width: 90%;
  background-color: rgba(52, 73, 94, 0.7);
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
  backdrop-filter: blur(10px);
}

.before-tracking,
.after-tracking {
  display: flex;
  flex-wrap: wrap;
}

.content-left {
  width: 45%;
  height: 450px;
  margin-top: 2%;
  border-radius: 15px;
  background-color: rgba(236, 240, 241, 0.1);
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.content-right {
  margin-left: 2%;
  width: 50%;
  position: relative;
}

.upload-img {
  height: 100%;
  width: 100%;
  border-radius: 15px;
  object-fit: contain;
}

:deep(.before-tracking .content-right .card) {
  width: 100%;
  height: 450px;
  border-radius: 15px;
  background-color: rgba(255, 255, 255, 0.85) !important;
  box-shadow: 0 10px 30px rgba(0,0,0,0.15) !important;
}

:deep(.content-right .card .el-card__header) {
  border-bottom: 2px solid #e0e0e0;
  font-size: 20px;
  padding: 15px 20px;
  background-color: #3498db;
  color: white;
}

:deep(.after-tracking .content-right .card) {
  width: 100%;
  height: 500px;
  border-radius: 15px;
  background-color: rgba(255, 255, 255, 0.85) !important;
}

.card .step1_before_upload .loading-icon {
  font-size: 40px;
  margin: 40px 0;
  color: #3498db;
}

.card .step1_before_upload p {
  margin: 20px 0;
  font-size: 18px;
  color: #34495e;
}

:deep(.el-icon-camera-solid) {
  cursor: pointer;
  transition: all 0.3s;
}

:deep(.el-icon-camera-solid:hover) {
  color: #3498db;
  transform: scale(1.2);
}

.card .step1_after_upload {
  font-size: 18px;
}

.card .step1_after_upload .img-info-item {
  text-align: left;
  margin: 15px;
  color: #34495e;
}

.img-button {
  padding: 10px 30px;
  margin: 10px;
  font-size: 16px;
  border-radius: 30px;
  background: #3498db;
  border-color: #3498db;
  transition: all 0.3s;
}

.img-button:hover {
  background: #2980b9;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.card .img-tip-step2 {
  margin: 30px 0;
  font-size: 18px;
  color: #34495e;
}

.card .step3,
.card .after-success-tracking {
  font-size: 16px;
}

.card .step3 .img-info-item,
.card .after-success-tracking .img-info-item {
  text-align: left;
  margin: 15px;
  color: #34495e;
  background-color: rgba(236, 240, 241, 0.5);
  padding: 10px 15px;
  border-radius: 8px;
}

.card .img-info-step3 {
  margin: 25px 0;
  font-size: 18px;
  color: #34495e;
}

:deep(.el-tag) {
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 20px;
}

:deep(.el-progress-circle) {
  margin: 20px 0;
}

.after-tracking .content-right .cancel-btn {
  margin: 20px auto;
  padding: 10px 30px;
  font-size: 16px;
  border-radius: 30px;
  background: #e74c3c;
  border-color: #e74c3c;
  transition: all 0.3s;
}

.after-tracking .content-right .cancel-btn:hover {
  background: #c0392b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.after-tracking .content-right .after-success-tracking .img-info-finish {
  margin: 25px auto;
  padding: 10px 25px;
  font-size: 16px;
  border-radius: 30px;
  background: #3498db;
  border-color: #3498db;
  display: block;
  transition: all 0.3s;
}

.after-tracking .content-right .after-success-tracking .img-info-finish:hover {
  background: #2980b9;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.dialog {
  max-width: 80vw;
}

.dialog-img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .content-left, .content-right {
    width: 100%;
    margin-left: 0;
    margin-bottom: 20px;
  }
  
  .before-tracking, .after-tracking {
    flex-direction: column;
  }
  
  .search-input {
    width: 80%;
    margin-left: 0;
  }
}

:deep(.upload-title) {
  font-size: 24px !important;
  font-weight: bold !important;
  color: white !important;
}
</style>