package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.MeasurementMapper;
import org.haoai.medixhub.ctkb.entity.Measurement;
import org.haoai.medixhub.ctkb.service.MeasurementService;
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
public class MeasurementServiceImpl extends ServiceImpl<MeasurementMapper, Measurement> implements MeasurementService {


    @Autowired
    private MeasurementMapper measurementMapper;

    private IPage<Measurement> getMeasurementIPage(QueryWrapper<Measurement> queryWrapper, IPage<Measurement> page) {
        return measurementMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Measurement> getByNCTID(String nctid) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);
        List<Measurement> conditions = measurementMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Measurement> getByNCTIDPage(String nctid) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Measurement> page = new Page<>(1, 10);
        return getMeasurementIPage(queryWrapper, page);
    }

    @Override
    public IPage<Measurement> getByNCTIDPage(String nctid, Integer currentPage, Integer size) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Measurement> page = new Page<>(currentPage, size);
        return getMeasurementIPage(queryWrapper, page);
    }

    @Override
    public List<Measurement> getByConceptName(String conceptName) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        List<Measurement> conditions = measurementMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Measurement> getByConceptNamePage(String conceptName) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Measurement> page = new Page<>(1, 10);
        return getMeasurementIPage(queryWrapper, page);
    }

    @Override
    public IPage<Measurement> getByConceptNamePage(String conceptName, Integer currentPage, Integer size) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Measurement> page = new Page<>(currentPage, size);
        return getMeasurementIPage(queryWrapper, page);
    }


    @Override
    public List<Measurement> getByConceptID(String conceptId) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Measurement> conditions = measurementMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Measurement> getByConceptIDPage(String conceptId) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Measurement> page = new Page<>(1, 10);
        return getMeasurementIPage(queryWrapper, page);
    }

    @Override
    public IPage<Measurement> getByConceptIDPage(String conceptId, Integer currentPage, Integer size) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Measurement> page = new Page<>(currentPage, size);
        return getMeasurementIPage(queryWrapper, page);
    }

    @Override
    public List<Measurement> getAllMeasurementValuesByConceptId(String conceptId) {
        QueryWrapper<Measurement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Measurement> measurements = measurementMapper.selectList(queryWrapper);
        return measurements;
    }
}
