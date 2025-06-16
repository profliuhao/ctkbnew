package org.haoai.medixhub.ctkb.service.impl;


import org.haoai.medixhub.ctkb.entity.ConceptSet;
import org.haoai.medixhub.ctkb.entity.Criteria;
import org.haoai.medixhub.ctkb.service.IOhdsiApiService;
import org.haoai.medixhub.ctkb.utils.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("ohdsiApiService")
public class OhdsiApiServiceImpl implements IOhdsiApiService {
	private final static String base_url="http://api.ohdsi.org/WebAPI/";
	private final static String conceptseturl = base_url+"conceptset/";
	private final static String cohorturl=base_url+"cohortdefinition/";
	
	
	@Override
	public String addConditionAsInitialEvent(JSONObject conceptSet, Integer conceptSetId, String conceptName) {
		// TODO Auto-generated method stub
		String conceptSetStr=conceptSet.toString();
		//add Primary criterion
		String newadded=add2PrimaryCriteria(conceptSetStr, conceptSetId);
		System.out.println("new added"+newadded);
		JSONObject cohortmap=new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		String date = df.format(new Date());
		cohortmap.put("name", conceptName+"_CTKB_"+date);
		cohortmap.put("expressionType", "SIMPLE_EXPRESSION");
		String jsonstr=newadded;
		System.out.println("jsonstr="+jsonstr);
		cohortmap.put("expression",jsonstr);
		String result=HttpUtil.doPost(cohorturl, cohortmap.toString());
		JSONObject jo=JSONObject.fromObject(result);
		String cohortId=String.valueOf(jo.getInt("id"));
		System.out.println("cohortId="+cohortId);
		return cohortId;
	}
	
	public String add2PrimaryCriteria(String conceptStr,Integer conceptSetId){
		JSONObject expression = new JSONObject();
		expression.accumulate("Type", "ALL");
		JSONArray ja=new JSONArray();
		String newadded="{\"ConceptSets\":["+conceptStr+"],\"PrimaryCriteria\":{\"CriteriaList\":[{\"ConditionOccurrence\":{\"CodesetId\": "+conceptSetId+"}}],\"ObservationWindow\":{\"PriorDays\":0,\"PostDays\":0},\"PrimaryCriteriaLimit\":{\"Type\":\"First\"}},\"QualifiedLimit\":{\"Type\":\"First\"},\"ExpressionLimit\":{\"Type\":\"First\"},\"InclusionRules\":[],\"CensoringCriteria\":[]}";
		return newadded;
	}
	
	@Override
	public String queryCohortDefinitionJSON(JSONObject conceptSet,Integer conceptSetId, String conceptName,String domain,String inc,String cohortId){
		String cohortdef= HttpUtil.doGet(cohorturl+cohortId);
		System.out.println("cohortId="+cohortId);
		JSONObject jsonformat=JSONObject.fromObject(cohortdef);
		System.out.println("jsonformat="+jsonformat);
		String jsonstr= jsonformat.get("expression").toString();
		System.out.println("jsonstr====>"+jsonstr);
		
		//add concept Set
		Integer index=jsonstr.indexOf("\"ConceptSets\":[");
		String conceptSetStr=conceptSet.toString();
		System.out.println("one="+jsonstr.substring(0,index+15));
		System.out.println("the other="+jsonstr.substring(index+15));
		String addConceptset=jsonstr.substring(0,index+15)+conceptSetStr+","+jsonstr.substring(index+15);
		
		System.out.println("added concept set=\n"+addConceptset);
		
		System.out.println("inc==>"+inc);
		boolean incflag=true;
		if(inc.equals("INC")){
			incflag=true;
		}else{
			incflag=false;
		}
		//add criterion
		JSONObject criteriaStr=add2InclusionRules(incflag,conceptSetId,conceptName,domain);
		Integer criteraListIndex=addConceptset.indexOf("\"InclusionRules\":");
		System.out.println(addConceptset.substring(0,criteraListIndex+18));
		System.out.println(addConceptset.substring(criteraListIndex+18));
		String finaljson=addConceptset.substring(0,criteraListIndex+18)+criteriaStr+","+addConceptset.substring(criteraListIndex+18);
		System.out.println(finaljson);
		storeInATLAS(JSONObject.fromObject(finaljson),"new Cohort",cohortId);
		return cohortdef;
	}
	
