package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.service.IMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@Api(tags = "通知控制类")
public class NotificationsController {

    @Autowired
    IMeetingService meetingService;

    /**
     * 未来14天要参加的会议
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("未来14天要参加的会议接口")
    @GetMapping("/sevenDayMeeting/{employeeid}")
    public Result sevenDayMeeting(@PathVariable(name = "employeeid") Long employeeid,
                                  @RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String meetingname) {
        return Result.succ(meetingService.getSevenDayMeeting(new Page<>(pageNum, pageSize), employeeid, meetingname));
    }

    /**
     * 取消的会议
     *
     * @param emplyeeid
     * @return
     */
    @ApiOperation("取消的会议的接口")
    @GetMapping("/cancelMeeting/{employeeid}")
    public Result cancelMeeting(@PathVariable(name = "employeeid") Integer emplyeeid,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String meetingname) {
        return Result.succ(meetingService.getCancelMeeting(new Page<>(pageNum, pageSize), emplyeeid, meetingname));
    }
}
