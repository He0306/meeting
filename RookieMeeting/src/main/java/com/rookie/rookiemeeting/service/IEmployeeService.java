package com.rookie.rookiemeeting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.rookiemeeting.dto.EmployeeDto;
import com.rookie.rookiemeeting.dto.ParticipantsDto;
import com.rookie.rookiemeeting.dto.UpdatePassDTO;
import com.rookie.rookiemeeting.entity.Employee;

import java.util.List;

public interface IEmployeeService extends IService<Employee> {

    //注册功能
    int register(Employee employee);

    //查询用户名是否存在
    Employee queryUsername(String username);

    //修改密码
    int updatePass(UpdatePassDTO updatePassDTO);

    //分页查询全部
    Page<Employee> findEmployee(Page<Employee> page, String employeename, String phone, String email, String departmentname);

    //查看未审批
    List<Employee> getByStatus(Integer status);

    //管理员通过审批
    Integer updateStatusAdopt(Long employeeid);

    //管理员不通过审批
    Integer updateStatusFail(Long employeeid);

    //根据ID查询参会人员
    List<ParticipantsDto> getEmpsById(Integer meetingid);

    //查询员工，状态为1的
    List<Employee> getAll();

    //导出方法
    List<EmployeeDto> export();


}
