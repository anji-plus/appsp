<template>
  <el-scrollbar wrap-class="scrollbar-wrapper" style="overflow: hidden; backgroundColor: #091544">
    <div class="menu-title-view">
      <img class="logoImg" src="../../../assets/logo_white.png"/>
      <div v-if="sidebar.opened" class="titleView">加加移动服务平台</div>
    </div>
    <el-menu
      :show-timeout="200"
      :default-active="$route.path"
      :collapse="isCollapse"
      mode="vertical"
      background-color="#091544"
      text-color="#FFFFFF"
      :unique-opened='false'
      active-text-color="#94B9F2"
      router
    >
      <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" />
    </el-menu>
  </el-scrollbar>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'

export default {
  data() {
    return {
    }
  },
  components: { SidebarItem },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    isCollapse() {
      return !this.sidebar.opened
    },
  },
  created() {
  },
  methods: {
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    }
  }
}
</script>

<style>
  .user-img{
    border-radius: 50%;
    width: 27%;
  }
  .user-label{
    background-color: rgb(48, 65, 86);
    width: 100%;
    height: 80px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
  }
  .menu-title-view {
    width: 100%;
    height: 50px;
    background-color: #1E3FB2;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    text-align: end;
    line-height: 50px;
    padding-right: 20px;

    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .logoImg {
    margin-left: 10px;
    width: 30px;
    height: 30px;
    object-fit: contain;
  }
  .titleView {
    width: 100%;
    height: 50px;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    text-align: end;
    line-height: 50px;
  }
</style>
