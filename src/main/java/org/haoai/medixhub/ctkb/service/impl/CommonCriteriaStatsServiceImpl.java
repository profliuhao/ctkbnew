package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.haoai.medixhub.ctkb.entity.CommonCriteriaStats;
import org.haoai.medixhub.ctkb.dao.CommonCriteriaStatsMapper;
import org.haoai.medixhub.ctkb.service.CommonCriteriaStatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-06-20
 */
@Service
public class CommonCriteriaStatsServiceImpl extends ServiceImpl<CommonCriteriaStatsMapper, CommonCriteriaStats> implements CommonCriteriaStatsService {

    @Autowired
    private CommonCriteriaStatsMapper commonCriteriaStatsMapper;


    public List<CommonCriteriaStats> getPhaseByCriteriaID(String criteria_concept_id, Integer include) {

        QueryWrapper<CommonCriteriaStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("criteria_concept_id", criteria_concept_id).eq("include", include);
        List<CommonCriteriaStats> commonCriteriaStats = commonCriteriaStatsMapper.selectList(queryWrapper);
        return commonCriteriaStats;
    }

    @Override
    public Map<String, Integer> getCriteriaCountByPhase(String criteria_concept_id, Integer include) {

        List<CommonCriteriaStats> commonCriteriaStats = getPhaseByCriteriaID(criteria_concept_id, include);
        int p1_sum = 0;
        int p2_sum = 0;
        int p3_sum = 0;
        int p4_sum = 0;

        for (CommonCriteriaStats commonCriteriaStat : commonCriteriaStats) {
            p1_sum += Optional.ofNullable(commonCriteriaStat.getP1Count()).orElse(0);
            p2_sum += Optional.ofNullable(commonCriteriaStat.getP2Count()).orElse(0);
            p3_sum += Optional.ofNullable(commonCriteriaStat.getP3Count()).orElse(0);
            p4_sum += Optional.ofNullable(commonCriteriaStat.getP4Count()).orElse(0);
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("Phase 1", p1_sum);
        map.put("Phase 2", p2_sum);
        map.put("Phase 3", p3_sum);
        map.put("Phase 4", p4_sum);

        return map;
    }

    @Override
    public Map<String, Integer> getCriteriaPhaseCountByCondition(String condition_concept_id, String criteria_concept_id, Integer include) {
        QueryWrapper<CommonCriteriaStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("condition_concept_id", condition_concept_id).eq("criteria_concept_id", criteria_concept_id).eq("include", include);
        List<CommonCriteriaStats> commonCriteriaStats = commonCriteriaStatsMapper.selectList(queryWrapper);
        int p1 = Optional.ofNullable(commonCriteriaStats.get(0).getP1Count()).orElse(0);
        int p2 = Optional.ofNullable(commonCriteriaStats.get(0).getP2Count()).orElse(0);
        int p3 = Optional.ofNullable(commonCriteriaStats.get(0).getP3Count()).orElse(0);
        int p4 = Optional.ofNullable(commonCriteriaStats.get(0).getP4Count()).orElse(0);
        Map<String, Integer> map = new HashMap<>();
        map.put("Phase 1", p1);
        map.put("Phase 2", p2);
        map.put("Phase 3", p3);
        map.put("Phase 4", p4);
        return map;
    }
}
