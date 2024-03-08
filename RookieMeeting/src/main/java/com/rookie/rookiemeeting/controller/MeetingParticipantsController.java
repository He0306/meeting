package com.rookie.rookiemeeting.controller;

import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.MeetingParticipants;
import com.rookie.rookiemeeting.service.IMeetingParticipantsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addMeeting")
@Api(tags = "会议参与者控制类")
public class MeetingParticipantsController {


    @Autowired
    IMeetingParticipantsService meetingParticipantsService;

    /**
     * 选择参会员工
     *
     * @param meetingid
     * @param emplyeeid
     * @return
     */
    @ApiOperation("添加参会员工接口")
    @PostMapping("/addEmployee/{meetingid}")
    public Result addEmployee(@PathVariable(name = "meetingid") Integer meetingid,
                              @RequestBody List<Long> emplyeeid) {
        meetingParticipantsService.addParticipants(meetingid, emplyeeid);
        return Result.succ(null);
    }

    /**
     * 根据会议ID，员工ID删除该员工的会议
     *
     * @param meetingid
     * @param employeeid
     * @return
     */
    @ApiOperation("根据会议ID，员工ID删除该员工的会议接口")
    @GetMapping("/delEmployee/{meetingid}/{employeeid}")
    public Result delEmployee(@PathVariable(name = "meetingid") Integer meetingid,
                              @PathVariable(name = "employeeid") Long employeeid) {
        meetingParticipantsService.delEmployee(meetingid, employeeid);
        return Result.succ(null);
    }

    /**
     * 补签
     *
     * @param meetingParticipants
     * @return
     */
    @ApiOperation("补签接口")
    @PostMapping("/signature")
    public Result signature(@RequestBody MeetingParticipants meetingParticipants) {
        meetingParticipantsService.signature(meetingParticipants);
        return Result.succ(null);
    }


}
