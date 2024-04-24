package com.rookie.rookiemeeting.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.rookiemeeting.dto.MeetingDto;
import com.rookie.rookiemeeting.entity.Meeting;

import java.sql.Date;
import java.util.List;


public interface IMeetingService extends IService<Meeting> {

    //分页查询全部
    Page<MeetingDto> findMeeting(Page<Meeting> page, MeetingDto meetingDto);

    //根据登录的员工ID查询自己预定的会议
    Page<MeetingDto> getmeetingofmybookCanCancle(Page<MeetingDto> page, Long employeeid, String meetingname);

    //根据会议ID，取消会议
    void cancelmeeting(Integer meetingid, String canceledreason);

    //查询最近14天内的会议
    Page<MeetingDto> getSevenDayMeeting(Page<MeetingDto> page, Long employeeid, String meetingname);

    //查询取消的会议
    Page<MeetingDto> getCancelMeeting(Page<MeetingDto> page, Integer emplyeeid, String meetingname);

    //根据已登录的员工ID查询我的会议
    Page<MeetingDto> getMyMeeting(Page<MeetingDto> page, Long employeeid, String meetingname);

    //预定会议
    Integer addMeeting(Meeting meeting, Long employeeid);

    //根据ID查询会议详情
    List<MeetingDto> meetingById(Integer meetingid);

    //根据会议ID删除会议和参会人员
    void delById(Integer meetingid);

    List<Date> getCalendar(Long employeeid);
}
