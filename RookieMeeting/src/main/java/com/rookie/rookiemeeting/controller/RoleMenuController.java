package com.rookie.rookiemeeting.controller;

import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.service.IRoleMenuService;
import com.rookie.rookiemeeting.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roleMenu")
@Api(tags = "角色与菜单模块")
public class RoleMenuController {

    @Autowired
    IRoleMenuService roleMenuService;

    @Autowired
    IRoleService roleService;

    //绑定角色和菜单的关系
    @ApiOperation("绑定角色和菜单的关系接口")
    @PostMapping("/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.succ(null);
    }

    @GetMapping("/{roleId}")
    @ApiOperation("根据角色ID查询菜单接口")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        Object roleMenu = roleService.getRoleMenu(roleId);
        return Result.succ(roleMenu);
    }
}
