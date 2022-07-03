<template>
  <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="dataList">
    <template #renderItem="{ item }">
      <a-card style="margin-top: 16px">
        <a-list-item key="item.name">
          <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
          </template>
          <template #extra>
            <img
                width="272"
                alt="logo"
                src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
            />
          </template>
          <a-list-item-meta :description="item.description">
            <template #title>
              <a :href="item.href">{{ item.name }}</a>
            </template>
            <template #avatar>
              <a-avatar :src="item.avatar"/>
            </template>
          </a-list-item-meta>
          <!--  todo 可以优化成秒 -->
          <a-space direction="vertical" size="middle">
            <div class="work-tags">
              <a-tag :color="DEFAULT_TAG_COLOR_ARRAY[index % DEFAULT_TAG_COLOR_ARRAY.length]"
                     v-for="(tag, index) in item.tagArray">
                {{ tag }}
              </a-tag>
            </div>
            <a-space direction="horizontal" :size="32">
              <div>工作时长：{{ item.duration }} 秒</div>
              <div>计划时间：{{ item.createTime }}</div>
              <div>创建时间：{{ item.createTime }}</div>
            </a-space>

          </a-space>
        </a-list-item>
      </a-card>
    </template>
  </a-list>

  <a-list item-layout="horizontal" :data-source="dataList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta>
          <template #title>
            {{ item.name }}
          </template>
          <template #description>
            <ul>
              <li>描述：{{ item.description }}</li>
              <li>标签：{{ item.tags }}</li>
              <li>时常：{{ item.duration }}</li>
              <li>计划时间：{{ item.planTime }}</li>
              <li>创建时间：{{ item.createTime }}</li>
            </ul>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import {EditOutlined, DeleteOutlined} from '@ant-design/icons-vue';
import WorkType from "../models/work";
import myAxios from "../plugins/myAxios";
import {onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import qs from 'qs';

const dataList = ref([]);

/**
 * 分页
 */
const pagination = ref({
  onChange: (pageNum: number) => {
    loadData(pageNum);
  },
  pageSize: 4,
});

/**
 * 默认标签颜色列表
 */
const DEFAULT_TAG_COLOR_ARRAY = [
  'red',
  'orange',
  'green',
  'cyan',
  'blue',
  'purple',
]


/**
 * 操作按钮
 */
const actions = [
  {type: EditOutlined, text: '更新'},
  {type: DeleteOutlined, text: '删除'},
];



const mockAvatar = 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png';

/**
 * 首次加载页面时
 */
onMounted(() => {
  loadData();
})

/**
 * 远程加载数据
 */
const loadData = async (pageNum = 1, pageSize = 4) => {
  const res = await myAxios.post('/work/list', {
    pageRequest: {
      current: pageNum,
      size: pageSize,
    },
    work: {}
  });
  console.log('data', res)

  if (res.code === 0) {
    const total = res.data.total;
    const tempDataList = res.data.records;
    tempDataList.forEach(data => {
      data.avatar = mockAvatar;
      data.tagArray = JSON.parse(data.tags ?? '[]');
      data.createTime = dayjs(data.createTime).format('YYYY-MM-DD HH:mm:ss');
      data.planTime = dayjs(data.planTime).format('YYYY-MM-DD HH:mm:ss');
    })
    dataList.value = tempDataList;
    pagination.value = {
      ...pagination.value,
      total,
    }
  } else {
    message.error('加载错误');
  }
}

const mockDataList: WorkType = [
  {
    name: '学 Java',
    description: '学习 Java 集合类',
    duration: 3600,
    tags: ['学习', 'Java'],
    createTime: new Date(),
    updateTime: new Date(),
    planTime: new Date(),
  },
  {
    name: '学 Java',
    description: '学习 Java 集合类',
    duration: 3600,
    tags: ['学习', 'Java'],
    createTime: new Date(),
    updateTime: new Date(),
  },
  {
    name: '学 Java',
    description: '学习 Java 集合类',
    duration: 3600,
    tags: ['学习', 'Java'],
    createTime: new Date(),
    updateTime: new Date(),
  }
]
</script>

<style scoped>

</style>