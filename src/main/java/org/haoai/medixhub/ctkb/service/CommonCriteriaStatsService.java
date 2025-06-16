package org.haoai.medixhub.ctkb.service;

import org.haoai.medixhub.ctkb.entity.CommonCriteriaStats;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoliu
 * @since 2020-06-20
 */
public interface CommonCriteriaStatsService extends IService<CommonCriteriaStats> {

    Map<String, Integer> getCriteriaCountByPhase(String criteria_concept_id, Integer include);

    Map<String, Integer> getCriteriaPhaseCountByCondition(String condition_concept_id, String criteria_concept_id, Integer include);

}
