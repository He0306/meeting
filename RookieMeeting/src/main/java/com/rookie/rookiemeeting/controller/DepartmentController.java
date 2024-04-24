package com.rookie.rookiemeeting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.entity.Department;
import com.rookie.rookiemeeting.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@Api(tags = "部门控制类")
public class DepartmentController {

    @Autowired
    IDepartmentService departmentService;

    /**
     * 获取全部部门、分页查询
     *
     * @return
     */
    @ApiOperation("分页查询所有部门接口")
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String departmentname) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("departmentname", departmentname);
        return Result.succ(departmentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 新增和编辑部门
     *
     * @param department
     * @return
     */
    @ApiOperation("新增和编辑部门接口")
    @PostMapping("/save")
    public Result save(@RequestBody Department department) {
        return Result.succ(departmentService.saveOrUpdate(department));
    }

    /**
     * 根据id删除单个部门
     *
     * @param departmentid
     * @return
     */
    @ApiOperation("根据ID删除单个部门接口")
    @DeleteMapping("/delete/{departmentid}")
    public Result delete(@PathVariable(name = "departmentid") Integer departmentid) {
        return Result.succ(departmentService.removeById(departmentid));
    }

    /**
     * 批量删除多个部门
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除多个部门接口")
    @PostMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        return Result.succ(departmentService.removeBatchByIds(ids));
    }

    /**
     * 查询所有部门
     *
     * @return
     */
    @ApiOperation("查询所有部门接口")
    @GetMapping("/all")
    public Result all() {
        return Result.succ(departmentService.list());
    }
}
