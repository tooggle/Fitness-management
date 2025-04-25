<template>
    <div>
        <navigation-bar />
        <Forum />
        <common-layout />
        <notification-box />

    </div>
</template>


<script>
import Forum from "../components/Forum.vue";
import {commonMixin} from '../mixins/checkLoginState';
import NotificationBox from '../components/NotificationBox.vue';
import NavigationBar from '../components/NavigationBar.vue';
import CommonLayout from '../components/CommonLayout.vue';

export default {
  mixins: [commonMixin],
  components: {Forum, NotificationBox, NavigationBar, CommonLayout},
  created() {
    this.checkAvailable()
  }
}
</script>