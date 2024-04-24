package com.rookie.rookiemeeting.controller;

import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approve")
@Api(tags = "管理员审批控制类")
public class ApproveAccountController {

    //待批准状态码为0
    public static final Integer PENDING_APPROVE = 0;
    @Autowired
    IEmployeeService employeeService;

    /**
     * 查询所有未审批的用户
     *
     * @return
     */
    @ApiOperation("查询所有未审批的用户接口")
    @PostMapping("getStatus")
    public Result getStatus() {
        return Result.succ(employeeService.getByStatus(PENDING_APPROVE));
    }

    /**
     * 根据ID通过注册审批
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("根据ID通过注册审批接口")
    @PostMapping("/updateStatusAdopt/{employeeid}")
    public Result updateStatusAdopt(@PathVariable(name = "employeeid") Long employeeid) {
        return Result.succ(employeeService.updateStatusAdopt(employeeid));
    }

    /**
     * 根据ID不通过注册审批
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("根据ID不通过注册审批接口")
    @PostMapping("/updateStatusFail/{employeeid}")
    public Result updateStatusFail(@PathVariable(name = "employeeid") Long employeeid) {
        return Result.succ(employeeService.updateStatusFail(employeeid));
    }
}
