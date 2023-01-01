package com.rookie.rookiemeeting.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.Employee;
import com.rookie.rookiemeeting.entity.Meeting;
import com.rookie.rookiemeeting.service.IEmployeeService;
import com.rookie.rookiemeeting.service.IMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ECharts")
@Api(tags = "数据报表模块")
public class EChartsController {

    @Autowired
    IMeetingService meetingService;

    @Autowired
    IEmployeeService employeeService;

    @ApiOperation("数据展示接口")
    @GetMapping("/members")
    public Result members() {
        List<Meeting> list = meetingService.list();
        int q1 = 0;  //第一季度
        int q2 = 0;  //第二季度
        int q3 = 0;  //第三季度
        int q4 = 0;  //第四季度
        for (Meeting meeting : list) {
            Date reservationtime = meeting.getReservationtime();
            Quarter quarter = DateUtil.quarterEnum(reservationtime);
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return Result.succ(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    /**
     * 正常账号总数
     *
     * @return
     */
    @GetMapping("/normal")
    public Result normal() {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getStatus, 1);
        return Result.succ(employeeService.count(queryWrapper));
    }

    /**
     * 未审批总数
     *
     * @return
     */
    @GetMapping("/notApproved")
    public Result notApproved() {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getStatus, 0);
        return Result.succ(employeeService.count(queryWrapper));
    }

    /**
     * 审批未通过总数
     *
     * @return
     */
    @GetMapping("/approvalFailed")
    public Result approvalFailed() {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getStatus, 2);
        return Result.succ(employeeService.count(queryWrapper));
    }
}
