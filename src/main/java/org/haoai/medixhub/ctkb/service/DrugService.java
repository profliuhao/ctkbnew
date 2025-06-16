package org.haoai.medixhub.ctkb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Drug;
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
public interface DrugService extends IService<Drug> {

    List<Drug> getByNCTID(String nctid);

    IPage<Drug> getByNCTIDPage(String nctid);

    IPage<Drug> getByNCTIDPage(String nctid, Integer currentPage, Integer size);

    List<Drug> getByConceptName(String conceptName);

    IPage<Drug> getByConceptNamePage(String conceptName);

    IPage<Drug> getByConceptNamePage(String conceptName, Integer currentPage, Integer size);

    List<Drug> getByConceptID(String conceptId);

    IPage<Drug> getByConceptIDPage(String conceptId);

    IPage<Drug> getByConceptIDPage(String conceptId, Integer currentPage, Integer size);
    
}
