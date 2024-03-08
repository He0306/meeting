package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {

    //部门编号
    @ApiModelProperty("部门编号")
    @TableId(value = "departmentid", type = IdType.AUTO)
    private Integer departmentid;

    //部门名称
    @ApiModelProperty("部门名称")
    private String departmentname;
}
