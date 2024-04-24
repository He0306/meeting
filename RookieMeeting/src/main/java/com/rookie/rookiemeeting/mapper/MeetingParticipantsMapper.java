package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.entity.MeetingParticipants;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface MeetingParticipantsMapper extends BaseMapper<MeetingParticipants> {

    //根据会议ID查询参会员工
    List<Integer> getAllBymeetingid(@Param("meetingid") Integer meetingid);

    //添加会议参加者
    void addParticipants(@Param("meetingid") Integer meetingid, @Param("employeeid") List<Long> employeeid);

    //根据会议ID，员工ID删除该员工的会议
    void delEmployee(@Param("meetingid") Integer meetingid, @Param("employeeid") Long employeeid);

    //根据会议ID，员工ID补签
    void signature(MeetingParticipants meetingParticipants);

    //签到
    void singIn(@Param("employeeid") Long employeeid, @Param("meetingid") Integer meetingie, @Param("location") String location);

    int queryNum(@Param("meetingid") Integer meetingid);

    int signed(@Param("meetingid") Integer meetingid);
}
