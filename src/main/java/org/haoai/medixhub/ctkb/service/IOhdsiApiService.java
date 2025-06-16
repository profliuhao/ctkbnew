package org.haoai.medixhub.ctkb.service;



import org.haoai.medixhub.ctkb.entity.Criteria;
import net.sf.json.JSONObject;


import java.util.List;

public interface IOhdsiApiService {
	Integer createConceptSetByConceptId(String conceptName,Integer conceptId);
	JSONObject querybyConceptSetid(Integer conceptSetId);
	String queryCohortDefinitionJSON(JSONObject conceptSet, Integer conceptSetId, String conceptName, String domain, String inc, String cohortId);
	String addConditionAsInitialEvent(JSONObject conceptSet,Integer conceptSetId, String conceptName);
	String addAdditionalCriteria(List<Criteria> criterialist, String cohortId);
}
