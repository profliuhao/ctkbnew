package org.haoai.medixhub.ctkb.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Drug;
import org.haoai.medixhub.ctkb.service.DrugService;
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
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;

//    @GetMapping("/concept_id/{concept_id}")
//    public List<Drug> searchDrugByConceptID(@PathVariable(value = "concept_id") String concept_id){
//        return drugService.getByConceptID(concept_id);
//    }

    @GetMapping("/page/concept_id/{concept_id}")
    public IPage<Drug> searchDrugByConceptIDPage(@PathVariable(value = "concept_id") String concept_id) {
        return drugService.getByConceptIDPage(concept_id);
    }

    @GetMapping("/page/concept_id/{concept_id}/{current_page}/{size}")
    public IPage<Drug> searchDrugByConceptIDPageWithSize(@PathVariable(value = "concept_id") String concept_id,
                                                                   @PathVariable(value = "current_page")Integer current_page,
                                                                   @PathVariable(value = "size")Integer size) {
        return drugService.getByConceptIDPage(concept_id, current_page, size);
    }

//    @GetMapping("/concept_name/{concept_name}")
//    public List<Drug> searchDrugByConceptName(@PathVariable(value = "concept_name") String concept_name){
//        return drugService.getByConceptName(concept_name);
//    }

    @GetMapping("/page/concept_name/{concept_name}")
    public IPage<Drug> searchDrugByConceptNamePage(@PathVariable(value = "concept_name") String concept_name) {
        return drugService.getByConceptNamePage(concept_name);
    }

    @GetMapping("/page/concept_name/{concept_name}/{current_page}/{size}")
    public IPage<Drug> searchDrugByConceptNamePageWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                     @PathVariable(value = "current_page")Integer current_page,
                                                                     @PathVariable(value = "size")Integer size) {
        return drugService.getByConceptNamePage(concept_name, current_page, size);
    }

//    @GetMapping("/concept_nctid/{concept_nctid}")
//    public List<Drug> searchDrugByNCTID(@PathVariable(value = "concept_nctid") String concept_nctid){
//        return drugService.getByNCTID(concept_nctid);
//    }

    @GetMapping("/page/concept_nctid/{concept_nctid}")
    public IPage<Drug> searchDrugByNCTIDNamePage(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return drugService.getByNCTIDPage(concept_nctid);
    }

    @GetMapping("/page/concept_nctid/{concept_nctid}/{current_page}/{size}")
    public IPage<Drug> searchDrugByNCTIDPageWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                               @PathVariable(value = "current_page")Integer current_page,
                                                               @PathVariable(value = "size")Integer size) {
        return drugService.getByNCTIDPage(concept_nctid, current_page, size);
    }

}

