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
@TableName("ec_common_intervention")
public class CommonIntervention implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long interventionConceptId;

    private String interventionConceptName;

    private Long criteriaConceptId;

    private String criteriaConceptName;

    private String criteriaDomain;

    private Long include;

    private Long conceptCount;

    private Long totalCount;


}
