package com.rookie.rookiemeeting.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.common.lang.Constants;
import com.rookie.rookiemeeting.common.lang.Result;
import com.rookie.rookiemeeting.dto.EmployeeDto;
import com.rookie.rookiemeeting.dto.LoginDto;
import com.rookie.rookiemeeting.dto.UpdatePassDTO;
import com.rookie.rookiemeeting.entity.Employee;
import com.rookie.rookiemeeting.service.IDepartmentService;
import com.rookie.rookiemeeting.service.IEmployeeService;
import com.rookie.rookiemeeting.service.IMenuService;
import com.rookie.rookiemeeting.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/employee")
@Api(tags = "员工控制类")
public class EmployeeController {

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IMenuService menuService;


    /**
     * 登录接口
     *
     * @param loginDto
     * @param response
     * @return
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto) {

        Employee employee = employeeService.getOne(new QueryWrapper<Employee>().eq("username", loginDto.getUsername()));

        if (employee == null) {
            return Result.fail("账号不存在，请先注册");
        } else if (!employee.getPassword().equals(DigestUtils.md5Hex(loginDto.getPassword() + Constants.SALT))) {
            return Result.fail("账号或密码不正确");
        } else if (employee.getStatus().equals(0)) {
            return Result.fail("账号未审批，请联系管理员");
        } else if (employee.getStatus().equals(2)) {
            return Result.fail("账号审批未通过，请联系管理员");
        }
        BeanUtil.copyProperties(employee, loginDto, true);
        String jwt = TokenUtils.genToken(employee.getEmployeeid().toString(), employee.getPassword());
        redisTemplate.opsForValue().set("JWT_" + jwt, JSON.toJSON(employee), 2, TimeUnit.HOURS);
        loginDto.setToken(jwt);
        loginDto.setMenus(menuService.getRoleMenu(employee.getRole()));

        return Result.succ(loginDto);
    }


    /**
     * 注册请求
     *
     * @param employee
     * @return
     */
    @ApiOperation("注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        Employee username = employeeService.queryUsername(employee.getUsername());
        if (username != null) {
            return Result.fail("用户已存在，换个账号名试试");
        } else {
            return Result.succ(employeeService.register(employee));
        }
    }

    /**
     * 修改密码
     *
     * @param updatePassDTO
     * @return
     */
    @ApiOperation("修改密码接口")
    @PostMapping("/updatePass")
    public Result updatePass(@RequestBody UpdatePassDTO updatePassDTO) {
        int pass = employeeService.updatePass(updatePassDTO);
        if (pass < 1) {
            return Result.fail("原密码错误");
        } else {
            return Result.succ(null);
        }
    }


    /**
     * 根据账号查询
     *
     * @param username
     * @return
     */
    @ApiOperation("根据账号查询接口")
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable(name = "username") String username) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.succ(employeeService.getOne(queryWrapper));
    }

    /**
     * 个人信息修改和保存
     *
     * @param employee
     * @return
     */
    @ApiOperation("个人信息修改和保存接口")
    @PostMapping("/personalSave")
    public Result personalSave(@RequestBody Employee employee) {
        return Result.succ(employeeService.saveOrUpdate(employee));
    }

    /**
     * 分页查询全部数据
     *
     * @param pageNum      页码
     * @param pageSize     多少条数据
     * @param employeename
     * @param phone
     * @param email
     * @return
     */
    @ApiOperation("分页查询全部员工信息接口")
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String employeename,
                           @RequestParam(defaultValue = "") String phone,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String departmentname) {
        return Result.succ(employeeService.findEmployee(new Page<>(pageNum, pageSize), employeename, phone, email, departmentname));
    }

    /**
     * 根据id删除单个用户
     *
     * @param employeeid
     * @return
     */
    @ApiOperation("根据ID删除单个用户接口")
    @DeleteMapping("/delete/{employeeid}")
    public Result delete(@PathVariable(name = "employeeid") Integer employeeid) {
        return Result.succ(employeeService.removeById(employeeid));
    }

    /**
     * 批量删除多个用户
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除多个用户接口")
    @PostMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        return Result.succ(employeeService.removeBatchByIds(ids));
    }

    /**
     * 编辑员工
     *
     * @param employee
     * @return
     */
    @ApiOperation("编辑员工接口")
    @PostMapping("/edit")
    public Result edit(@RequestBody Employee employee) {
        return Result.succ(employeeService.saveOrUpdate(employee));
    }

    /**
     * 增加员工，默认密码为111111
     *
     * @param employee
     * @return
     */
    @ApiOperation("增加员工接口")
    @PostMapping("/save")
    public Result save(@RequestBody Employee employee) {
        employee.setPassword(DigestUtils.md5Hex(111111 + Constants.SALT));
        return Result.succ(employeeService.save(employee));
    }

    /**
     * 查询所有员工
     *
     * @return
     */
    @ApiOperation("查询所有员工接口")
    @PostMapping("/list")
    public Result list() {
        return Result.succ(employeeService.getAll());
    }

    /**
     * 导出功能
     *
     * @param response
     * @throws IOException
     */
    @ApiOperation("导出功能接口")
    @GetMapping("/export")
    public void export(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<EmployeeDto> list = employeeService.export();
        ExportParams params = new ExportParams("员工信息", "员工信息", ExcelType.XSSF);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, EmployeeDto.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "员工信息表");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}
