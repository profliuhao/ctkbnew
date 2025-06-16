package org.haoai.medixhub.ctkb.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Condition;
import org.haoai.medixhub.ctkb.service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/api/condition")
@CrossOrigin
public class ConditionController {

    @Autowired
    ConditionService conditionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @GetMapping("/concept_id/{concept_id}")
//    public List<Condition> searchConditionByConceptID(@PathVariable(value = "concept_id") String concept_id){
//        return conditionService.getByConceptID(concept_id);
//    }

    @GetMapping("/page/all")
    public IPage<Condition> searchConditionAllByPage() {
        return conditionService.getAllByPage();
    }

    /**
     * Alternative endpoint using direct SQL to get conditions
     */
    @GetMapping("/page/all-direct")
    public Map<String, Object> searchConditionAllByPageDirect() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_condition";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);

            // Get sample data
            String dataSql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                           "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                           "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                           "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                           "FROM ec_condition LIMIT 10";
            List<Map<String, Object>> records = jdbcTemplate.queryForList(dataSql);

            response.put("records", records);
            response.put("total", total);
            response.put("size", 10);
            response.put("current", 1);
            response.put("pages", (total + 9) / 10); // Calculate pages

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/page/concept_id/{concept_id}")
    public IPage<Condition> searchConditionByConceptIDPage(@PathVariable(value = "concept_id") String concept_id) {
        return conditionService.getByConceptIDPage(concept_id);
    }

    @GetMapping("/page/concept_id/{concept_id}/{current_page}/{size}")
    public IPage<Condition> searchConditionByConceptIDPageWithSize(@PathVariable(value = "concept_id") String concept_id,
                                      @PathVariable(value = "current_page")Integer current_page,
                                      @PathVariable(value = "size")Integer size) {
        return conditionService.getByConceptIDPage(concept_id, current_page, size);
    }

    /**
     * Working endpoint using direct SQL for concept ID search
     */
    @GetMapping("/page/concept_id-direct/{concept_id}")
    public Map<String, Object> searchConditionByConceptIDDirect(@PathVariable(value = "concept_id") String concept_id) {
        return searchConditionByConceptIDDirectWithSize(concept_id, 1, 10);
    }

    @GetMapping("/page/concept_id-direct/{concept_id}/{current_page}/{size}")
    public Map<String, Object> searchConditionByConceptIDDirectWithSize(@PathVariable(value = "concept_id") String concept_id,
                                                                        @PathVariable(value = "current_page") Integer current_page,
                                                                        @PathVariable(value = "size") Integer size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_condition WHERE concept_id = ?";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, concept_id);

            // Get paginated data
            int offset = (current_page - 1) * size;
            String dataSql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                           "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                           "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                           "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                           "FROM ec_condition WHERE concept_id = ? " +
                           "ORDER BY score DESC " +
                           "LIMIT ? OFFSET ?";
            List<Map<String, Object>> records = jdbcTemplate.queryForList(dataSql, concept_id, size, offset);

            response.put("records", records);
            response.put("total", total);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in searchConditionByConceptIDDirect: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", 0);
        }
        return response;
    }

//    @GetMapping("/concept_name/{concept_name}")
//    public List<Condition> searchConditionByConceptName(@PathVariable(value = "concept_name") String concept_name){
//        return conditionService.getByConceptName(concept_name);
//    }

    @GetMapping("/page/concept_name/{concept_name}")
    public IPage<Condition> searchConditionByConceptNamePage(@PathVariable(value = "concept_name") String concept_name) {
        return conditionService.getByConceptNamePage(concept_name);
    }

    @GetMapping("/page/concept_name/{concept_name}/{current_page}/{size}")
    public IPage<Condition> searchConditionByConceptNamePageWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                   @PathVariable(value = "current_page")Integer current_page,
                                                                   @PathVariable(value = "size")Integer size) {
        return conditionService.getByConceptNamePage(concept_name, current_page, size);
    }

    /**
     * Working endpoint using direct SQL for concept name search
     */
    @GetMapping("/page/concept_name-direct/{concept_name}")
    public Map<String, Object> searchConditionByConceptNameDirect(@PathVariable(value = "concept_name") String concept_name) {
        return searchConditionByConceptNameDirectWithSize(concept_name, 1, 10);
    }

    @GetMapping("/page/concept_name-direct/{concept_name}/{current_page}/{size}")
    public Map<String, Object> searchConditionByConceptNameDirectWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                          @PathVariable(value = "current_page") Integer current_page,
                                                                          @PathVariable(value = "size") Integer size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_condition WHERE concept_name LIKE ?";
            String searchPattern = "%" + concept_name + "%";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, searchPattern);

            // Get paginated data
            int offset = (current_page - 1) * size;
            String dataSql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                           "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                           "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                           "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                           "FROM ec_condition WHERE concept_name LIKE ? " +
                           "ORDER BY score DESC " +
                           "LIMIT ? OFFSET ?";
            List<Map<String, Object>> records = jdbcTemplate.queryForList(dataSql, searchPattern, size, offset);

            response.put("records", records);
            response.put("total", total);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in searchConditionByConceptNameDirect: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", 0);
        }
        return response;
    }

