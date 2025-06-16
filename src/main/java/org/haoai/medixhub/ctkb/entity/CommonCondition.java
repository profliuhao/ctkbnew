package org.haoai.medixhub.ctkb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ec_common_condition")
public class CommonCondition implements Serializable {

    private static final long serialVersionUID=1L;

    // No primary key - using composite key of condition_concept_id and criteria_concept_id

    private Long conditionConceptId;

    private String conditionConceptName;

    private Long criteriaConceptId;

    private String criteriaConceptName;

    private String criteriaDomain;

    private Long include;

    private Long conceptCount;

    private Long totalCount;

    // Manual getters for Phase 1 compatibility
    public String getCriteriaConceptName() {
        return criteriaConceptName;
    }

    public Long getCriteriaConceptId() {
        return criteriaConceptId;
    }

    public String getConditionConceptName() {
        return conditionConceptName;
    }

    public Long getConditionConceptId() {
        return conditionConceptId;
    }

}
