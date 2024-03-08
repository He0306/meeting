package com.rookie.rookiemeeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("sys_role_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu implements Serializable {

    @ApiModelProperty("角色ID")
    private Integer roleId;
    @ApiModelProperty("菜单ID")
    private Integer menuId;
}