package com.rookie.rookiemeeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.rookiemeeting.entity.MeetingParticipants;

import java.util.List;

public interface IMeetingParticipantsService extends IService<MeetingParticipants> {

    //添加会议参加者
    void addParticipants(Integer meetingid, List<Long> employeeidps);

    //删除参会员工
    void delEmployee(Integer meetingid, Long employeeid);

    //根据会议ID，员工ID补签
    void signature(MeetingParticipants meetingParticipants);

    //签到
    void singIn(Long employeeid, Integer meetingie, String location);

    int queryNum(Integer meetingid);

    int signed(Integer meetingid);

}
