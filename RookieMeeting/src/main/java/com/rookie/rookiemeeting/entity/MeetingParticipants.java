package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@TableName("meetingparticipants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingParticipants implements Serializable {

    @ApiModelProperty("会议ID")
    private Integer meetingid;

    @ApiModelProperty("参加会议的员工ID")
    private Long employeeid;

    @ApiModelProperty("签到状态，0未签到，1已签到")
    private Integer status;

    @ApiModelProperty("签到时间")
    private Date checkintime;

    @ApiModelProperty("签到地点")
    private String location;
}
