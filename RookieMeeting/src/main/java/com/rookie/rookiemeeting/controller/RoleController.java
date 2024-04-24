package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.Role;
import com.rookie.rookiemeeting.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 何超
 * @since 2022-08-22
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色模块")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    //新增或修改
    @ApiOperation("新增或修改接口")
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.succ(null);
    }

    //根据ID删除
    @ApiOperation("根据ID删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.succ(null);
    }

    @ApiOperation("根据ID批量删除")
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.succ(null);
    }

    //查询全部
    @ApiOperation("查询全部接口")
    @GetMapping
    public Result findAll() {
        return Result.succ(roleService.list());
    }

    //根据ID查询
    @ApiOperation("根据ID查询接口")
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.succ(roleService.getById(id));
    }

    @ApiOperation("分页查询全部数据接口")
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.like("name", name);
        return Result.succ(roleService.page(new Page<>(pageNum, pageSize), roleQueryWrapper));
    }
}

