package org.haoai.medixhub.ctkb.service;

import org.haoai.medixhub.ctkb.entity.AllCriteria;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoliu
 * @since 2020-07-21
 */
public interface AllCriteriaService extends IService<AllCriteria> {

    int getInclusionCountByCriteriaId(String criteriaId);

    int getExclusionCountByCriteriaId(String criteriaId);

}