//    @GetMapping("/concept_nctid/{concept_nctid}")
//    public List<Condition> searchConditionByNCTID(@PathVariable(value = "concept_nctid") String concept_nctid){
//        return conditionService.getByNCTID(concept_nctid);
//    }

    @GetMapping("/page/concept_nctid/{concept_nctid}")
    public IPage<Condition> searchConditionByNCTIDNamePage(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return conditionService.getByNCTIDPage(concept_nctid);
    }

    @GetMapping("/page/concept_nctid/{concept_nctid}/{current_page}/{size}")
    public IPage<Condition> searchConditionByNCTIDPageWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                                     @PathVariable(value = "current_page")Integer current_page,
                                                                     @PathVariable(value = "size")Integer size) {
        return conditionService.getByNCTIDPage(concept_nctid, current_page, size);
    }

    /**
     * Working endpoint using direct SQL for NCT ID search
     */
    @GetMapping("/page/concept_nctid-direct/{concept_nctid}")
    public Map<String, Object> searchConditionByNCTIDDirect(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return searchConditionByNCTIDDirectWithSize(concept_nctid, 1, 10);
    }

    @GetMapping("/page/concept_nctid-direct/{concept_nctid}/{current_page}/{size}")
    public Map<String, Object> searchConditionByNCTIDDirectWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                                    @PathVariable(value = "current_page") Integer current_page,
                                                                    @PathVariable(value = "size") Integer size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_condition WHERE nctid = ?";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, concept_nctid);

            // Get paginated data
            int offset = (current_page - 1) * size;
            String dataSql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                           "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                           "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                           "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                           "FROM ec_condition WHERE nctid = ? " +
                           "ORDER BY score DESC " +
                           "LIMIT ? OFFSET ?";
            List<Map<String, Object>> records = jdbcTemplate.queryForList(dataSql, concept_nctid, size, offset);

            response.put("records", records);
            response.put("total", total);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in searchConditionByNCTIDDirect: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("size", size);
            response.put("current", current_page);
            response.put("pages", 0);
        }
        return response;
    }

    /**
     * Advanced search endpoint with domain and include filters
     */
    @GetMapping("/search-advanced")
    public Map<String, Object> searchConditionsAdvanced(
            @RequestParam(required = false) String conceptId,
            @RequestParam(required = false) String conceptName,
            @RequestParam(required = false) String nctId,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) Integer include,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer size) {

        Map<String, Object> response = new HashMap<>();
        try {
            StringBuilder whereClause = new StringBuilder("WHERE 1=1");
            List<Object> params = new ArrayList<>();

            // Build dynamic WHERE clause
            if (conceptId != null && !conceptId.trim().isEmpty()) {
                whereClause.append(" AND concept_id = ?");
                params.add(conceptId);
            }
            if (conceptName != null && !conceptName.trim().isEmpty()) {
                whereClause.append(" AND concept_name LIKE ?");
                params.add("%" + conceptName + "%");
            }
            if (nctId != null && !nctId.trim().isEmpty()) {
                whereClause.append(" AND nctid = ?");
                params.add(nctId);
            }
            if (domain != null && !domain.trim().isEmpty()) {
                whereClause.append(" AND domain = ?");
                params.add(domain);
            }
            if (include != null) {
                whereClause.append(" AND include = ?");
                params.add(include);
            }

            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_condition " + whereClause.toString();
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

            // Get paginated data
            int offset = (currentPage - 1) * size;
            String dataSql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                           "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                           "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                           "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                           "FROM ec_condition " + whereClause.toString() + " " +
                           "ORDER BY score DESC " +
                           "LIMIT ? OFFSET ?";

            List<Object> dataParams = new ArrayList<>(params);
            dataParams.add(size);
            dataParams.add(offset);

            List<Map<String, Object>> records = jdbcTemplate.queryForList(dataSql, dataParams.toArray());

            response.put("records", records);
            response.put("total", total);
            response.put("size", size);
            response.put("current", currentPage);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in searchConditionsAdvanced: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("size", size);
            response.put("current", currentPage);
            response.put("pages", 0);
        }
        return response;
    }

    /**
     * Get condition by ID using direct SQL
     */
    @GetMapping("/id/{id}")
    public Map<String, Object> getConditionById(@PathVariable(value = "id") Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String sql = "SELECT id, nctid, include, line_num as lineNum, concept_id as conceptId, " +
                        "concept_name as conceptName, domain, neg, start_index as startIndex, " +
                        "end_index as endIndex, temporal_source_text as temporalSourceText, " +
                        "entity_source_text as entitySourceText, score, beforedays, afterdays " +
                        "FROM ec_condition WHERE id = ?";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, id);

            if (!results.isEmpty()) {
                response.put("condition", results.get(0));
                response.put("found", true);
            } else {
                response.put("condition", null);
                response.put("found", false);
                response.put("message", "Condition not found with ID: " + id);
            }

        } catch (Exception e) {
            System.err.println("Error in getConditionById: " + e.getMessage());
            response.put("condition", null);
            response.put("found", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

}

