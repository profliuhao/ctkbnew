package org.haoai.medixhub.ctkb.controller;


import org.haoai.medixhub.ctkb.service.CommonCriteriaStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-06-20
 */
@RestController
@RequestMapping("/api/common-criteria-stats")
@CrossOrigin
public class CommonCriteriaStatsController {

    @Autowired
    CommonCriteriaStatsService commonCriteriaStatsService;

    @GetMapping("/criteria-phase/{criteria_id}/{include}")
    public Map<String, Integer> getCriteriaByConditionName(@PathVariable(value = "criteria_id") String criteria_id,
                                                           @PathVariable(value = "include") Integer include){
        return commonCriteriaStatsService.getCriteriaCountByPhase(criteria_id, include);
    }

    @GetMapping("/criteria-phase/{condition_id}/{criteria_id}/{include}")
    public Map<String, Integer> getCriteriaPhaseCountByConditionId(@PathVariable(value = "condition_id") String condition_id,
                                                                   @PathVariable(value = "criteria_id") String criteria_id,
                                                           @PathVariable(value = "include") Integer include){
        return commonCriteriaStatsService.getCriteriaPhaseCountByCondition(condition_id, criteria_id, include);
    }



}

