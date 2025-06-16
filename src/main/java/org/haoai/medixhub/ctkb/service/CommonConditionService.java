package org.haoai.medixhub.ctkb.service;

import org.haoai.medixhub.ctkb.entity.CommonCondition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
public interface CommonConditionService extends IService<CommonCondition> {

    List<CommonCondition> getCriteriaByConditionName(String conditionName, Integer include);

    List<CommonCondition> getCriteriaByConditionID(String conditionID, Integer include);

    List<CommonCondition> getCriteriaByConditionIDandDomain(String conditionID, Integer include, String domain);

    List<CommonCondition> getConditionByCriteriaName(String criteriaName, Integer include);

    List<CommonCondition> getConditionByCriteriaID(String criteriaID, Integer include);

    List<String> getAllAvailableCriteriaNames();

    String getCriterionIDBYName(String criteriaName);

    List<String> getAllAvailableConditionNames();

    String getConditionIDBYName(String conditionName);

    List<CommonCondition> getByConditionIDAndCriteriaID(String conditionID, String criteriaID, Integer include);
}
