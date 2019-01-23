<template>
</template>
<script>
    const globalEvent = weex.requireModule('globalEvent');
    const nav = weex.requireModule('navigator');
    export default {
        created() {
            //进行跳转到首页
            globalEvent.addEventListener("onPageInit", () => {
                //进入首页
                nav.presentFull({url:'root:home/index.js',animated:false},()=>{
                })
            })
        }
    }
</script>
<style scoped>

</style>
