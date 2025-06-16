package org.haoai.medixhub.ctkb;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.ConditionMapper;
import org.haoai.medixhub.ctkb.entity.Condition;
import org.haoai.medixhub.ctkb.service.ConditionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestConditionService {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private ConditionMapper conditionMapper;

    @Test
    public void testFindAll(){

        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", "HYPERTENSION");
//        conditionService.list(queryWrapper).forEach(condition -> System.out.println("condition = " + condition));
        conditionService.list(queryWrapper);
        conditionService.list(queryWrapper);
    }



    @Test
    public void testFindbyPage(){
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", "HYPERTENSION");

        IPage<Condition> page = new Page<>(2, 10);
        IPage<Condition> condIPage = conditionService.page(page, queryWrapper);
        condIPage = conditionService.page(page, queryWrapper);
        long total = condIPage.getTotal();
        System.out.println("total = " + total);
        long pages = condIPage.getPages();
        System.out.println("pages = " + pages);
        long current = condIPage.getCurrent();
        System.out.println("current = " + current);
        condIPage = conditionService.page(page, queryWrapper);
//        condIPage.getRecords().forEach(condition -> System.out.println("condition = " + condition));
        conditionService.getAllByPage();
        conditionService.getAllByPage();
    }





}
