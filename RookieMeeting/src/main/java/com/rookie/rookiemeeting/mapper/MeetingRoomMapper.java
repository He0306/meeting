package com.rookie.rookiemeeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.rookiemeeting.config.MybatisRedisCache;
import com.rookie.rookiemeeting.dto.RoomDto;
import com.rookie.rookiemeeting.entity.MeetingRoom;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {

    //得到所有RoomDto
    List<RoomDto> getAll();

    //查询可用的会议室
    List<MeetingRoom> queryAll();
}
