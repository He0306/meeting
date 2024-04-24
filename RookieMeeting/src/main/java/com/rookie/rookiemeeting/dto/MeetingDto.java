package com.rookie.rookiemeeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDto implements Serializable {

    //会议ID
    private Integer meetingid;

    //员工ID
    private Long employeeid;

    //会议名称
    private String meetingname;

    //会议开始签到时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinstarttime;

    //会议结束签到时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinendtime;

    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    //预约时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservationtime;

    //取消会议时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss-")
    private Date canceledtime;

    //房间号
    private Integer roomid;

    //预订房间的人的ID
    private Integer reservationistid;

    //预订房间的人的姓名
    private String reservationistname;

    //房间名
    private String roomname;

    //房间号
    private Integer roomnum;

    //预计参会人数
    private Integer numberofparticipants;

    //取消会议原因
    private String canceledreason;

    //会议说明
    private String description;

    //签到状态
    private Integer status;

}
