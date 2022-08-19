<template>
  <a-input-search
      style="margin-top: 16px"
      v-model:value="searchParams.name"
      placeholder="搜索任务名称"
      enter-button
      @search="onSearch"
  />
  <TaskModal :visible="modalVisible" :data="updateData" @onClose="onClose" @onOk="onOk"/>
  <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="dataList">
    <template #renderItem="{ item, index }">
      <a-card style="margin-top: 16px">
        <a-list-item :key="index">
          <template #actions>
            <a-button @click="doUpdate(item)">
              <EditOutlined/>
              更新
            </a-button>
            <a-button danger @click="doDelete(item.id)">
              <DeleteOutlined/> 删除
            </a-button>
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
              <div>计划时间：{{ item.planTime }}</div>
              <div>创建时间：{{ item.createTime }}</div>
            </a-space>
          </a-space>
        </a-list-item>
      </a-card>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import WorkType from "../models/work";
import myAxios from "../plugins/myAxios";
import {onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import TaskModal from "../components/TaskModal.vue";

const DEFAULT_PAGE_SIZE = 4;

// 搜索条件
const searchParams = ref<Record<string, any>>({
  name: '',
  pageSize: DEFAULT_PAGE_SIZE,
  pageNum: 1,
});
const dataList = ref([]);
const modalVisible = ref(false);
const updateData = ref(null)

const doUpdate = (data) => {
  modalVisible.value = true;
  updateData.value = data;
}

/**
 * 删除
 */
const doDelete = async (id: string) => {
  const res = await myAxios.post("/work/delete", {
    id
  });
  if (res?.code === 0) {
    loadData();
    message.success('删除成功');
  } else {
    message.error('删除失败');
  }
}

const onClose = () => {
  modalVisible.value = false;
}

const onOk = () => {
  searchParams.value = {...searchParams.value, current: 1}
  loadData();
  modalVisible.value = false;
}

/**
 * 分页
 */
const pagination = ref({
  onChange: (pageNum: number) => {
    searchParams.value = {...searchParams.value, pageNum};
    loadData();
  },
  pageSize: DEFAULT_PAGE_SIZE,
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

const mockAvatar = 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png';

/**
 * 首次加载页面时
 */
onMounted(() => {
  loadData();
})

/**
 * 搜索
 */
const onSearch = async () => {
  searchParams.value = {...searchParams.value, pageNum: 1};
  loadData()
}

const blackWord = '1 = 1';

/**
 * 远程加载数据
 */
const loadData = async () => {
  const {name, pageNum, pageSize} = searchParams.value;
  if (name?.includes(blackWord)) {
    message.error('我爱你！！！');
    return;
  }
  const res: any = await myAxios.post('/work/list', {
    current: pageNum,
    size: pageSize,
    name,
  });
  console.log('data', res)

  if (res.code === 0) {
    const total = res.data.total;
    const tempDataList = res.data.records;
    tempDataList.forEach(data => {
      data.avatar = mockAvatar;
      if (data.tags && data.tags.length > 0) {
        data.tagArray = JSON.parse(data.tags);
      } else {
        data.tagArray = [];
      }
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