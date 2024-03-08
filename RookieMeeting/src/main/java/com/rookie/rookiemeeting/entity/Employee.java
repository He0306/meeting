package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@TableName("employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    //员工ID
    @ApiModelProperty("员工ID")
    @TableId(value = "employeeid", type = IdType.AUTO)
    private Long employeeid;

    //员工姓名
    @ApiModelProperty("员工姓名")
    private String employeename;

    //用户名（登录的账号）
    @ApiModelProperty("用户名（登录的账号）")
    @NotBlank(message = "账号不能为空")
    private String username;

    //员工手机号
    @ApiModelProperty("手机号")
    private String phone;

    //账号状态（1表示账号正常，2表示账号已被冻结）
    @ApiModelProperty("账号状态")
    private Integer status;

    //员工邮箱
    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    //员工所属部门
    @ApiModelProperty("员工所属部门")
    private Integer departmentid;

    @TableField(exist = false)
    private String departmentname;

    //登录密码
    @ApiModelProperty("登录密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    //角色（1表示普通用户，2表示管理员）
    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("人脸照片")
    private String faceimg;

    @ApiModelProperty("逻辑删除 0是未删除 1是已删除")
    @TableLogic
    private Integer isDelete;

}
