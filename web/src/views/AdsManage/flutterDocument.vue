<template>
    <div class="baseView" style="">
      <article class="markdown-body"><readme></readme></article>
    </div>
</template>

<script>
  //引入marked
  const marked = require('marked')
  import readme from './md/flutter.md' //导入！！！注意使用你存放的路径
  export default {
    components: {readme},
    data() {
      return {
        md: ''
      }
    },
    created() {
      // 调用marked函数，传入helloMarked，返回一段html
      this.md = marked(readme)
    }
  }
</script>

<style scoped>
    @import "../../styles/markdown.css";

    .baseView {
        padding-left: 40px;
        padding-right: 40px;
    }
</style>

