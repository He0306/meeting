package com.rookie.rookiemeeting.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {

    @Excel(name = "员工ID")
    private Long employeeid;

    @Excel(name = "员工姓名")
    private String employeename;

    @Excel(name = "登录账号")
    private String username;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "所属部门")
    private String departmentname;

    @Excel(name = "角色", replace = {"普通用户_1", "管理员_2"})
    private String role;

    @Excel(name = "账号状态", replace = {"未审批_0", "正常_1", "审批未通过_2"})
    private Integer status;
}
