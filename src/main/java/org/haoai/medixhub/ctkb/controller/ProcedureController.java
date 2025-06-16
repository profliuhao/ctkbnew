package org.haoai.medixhub.ctkb.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Procedure;
import org.haoai.medixhub.ctkb.service.ProcedureService;
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
@RequestMapping("/procedure")
public class ProcedureController {


    @Autowired
    ProcedureService procedureService;

//    @GetMapping("/concept_id/{concept_id}")
//    public List<Procedure> searchProcedureByConceptID(@PathVariable(value = "concept_id") String concept_id){
//        return procedureService.getByConceptID(concept_id);
//    }

    @GetMapping("/page/concept_id/{concept_id}")
    public IPage<Procedure> searchProcedureByConceptIDPage(@PathVariable(value = "concept_id") String concept_id) {
        return procedureService.getByConceptIDPage(concept_id);
    }

    @GetMapping("/page/concept_id/{concept_id}/{current_page}/{size}")
    public IPage<Procedure> searchProcedureByConceptIDPageWithSize(@PathVariable(value = "concept_id") String concept_id,
                                                                   @PathVariable(value = "current_page")Integer current_page,
                                                                   @PathVariable(value = "size")Integer size) {
        return procedureService.getByConceptIDPage(concept_id, current_page, size);
    }

//    @GetMapping("/concept_name/{concept_name}")
//    public List<Procedure> searchProcedureByConceptName(@PathVariable(value = "concept_name") String concept_name){
//        return procedureService.getByConceptName(concept_name);
//    }

    @GetMapping("/page/concept_name/{concept_name}")
    public IPage<Procedure> searchProcedureByConceptNamePage(@PathVariable(value = "concept_name") String concept_name) {
        return procedureService.getByConceptNamePage(concept_name);
    }

    @GetMapping("/page/concept_name/{concept_name}/{current_page}/{size}")
    public IPage<Procedure> searchProcedureByConceptNamePageWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                     @PathVariable(value = "current_page")Integer current_page,
                                                                     @PathVariable(value = "size")Integer size) {
        return procedureService.getByConceptNamePage(concept_name, current_page, size);
    }

//    @GetMapping("/concept_nctid/{concept_nctid}")
//    public List<Procedure> searchProcedureByNCTID(@PathVariable(value = "concept_nctid") String concept_nctid){
//        return procedureService.getByNCTID(concept_nctid);
//    }

    @GetMapping("/page/concept_nctid/{concept_nctid}")
    public IPage<Procedure> searchProcedureByNCTIDNamePage(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return procedureService.getByNCTIDPage(concept_nctid);
    }

    @GetMapping("/page/concept_nctid/{concept_nctid}/{current_page}/{size}")
    public IPage<Procedure> searchProcedureByNCTIDPageWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                               @PathVariable(value = "current_page")Integer current_page,
                                                               @PathVariable(value = "size")Integer size) {
        return procedureService.getByNCTIDPage(concept_nctid, current_page, size);
    }

}

