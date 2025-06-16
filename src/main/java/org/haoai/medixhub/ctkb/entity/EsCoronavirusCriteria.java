package org.haoai.medixhub.ctkb.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
@Data
@Document(indexName = "coronavirus_criteria_info", createIndex = false)
public class EsCoronavirusCriteria implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    private Integer id;

    private String nctId;

    private String nctIdOriginal;

    private String ptCohort;

    private String include;

    private String conceptId;

    @Field(type = FieldType.Keyword)
    private String conceptName;

    @Field(type = FieldType.Keyword)
    private String domain;

    private String negation;

    private String conceptGroupId;

    private String startIndex;

    private String endIndex;

    @Field(type = FieldType.Text)
    private String entitySourceText;

    @Field(type = FieldType.Text)
    private String temporalSourceText;

    private String beforeDays;

    @Field(type = FieldType.Text)
    private String numericSourceText;

    private String numericAttMin;

    private String numericAttMax;

    private String isExclusion;

    private String toDisplay;


}
