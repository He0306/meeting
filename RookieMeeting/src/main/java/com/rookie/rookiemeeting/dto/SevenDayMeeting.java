package com.rookie.rookiemeeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SevenDayMeeting implements Serializable {

    //会议名称
    private String meetingname;

    //会议室名称
    private String roomname;

    //会议开始时间
    private Date starttime;

    //会议结束时间
    private Date endtime;

    //会议室ID
    private Integer roomid;

    //会议ID
    private Integer meetingid;
}
