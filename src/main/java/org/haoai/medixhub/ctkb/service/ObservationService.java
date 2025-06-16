package org.haoai.medixhub.ctkb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Observation;
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
public interface ObservationService extends IService<Observation> {


    List<Observation> getByNCTID(String nctid);

    IPage<Observation> getByNCTIDPage(String nctid);

    IPage<Observation> getByNCTIDPage(String nctid, Integer currentPage, Integer size);

    List<Observation> getByConceptName(String conceptName);

    IPage<Observation> getByConceptNamePage(String conceptName);

    IPage<Observation> getByConceptNamePage(String conceptName, Integer currentPage, Integer size);

    List<Observation> getByConceptID(String conceptId);

    IPage<Observation> getByConceptIDPage(String conceptId);

    IPage<Observation> getByConceptIDPage(String conceptId, Integer currentPage, Integer size);
}
