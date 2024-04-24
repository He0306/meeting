package com.rookie.rookiemeeting.controller;

import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.MeetingRoom;
import com.rookie.rookiemeeting.service.IMeetingRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetingroom")
@Api(tags = "会议室控制类")
public class MeetingRoomController {

    @Autowired
    IMeetingRoomService meetingRoomService;

    /**
     * 分页查询全部会议室
     *
     * @param pageNum
     * @param pageSize
     * @param roomname
     * @param roomnum
     * @return
     */
    @ApiOperation("分页查询全部会议室接口")
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String roomname,
                       @RequestParam(defaultValue = "") String roomnum) {

        return Result.succ(meetingRoomService.mrPage(pageNum, pageSize, roomname, roomnum));
    }

    /**
     * 新增和编辑
     *
     * @param meetingRoom
     * @return
     */
    @ApiOperation("新增和编辑接口")
    @PostMapping("/save")
    public Result save(@RequestBody MeetingRoom meetingRoom) {
        return Result.succ(meetingRoomService.saveOrUpdate(meetingRoom));
    }

    /**
     * 根据id删除单个会议室
     *
     * @param roomid
     * @return
     */
    @ApiOperation("根据会议室ID删除单个会议室接口")
    @DeleteMapping("/delete/{roomid}")
    public Result delete(@PathVariable(name = "roomid") Integer roomid) {
        return Result.succ(meetingRoomService.removeById(roomid));
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除会议室接口")
    @PostMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        return Result.succ(meetingRoomService.removeBatchByIds(ids));
    }

    /**
     * 查询可用的会议室
     *
     * @return
     */
    @ApiOperation("查询可用的会议室接口")
    @PostMapping("/list")
    public Result list() {
        return Result.succ(meetingRoomService.queryAll());
    }
}
