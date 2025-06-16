package org.haoai.medixhub.ctkb.controller;



import org.haoai.medixhub.ctkb.entity.Criteria;
import org.haoai.medixhub.ctkb.service.IOhdsiApiService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/function")
@CrossOrigin

public class FunctionController {

    @Resource
    private IOhdsiApiService ohdsiApiService;


    @PostMapping("/cohortdef")
    public Map<String, Object> cohortDefine(@RequestBody String params){
        Map<String,Object> map=new HashMap<String,Object>();

        JSONObject j1 = JSONObject.fromObject(params);

        String standardQueryTerm = j1.getString("cTerm");
        String standardId= j1.getString("cId");
        JSONArray jsa = j1.getJSONArray("criList");
        List<Criteria> criterialist = (List<Criteria>)JSONArray.toCollection(jsa, Criteria.class);

        Integer conceptSetId=ohdsiApiService.createConceptSetByConceptId(standardQueryTerm, Integer.valueOf(standardId));
        JSONObject jo=ohdsiApiService.querybyConceptSetid(conceptSetId);
        System.out.println("jo===>"+jo);
        String cohortId=ohdsiApiService.addConditionAsInitialEvent(jo, conceptSetId, standardQueryTerm);
        System.out.println("cohort created successfully! cohortId="+cohortId);

//		//Concept Set
        for(Criteria c:criterialist){
            Integer tconceptSetId=ohdsiApiService.createConceptSetByConceptId(c.getCriteriaConceptName(), Integer.valueOf(c.getCriteriaConceptId().toString()));
            JSONObject tjo=ohdsiApiService.querybyConceptSetid(tconceptSetId);
            System.out.println("created concept Set="+tjo);
            ohdsiApiService.queryCohortDefinitionJSON(tjo,tconceptSetId,c.getCriteriaConceptName(),c.getCriteriaDomain(),c.getInclude().toString(),cohortId);
        }
        ohdsiApiService.addAdditionalCriteria(criterialist,cohortId);
        map.put("cohortId",cohortId);
        return map;
    }
}
