package org.haoai.medixhub.ctkb.controller;


import org.haoai.medixhub.ctkb.service.CriterionRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-07-26
 */
@RestController
@RequestMapping("/api/criterion-rank")
@CrossOrigin
public class CriterionRankController {

    @Autowired
    private CriterionRankService criterionRankService;


    @GetMapping("/{criteria_id}")
    public int getCriteriaByConditionName(@PathVariable(value = "criteria_id") String criteria_id){
        return criterionRankService.getRankingByCriteriaId(criteria_id);
    }
    

}

