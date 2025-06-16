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
 * @since 2020-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ec_common_criteria_stats")
public class CommonCriteriaStats implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String conditionConceptId;

    private String criteriaConceptId;

    private Integer include;

    private Integer p1Count;

    private Integer p2Count;

    private Integer p3Count;

    private Integer p4Count;

    private Integer peCount;

    // Manual getters for Phase 1 compatibility
    public Integer getP1Count() {
        return p1Count;
    }

    public Integer getP2Count() {
        return p2Count;
    }

    public Integer getP3Count() {
        return p3Count;
    }

    public Integer getP4Count() {
        return p4Count;
    }

}
