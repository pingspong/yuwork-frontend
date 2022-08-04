<template>
  <div>
<!--    <a-button type="primary" @click="showModal">Open Modal</a-button>-->
    <a-modal :visible="props.visible" title="Basic Modal" :footer="null" @cancel="handleClose">
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
          <a-select
              v-model:value="formState.tags"
              mode="tags"
              style="width: 100%"
              placeholder="选择标签"
              :options="[]"
          />
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
    </a-modal>
  </div>
</template>
<script setup lang="ts">
import {ref, watchEffect} from "vue";
import WorkType from "../models/work";
import myAxios from "../plugins/myAxios";
import {message} from "ant-design-vue";
import dayjs from "dayjs";


let formState = ref<WorkType> ({
  // name: '默认值',
  // description: '学习 Java 集合类',
  // duration: 3600,
  // tags: ['学习', 'Java'],
  // planTime: undefined,
} as WorkType);


const props = defineProps({
  visible: Boolean,
  data: {
    required: true
  },
})

watchEffect(() => {
  if (props.data) {
    const tempData = {...props.data};
    tempData.tags = JSON.parse(tempData.tags ?? '[]');
    console.log('计划时间', tempData.planTime);
    tempData.planTime = dayjs(tempData.planTime);
    formState.value = tempData;
  }

})

const emits = defineEmits(['onClose', 'onOk']);

const handleClose = (e: MouseEvent) => {
  emits('onClose');
}

const onSubmit = async () => {
  const values: any = formState.value;
  values.createTime = undefined;
  values.updateTime = undefined;
  console.log(values)
  // 校验和处理
  if (values.tags) {
    values.tags = JSON.stringify(values.tags);
  }
  console.log('Success:', values);

  const res: any = await myAxios.post('/work/update', values);
  if (res.code === 0) {
    message.success('操作成功');
    emits('onOk');
  } else {
    message.error('操作失败');
  }
};

</script>