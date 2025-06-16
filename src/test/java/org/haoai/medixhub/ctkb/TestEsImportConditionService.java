package org.haoai.medixhub.ctkb;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.repository.EsCommonConditionMapper;
import org.haoai.medixhub.ctkb.repository.EsConditionMapper;
import org.haoai.medixhub.ctkb.entity.*;
import org.haoai.medixhub.ctkb.entity.CommonCondition;
import org.haoai.medixhub.ctkb.entity.Condition;
import org.haoai.medixhub.ctkb.entity.EsCommonCondition;
import org.haoai.medixhub.ctkb.entity.EsCondition;
import org.haoai.medixhub.ctkb.service.CommonConditionService;
import org.haoai.medixhub.ctkb.service.ConditionService;
import org.haoai.medixhub.ctkb.service.EsConditionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestEsImportConditionService {

    @Autowired
    private EsConditionService esConditionService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private EsConditionMapper esConditionMapper;

    @Autowired
    private CommonConditionService commonConditionService;

    @Autowired
    private EsCommonConditionMapper esCommonConditionMapper;



    @Test
    public void testEsConditionImport(){
        esConditionService.importConditionToES();
    }


    @Test
    public void testEsConditionImportbyPage(){

        IPage<Condition> pageInit = new Page<>(1, 1000);
        IPage<Condition> condIPageInit = conditionService.page(pageInit, null);
        int total = (int) condIPageInit.getTotal();

        for (int i = 0; i < total; i++) {

            IPage<Condition> page = new Page<>(i, 1000);
            IPage<Condition> condIPage = conditionService.page(page, null);
//            long total = condIPage.getTotal();
//            System.out.println("total = " + total);
//            long pages = condIPage.getPages();
//            System.out.println("pages = " + pages);
//            long current = condIPage.getCurrent();
//            System.out.println("current = " + current);
//        condIPage.getRecords().forEach(condition -> System.out.println("condition = " + condition));

            List<Condition> conditions = condIPage.getRecords();

            List<EsCondition> esConditions = JSON.parseArray(JSON.toJSONString(conditions),EsCondition.class);

            esConditionMapper.saveAll(esConditions);

        }
    }

    @Test
    public void testEsCoronavirusCriteriaImport(){

        QueryWrapper<CommonCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("condition_concept_name", "flu").or().like("condition_concept_name", "influenza");

        List<CommonCondition> items = commonConditionService.list(queryWrapper);

        List<EsCommonCondition> esItems = JSON.parseArray(JSON.toJSONString(items), EsCommonCondition.class);

        esCommonConditionMapper.saveAll(esItems);
    }

    @Test
    public List<EsCommonCondition> testEsFindNameAndContent(){

        return esCommonConditionMapper.findByConditionConceptNameAndCriteriaConceptName("Hypertension", "BMI");
    }
}






