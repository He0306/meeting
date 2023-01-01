package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("meetingroom")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoom implements Serializable {

    //会议室ID
    @ApiModelProperty("会议室ID")
    @TableId(value = "roomid", type = IdType.AUTO)
    private Integer roomid;

    //会议室房间号
    @ApiModelProperty("会议室房间号")
    private Integer roomnum;

    //会议室名称
    @ApiModelProperty("会议室名称")
    private String roomname;

    //最多容纳人数
    @ApiModelProperty("最多容纳人数")
    private Integer capacity;

    //当前状态（0启用，1已启用）
    @ApiModelProperty("会议室状态")
    private Integer status;

    //备注
    @ApiModelProperty("备注")
    private String description;
}
