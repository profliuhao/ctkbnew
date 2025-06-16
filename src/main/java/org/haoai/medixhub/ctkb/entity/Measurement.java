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
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ec_measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    // Manual getters for Phase 1 compatibility
    public Integer getInclude() {
        return include;
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() {
        return max;
    }

}
