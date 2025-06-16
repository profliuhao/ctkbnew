package org.haoai.medixhub.ctkb;

import org.haoai.medixhub.ctkb.dao.CommonConditionMapper;
import org.haoai.medixhub.ctkb.service.CommonConditionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCommonConditionService {

    @Autowired
    private CommonConditionService ccs;

    @Autowired
    private CommonConditionMapper ccm;

    @Test
    public void testFindCriteriaByCondition(){
        String conditionName = "Disorder of lung";
        ccs.getCriteriaByConditionName(conditionName, 1);
//        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("concept_name", "HYPERTENSION");
////        conditionService.list(queryWrapper).forEach(condition -> System.out.println("condition = " + condition));
//        conditionService.list(queryWrapper);
//        conditionService.list(queryWrapper);
    }

    @Test
    public void testFindConditionsByCriteria(){
        String criteriaName = "Body weight";
        ccs.getConditionByCriteriaName(criteriaName, 1);

    }

    @Test
    public void testGetColumn(){

        List<String> result = ccs.getAllAvailableCriteriaNames();
        result.forEach(System.out::println);

    }








}
