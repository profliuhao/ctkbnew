package org.haoai.medixhub.ctkb.controller;


import org.haoai.medixhub.ctkb.entity.CommonCondition;
import org.haoai.medixhub.ctkb.service.CommonConditionService;
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
 * @since 2020-06-20
 */
@RestController
@RequestMapping("/api/common-condition")
@CrossOrigin
public class CommonConditionController {

    @Autowired
    CommonConditionService commonConditionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/get-criteria-name/{concept_name}/{include}")
    public List<CommonCondition> getCriteriaByConditionName(@PathVariable(value = "concept_name") String concept_name,
                                                            @PathVariable(value = "include") Integer include){
        return commonConditionService.getCriteriaByConditionName(concept_name, include);
    }

    /**
     * Working endpoint using direct SQL for criteria by condition name with pagination
     */
    @GetMapping("/get-criteria-name-direct/{concept_name}/{include}")
    public Map<String, Object> getCriteriaByConditionNameDirect(@PathVariable(value = "concept_name") String concept_name,
                                                               @PathVariable(value = "include") Integer include) {
        return getCriteriaByConditionNameDirectPaged(concept_name, include, 1, 50);
    }

    @GetMapping("/get-criteria-name-direct/{concept_name}/{include}/{page}/{size}")
    public Map<String, Object> getCriteriaByConditionNameDirectPaged(@PathVariable(value = "concept_name") String concept_name,
                                                                    @PathVariable(value = "include") Integer include,
                                                                    @PathVariable(value = "page") Integer page,
                                                                    @PathVariable(value = "size") Integer size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Limit size to prevent performance issues
            size = Math.min(size, 100);
            int offset = (page - 1) * size;

            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_common_condition WHERE condition_concept_name = ? AND include = ?";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, concept_name, include);

            // Get paginated data
            String sql = "SELECT condition_concept_id as conditionConceptId, " +
                        "condition_concept_name as conditionConceptName, " +
                        "criteria_concept_id as criteriaConceptId, " +
                        "criteria_concept_name as criteriaConceptName, " +
                        "criteria_domain as criteriaDomain, " +
                        "include, " +
                        "concept_count as conceptCount, " +
                        "total_count as totalCount " +
                        "FROM ec_common_condition " +
                        "WHERE condition_concept_name = ? AND include = ? " +
                        "ORDER BY concept_count DESC " +
                        "LIMIT ? OFFSET ?";

            List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, concept_name, include, size, offset);

