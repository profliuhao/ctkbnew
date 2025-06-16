package org.haoai.medixhub.ctkb.dao;

import org.haoai.medixhub.ctkb.cache.RedisCache;
import org.haoai.medixhub.ctkb.entity.CriterionRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-07-26
 */
@CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
public interface CriterionRankMapper extends BaseMapper<CriterionRank> {

}