	public Integer storeInATLAS(JSONObject expression,String cohortname,String cohortId) {
		// TODO Auto-generated method stub
//		HashMap<String,String> cohortmap=new HashMap<String,String>();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
//		String date = df.format(new Date());
//		cohortmap.put("name", cohortname+date);
//		cohortmap.put("expressionType", "SIMPLE_EXPRESSION");
//		String jsonstr=expression.toString();
//		System.out.println("jsonstr="+jsonstr);
//		cohortmap.put("expression",jsonstr);
//		//System.out.println("to be added cohort def="+);
//		HttpUtil.doPut(cohorturl+cohortId, JSONObject.fromObject(cohortmap).toString());
////		JSONObject resultjson=JSONObject.fromObject(result);
////		Integer cohortId=(Integer) resultjson.get("id");
		//return 1;
		JSONObject cohortdef=new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		String date = df.format(new Date());
		
		cohortdef.put("name", "[CTKB]"+date);
		cohortdef.put("expressionType", "SIMPLE_EXPRESSION");
		String jsonstr=expression.toString();
		System.out.println("jsonstr="+jsonstr);
		cohortdef.put("expression",jsonstr);
		HttpUtil.doPut(cohorturl+cohortId, cohortdef.toString());
		System.out.println("modified cohort def="+cohortdef.toString());
		//JSONObject resultjson=JSONObject.fromObject(result);
		//Integer cohortId=(Integer) resultjson.get("id");
		return 1;
		
		
	}
	
	
	public JSONObject add2InclusionRules(boolean include,Integer conceptSetId,String conceptName,String domain){
		JSONObject jo = new JSONObject();
		jo.accumulate("name", conceptName);
		jo.accumulate("description", "inserted by Criteria Library");
		//expression
		JSONObject expression = new JSONObject();
		expression.accumulate("Type", "ALL");
		//CriteriaList
		JSONArray criterialist = new JSONArray();
		JSONObject criteria=new JSONObject();
		JSONObject codesetID=new JSONObject();
		codesetID.accumulate("CodesetId", conceptSetId);
		if(domain.equals("Condition")){
			criteria.accumulate("ConditionOccurrence", codesetID);
		}else if(domain.equals("Measurement")){
			criteria.accumulate("Measurement", codesetID);
		}else if(domain.equals("Procedure")){
			criteria.accumulate("ProcedureOccurrence", codesetID);
		}else if(domain.equals("Drug")){
			criteria.accumulate("DrugExposure", codesetID);
		}else if(domain.equals("Drug")){
			criteria.accumulate("DrugExposure", codesetID);
		}else if(domain.equals("Observation")){
			criteria.accumulate("Observation", codesetID);
		}
		JSONObject onecriterion=new JSONObject();
		onecriterion.accumulate("Criteria", criteria);
		JSONObject startwindow=new JSONObject();
		
		JSONObject startcoeff=new JSONObject();
		startcoeff.accumulate("Coeff", -1);
		
		JSONObject endcoeff=new JSONObject();
		endcoeff.accumulate("Coeff", 1);
		startwindow.accumulate("Start", startcoeff);
		startwindow.accumulate("End", endcoeff);
		startwindow.accumulate("UseEventEnd", false);
		
		onecriterion.accumulate("StartWindow", startwindow);
		
		JSONObject occurrence=new JSONObject();
		if(include==true){
		occurrence.accumulate("Type", 2);
		occurrence.accumulate("Count", 1);
		}else{
			occurrence.accumulate("Type", 0);
			occurrence.accumulate("Count", 0);
		}
		onecriterion.accumulate("Occurrence", occurrence);
		criterialist.add(onecriterion);
		expression.accumulate("CriteriaList", criterialist);
		JSONArray janull = new JSONArray();
		expression.accumulate("DemographicCriteriaList", janull);
		expression.accumulate("Groups", janull);
		System.out.println(expression);
		jo.accumulate("expression", expression);
		return jo;
	}
	
