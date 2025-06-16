package org.haoai.medixhub.ctkb.repository;

import org.haoai.medixhub.ctkb.entity.EsCoronavirusCriteria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
public interface EsCoronavirusCriteriaMapper extends ElasticsearchRepository<EsCoronavirusCriteria, Integer> {

}
