package org.haoai.medixhub.ctkb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-05-29
 */
@Data
@Document(indexName = "condition_info", createIndex = false)
public class EsCondition implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    private Integer id;

    private String nctid;

    private Integer include;

    private Integer lineNum;

    private String conceptId;

    @Field(type = FieldType.Keyword)
    private String conceptName;

    @Field(type = FieldType.Keyword)
    private String domain;

    private Integer neg;

    private Integer startIndex;

    private Integer endIndex;

    @Field(type = FieldType.Text)
    private String temporalSourceText;

    @Field(type = FieldType.Text)
    private String entitySourceText;

    private Float score;

    private Integer beforedays;

    private Integer afterdays;


}
