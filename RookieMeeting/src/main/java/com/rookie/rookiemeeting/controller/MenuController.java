package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.Menu;
import com.rookie.rookiemeeting.service.IMenuService;
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
@RequestMapping("/menu")
@Api(tags = "菜单模块")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    //新增或修改
    @ApiOperation("新增或修改接口")
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.succ(null);
    }

    //根据ID删除
    @ApiOperation("根据ID删除接口")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.succ(null);
    }

    @ApiOperation("根据ID批量删除接口")
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        menuService.removeByIds(ids);
        return Result.succ(null);
    }

    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.succ(menuService.list().stream().map(Menu::getId));
    }

    //查询全部
    @GetMapping
    @ApiOperation("查询全部接口")
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.succ(menuService.findMenus(name));
    }

    //根据ID查询
    @ApiOperation("根据ID查询接口")
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.succ(menuService.getById(id));
    }

    @ApiOperation("分页查询接口")
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.succ(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

