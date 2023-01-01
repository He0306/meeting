package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.entity.RoleMenu;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    int deleteByRoleId(@Param("roleId") Integer roleId);

    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
