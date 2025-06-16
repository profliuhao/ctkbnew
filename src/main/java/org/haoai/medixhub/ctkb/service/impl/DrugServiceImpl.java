package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.dao.DrugMapper;
import org.haoai.medixhub.ctkb.entity.Drug;
import org.haoai.medixhub.ctkb.service.DrugService;
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
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    @Autowired
    private DrugMapper drugMapper;

    private IPage<Drug> getDrugIPage(QueryWrapper<Drug> queryWrapper, IPage<Drug> page) {
        return drugMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Drug> getByNCTID(String nctid) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);
        List<Drug> conditions = drugMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Drug> getByNCTIDPage(String nctid) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Drug> page = new Page<>(1, 10);
        return getDrugIPage(queryWrapper, page);
    }

    @Override
    public IPage<Drug> getByNCTIDPage(String nctid, Integer currentPage, Integer size) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Drug> page = new Page<>(currentPage, size);
        return getDrugIPage(queryWrapper, page);
    }

    @Override
    public List<Drug> getByConceptName(String conceptName) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        List<Drug> conditions = drugMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Drug> getByConceptNamePage(String conceptName) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Drug> page = new Page<>(1, 10);
        return getDrugIPage(queryWrapper, page);
    }

    @Override
    public IPage<Drug> getByConceptNamePage(String conceptName, Integer currentPage, Integer size) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Drug> page = new Page<>(currentPage, size);
        return getDrugIPage(queryWrapper, page);
    }


    @Override
    public List<Drug> getByConceptID(String conceptId) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Drug> conditions = drugMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Drug> getByConceptIDPage(String conceptId) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Drug> page = new Page<>(1, 10);
        return getDrugIPage(queryWrapper, page);
    }

    @Override
    public IPage<Drug> getByConceptIDPage(String conceptId, Integer currentPage, Integer size) {
        QueryWrapper<Drug> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Drug> page = new Page<>(currentPage, size);
        return getDrugIPage(queryWrapper, page);
    }
}
