package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.haoai.medixhub.ctkb.entity.CriterionRank;
import org.haoai.medixhub.ctkb.dao.CriterionRankMapper;
import org.haoai.medixhub.ctkb.service.CriterionRankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-07-26
 */
@Service
public class CriterionRankServiceImpl extends ServiceImpl<CriterionRankMapper, CriterionRank> implements CriterionRankService {

    @Autowired
    private CriterionRankMapper criterionRankMapper;

    @Override
    public int getRankingByCriteriaId(String criteriaId) {
        QueryWrapper<CriterionRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("rank_no").eq("concept_id", criteriaId);
//        CriterionRank criterionRank = criterionRankMapper.selectOne(queryWrapper);
        Optional<CriterionRank> criterionRank = Optional
                .ofNullable(criterionRankMapper.selectOne(queryWrapper));

        if(criterionRank.isPresent()){
            return Math.toIntExact(criterionRank.get().getRankNo());
        }else{
            return -1;
        }
    }
}
