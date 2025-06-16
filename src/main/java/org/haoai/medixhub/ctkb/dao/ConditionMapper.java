package org.haoai.medixhub.ctkb.dao;

import org.haoai.medixhub.ctkb.cache.RedisCache;
import org.haoai.medixhub.ctkb.entity.Condition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
@CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
public interface ConditionMapper extends BaseMapper<Condition> {

}
