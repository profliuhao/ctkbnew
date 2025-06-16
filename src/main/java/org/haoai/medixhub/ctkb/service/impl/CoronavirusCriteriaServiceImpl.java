package org.haoai.medixhub.ctkb.service.impl;

import org.haoai.medixhub.ctkb.entity.CoronavirusCriteria;
import org.haoai.medixhub.ctkb.dao.CoronavirusCriteriaMapper;
import org.haoai.medixhub.ctkb.service.CoronavirusCriteriaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-06-03
 */
@Service
public class CoronavirusCriteriaServiceImpl extends ServiceImpl<CoronavirusCriteriaMapper, CoronavirusCriteria> implements CoronavirusCriteriaService {

    @Autowired
    private CoronavirusCriteriaMapper coronavirusCriteriaMapper;
}
