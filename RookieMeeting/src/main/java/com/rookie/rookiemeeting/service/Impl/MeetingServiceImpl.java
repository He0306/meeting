package com.rookie.rookiemeeting.service.Impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.rookiemeeting.dto.MeetingDto;
import com.rookie.rookiemeeting.entity.Meeting;
import com.rookie.rookiemeeting.mapper.MeetingMapper;
import com.rookie.rookiemeeting.service.IMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

    @Autowired
    MeetingMapper meetingMapper;

    @Autowired
    RedisTemplate redisTemplate;


    //分页查询全部
    @Override
    public Page<MeetingDto> findMeeting(Page<Meeting> page, MeetingDto meetingDto) {
        return meetingMapper.findMeeting(page, meetingDto);
    }


    //根据登录的员工ID查询自己预定的会议
    @Override
    public Page<MeetingDto> getmeetingofmybookCanCancle(Page<MeetingDto> page, Long employeeid, String meetingname) {
        return meetingMapper.getmeetingofmybookCanCancle(page, employeeid, meetingname);
    }

    //根据会议ID，取消会议
    @Override
    public void cancelmeeting(Integer meetingid, String canceledreason) {
        meetingMapper.cancelmeeting(meetingid, canceledreason);
    }

    //查询最近14天内的会议
    @Override
    public Page<MeetingDto> getSevenDayMeeting(Page<MeetingDto> page, Long employeeid, String meetingname) {
        return meetingMapper.getSevenDayMeeting(page, employeeid, meetingname);
    }


    //查询取消的会议
    @Override
    public Page<MeetingDto> getCancelMeeting(Page<MeetingDto> page, Integer emplyeeid, String meetingname) {
        return meetingMapper.getCancelMeeting(page, emplyeeid, meetingname);
    }

    //根据已登录的员工ID查询我的会议
    @Override
    public Page<MeetingDto> getMyMeeting(Page<MeetingDto> page, Long employeeid, String meetingname) {
        return meetingMapper.getMyMeeting(page, employeeid, meetingname);
    }

    //预定会议
    @Override
    public Integer addMeeting(Meeting meeting, Long employeeid) {
        meeting.setReservationtime(new Date());
        meeting.setReservationistid(employeeid);
        meeting.setStatus(0);
        return meetingMapper.addMeeting(meeting);
    }

    //根据ID查询会议详情
    @Override
    public List<MeetingDto> meetingById(Integer meetingid) {
        return meetingMapper.meetingById(meetingid);
    }

    //根据会议ID删除会议和参会人员
    @Override
    public void delById(Integer meetingid) {
        meetingMapper.delById(meetingid);
    }

    @Override
    public List<java.sql.Date> getCalendar(Long employeeid) {
        return meetingMapper.getCalendar(employeeid);
    }


}