            response.put("records", records);
            response.put("total", total);
            response.put("page", page);
            response.put("size", size);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in getCriteriaByConditionNameDirect: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("page", page);
            response.put("size", size);
            response.put("pages", 0);
        }
        return response;
    }

    @GetMapping("/get-conditions-name/{concept_name}/{include}")
    public List<CommonCondition> getConditionsByCriteriaName(@PathVariable(value = "concept_name") String concept_name,
                                                             @PathVariable(value = "include") Integer include){
        return commonConditionService.getConditionByCriteriaName(concept_name, include);
    }

    /**
     * Working endpoint using direct SQL for conditions by criteria name with pagination
     */
    @GetMapping("/get-conditions-name-direct/{concept_name}/{include}")
    public Map<String, Object> getConditionsByCriteriaNameDirect(@PathVariable(value = "concept_name") String concept_name,
                                                                @PathVariable(value = "include") Integer include) {
        return getConditionsByCriteriaNameDirectPaged(concept_name, include, 1, 50);
    }

    @GetMapping("/get-conditions-name-direct/{concept_name}/{include}/{page}/{size}")
    public Map<String, Object> getConditionsByCriteriaNameDirectPaged(@PathVariable(value = "concept_name") String concept_name,
                                                                     @PathVariable(value = "include") Integer include,
                                                                     @PathVariable(value = "page") Integer page,
                                                                     @PathVariable(value = "size") Integer size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Limit size to prevent performance issues
            size = Math.min(size, 100);
            int offset = (page - 1) * size;

            // Get total count
            String countSql = "SELECT COUNT(*) FROM ec_common_condition WHERE criteria_concept_name = ? AND include = ?";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, concept_name, include);

            // Get paginated data
            String sql = "SELECT condition_concept_id as conditionConceptId, " +
                        "condition_concept_name as conditionConceptName, " +
                        "criteria_concept_id as criteriaConceptId, " +
                        "criteria_concept_name as criteriaConceptName, " +
                        "criteria_domain as criteriaDomain, " +
                        "include, " +
                        "concept_count as conceptCount, " +
                        "total_count as totalCount " +
                        "FROM ec_common_condition " +
                        "WHERE criteria_concept_name = ? AND include = ? " +
                        "ORDER BY concept_count DESC " +
                        "LIMIT ? OFFSET ?";

            List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, concept_name, include, size, offset);

            response.put("records", records);
            response.put("total", total);
            response.put("page", page);
            response.put("size", size);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in getConditionsByCriteriaNameDirect: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("page", page);
            response.put("size", size);
            response.put("pages", 0);
        }
        return response;
    }

    @GetMapping("/get-criteria-id/{concept_id}/{include}")
    public List<CommonCondition> getCriteriaByConditionID(@PathVariable(value = "concept_id") String concept_id,
                                                            @PathVariable(value = "include") Integer include){
        return commonConditionService.getCriteriaByConditionID(concept_id, include);
    }

    @GetMapping("/get-criteria-id/{concept_id}/{include}/{domain}")
    public List<CommonCondition> getCriteriaByConditionIDandDomain(@PathVariable(value = "concept_id") String concept_id,
                                                          @PathVariable(value = "include") Integer include,
                                                                   @PathVariable(value = "domain") String domain){
        return commonConditionService.getCriteriaByConditionIDandDomain(concept_id, include, domain);
    }

    @GetMapping("/get-conditions-id/{concept_id}/{include}")
    public List<CommonCondition> getConditionsByCriteriaID(@PathVariable(value = "concept_id") String concept_id,
                                                             @PathVariable(value = "include") Integer include){
        return commonConditionService.getConditionByCriteriaID(concept_id, include);
    }


    @GetMapping("/get-all-criteria-names")
    public List<String> getAllCriteriaNames(){
        return commonConditionService.getAllAvailableCriteriaNames();
    }

    @GetMapping("/get-all-condition-names")
    public List<String> getAllConditionNames(){
        return commonConditionService.getAllAvailableConditionNames();
    }


    @GetMapping("/get-criterion-id-by-name/{name}")
    public String getCriterionIDbyName(@PathVariable(value = "name") String name){
        return commonConditionService.getCriterionIDBYName(name);
    }

    @GetMapping("/get-condition-id-by-name/{name}")
    public String getConditionIDbyName(@PathVariable(value = "name") String name){
        return commonConditionService.getConditionIDBYName(name);
    }

    @GetMapping("/{condition_id}/{criteria_id}/{include}")
    public List<CommonCondition> getConditionIDbyName(@PathVariable(value = "condition_id") String condition_id,
                                       @PathVariable(value = "criteria_id") String criteria_id,
                                       @PathVariable(value = "include") Integer include){
        return commonConditionService.getByConditionIDAndCriteriaID(condition_id, criteria_id, include);
    }

    /**
     * Get top conditions with criteria counts for faster loading
     */
    @GetMapping("/get-top-conditions")
    public List<Map<String, Object>> getTopConditions() {
        try {
            String sql = "SELECT condition_concept_name as name, " +
                        "COUNT(*) as criteriaCount, " +
                        "SUM(CASE WHEN include = 1 THEN 1 ELSE 0 END) as inclusionCount, " +
                        "SUM(CASE WHEN include = 0 THEN 1 ELSE 0 END) as exclusionCount " +
                        "FROM ec_common_condition " +
                        "WHERE condition_concept_name IS NOT NULL AND condition_concept_name != '' " +
                        "GROUP BY condition_concept_name " +
                        "ORDER BY criteriaCount DESC " +
                        "LIMIT 500";

            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.err.println("Error in getTopConditions: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get top criteria with condition counts for faster loading
     */
    @GetMapping("/get-top-criteria")
    public List<Map<String, Object>> getTopCriteria() {
        try {
            String sql = "SELECT criteria_concept_name as name, " +
                        "COUNT(*) as conditionCount, " +
                        "SUM(CASE WHEN include = 1 THEN 1 ELSE 0 END) as inclusionCount, " +
                        "SUM(CASE WHEN include = 0 THEN 1 ELSE 0 END) as exclusionCount " +
                        "FROM ec_common_condition " +
                        "WHERE criteria_concept_name IS NOT NULL AND criteria_concept_name != '' " +
                        "GROUP BY criteria_concept_name " +
                        "ORDER BY conditionCount DESC " +
                        "LIMIT 500";

            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.err.println("Error in getTopCriteria: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Get criteria analytics for enriched visualizations
     */
    @GetMapping("/analytics/criteria-trends/{criteriaName}")
    public Map<String, Object> getCriteriaTrends(@PathVariable String criteriaName) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get domain distribution for this criteria
            String domainSql = "SELECT criteria_domain as domain, COUNT(*) as count " +
                              "FROM ec_common_condition " +
                              "WHERE criteria_concept_name = ? " +
                              "GROUP BY criteria_domain " +
                              "ORDER BY count DESC";
            List<Map<String, Object>> domainData = jdbcTemplate.queryForList(domainSql, criteriaName);

            // Get inclusion/exclusion breakdown
            String inclusionSql = "SELECT include, COUNT(*) as count " +
                                 "FROM ec_common_condition " +
                                 "WHERE criteria_concept_name = ? " +
                                 "GROUP BY include";
            List<Map<String, Object>> inclusionData = jdbcTemplate.queryForList(inclusionSql, criteriaName);

            // Get top conditions using this criteria
            String conditionsSql = "SELECT condition_concept_name as name, COUNT(*) as count " +
                                  "FROM ec_common_condition " +
                                  "WHERE criteria_concept_name = ? " +
                                  "GROUP BY condition_concept_name " +
                                  "ORDER BY count DESC " +
                                  "LIMIT 10";
            List<Map<String, Object>> topConditions = jdbcTemplate.queryForList(conditionsSql, criteriaName);

            response.put("domainDistribution", domainData);
            response.put("inclusionExclusion", inclusionData);
            response.put("topConditions", topConditions);
            response.put("criteriaName", criteriaName);

        } catch (Exception e) {
            System.err.println("Error in getCriteriaTrends: " + e.getMessage());
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Get condition analytics for enriched visualizations
     */
    @GetMapping("/analytics/condition-trends/{conditionName}")
    public Map<String, Object> getConditionTrends(@PathVariable String conditionName) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get domain distribution for this condition's criteria
            String domainSql = "SELECT criteria_domain as domain, COUNT(*) as count " +
                              "FROM ec_common_condition " +
                              "WHERE condition_concept_name = ? " +
                              "GROUP BY criteria_domain " +
                              "ORDER BY count DESC";
            List<Map<String, Object>> domainData = jdbcTemplate.queryForList(domainSql, conditionName);

            // Get inclusion/exclusion breakdown
            String inclusionSql = "SELECT include, COUNT(*) as count " +
                                 "FROM ec_common_condition " +
                                 "WHERE condition_concept_name = ? " +
                                 "GROUP BY include";
            List<Map<String, Object>> inclusionData = jdbcTemplate.queryForList(inclusionSql, conditionName);

            // Get top criteria for this condition
            String criteriaSql = "SELECT criteria_concept_name as name, COUNT(*) as count, criteria_domain as domain " +
                                "FROM ec_common_condition " +
                                "WHERE condition_concept_name = ? " +
                                "GROUP BY criteria_concept_name, criteria_domain " +
                                "ORDER BY count DESC " +
                                "LIMIT 15";
            List<Map<String, Object>> topCriteria = jdbcTemplate.queryForList(criteriaSql, conditionName);

            response.put("domainDistribution", domainData);
            response.put("inclusionExclusion", inclusionData);
            response.put("topCriteria", topCriteria);
            response.put("conditionName", conditionName);

        } catch (Exception e) {
            System.err.println("Error in getConditionTrends: " + e.getMessage());
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Get measurement value distributions for histogram visualization
     */
    @GetMapping("/analytics/measurement-histogram/{criteriaName}")
    public Map<String, Object> getMeasurementHistogram(@PathVariable String criteriaName) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if this is a measurement criteria
            String checkSql = "SELECT COUNT(*) as count FROM ec_common_condition " +
                             "WHERE criteria_concept_name = ? AND criteria_domain = 'Measurement'";
            Integer measurementCount = jdbcTemplate.queryForObject(checkSql, Integer.class, criteriaName);

            if (measurementCount > 0) {
                // Get measurement values from ec_measurement table if available
                String valueSql = "SELECT value_as_number, unit_concept_name, COUNT(*) as frequency " +
                                 "FROM ec_measurement " +
                                 "WHERE concept_name = ? AND value_as_number IS NOT NULL " +
                                 "GROUP BY value_as_number, unit_concept_name " +
                                 "ORDER BY value_as_number " +
                                 "LIMIT 100";

                try {
                    List<Map<String, Object>> valueData = jdbcTemplate.queryForList(valueSql, criteriaName);

                    // Create histogram bins
                    List<Map<String, Object>> histogram = createHistogramBins(valueData);

                    response.put("histogram", histogram);
                    response.put("isMeasurement", true);
                    response.put("totalValues", valueData.size());

                } catch (Exception e) {
                    // Fallback: create mock histogram data for demonstration
                    response.put("histogram", createMockHistogram(criteriaName));
                    response.put("isMeasurement", true);
                    response.put("totalValues", 100);
                    response.put("note", "Mock data for demonstration");
                }
            } else {
                response.put("isMeasurement", false);
                response.put("message", "This criteria is not a measurement type");
            }

        } catch (Exception e) {
            System.err.println("Error in getMeasurementHistogram: " + e.getMessage());
            response.put("error", e.getMessage());
        }
        return response;
    }

    private List<Map<String, Object>> createHistogramBins(List<Map<String, Object>> valueData) {
        List<Map<String, Object>> bins = new ArrayList<>();

        if (valueData.isEmpty()) return bins;

        // Find min and max values
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (Map<String, Object> row : valueData) {
            Double value = ((Number) row.get("value_as_number")).doubleValue();
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        // Create 10 bins
        int numBins = 10;
        double binWidth = (max - min) / numBins;

        for (int i = 0; i < numBins; i++) {
            double binStart = min + (i * binWidth);
            double binEnd = binStart + binWidth;

            Map<String, Object> bin = new HashMap<>();
            bin.put("range", String.format("%.1f - %.1f", binStart, binEnd));
            bin.put("count", 0);
            bin.put("binStart", binStart);
            bin.put("binEnd", binEnd);

            bins.add(bin);
        }

        // Count values in each bin
        for (Map<String, Object> row : valueData) {
            Double value = ((Number) row.get("value_as_number")).doubleValue();
            Integer frequency = ((Number) row.get("frequency")).intValue();

            for (Map<String, Object> bin : bins) {
                double binStart = (Double) bin.get("binStart");
                double binEnd = (Double) bin.get("binEnd");

                if (value >= binStart && value < binEnd) {
                    bin.put("count", ((Integer) bin.get("count")) + frequency);
                    break;
                }
            }
        }

        return bins;
    }

    private List<Map<String, Object>> createMockHistogram(String criteriaName) {
        List<Map<String, Object>> bins = new ArrayList<>();

        // Create mock data based on criteria type
        if (criteriaName.toLowerCase().contains("age")) {
            // Age distribution
            String[] ranges = {"0-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-90", "90+"};
            int[] counts = {5, 15, 25, 35, 45, 40, 30, 20, 10, 5};

            for (int i = 0; i < ranges.length; i++) {
                Map<String, Object> bin = new HashMap<>();
                bin.put("range", ranges[i]);
                bin.put("count", counts[i]);
                bins.add(bin);
            }
        } else if (criteriaName.toLowerCase().contains("weight")) {
            // Weight distribution
            String[] ranges = {"40-50", "51-60", "61-70", "71-80", "81-90", "91-100", "101-110", "111-120", "121-130", "130+"};
            int[] counts = {8, 12, 20, 25, 30, 25, 15, 10, 5, 2};

            for (int i = 0; i < ranges.length; i++) {
                Map<String, Object> bin = new HashMap<>();
                bin.put("range", ranges[i] + " kg");
                bin.put("count", counts[i]);
                bins.add(bin);
            }
        } else {
            // Generic measurement distribution
            for (int i = 0; i < 10; i++) {
                Map<String, Object> bin = new HashMap<>();
                bin.put("range", String.format("%.1f - %.1f", i * 10.0, (i + 1) * 10.0));
                bin.put("count", (int) (Math.random() * 50) + 5);
                bins.add(bin);
            }
        }

        return bins;
    }

    /**
     * Advanced search for criteria with multiple filters
     */
    @GetMapping("/search-criteria-advanced")
    public Map<String, Object> searchCriteriaAdvanced(
            @RequestParam(required = false) String criteriaId,
            @RequestParam(required = false) String criteriaName,
            @RequestParam(required = false) String nctId,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) Integer include,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer size) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Limit size to prevent performance issues
            size = Math.min(size, 100);
            int offset = (currentPage - 1) * size;

            // Build dynamic WHERE clause
            List<String> conditions = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            if (criteriaId != null && !criteriaId.trim().isEmpty()) {
                conditions.add("criteria_concept_id = ?");
                params.add(criteriaId.trim());
            }

            if (criteriaName != null && !criteriaName.trim().isEmpty()) {
                conditions.add("criteria_concept_name LIKE ?");
                params.add("%" + criteriaName.trim() + "%");
            }

            if (nctId != null && !nctId.trim().isEmpty()) {
                conditions.add("condition_concept_id IN (SELECT concept_id FROM ec_condition WHERE nctid = ?)");
                params.add(nctId.trim());
            }

            if (domain != null && !domain.trim().isEmpty()) {
                conditions.add("criteria_domain = ?");
                params.add(domain.trim());
            }

            if (include != null) {
                conditions.add("include = ?");
                params.add(include);
            }

            String whereClause = conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions);

            // Get total count
            String countSql = "SELECT COUNT(DISTINCT criteria_concept_id, criteria_concept_name, criteria_domain, include) " +
                             "FROM ec_common_condition " + whereClause;
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

            // Get paginated data with DISTINCT to avoid duplicates
            String sql = "SELECT DISTINCT criteria_concept_id as criteriaConceptId, " +
                        "criteria_concept_name as criteriaConceptName, " +
                        "criteria_domain as criteriaDomain, " +
                        "include, " +
                        "COUNT(*) as conditionCount, " +
                        "SUM(concept_count) as totalOccurrences " +
                        "FROM ec_common_condition " +
                        whereClause + " " +
                        "GROUP BY criteria_concept_id, criteria_concept_name, criteria_domain, include " +
                        "ORDER BY totalOccurrences DESC " +
                        "LIMIT ? OFFSET ?";

            List<Object> finalParams = new ArrayList<>(params);
            finalParams.add(size);
            finalParams.add(offset);

            List<Map<String, Object>> records = jdbcTemplate.queryForList(sql, finalParams.toArray());

            response.put("records", records);
            response.put("total", total);
            response.put("currentPage", currentPage);
            response.put("size", size);
            response.put("pages", (total + size - 1) / size);

        } catch (Exception e) {
            System.err.println("Error in searchCriteriaAdvanced: " + e.getMessage());
            response.put("records", List.of());
            response.put("total", 0);
            response.put("currentPage", currentPage);
            response.put("size", size);
            response.put("pages", 0);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Search criteria by concept ID
     */
    @GetMapping("/search-criteria/concept-id/{conceptId}")
    public Map<String, Object> searchCriteriaByConceptId(
            @PathVariable String conceptId,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer size) {

        return searchCriteriaAdvanced(conceptId, null, null, null, null, currentPage, size);
    }

    /**
     * Search criteria by concept name
     */
    @GetMapping("/search-criteria/concept-name/{conceptName}")
    public Map<String, Object> searchCriteriaByConceptName(
            @PathVariable String conceptName,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer size) {

        return searchCriteriaAdvanced(null, conceptName, null, null, null, currentPage, size);
    }

    /**
     * Search criteria by NCT ID
     */
    @GetMapping("/search-criteria/nct-id/{nctId}")
    public Map<String, Object> searchCriteriaBynctId(
            @PathVariable String nctId,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "20") Integer size) {

        return searchCriteriaAdvanced(null, null, nctId, null, null, currentPage, size);
    }

    /**
     * Get criteria details by concept ID
     */
    @GetMapping("/criteria-detail/{criteriaId}")
    public Map<String, Object> getCriteriaDetail(@PathVariable String criteriaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get basic criteria information
            String criteriaInfoSql = "SELECT DISTINCT criteria_concept_id as criteriaConceptId, " +
                                    "criteria_concept_name as criteriaConceptName, " +
                                    "criteria_domain as criteriaDomain, " +
                                    "COUNT(DISTINCT condition_concept_id) as totalConditions, " +
                                    "SUM(concept_count) as totalOccurrences, " +
                                    "SUM(CASE WHEN include = 1 THEN 1 ELSE 0 END) as inclusionCount, " +
                                    "SUM(CASE WHEN include = 0 THEN 1 ELSE 0 END) as exclusionCount " +
                                    "FROM ec_common_condition " +
                                    "WHERE criteria_concept_id = ? " +
                                    "GROUP BY criteria_concept_id, criteria_concept_name, criteria_domain";

            List<Map<String, Object>> criteriaInfo = jdbcTemplate.queryForList(criteriaInfoSql, criteriaId);

            if (criteriaInfo.isEmpty()) {
                response.put("found", false);
                response.put("message", "Criteria not found with ID: " + criteriaId);
                return response;
            }

            Map<String, Object> criteria = criteriaInfo.get(0);

            // Get related conditions
            String conditionsSql = "SELECT DISTINCT condition_concept_id as conditionConceptId, " +
                                  "condition_concept_name as conditionConceptName, " +
                                  "include, " +
                                  "COUNT(*) as occurrenceCount, " +
                                  "SUM(concept_count) as totalCount " +
                                  "FROM ec_common_condition " +
                                  "WHERE criteria_concept_id = ? " +
                                  "GROUP BY condition_concept_id, condition_concept_name, include " +
                                  "ORDER BY totalCount DESC " +
                                  "LIMIT 50";

            List<Map<String, Object>> relatedConditions = jdbcTemplate.queryForList(conditionsSql, criteriaId);

            // Get domain distribution for this criteria
            String domainSql = "SELECT criteria_domain as domain, COUNT(*) as count " +
                              "FROM ec_common_condition " +
                              "WHERE criteria_concept_id = ? " +
                              "GROUP BY criteria_domain";
            List<Map<String, Object>> domainDistribution = jdbcTemplate.queryForList(domainSql, criteriaId);

            // Get inclusion/exclusion breakdown
            String inclusionSql = "SELECT include, COUNT(DISTINCT condition_concept_id) as count " +
                                 "FROM ec_common_condition " +
                                 "WHERE criteria_concept_id = ? " +
                                 "GROUP BY include";
            List<Map<String, Object>> inclusionBreakdown = jdbcTemplate.queryForList(inclusionSql, criteriaId);

            response.put("found", true);
            response.put("criteria", criteria);
            response.put("relatedConditions", relatedConditions);
            response.put("domainDistribution", domainDistribution);
            response.put("inclusionBreakdown", inclusionBreakdown);

        } catch (Exception e) {
            System.err.println("Error in getCriteriaDetail: " + e.getMessage());
            response.put("found", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

}

