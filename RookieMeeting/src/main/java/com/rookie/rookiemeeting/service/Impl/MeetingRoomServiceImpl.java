package com.rookie.rookiemeeting.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.rookiemeeting.entity.MeetingRoom;
import com.rookie.rookiemeeting.mapper.MeetingRoomMapper;
import com.rookie.rookiemeeting.service.IMeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Autowired
    MeetingRoomMapper meetingRoomMapper;

    //查询可用的会议室
    @Override
    public List<MeetingRoom> queryAll() {
        return meetingRoomMapper.queryAll();
    }

    @Override
    public IPage<MeetingRoom> mrPage(Integer pageNum, Integer pageSize, String roomname, String roomnum) {
        Page<MeetingRoom> meetingRoomPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MeetingRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(MeetingRoom::getRoomname, roomname);
        queryWrapper.like(MeetingRoom::getRoomnum, roomnum);
        return meetingRoomMapper.selectPage(meetingRoomPage, queryWrapper);
    }


}
