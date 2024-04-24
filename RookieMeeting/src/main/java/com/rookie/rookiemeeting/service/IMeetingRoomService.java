package com.rookie.rookiemeeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.rookiemeeting.entity.MeetingRoom;

import java.util.List;

public interface IMeetingRoomService extends IService<MeetingRoom> {

    //查询可用的会议室
    List<MeetingRoom> queryAll();


    IPage<MeetingRoom> mrPage(Integer pageNum, Integer pageSize, String roomname, String roomnum);
}
