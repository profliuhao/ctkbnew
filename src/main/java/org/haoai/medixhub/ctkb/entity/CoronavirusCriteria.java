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
@TableName("ec_coronavirus_criteria")
public class CoronavirusCriteria implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String nctId;

    private String nctIdOriginal;

    private String ptCohort;

    private String include;

    private String conceptId;

    private String conceptName;

    private String domain;

    private String negation;

    private String conceptGroupId;

    private String startIndex;

    private String endIndex;

    private String entitySourceText;

    private String temporalSourceText;

    private String beforeDays;

    private String numericSourceText;

    private String numericAttMin;

    private String numericAttMax;

    private String isExclusion;

    private String toDisplay;


}
