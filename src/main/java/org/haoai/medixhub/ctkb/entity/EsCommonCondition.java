package org.haoai.medixhub.ctkb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Document(indexName = "flu_common_condition_info", createIndex = false)
public class EsCommonCondition implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    private Integer id;

    private Long conditionConceptId;

    @Field(type = FieldType.Keyword)
    private String conditionConceptName;

    private Long criteriaConceptId;

    @Field(type = FieldType.Keyword)
    private String criteriaConceptName;

    @Field(type = FieldType.Keyword)
    private String criteriaDomain;

    private Long include;

    private Long conceptCount;

    private Long totalCount;


}
