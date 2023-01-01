package com.rookie.rookiemeeting.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.dto.ParticipantsDto;
import com.rookie.rookiemeeting.service.IEmployeeService;
import com.rookie.rookiemeeting.service.IMeetingParticipantsService;
import com.rookie.rookiemeeting.service.IMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Api(tags = "更改预订控制类")
public class ChangeBookingController {

    @Autowired
    IMeetingService meetingService;

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    IMeetingParticipantsService meetingParticipantsService;

    /**
     * 我的预定，并且会议状态为0
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("根据已登录的员工ID查询自己的会议并且会议状态为0接口")
    @GetMapping("/mybookings/{employeeid}")
    public Result mybookings(@PathVariable(name = "employeeid") Long employeeid,
                             @RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(defaultValue = "") String meetingname) {
        return Result.succ(meetingService.getmeetingofmybookCanCancle(new Page<>(pageNum, pageSize), employeeid, meetingname));
    }

    /**
     * 取消会议的原因
     *
     * @param meetingid
     * @param canceledreason
     * @return
     */
    @ApiOperation("取消会议接口")
    @PostMapping("/cancelmeeting/{meetingid}")
    public Result cancelmeeting(@PathVariable(name = "meetingid") Integer meetingid, @RequestBody String canceledreason) {
        meetingService.cancelmeeting(meetingid, canceledreason);
        return Result.succ(null);
    }

    /**
     * 根据ID查询会议详情
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("根据ID查询会议详情接口")
    @PostMapping("/meetingById/{meetingid}")
    public Result meetingById(@PathVariable(name = "meetingid") Integer meetingid) {
        return Result.succ(meetingService.getById(meetingid));
    }

    /**
     * 根据已登录的员工ID查询自己的会议
     *
     * @param employeeid
     * @param pageNum
     * @param pageSize
     * @param meetingname
     * @return
     */
    @ApiOperation("根据已登录的员工ID查询自己的会议接口")
    @GetMapping("/myMeeting/{employeeid}")
    public Result myMeeting(@PathVariable(name = "employeeid") Long employeeid,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "") String meetingname) {
        return Result.succ(meetingService.getMyMeeting(new Page<>(pageNum, pageSize), employeeid, meetingname));
    }

    /**
     * 查询参会人员
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("查询参会人员接口")
    @PostMapping("/participants/{meetingid}")
    public Result participants(@PathVariable(name = "meetingid") Integer meetingid) {
        return Result.succ(employeeService.getEmpsById(meetingid));
    }

    /**
     * 导出考勤信息
     *
     * @param map
     * @param response
     * @param request
     * @param meetingid
     */
    @GetMapping("/exports/{meetingid}")
    public void export(ModelMap map, HttpServletResponse response, HttpServletRequest request, @PathVariable("meetingid") Integer meetingid) {
        List<ParticipantsDto> emps = employeeService.getEmpsById(meetingid);
        ExportParams exportParams = new ExportParams("考勤信息", "考勤信息", ExcelType.XSSF);
        map.put(NormalExcelConstants.DATA_LIST, emps);
        map.put(NormalExcelConstants.CLASS, ParticipantsDto.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        map.put(NormalExcelConstants.FILE_NAME, "考勤信息表");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);

    }

    /**
     * 日历显示会议功能
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("日历显示会议接口")
    @GetMapping("/calendar/{employeeid}")
    public Result calendar(@PathVariable(name = "employeeid") Long employeeid) {
        return Result.succ(meetingService.getCalendar(employeeid));
    }

    /**
     * 参加会议总人数
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("参加会议总人数接口")
    @PostMapping("/queryNum/{meetingid}")
    public Result queryNum(@PathVariable(name = "meetingid") Integer meetingid) {
        return Result.succ(meetingParticipantsService.queryNum(meetingid));
    }

    /**
     * 未签到人数
     *
     * @param meetingid
     * @return
     */
    @ApiOperation("未签到人数接口")
    @PostMapping("/signed/{meetingid}")
    public Result signed(@PathVariable(name = "meetingid") Integer meetingid) {
        return Result.succ(meetingParticipantsService.signed(meetingid));
    }
}
