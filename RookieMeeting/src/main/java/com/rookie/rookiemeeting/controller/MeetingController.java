package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.dto.MeetingDto;
import com.rookie.rookiemeeting.entity.Meeting;
import com.rookie.rookiemeeting.service.IMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting")
@Api(tags = "会议控制类")
public class MeetingController {

    @Autowired
    IMeetingService meetingService;


    /**
     * 分页和查询全部
     *
     * @param meetingDto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("分页和查询全部接口")
    @GetMapping("/page")
    public Result page(MeetingDto meetingDto,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {


        return Result.succ(meetingService.findMeeting(new Page<>(pageNum, pageSize), meetingDto));

    }

    /**
     * 根据ID删除会议
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("根据会议ID删除会议接口")
    @DeleteMapping("/delete/{meetingid}")
    public Result delete(@PathVariable(name = "meetingid") Integer meetingid) {
        meetingService.delById(meetingid);
        return Result.succ(null);
    }

    /**
     * 根据ID查询会议详情
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("根据会议ID查询会议详情")
    @PostMapping("/meetingById/{meetingid}")
    public Result meetingById(@PathVariable(name = "meetingid") Integer meetingid) {
        return Result.succ(meetingService.meetingById(meetingid));
    }


    /**
     * 修改会议信息
     *
     * @param meeting
     * @return
     */
    @ApiOperation("修改会议信息接口")
    @PostMapping("/save")
    public Result save(@RequestBody Meeting meeting) {
        meetingService.saveOrUpdate(meeting);
        return Result.succ(null);
    }

    /**
     * 预定会议
     *
     * @param meeting
     * @param employeeid
     * @return
     */
    @ApiOperation("预定会议接口")
    @PostMapping("/addMeeting/{employeeid}")
    public Result addMeeting(@RequestBody Meeting meeting,
                             @PathVariable(name = "employeeid") Long employeeid) {
        return Result.succ(meetingService.addMeeting(meeting, employeeid));
    }

}
