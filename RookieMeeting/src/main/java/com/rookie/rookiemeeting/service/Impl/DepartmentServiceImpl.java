package com.rookie.rookiemeeting.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.rookiemeeting.entity.Department;
import com.rookie.rookiemeeting.mapper.DepartmentMapper;
import com.rookie.rookiemeeting.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
