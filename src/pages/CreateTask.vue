<template>
  <br />
  <br />
  <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="onSubmit"
  >
    <a-form-item
        label="任务名称"
        name="name"
    >
      <a-input v-model:value="formState.name"/>
    </a-form-item>
    <a-form-item
        label="任务描述"
        name="description"
    >
      <a-input v-model:value="formState.description"/>
    </a-form-item>
    <a-form-item
        label="时长"
        name="duration"
    >
      <a-input v-model:value="formState.duration"/>
    </a-form-item>
    <a-form-item
        label="标签"
        name="tags"
    >
      <a-input v-model:value="formState.tags"/>
    </a-form-item>
    <a-form-item
        label="计划时间"
        name="planTime"
    >
      <a-date-picker show-time v-model:value="formState.planTime" />
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">提交</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import WorkType from "../models/work";
import myAxios from "../plugins/myAxios";
import {message} from "ant-design-vue";

const formState = reactive<WorkType> ({
  name: '学 Java',
  description: '学习 Java 集合类',
  duration: 3600,
  tags: ['学习', 'Java'],
  planTime: undefined,
});

const onSubmit = async (values) => {
  // 校验和处理
  if (values.tags) {
    values.tags = JSON.stringify(values.tags);
  }
  console.log('Success:', values);

  const res = await myAxios.post('/work/create', values);
  if (res.code === 0) {
    message.success('插入成功');
  } else {
    message.error('插入失败');
  }
};
</script>

<style scoped>

</style>