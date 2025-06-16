package org.haoai.medixhub.ctkb.service.impl;

import com.alibaba.fastjson.JSON;
import org.haoai.medixhub.ctkb.repository.EsConditionMapper;
import org.haoai.medixhub.ctkb.entity.Condition;
import org.haoai.medixhub.ctkb.entity.EsCondition;
import org.haoai.medixhub.ctkb.service.ConditionService;
import org.haoai.medixhub.ctkb.service.EsConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EsConditionServiceImpl implements EsConditionService {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private EsConditionMapper esConditionMapper;

    @Override
    public void importConditionToES() {


        List<Condition> conditions = conditionService.getByConceptID("316866");

        List<EsCondition> esConditions = JSON.parseArray(JSON.toJSONString(conditions),EsCondition.class);

        esConditionMapper.saveAll(esConditions);
    }
}
