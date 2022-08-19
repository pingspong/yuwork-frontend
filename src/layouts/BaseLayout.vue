<template>
  <a-layout class="layout">
    <a-layout-header>
      <img class="logo" src="https://yupi.icu/logo.png"/>
      <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
          @click="toPage"
      >
        <a-menu-item key="/">主页</a-menu-item>
        <a-menu-item key="/task/create">创建任务</a-menu-item>
        <a-menu-item key="/analysis">分析</a-menu-item>
      </a-menu>
    </a-layout-header>
    <a-layout-content style="padding: 0 50px">
      <router-view/>
    </a-layout-content>
    <a-layout-footer style="text-align: center">
      程序员鱼皮 ©2022
    </a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import {computed, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();

const selectedKeys = computed(() => {
  return [route.path];
})

const toPage = ({key}: { key: string }) => {
  router.push({
    path: key
  })
}
</script>

<style scoped>
.logo {
  height: 42px;
  float: left;
  margin-top: 12px;
  margin-right: 12px;
}
</style>