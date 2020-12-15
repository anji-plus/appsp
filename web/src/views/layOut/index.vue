<template>
  <div :class="classObj" class="app-wrapper">
    <!-- <div v-if="sidebar.opened" class="drawer-bg" @click="handleClickOutside"/> -->
    <sidebar class="sidebar-container"/>
    <div class="main-container">
      <div class="navWrap">
        <navbar/>
        <tags-view/>
      </div>
      <app-main/>
    </div>
  </div>
</template>

<script>
import AppMain from "./AppMain"
import Navbar from "./Navbar"
import Sidebar from "./Sidebar"
import TagsView from "./TagsView"

export default {
  name: 'Wapper',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  data() {
    return {
      fullHeight: document.documentElement.clientHeight
    }
  },
  mounted() {
    let h = document.documentElement.clientHeight - 100 -48
    console.log(h)
    console.log(document.documentElement.clientHeight)
    localStorage.setItem('clientHeight', h)
    const that = this
      window.onresize = () => {
        return (() => {
          window.fullHeight = document.documentElement.clientHeight
          that.fullHeight = window.fullHeight- 100 -48
          console.log(that.fullHeight)
          localStorage.setItem('clientHeight', that.fullHeight)
        })()
      }
  },
  watch: {
    fullHeight (val) {
        if(!this.timer) {
          this.fullHeight = val
          this.timer = true
          let that = this
          setTimeout(function (){
            that.timer = false
          },400)
        }
      }
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.mobile.openSidebar{
      position: fixed;
      top: 0;
    }
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }
</style>
