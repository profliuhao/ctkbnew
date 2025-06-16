package org.haoai.medixhub.ctkb.repository;

import org.haoai.medixhub.ctkb.entity.EsCondition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
public interface EsConditionMapper extends ElasticsearchRepository<EsCondition, Integer> {

}
