<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-bottom: 20px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日历</el-breadcrumb-item>
      <span v-if="role === '2'">您好，管理员</span>
    </el-breadcrumb>

    <div><el-tag type="danger">注意：带 <span style="color: red;font-size: 15px">*</span> 的是有需要参加的会议</el-tag></div>

    <div>
      <el-calendar v-model="nowDate">
        <template slot="dateCell" slot-scope="{date, data}">
          <div :class="{ selected : isSelected(date, data) }">
            <div class="solar">{{ data.day.split('-')[2] }}<span class="met" v-for="(i,item) in meetingData" v-if="data.day === i">*</span></div>
            <div class="lunar" :class="{ festival : isFestival(date, data) }">{{ solarToLunar(date, data) }}</div>
          </div>
        </template>
      </el-calendar>
    </div>
  </div>
</template>

<script>
import calendar from '@/util/calendar.js'
export default {
  name: "Home",
  data(){
    return {
      role: "",
      nowDate: new Date(),
      selectedDates: [], // 根据selectedDates设置选中日期
      employee: localStorage.getItem("employee") ? JSON.parse(localStorage.getItem("employee")) : {},
      meetingData: []
    }
  },
  created() {
    this.role = localStorage.getItem("role")
    this.request.get("/bookings/calendar/" + this.employee.employeeid).then(res=>{
      this.meetingData = res.data
      console.log(this.meetingData)

    })
  },
  methods:{
    // 是否选中日期
    isSelected : function(slotDate, slotData) {
      return this.selectedDates.includes(slotData.day)
    },
    // 是否节假日
    isFestival(slotDate, slotData) {
      let solarDayArr = slotData.day.split('-');
      let lunarDay = calendar.solar2lunar(solarDayArr[0], solarDayArr[1], solarDayArr[2])

      // 公历节日\农历节日\农历节气
      let festAndTerm = [];
      festAndTerm.push(lunarDay.festival == null ? '' : ' ' + lunarDay.festival)
      festAndTerm.push(lunarDay.lunarFestival == null ? '' : '' + lunarDay.lunarFestival)
      festAndTerm.push(lunarDay.Term == null ? '' : '' + lunarDay.Term)
      festAndTerm = festAndTerm.join('')

      return festAndTerm !== ''
    },
    // 公历转农历
    solarToLunar(slotDate, slotData) {
      let solarDayArr = slotData.day.split('-');
      let lunarDay = calendar.solar2lunar(solarDayArr[0], solarDayArr[1], solarDayArr[2])

      // 农历日期
      let lunarMD = lunarDay.IMonthCn + lunarDay.IDayCn

      // 公历节日\农历节日\农历节气
      let festAndTerm = [];
      festAndTerm.push(lunarDay.festival == null ? '' : ' ' + lunarDay.festival)
      festAndTerm.push(lunarDay.lunarFestival == null ? '' : '' + lunarDay.lunarFestival)
      festAndTerm.push(lunarDay.Term == null ? '' : '' + lunarDay.Term)
      festAndTerm = festAndTerm.join('')

      return festAndTerm === '' ? lunarMD : festAndTerm
    },
  }
}
</script>

<style>
.el-calendar-table .el-calendar-day{
  height: 65px;
}
/**日期div的样式-公历*/
.el-calendar-table .el-calendar-day > div .solar {
  text-align: center
}
/**日期div的样式-农历*/
.el-calendar-table .el-calendar-day > div .lunar {

  text-align: center
}

/**本月农历设置为灰色*/
.el-calendar-table .current .lunar {
  color: #606266;
}
/**本月农历节日设置为红色*/
.el-calendar-table .current .lunar.festival {
  color: red;
}
.met {
  color: red;
  padding-left: 10px;
}

</style>