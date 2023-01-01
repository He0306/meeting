package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meeting implements Serializable {

    //会议ID
    @ApiModelProperty("会议ID")
    @TableId(value = "meetingid", type = IdType.AUTO)
    private Integer meetingid;

    //会议名称
    @ApiModelProperty("会议名称")
    private String meetingname;

    //房间号
    @ApiModelProperty("房间号")
    private Integer roomid;

    //预定会议人ID
    @ApiModelProperty("预定会议人ID")
    private Long reservationistid;

    //参会人数
    @ApiModelProperty("参会人数")
    private Integer numberofparticipants;

    //会议开始签到时间
    @ApiModelProperty("会议开始签到时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinstarttime;

    //会议结束签到时间
    @ApiModelProperty("会议结束签到时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinendtime;

    //会议开始时间
    @ApiModelProperty("会议开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    //会议结束时间
    @ApiModelProperty("会议结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    //会议预约时间
    @ApiModelProperty("会议预约时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservationtime;

    //会议取消时间
    @ApiModelProperty("会议取消时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date canceledtime;

    //会议说明
    @ApiModelProperty("会议说明")
    private String description;

    //状态 0代表有的会议，1代表取消的会议
    @ApiModelProperty("会议状态")
    private Integer status;

    //取消会议原因
    @ApiModelProperty("会议取消原因")
    private String canceledreason;

}
