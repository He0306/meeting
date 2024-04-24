package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.dto.MeetingDto;
import com.rookie.rookiemeeting.entity.Meeting;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;


@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface MeetingMapper extends BaseMapper<Meeting> {

    //分页查询全部
    Page<MeetingDto> findMeeting(Page<Meeting> page, @Param("mdto") MeetingDto meetingDto);

    //根据登录的员工ID查询自己预定的会议
    Page<MeetingDto> getmeetingofmybookCanCancle(Page<MeetingDto> page, @Param("employeeid") Long employeeid, @Param("meetingname") String meetingname);

    //根据会议ID，取消会议
    void cancelmeeting(@Param("meetingid") Integer meetingid, @Param("canceledreason") String canceledreason);

    //查询取消会议
    Page<MeetingDto> getCancelMeeting(Page<MeetingDto> page, @Param("employeeid") Integer emplyeeid, @Param("meetingname") String meetingname);

    //根据employeeid获取会议信息
    List<Meeting> getMeetingById(@Param("employeeid") Long employeeid);

    //根据已登录的员工ID查询自己的会议
    Page<MeetingDto> getMyMeeting(Page<MeetingDto> page, @Param("employeeid") Long employeeid, @Param("meetingname") String meetingname);

    //查询最近14天内的会议
    Page<MeetingDto> getSevenDayMeeting(Page<MeetingDto> page, @Param("employeeid") Long employeeid, @Param("meetingname") String meetingname);

    //添加会议
    Integer addMeeting(Meeting meeting);

    //根据ID查询会议详情
    List<MeetingDto> meetingById(@Param("meetingid") Integer meetingid);

    //根据会议ID删除会议和参会人员
    void delById(@Param("meetingid") Integer meetingid);

    List<Date> getCalendar(@Param("employeeid") Long employeeid);
}
