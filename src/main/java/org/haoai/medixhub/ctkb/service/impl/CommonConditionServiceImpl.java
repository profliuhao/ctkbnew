package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.haoai.medixhub.ctkb.entity.CommonCondition;
import org.haoai.medixhub.ctkb.dao.CommonConditionMapper;
import org.haoai.medixhub.ctkb.service.CommonConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
@Service
public class CommonConditionServiceImpl extends ServiceImpl<CommonConditionMapper, CommonCondition> implements CommonConditionService {

    @Autowired
    private CommonConditionMapper commonConditionMapper;

    @Override
    public List<CommonCondition> getCriteriaByConditionName(String conditionName, Integer include) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("condition_concept_name", conditionName).eq("include", include).orderByDesc("concept_count");
        List<CommonCondition> criteria = commonConditionMapper.selectList(queryWrapper);
        return criteria;
    }

    @Override
    public List<CommonCondition> getCriteriaByConditionID(String conditionID, Integer include) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("condition_concept_id", conditionID).eq("include", include).orderByDesc("concept_count");
        List<CommonCondition> criteria = commonConditionMapper.selectList(queryWrapper);
        return criteria;
    }

    @Override
    public List<CommonCondition> getCriteriaByConditionIDandDomain(String conditionID, Integer include, String domain) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("condition_concept_id", conditionID).eq("include", include).eq("criteria_domain", domain).orderByDesc("concept_count");
        List<CommonCondition> criteria = commonConditionMapper.selectList(queryWrapper);
        return criteria;
    }

    @Override
    public List<CommonCondition> getConditionByCriteriaName(String criteriaName, Integer include) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("criteria_concept_name", criteriaName).eq("include", include).orderByDesc("concept_count");
        List<CommonCondition> conditions = commonConditionMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public List<CommonCondition> getConditionByCriteriaID(String criteriaID, Integer include) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("criteria_concept_id", criteriaID).eq("include", include).orderByDesc("concept_count");
        List<CommonCondition> conditions = commonConditionMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public List<String> getAllAvailableCriteriaNames() {

        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct criteria_concept_name", "criteria_concept_id")
                   .isNotNull("criteria_concept_name")
                   .ne("criteria_concept_name", "");
        List<CommonCondition> commonConditions = commonConditionMapper.selectList(queryWrapper);
        List<String> results = new ArrayList<>();
        commonConditions.forEach(t-> {
            String name = t.getCriteriaConceptName();
            if (name != null && !name.trim().isEmpty()) {
                results.add(name);
            }
        });
        return results;
    }

    @Override
    public String getCriterionIDBYName(String criteriaName) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct criteria_concept_id").eq("criteria_concept_name", criteriaName);
        List<CommonCondition> conditions = commonConditionMapper.selectList(queryWrapper);
        return conditions.get(0).getCriteriaConceptId().toString();

    }

    @Override
    public List<String> getAllAvailableConditionNames() {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct condition_concept_name", "condition_concept_id")
                   .isNotNull("condition_concept_name")
                   .ne("condition_concept_name", "");
        List<CommonCondition> commonConditions = commonConditionMapper.selectList(queryWrapper);
        List<String> results = new ArrayList<>();
        commonConditions.forEach(t-> {
            String name = t.getConditionConceptName();
            if (name != null && !name.trim().isEmpty()) {
                results.add(name);
            }
        });
        return results;
    }

    @Override
    public String getConditionIDBYName(String conditionName) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct condition_concept_id").eq("condition_concept_name", conditionName);
        List<CommonCondition> conditions = commonConditionMapper.selectList(queryWrapper);
        return conditions.get(0).getConditionConceptId().toString();
    }

    @Override
    public List<CommonCondition> getByConditionIDAndCriteriaID(String conditionID, String criteriaID, Integer include) {
        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("condition_concept_id", conditionID).eq("criteria_concept_id",criteriaID).eq("include", include);
        List<CommonCondition> conditions = commonConditionMapper.selectList(queryWrapper);
        return conditions;
    }


}
