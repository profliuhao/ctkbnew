package org.haoai.medixhub.ctkb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Condition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
public interface ConditionService extends IService<Condition> {

    IPage<Condition> getAllByPage();

    List<Condition> getByNCTID(String nctid);

    IPage<Condition> getByNCTIDPage(String nctid);

    IPage<Condition> getByNCTIDPage(String nctid, Integer currentPage, Integer size);

    List<Condition> getByConceptName(String conceptName);

    IPage<Condition> getByConceptNamePage(String conceptName);

    IPage<Condition> getByConceptNamePage(String conceptName, Integer currentPage, Integer size);

    List<Condition> getByConceptID(String conceptId);

    IPage<Condition> getByConceptIDPage(String conceptId);

    IPage<Condition> getByConceptIDPage(String conceptId, Integer currentPage, Integer size);

}
