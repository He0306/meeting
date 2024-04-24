<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>数据报表</el-breadcrumb-item>
        </el-breadcrumb>
        <el-row :gutter="20" style="margin-bottom: 70px">
            <el-col :span="8">
                <el-card style="color: #409EFF">
                    <div><i class="el-icon-user-solid"></i>正常账号总人数</div>
                    <div style="padding: 10px 0;text-align: center;font-weight: bold">
                        {{normal}} 人
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #F56C6C">
                    <div><i class="el-icon-user-solid"></i>未通过审批总人数</div>
                    <div style="padding: 10px 0;text-align: center;font-weight: bold">
                        {{approvalFailed}} 人
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #E6A23C">
                    <div><i class="el-icon-user-solid"></i>未审批总人数</div>
                    <div style="padding: 10px 0;text-align: center;font-weight: bold">
                        {{notApproved}} 人
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <div id="main" style="width: 500px;height: 410px"></div>
            </el-col>
            <el-col :span="12">
                <div id="pie" style="width: 600px;height: 410px;"></div>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    import * as echarts from 'echarts'

    export default {
        name: "DataReport",
        data() {
            return {
                normal: '',
                notApproved: '',
                approvalFailed: ''
            }
        },
        mounted() {
            //折线图
            var option = {
                title: {
                    text: '各季度预定会议次数统计',
                    subtext: '趋势图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                xAxis: {
                    type: 'category',
                    data: ["第一季度", "第二季度", "第三季度", "第四季度"]
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: '会议数',
                        data: [],
                        type: 'bar'
                    },
                ]
            };

            //饼图
            var pieOption = {
                title: {
                    text: '各季度预定会议次数统计图',
                    subtext: '比例图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        name: '会议数',
                        type: 'pie',
                        radius: '70%',
                        center: ['50%', '50%'],
                        label: {            //饼图图形上的文本标签
                            normal: {
                                show: true,
                                position: 'inner', //标签的位置
                                textStyle: {
                                    fontWeight: 300,
                                    fontSize: 14,    //文字的字体大小
                                    color: "#fff"
                                },
                                formatter: '{d}%'
                            }
                        },
                        data: [],  //数据
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    },
                ]
            };
            //折线图
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);
            //饼图
            var pieDom = document.getElementById('pie');
            var pieChart = echarts.init(pieDom);

            this.request.get('/ECharts/members').then(res => {
                console.log(res)
                option.series[0].data = res.data
                myChart.setOption(option);

                pieOption.series[0].data = [
                    {name: '第一季度', value: res.data[0]},
                    {name: '第二季度', value: res.data[1]},
                    {name: '第三季度', value: res.data[2]},
                    {name: '第四季度', value: res.data[3]}
                ]
                pieChart.setOption(pieOption)
            })
        },
        created() {
            this.request.get("/ECharts/normal").then(res=>{
                this.normal = res.data
            })
            this.request.get("/ECharts/notApproved").then(res=>{
                this.notApproved = res.data
            })
            this.request.get("/ECharts/approvalFailed").then(res=>{
                this.approvalFailed = res.data
            })
        }
    }
</script>

<style>

</style>