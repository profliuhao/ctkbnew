package org.haoai.medixhub.ctkb.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Observation;
import org.haoai.medixhub.ctkb.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/observation")
public class ObservationController {

    @Autowired
    ObservationService observationService;

//    @GetMapping("/concept_id/{concept_id}")
//    public List<Observation> searchObservationByConceptID(@PathVariable(value = "concept_id") String concept_id){
//        return observationService.getByConceptID(concept_id);
//    }

    @GetMapping("/page/concept_id/{concept_id}")
    public IPage<Observation> searchObservationByConceptIDPage(@PathVariable(value = "concept_id") String concept_id) {
        return observationService.getByConceptIDPage(concept_id);
    }

    @GetMapping("/page/concept_id/{concept_id}/{current_page}/{size}")
    public IPage<Observation> searchObservationByConceptIDPageWithSize(@PathVariable(value = "concept_id") String concept_id,
                                                                       @PathVariable(value = "current_page")Integer current_page,
                                                                       @PathVariable(value = "size")Integer size) {
        return observationService.getByConceptIDPage(concept_id, current_page, size);
    }

//    @GetMapping("/concept_name/{concept_name}")
//    public List<Observation> searchObservationByConceptName(@PathVariable(value = "concept_name") String concept_name){
//        return observationService.getByConceptName(concept_name);
//    }

    @GetMapping("/page/concept_name/{concept_name}")
    public IPage<Observation> searchObservationByConceptNamePage(@PathVariable(value = "concept_name") String concept_name) {
        return observationService.getByConceptNamePage(concept_name);
    }

    @GetMapping("/page/concept_name/{concept_name}/{current_page}/{size}")
    public IPage<Observation> searchObservationByConceptNamePageWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                         @PathVariable(value = "current_page")Integer current_page,
                                                                         @PathVariable(value = "size")Integer size) {
        return observationService.getByConceptNamePage(concept_name, current_page, size);
    }

//    @GetMapping("/concept_nctid/{concept_nctid}")
//    public List<Observation> searchObservationByNCTID(@PathVariable(value = "concept_nctid") String concept_nctid){
//        return observationService.getByNCTID(concept_nctid);
//    }

    @GetMapping("/page/concept_nctid/{concept_nctid}")
    public IPage<Observation> searchObservationByNCTIDNamePage(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return observationService.getByNCTIDPage(concept_nctid);
    }

    @GetMapping("/page/concept_nctid/{concept_nctid}/{current_page}/{size}")
    public IPage<Observation> searchObservationByNCTIDPageWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                                   @PathVariable(value = "current_page")Integer current_page,
                                                                   @PathVariable(value = "size")Integer size) {
        return observationService.getByNCTIDPage(concept_nctid, current_page, size);
    }

}

