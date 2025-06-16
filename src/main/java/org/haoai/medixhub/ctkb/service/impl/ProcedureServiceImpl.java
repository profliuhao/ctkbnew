package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.ProcedureMapper;
import org.haoai.medixhub.ctkb.entity.Procedure;
import org.haoai.medixhub.ctkb.service.ProcedureService;
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
public class ProcedureServiceImpl extends ServiceImpl<ProcedureMapper, Procedure> implements ProcedureService {


    @Autowired
    private ProcedureMapper procedureMapper;

    private IPage<Procedure> getProcedureIPage(QueryWrapper<Procedure> queryWrapper, IPage<Procedure> page) {
        return procedureMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Procedure> getByNCTID(String nctid) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);
        List<Procedure> conditions = procedureMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Procedure> getByNCTIDPage(String nctid) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Procedure> page = new Page<>(1, 10);
        return getProcedureIPage(queryWrapper, page);
    }

    @Override
    public IPage<Procedure> getByNCTIDPage(String nctid, Integer currentPage, Integer size) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Procedure> page = new Page<>(currentPage, size);
        return getProcedureIPage(queryWrapper, page);
    }

    @Override
    public List<Procedure> getByConceptName(String conceptName) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        List<Procedure> conditions = procedureMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Procedure> getByConceptNamePage(String conceptName) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Procedure> page = new Page<>(1, 10);
        return getProcedureIPage(queryWrapper, page);
    }

    @Override
    public IPage<Procedure> getByConceptNamePage(String conceptName, Integer currentPage, Integer size) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Procedure> page = new Page<>(currentPage, size);
        return getProcedureIPage(queryWrapper, page);
    }


    @Override
    public List<Procedure> getByConceptID(String conceptId) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Procedure> conditions = procedureMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Procedure> getByConceptIDPage(String conceptId) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Procedure> page = new Page<>(1, 10);
        return getProcedureIPage(queryWrapper, page);
    }

    @Override
    public IPage<Procedure> getByConceptIDPage(String conceptId, Integer currentPage, Integer size) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Procedure> page = new Page<>(currentPage, size);
        return getProcedureIPage(queryWrapper, page);
    }
}
