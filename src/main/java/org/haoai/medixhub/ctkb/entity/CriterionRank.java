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
 * @since 2020-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ec_criterion_rank")
public class CriterionRank implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String conceptId;

    private Long criCount;

    private Long rankNo;

    // Manual getter for Phase 1 compatibility
    public Long getRankNo() {
        return rankNo;
    }

}
