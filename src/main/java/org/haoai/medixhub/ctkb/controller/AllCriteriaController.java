package org.haoai.medixhub.ctkb.controller;


import org.haoai.medixhub.ctkb.service.AllCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-07-21
 */
@RestController
@RequestMapping("/api/all-criteria")
@CrossOrigin
public class AllCriteriaController {

    @Autowired
    AllCriteriaService allCriteriaService;


    @GetMapping("/get-cri-count/{criteria_id}/{include}")
    public int getCriteriaByConditionName(@PathVariable(value = "criteria_id") String criteria_id,
                                                            @PathVariable(value = "include") Integer include){
        if(include == 1){
            return allCriteriaService.getInclusionCountByCriteriaId(criteria_id);
        }
        else{
            return allCriteriaService.getExclusionCountByCriteriaId(criteria_id);
        }
    }

}

