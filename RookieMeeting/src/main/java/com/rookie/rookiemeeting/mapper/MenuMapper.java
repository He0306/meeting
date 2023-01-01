package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.entity.Menu;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 何超
 * @since 2022-08-22
 */
@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface MenuMapper extends BaseMapper<Menu> {

}
