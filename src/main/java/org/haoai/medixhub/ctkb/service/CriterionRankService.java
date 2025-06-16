package org.haoai.medixhub.ctkb.service;

import org.haoai.medixhub.ctkb.entity.CriterionRank;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoliu
 * @since 2020-07-26
 */
public interface CriterionRankService extends IService<CriterionRank> {

    int getRankingByCriteriaId(String criteriaId);

}
