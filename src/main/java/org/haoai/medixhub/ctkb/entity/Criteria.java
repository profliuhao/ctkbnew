package org.haoai.medixhub.ctkb.entity;

public class Criteria {

    private Integer id;

    private Integer conditionConceptId;

    private String conditionConceptName;

    private Integer criteriaConceptId;

    private String criteriaConceptName;

    private String criteriaDomain;

    private Integer include;

    private Integer conceptCount;

    private Integer totalCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConditionConceptId() {
        return conditionConceptId;
    }

    public void setConditionConceptId(Integer conditionConceptId) {
        this.conditionConceptId = conditionConceptId;
    }

    public String getConditionConceptName() {
        return conditionConceptName;
    }

    public void setConditionConceptName(String conditionConceptName) {
        this.conditionConceptName = conditionConceptName;
    }

    public Integer getCriteriaConceptId() {
        return criteriaConceptId;
    }

    public void setCriteriaConceptId(Integer criteriaConceptId) {
        this.criteriaConceptId = criteriaConceptId;
    }

    public String getCriteriaConceptName() {
        return criteriaConceptName;
    }

    public void setCriteriaConceptName(String criteriaConceptName) {
        this.criteriaConceptName = criteriaConceptName;
    }

    public String getCriteriaDomain() {
        return criteriaDomain;
    }

    public void setCriteriaDomain(String criteriaDomain) {
        this.criteriaDomain = criteriaDomain;
    }

    public Integer getInclude() {
        return include;
    }

    public void setInclude(Integer include) {
        this.include = include;
    }

    public Integer getConceptCount() {
        return conceptCount;
    }

    public void setConceptCount(Integer conceptCount) {
        this.conceptCount = conceptCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
