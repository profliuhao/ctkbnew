package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.ObservationMapper;
import org.haoai.medixhub.ctkb.entity.Observation;
import org.haoai.medixhub.ctkb.service.ObservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
@Service
public class ObservationServiceImpl extends ServiceImpl<ObservationMapper, Observation> implements ObservationService {


    @Autowired
    private ObservationMapper observationMapper;

    private IPage<Observation> getObservationIPage(QueryWrapper<Observation> queryWrapper, IPage<Observation> page) {
        return observationMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Observation> getByNCTID(String nctid) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);
        List<Observation> conditions = observationMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Observation> getByNCTIDPage(String nctid) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Observation> page = new Page<>(1, 10);
        return getObservationIPage(queryWrapper, page);
    }

    @Override
    public IPage<Observation> getByNCTIDPage(String nctid, Integer currentPage, Integer size) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Observation> page = new Page<>(currentPage, size);
        return getObservationIPage(queryWrapper, page);
    }

    @Override
    public List<Observation> getByConceptName(String conceptName) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        List<Observation> conditions = observationMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Observation> getByConceptNamePage(String conceptName) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Observation> page = new Page<>(1, 10);
        return getObservationIPage(queryWrapper, page);
    }

    @Override
    public IPage<Observation> getByConceptNamePage(String conceptName, Integer currentPage, Integer size) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Observation> page = new Page<>(currentPage, size);
        return getObservationIPage(queryWrapper, page);
    }


    @Override
    public List<Observation> getByConceptID(String conceptId) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Observation> conditions = observationMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Observation> getByConceptIDPage(String conceptId) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Observation> page = new Page<>(1, 10);
        return getObservationIPage(queryWrapper, page);
    }

    @Override
    public IPage<Observation> getByConceptIDPage(String conceptId, Integer currentPage, Integer size) {
        QueryWrapper<Observation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Observation> page = new Page<>(currentPage, size);
        return getObservationIPage(queryWrapper, page);
    }
}
