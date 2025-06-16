package org.haoai.medixhub.ctkb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.haoai.medixhub.ctkb.entity.Condition;
import org.haoai.medixhub.ctkb.dao.ConditionMapper;
import org.haoai.medixhub.ctkb.service.ConditionService;
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
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements ConditionService {

    @Autowired
    private ConditionMapper conditionMapper;

    private IPage<Condition> getConditionIPage(QueryWrapper<Condition> queryWrapper, IPage<Condition> page) {
        return conditionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Condition> getAllByPage() {
        IPage<Condition> page = new Page<>(1, 10);
        return conditionMapper.selectPage(page, null);
    }

    @Override
    public List<Condition> getByNCTID(String nctid) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);
        List<Condition> conditions = conditionMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Condition> getByNCTIDPage(String nctid) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Condition> page = new Page<>(1, 10);
        return getConditionIPage(queryWrapper, page);
    }

    @Override
    public IPage<Condition> getByNCTIDPage(String nctid, Integer currentPage, Integer size) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nctid", nctid);

        IPage<Condition> page = new Page<>(currentPage, size);
        return getConditionIPage(queryWrapper, page);
    }

    @Override
    public List<Condition> getByConceptName(String conceptName) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        List<Condition> conditions = conditionMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Condition> getByConceptNamePage(String conceptName) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Condition> page = new Page<>(1, 10);
        return getConditionIPage(queryWrapper, page);
    }

    @Override
    public IPage<Condition> getByConceptNamePage(String conceptName, Integer currentPage, Integer size) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_name", conceptName);

        IPage<Condition> page = new Page<>(currentPage, size);
        return getConditionIPage(queryWrapper, page);
    }


    @Override
    public List<Condition> getByConceptID(String conceptId) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        List<Condition> conditions = conditionMapper.selectList(queryWrapper);
        return conditions;
    }

    @Override
    public IPage<Condition> getByConceptIDPage(String conceptId) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Condition> page = new Page<>(1, 10);
        return getConditionIPage(queryWrapper, page);
    }

    @Override
    public IPage<Condition> getByConceptIDPage(String conceptId, Integer currentPage, Integer size) {
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("concept_id", conceptId);
        IPage<Condition> page = new Page<>(currentPage, size);
        return getConditionIPage(queryWrapper, page);
    }

}
