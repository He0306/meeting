package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.entity.Department;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface DepartmentMapper extends BaseMapper<Department> {


}
