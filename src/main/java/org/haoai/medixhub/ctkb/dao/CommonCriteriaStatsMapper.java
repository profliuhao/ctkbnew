package org.haoai.medixhub.ctkb.dao;

import org.haoai.medixhub.ctkb.cache.RedisCache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.haoai.medixhub.ctkb.entity.CommonCriteriaStats;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-06-20
 */
@CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
public interface CommonCriteriaStatsMapper extends BaseMapper<CommonCriteriaStats> {

}
