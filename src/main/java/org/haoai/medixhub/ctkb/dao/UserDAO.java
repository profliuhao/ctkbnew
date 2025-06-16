package org.haoai.medixhub.ctkb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.haoai.medixhub.ctkb.cache.RedisCache;
import org.haoai.medixhub.ctkb.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
public interface UserDAO extends BaseMapper<User> {
}
