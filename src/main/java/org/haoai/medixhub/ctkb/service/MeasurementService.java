package org.haoai.medixhub.ctkb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Measurement;
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
public interface MeasurementService extends IService<Measurement> {

    List<Measurement> getByNCTID(String nctid);

    IPage<Measurement> getByNCTIDPage(String nctid);

    IPage<Measurement> getByNCTIDPage(String nctid, Integer currentPage, Integer size);

    List<Measurement> getByConceptName(String conceptName);

    IPage<Measurement> getByConceptNamePage(String conceptName);

    IPage<Measurement> getByConceptNamePage(String conceptName, Integer currentPage, Integer size);

    List<Measurement> getByConceptID(String conceptId);

    IPage<Measurement> getByConceptIDPage(String conceptId);

    IPage<Measurement> getByConceptIDPage(String conceptId, Integer currentPage, Integer size);

    List<Measurement> getAllMeasurementValuesByConceptId(String conceptId);



}
