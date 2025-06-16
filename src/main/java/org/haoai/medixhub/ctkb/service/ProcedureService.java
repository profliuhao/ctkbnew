package org.haoai.medixhub.ctkb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Procedure;
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
public interface ProcedureService extends IService<Procedure> {

    List<Procedure> getByNCTID(String nctid);

    IPage<Procedure> getByNCTIDPage(String nctid);

    IPage<Procedure> getByNCTIDPage(String nctid, Integer currentPage, Integer size);

    List<Procedure> getByConceptName(String conceptName);

    IPage<Procedure> getByConceptNamePage(String conceptName);

    IPage<Procedure> getByConceptNamePage(String conceptName, Integer currentPage, Integer size);

    List<Procedure> getByConceptID(String conceptId);

    IPage<Procedure> getByConceptIDPage(String conceptId);

    IPage<Procedure> getByConceptIDPage(String conceptId, Integer currentPage, Integer size);
}
