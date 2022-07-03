<template>
  <div id="container" style="width: 100%; height: 1000px">
    容器
  </div>
</template>

<script setup>
import {Chart, Util} from '@antv/g2';
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {message} from "ant-design-vue";

const dataList = ref([]);

onMounted(async () => {
  const res = await myAxios.get("/work/list");
  if (res.code === 0) {
    dataList.value = res.data;
  } else {
    message.error('加载错误');
  }

  let totalDuration = dataList.value.reduce((totalDuration, data) => {
        return totalDuration + data.duration
      }, 0
  )
  const chartData = dataList.value.map(data => {
    return {
      type: data.name,
      value: data.duration / totalDuration,
    }
  })
  renderBarChart(chartData);
})

/**
 * 渲染饼图
 */
function renderBarChart(chartData) {
  const chart = new Chart({
    container: 'container',
    autoFit: true,
    height: 500,
  });
  chart.data(chartData);

  chart.coordinate('theta', {
    radius: 0.75
  });
  chart.tooltip({
    showMarkers: false
  });

  const interval = chart
      .interval()
      .adjust('stack')
      .position('value')
      // .color('type', ['#063d8a', '#1770d6', '#47abfc', '#38c060'])
      .style({opacity: 0.4})
      .state({
        active: {
          style: (element) => {
            const shape = element.shape;
            return {
              matrix: Util.zoom(shape, 1.1),
            }
          }
        }
      })
      .label('type', (val) => {
        const opacity = val === '四线及以下' ? 1 : 0.5;
        return {
          offset: -30,
          style: {
            opacity,
            fill: 'white',
            fontSize: 12,
            shadowBlur: 2,
            shadowColor: 'rgba(0, 0, 0, .45)',
          },
          content: (obj) => {
            return obj.type + '\n' + obj.value + '%';
          },
        };
      });

  chart.interaction('element-single-selected');

  chart.render();

}

</script>

<style scoped>

</style>