package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Constants;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.Dict;
import com.rookie.rookiemeeting.service.IDictService;
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
 * @since 2022-08-23
 */
@RestController
@RequestMapping("/dict")
@Api(tags = "图标模块")
public class DictController {

    @Autowired
    private IDictService dictService;

    //新增或修改
    @ApiOperation("新增或修改接口")
    @PostMapping("/save")
    public Result save(@RequestBody Dict dict) {
        return Result.succ(dictService.saveOrUpdate(dict));
    }

    //根据ID删除
    @ApiOperation("根据ID删除接口")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {

        return Result.succ(dictService.removeById(id));
    }

    @ApiOperation("根据ID批量删除接口")
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        return Result.succ(dictService.removeByIds(ids));
    }

    //查询全部
    @ApiOperation("根据类型查询图标接口")
    @GetMapping("/getAll")
    public Result findAll() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_ICON);
        List<Dict> list = dictService.list(queryWrapper);
        return Result.succ(list);
    }

    //根据ID查询
    @ApiOperation("根据ID查询接口")
    @GetMapping("/dict/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.succ(dictService.getById(id));
    }

    @ApiOperation("分页查询全部接口")
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.like("name", name);
        return Result.succ(dictService.page(new Page<>(pageNum, pageSize), dictQueryWrapper));
    }
}