	@Override
	public Integer createConceptSetByConceptId(String criteriaName, Integer conceptId) {
		// TODO Auto-generated method stub
		System.out.println("=>" + conceptId);
		JSONObject conceptunit = formatOneitem(conceptId);
		System.out.println("conceptunit=>" + conceptunit);
		JSONArray conceptSet = new JSONArray();
		conceptSet.add(conceptunit);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		String date = df.format(new Date());
		// do get
		// http://api.ohdsi.org/WebAPI/conceptset/0/exists?name=CY_test_APIs
		String conceptSetName="[CTKB]" + criteriaName;
		String para = URLEncoder.encode(conceptSetName);

		String isExist = HttpUtil.doGet(conceptseturl + "0/exists?name=" + para);
		System.out.println(conceptseturl + "0/exists?name=" + para);
		System.out.println("isExist=>" + isExist);
		Integer exist_flag=Integer.valueOf(isExist);
		if(exist_flag==0){
		System.out.println("new concept set added");
		JSONObject jo = new JSONObject();
		// deleteConceptSetBySubString("[C2Q]"+criteriaName);
			jo.accumulate("name", conceptSetName);
		jo.accumulate("id", 9000);
		jo.accumulate("createdBy", "CTKB");
			System.out.println("concept set url="+conceptseturl);
			System.out.println("post para="+jo.toString());
		String result = HttpUtil.doPost(conceptseturl, jo.toString());
		System.out.println("jo->" + jo.toString());
		System.out.println("conceptset->" + result);
		JSONObject rejo = JSONObject.fromObject(result);
		HttpUtil.doPut(conceptseturl + rejo.getString("id") + "/items", conceptSet.toString());
		return Integer.valueOf(rejo.getString("id"));
		}else{
			return existConceptSetId(conceptSetName);
		}

	}
	
	
	
	
	@Override
	public JSONObject querybyConceptSetid(Integer conceptSetId){
		JSONObject jot=new JSONObject();
    	String re2=HttpUtil.doGet(conceptseturl+conceptSetId);
    	JSONObject jore2=JSONObject.fromObject(re2);
    	System.out.println("jore2="+jore2);
    	jot.accumulateAll(jore2);
    	String re3=HttpUtil.doGet(conceptseturl+conceptSetId+"/expression");
    	JSONObject expression=JSONObject.fromObject(re3);
    	jot.accumulate("expression",expression);   	
    	return jot;
	}
	
	public static JSONObject formatOneitem(Integer conceptId){
		JSONObject jo=new JSONObject();
		try {
			jo.put("conceptId", conceptId);
			jo.put("isExcluded", 0);
			jo.put("includeDescendants", 1);//
			jo.put("includeMapped", 0);//
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

	@Override
	public String addAdditionalCriteria(List<Criteria> criterialist, String cohortId) {
		// TODO Auto-generated method stub
		System.out.println("cohortId="+cohortId);
		String cohortdef=HttpUtil.doGet(cohorturl+cohortId);
		
		//JSONObject jsonformat=JSONObject.fromObject(cohortdef);
		System.out.println("cohortDf="+cohortdef);
		System.out.println("test put cohortDf="+cohortdef);
		HttpUtil.doPut(cohorturl+cohortId, cohortdef);
		System.out.println("end test put cohortDf="+cohortdef);
		return null;
	}

	public Integer existConceptSetId(String entity){
		System.out.println("Look for entity="+entity);
		List<ConceptSet> concept_sets=getallConceptSet();
		for(ConceptSet cs:concept_sets){
			 if(cs.getName().equals(entity)){
				System.out.println("exist concept set id="+cs.getId());
				return cs.getId();
			 }
		}
		System.out.println("concept set id = 0");
		return 0;
	}
	
	public List<ConceptSet> getallConceptSet() {
		String strResult = HttpUtil.doGet(conceptseturl);
		JSONArray array = JSONArray.fromObject(strResult);
		List<ConceptSet> list = JSONArray.toList(array, ConceptSet.class);
		return list;
	}


}
