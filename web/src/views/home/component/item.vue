<template>
  <div class="app-item-base-view" @click="getDetail">
    <div class="app-item-top-view">
      <div class="app-item-top-left-view">
        <div class="app-item-color-view"/>
        <h1>{{itemModel.name}}</h1>
        <h2>最近操作：{{itemModel.operationTime}}</h2>
        <h2>操作内容：{{itemModel.operationName}} {{itemModel.operationTitle}}</h2>
      </div>
      <img class="app-item-app-logo" src="@/assets/logo.png"/>
    </div>
    <div class="app-item-row-line"/>
    <div v-if="isAdmin" class="app-item-bottom-view">
      <!-- <i class="el-icon-delete" style="marginRight: 10px;cursor:pointer" @click.stop="doDelete"></i> -->
      <!-- <i class="el-icon-edit-outline" style="cursor:pointer" @click.stop="doEdit"></i> -->
      <i class="btn-delete" style="marginRight: 10px;cursor:pointer" @click.stop="doDelete"></i>
      <i class="btn-edit" style="cursor:pointer" @click.stop="doEdit"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AppItem',
  props: {
      itemModel: Object,
    isAdmin: Boolean,
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods: {
    // 点击删除
    doDelete() {
      var that = this;
      that.$confirm('确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            that.$emit("deleteClick", that.itemModel.appId);
        }).catch(() => {

        });
    },

    // 点击编辑
    doEdit() {
      this.$emit("editClick", this.itemModel.appId);
    },

    // 进入详情
    getDetail() {
      this.$emit("getDetail", this.itemModel.appId);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1 {
    color: #333333;
    font-size: 16px;
    font-weight: bold;
  }
  h2 {
    color: #D5D5D5;
    font-size: 10px;
    font-weight: 500;
  }
  .app-item-base-view {
      cursor:pointer;
      width: 23%;
      /* height: 100%; */
      height: 170px;
      background-color:#fff;
      padding: 15px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      border-radius: 8px;
      -moz-box-shadow:0px 0px 7px rgba(54, 141, 255, 0.2);
      -webkit-box-shadow:0px 0px 7px rgba(54, 141, 255, 0.2);
      box-shadow:0px 0px 7px rgba(54, 141, 255, 0.2);
  }
  .app-item-top-view {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    width: 100%;
  }
  .app-item-top-left-view {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
  .app-item-app-logo {
    object-fit: contain;
    width: 30%;
  }
  .app-item-color-view {
    width: 30%;
    height: 4px;
    background-image: linear-gradient(to right, #2D54F8, #7552F2);
  }
  .app-item-row-line {
    width: 100%;
    height: 1px;
    margin-top: 10px;
    margin-bottom: 10px;
    background-color: #E9E9E9;
  }
  .app-item-bottom-view {
    display: flex;
    width: 100%;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
  }
  .btn-delete {
    width: 20px;
    height: 20px;
    background: url('../../../assets/img/delete.png');
    background-size:contain;
  }
  .btn-edit {
    width: 20px;
    height: 20px;
    background: url('../../../assets/img/edit.png');
    background-size:contain;
  }
</style>
