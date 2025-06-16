package org.haoai.medixhub.ctkb.repository;

import org.haoai.medixhub.ctkb.entity.EsCommonCondition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
public interface EsCommonConditionMapper extends ElasticsearchRepository<EsCommonCondition, Integer> {

    List<EsCommonCondition> findByConditionConceptNameAndCriteriaConceptName(String condKeyword, String criKeyword);

}
