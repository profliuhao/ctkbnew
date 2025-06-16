package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.haoai.medixhub.ctkb.entity.AllCriteria;
import org.haoai.medixhub.ctkb.dao.AllCriteriaMapper;
import org.haoai.medixhub.ctkb.service.AllCriteriaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-07-21
 */
@Service
public class AllCriteriaServiceImpl extends ServiceImpl<AllCriteriaMapper, AllCriteria> implements AllCriteriaService {

    @Autowired
    private AllCriteriaMapper allCriteriaMapper;

    @Override
    public int getInclusionCountByCriteriaId(String criteriaId) {
        QueryWrapper<AllCriteria> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct nctid").eq("concept_id", criteriaId).eq("include", 1);
        long count = count(queryWrapper);
        return Math.toIntExact(count);
    }

    @Override
    public int getExclusionCountByCriteriaId(String criteriaId) {
        QueryWrapper<AllCriteria> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct nctid").eq("concept_id", criteriaId).eq("include", 0);
        long count = count(queryWrapper);
        return Math.toIntExact(count);
    }
}
