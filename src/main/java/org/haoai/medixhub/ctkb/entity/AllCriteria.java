package org.haoai.medixhub.ctkb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoliu
 * @since 2020-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ec_all_criteria")
public class AllCriteria implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String nctid;

    private Integer include;

    private Integer lineNum;

    private String conceptId;

    private String conceptName;

    private String domain;

    private Integer neg;

    private Integer startIndex;

    private Integer endIndex;

    private String temporalSourceText;

    private String entitySourceText;

    private Float score;

    private Integer beforedays;

    private Integer afterdays;

    private String numericSourceText;

    private Float max;

    private Float min;

    private Integer operate;

    private Integer times;

    private String unit;


}
