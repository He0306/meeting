package com.rookie.rookiemeeting.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.rookiemeeting.entity.MeetingParticipants;
import com.rookie.rookiemeeting.mapper.MeetingParticipantsMapper;
import com.rookie.rookiemeeting.service.IMeetingParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MeetingParticipantsServiceImpl extends ServiceImpl<MeetingParticipantsMapper, MeetingParticipants> implements IMeetingParticipantsService {

    @Autowired
    MeetingParticipantsMapper meetingParticipantsMapper;

    //添加会议参加者
    @Override
    public void addParticipants(Integer meetingid, List<Long> employeeid) {
        meetingParticipantsMapper.addParticipants(meetingid, employeeid);
    }

    //根据会议ID，员工ID删除该员工的会议
    @Override
    public void delEmployee(Integer meetingid, Long employeeid) {
        meetingParticipantsMapper.delEmployee(meetingid, employeeid);
    }

    //根据会议ID，员工ID补签
    @Override
    public void signature(MeetingParticipants meetingParticipants) {
        meetingParticipantsMapper.signature(meetingParticipants);
    }

    //签到
    @Override
    public void singIn(Long employeeid, Integer meetingie, String location) {
        meetingParticipantsMapper.singIn(employeeid, meetingie, location);
    }

    @Override
    public int queryNum(Integer meetingid) {
        return meetingParticipantsMapper.queryNum(meetingid);
    }

    @Override
    public int signed(Integer meetingid) {
        return meetingParticipantsMapper.signed(meetingid);
    }


}
