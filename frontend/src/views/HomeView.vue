<template>
  <navigation-bar />
  <div class="home-page">
    <video ref="videoPlayer" class="background-image" autoplay loop muted>
      <source src="../assets/videos/HomeVideo.mp4" type="video/mp4">
      Your browser does not support the video tag.
    </video>
    
  </div>

</template>

<script>
import { ref } from 'vue'

</script>

<style scoped>
#app {
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: auto;
}

.contant {
  background-color: rgba(255, 255, 255, 0.7);
  vertical-align: middle;
  border-radius: 20px;
  width: auto;
  height: auto;
  padding: 5px;
  position: fixed;
  bottom: 1vw;
  right: 1vw;
}


.custom-link {
  width: 33px;
  height: 34px;
  border-radius: 15px;
}

.member-list {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-top: 1100px;
  margin-left: -35%;
}

.text {
  color: #0b2d47;
  font-weight: bold;
  font-size: 100px;
}

.background-image {
  overflow-y: hidden;
  position: absolute;
  top: 0px;
  left: 0;
  width: 100%;
  height: 100%;
  /* 使图片覆盖整个背景区域 */
  object-fit: fill;
  z-index: -1;
  /* 使背景图片位于最底层 */
}

.overlay-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  pointer-events: none;
  /* 使得点击事件不会影响到背景 */
}

.overlay-image {
  width: 500px;
  height: auto;
  /* 你可以添加更多样式来调整图片位置和大小 */
}
</style>
