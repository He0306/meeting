package com.rookie.rookiemeeting.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsDto implements Serializable {

    //会议ID
    private Integer meetingid;

    //员工ID
    private Long employeeid;

    //员工姓名
    @Excel(name = "员工姓名")
    private String employeename;

    //邮箱
    @Excel(name = "邮箱")
    private String email;

    //手机号
    @Excel(name = "手机号")
    private String phone;

    //签到状态
    @Excel(name = "会议签到状态", replace = {"未签到_0", "已签到_1"})
    private Integer status;

    //签到地址
    @Excel(name = "签到地址")
    private String location;

    //会议开始签到时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinstarttime;

    //会议结束签到时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinendtime;

    //所属部门
    @Excel(name = "所属部门")
    private String departmentname;

    //签到时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签到时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkintime;
}
