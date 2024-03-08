package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.dto.EmployeeDto;
import com.rookie.rookiemeeting.dto.ParticipantsDto;
import com.rookie.rookiemeeting.dto.UpdatePassDTO;
import com.rookie.rookiemeeting.entity.Employee;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface EmployeeMapper extends BaseMapper<Employee> {

    //注册
    int register(Employee employee);

    //查询用户名是否存在
    Employee queryUsername(@Param("username") String username);

    //修改密码
    int updatePass(UpdatePassDTO updatePassDTO);

    //查询全部
    Page<Employee> findEmployee(Page<Employee> page, @Param("employeename") String employeename, @Param("phone") String phone, @Param("email") String email, @Param("departmentname") String departmentname);

    //查看未审批
    List<Employee> getByStatus(Integer status);

    //管理员通过审批
    Integer updateStatusAdopt(@Param("employeeid") Long employeeid);

    //管理员不通过审批
    Integer updateStatusFail(Long employeeid);

    //根据ID查询参会人员
    List<ParticipantsDto> getAllEmpsById(@Param("meetingid") Integer meeting);

    //查询员工，状态为1的
    List<Employee> getAll();

    //导出功能方法
    List<EmployeeDto> export();


}
