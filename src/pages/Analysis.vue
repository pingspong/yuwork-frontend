<template>
  <div>
    <h1>各工作时长占比</h1>
    <div id="container" style="width: 100%; height: 600px">
    </div>
    <a-divider />
    <h1>工作时长分析</h1>
    <div id="histogramChartContainer" style="width: 100%; height: 600px">
    </div>
  </div>

</template>

<script setup>
import {Chart, Util} from '@antv/g2';
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {message} from "ant-design-vue";

const dataList = ref([]);
const chartDataList = ref([]);

onMounted(async () => {
  const res = await myAxios.post("/work/list", {});
  if (res.code === 0) {
    dataList.value = res.data.records;
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
      absoluteValue: data.duration,
      value: Number(Number(data.duration * 100 / totalDuration).toFixed(2)),
      percent: Number(Number(data.duration * 100 / totalDuration).toFixed(2)),
    }
  })
  chartDataList.value = chartData;
  renderBarChart(chartData);
  renderHistogramChart(chartData);
})

/**
 * 渲染柱状图
 */
function renderHistogramChart(chartData) {
  const data = chartData.map(tempData => {
    return {
      type: tempData.type,
      value: tempData.absoluteValue,
      percent: tempData.value,
    }
  })

  const chart = new Chart({
    container: 'histogramChartContainer',
    autoFit: true,
    height: 500,
    padding: [50, 20, 50, 20],
  });
  chart.data(data);
  chart.scale('value', {
    alias: '练习时长',
  });

  chart.axis('type', {
    tickLine: {
      alignTick: false,
    },
  });
  chart.axis('value', false);

  chart.tooltip({
    showMarkers: false,
  });
  chart.interval().position('type*value');
  chart.interaction('element-active');

// 添加文本标注
  data.forEach((item) => {
    chart
        .annotation()
        .text({
          position: [item.type, item.value],
          content: item.value,
          style: {
            textAlign: 'center',
          },
          offsetY: -30,
        })
        .text({
          position: [item.type, item.value],
          content: (item.percent * 100).toFixed(0) + '%',
          style: {
            textAlign: 'center',
          },
          offsetY: -12,
        });
  });
  chart.render();
}

/**
 * 渲染饼图
 */
function renderBarChart(chartData) {
  const chart = new Chart({
    container: 'container',
    autoFit: true,
    height: 400,
  });
  chart.data(chartData);

  chart.coordinate('theta', {
    radius: 0.75
  });
  chart.tooltip({
    showMarkers: false
  });

  const colorPlattle = ["#025DF4", "#DB6BCF", "#2498D1", "#BBBDE6", "#4045B2", "#21A97A", "#FF745A", "#007E99", "#FFA8A8", "#2391FF"];

  const interval = chart
      .interval()
      .adjust('stack')
      .position('value')
      .color('type')
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
        return {
          offset: -30,
          style: {
            fill: 'white',
            fontSize: 20,
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