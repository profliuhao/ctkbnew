package org.haoai.medixhub.ctkb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Advanced Analytics Service
 * Provides complex analytical insights and data mining capabilities
 */
@Service
public class AnalyticsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get trial timeline analysis
     */
    public Map<String, Object> getTrialTimelineAnalysis(String conditionId, String startYear, String endYear) {
        String sql;
        List<Map<String, Object>> results;
        
        if (conditionId != null && !conditionId.isEmpty()) {
            sql = "SELECT DATE_FORMAT(ti.start_date, '%Y-%m') as month, " +
                  "COUNT(DISTINCT ti.nct_id) as trial_count, " +
                  "AVG(DATEDIFF(ti.completion_date, ti.start_date)) as avg_duration " +
                  "FROM ctgov_trial_info ti " +
                  "JOIN ctgov_trial_condition tc ON ti.nct_id = tc.nct_id " +
                  "WHERE tc.condition_concept_id = ? " +
                  "AND YEAR(ti.start_date) BETWEEN ? AND ? " +
                  "AND ti.start_date IS NOT NULL " +
                  "GROUP BY DATE_FORMAT(ti.start_date, '%Y-%m') " +
                  "ORDER BY month";
            results = jdbcTemplate.queryForList(sql, conditionId, startYear, endYear);
        } else {
            sql = "SELECT DATE_FORMAT(start_date, '%Y-%m') as month, " +
                  "COUNT(*) as trial_count, " +
                  "AVG(DATEDIFF(completion_date, start_date)) as avg_duration " +
                  "FROM ctgov_trial_info " +
                  "WHERE YEAR(start_date) BETWEEN ? AND ? " +
                  "AND start_date IS NOT NULL " +
                  "GROUP BY DATE_FORMAT(start_date, '%Y-%m') " +
                  "ORDER BY month";
            results = jdbcTemplate.queryForList(sql, startYear, endYear);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("timeline", results);
        response.put("conditionId", conditionId);
        
        return response;
    }

    /**
     * Get criteria correlation matrix
     */
    public Map<String, Object> getCriteriaCorrelationMatrix(String domain, int limit) {
        String sql = "SELECT c1.concept_name as criteria1, c2.concept_name as criteria2, " +
                    "COUNT(DISTINCT c1.nctid) as cooccurrence_count " +
                    "FROM ec_all_criteria c1 " +
                    "JOIN ec_all_criteria c2 ON c1.nctid = c2.nctid " +
                    "WHERE c1.concept_id != c2.concept_id " +
                    (domain != null ? "AND c1.domain = ? AND c2.domain = ? " : "") +
                    "GROUP BY c1.concept_name, c2.concept_name " +
                    "HAVING cooccurrence_count > 5 " +
                    "ORDER BY cooccurrence_count DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> results;
        if (domain != null) {
            results = jdbcTemplate.queryForList(sql, domain, domain, limit);
        } else {
            results = jdbcTemplate.queryForList(sql, limit);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("correlations", results);
        response.put("domain", domain);
        
        return response;
    }

    /**
     * Get phase progression analysis
     */
    public Map<String, Object> getPhaseProgressionAnalysis(String conditionId) {
        String sql;
        List<Map<String, Object>> results;
        
        if (conditionId != null && !conditionId.isEmpty()) {
            sql = "SELECT ti.phase, COUNT(*) as count, " +
                  "AVG(DATEDIFF(ti.completion_date, ti.start_date)) as avg_duration " +
                  "FROM ctgov_trial_info ti " +
                  "JOIN ctgov_trial_condition tc ON ti.nct_id = tc.nct_id " +
                  "WHERE tc.condition_concept_id = ? " +
                  "AND ti.phase IS NOT NULL " +
                  "GROUP BY ti.phase " +
                  "ORDER BY ti.phase";
            results = jdbcTemplate.queryForList(sql, conditionId);
        } else {
            sql = "SELECT phase, COUNT(*) as count, " +
                  "AVG(DATEDIFF(completion_date, start_date)) as avg_duration " +
                  "FROM ctgov_trial_info " +
                  "WHERE phase IS NOT NULL " +
                  "GROUP BY phase " +
                  "ORDER BY phase";
            results = jdbcTemplate.queryForList(sql);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("phaseData", results);
        response.put("conditionId", conditionId);
        
        return response;
    }

    /**
     * Get criteria complexity analysis
     */
    public Map<String, Object> getCriteriaComplexityAnalysis() {
        // Analyze criteria complexity based on number of criteria per trial
        String complexitySql = "SELECT nctid, COUNT(*) as criteria_count " +
                              "FROM ec_all_criteria " +
                              "GROUP BY nctid";
        
        List<Map<String, Object>> complexityResults = jdbcTemplate.queryForList(complexitySql);
        
        // Calculate complexity distribution
        Map<String, Integer> complexityDistribution = new HashMap<>();
        for (Map<String, Object> row : complexityResults) {
            Integer count = ((Number) row.get("criteria_count")).intValue();
            String range;
            if (count <= 5) range = "1-5";
            else if (count <= 10) range = "6-10";
            else if (count <= 20) range = "11-20";
            else range = "20+";
            
            complexityDistribution.put(range, complexityDistribution.getOrDefault(range, 0) + 1);
        }
        
        // Get average criteria per domain
        String domainSql = "SELECT domain, AVG(criteria_per_trial) as avg_criteria " +
                          "FROM (SELECT nctid, domain, COUNT(*) as criteria_per_trial " +
                          "FROM ec_all_criteria " +
                          "WHERE domain IS NOT NULL " +
                          "GROUP BY nctid, domain) as domain_counts " +
                          "GROUP BY domain";
        
        List<Map<String, Object>> domainResults = jdbcTemplate.queryForList(domainSql);
        
        Map<String, Object> response = new HashMap<>();
        response.put("complexityDistribution", complexityDistribution);
        response.put("domainComplexity", domainResults);
        
        return response;
    }

    /**
     * Get condition similarity analysis
     */
    public Map<String, Object> getConditionSimilarityAnalysis(String conditionId, int limit) {
        String sql = "SELECT cc2.condition_concept_id, cc2.condition_concept_name, " +
                    "COUNT(DISTINCT cc1.criteria_concept_id) as shared_criteria " +
                    "FROM ec_common_condition cc1 " +
                    "JOIN ec_common_condition cc2 ON cc1.criteria_concept_id = cc2.criteria_concept_id " +
                    "WHERE cc1.condition_concept_id = ? " +
                    "AND cc2.condition_concept_id != ? " +
                    "GROUP BY cc2.condition_concept_id, cc2.condition_concept_name " +
                    "ORDER BY shared_criteria DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, conditionId, conditionId, limit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("similarConditions", results);
        response.put("baseConditionId", conditionId);
        
        return response;
    }

    /**
     * Get intervention patterns
     */
    public Map<String, Object> getInterventionPatterns(String interventionId) {
        String sql;
        List<Map<String, Object>> results;
        
        if (interventionId != null && !interventionId.isEmpty()) {
            sql = "SELECT ti.phase, ti.overall_status, COUNT(*) as count " +
                  "FROM ctgov_trial_info ti " +
                  "JOIN ctgov_trial_intervention tint ON ti.nct_id = tint.nct_id " +
                  "WHERE tint.intervention_concept_id = ? " +
                  "GROUP BY ti.phase, ti.overall_status " +
                  "ORDER BY ti.phase, count DESC";
            results = jdbcTemplate.queryForList(sql, interventionId);
        } else {
            sql = "SELECT tint.intervention_concept_id, tint.mesh_term, " +
                  "COUNT(DISTINCT ti.nct_id) as trial_count, " +
                  "COUNT(CASE WHEN ti.overall_status = 'Completed' THEN 1 END) as completed_trials " +
                  "FROM ctgov_trial_intervention tint " +
                  "JOIN ctgov_trial_info ti ON tint.nct_id = ti.nct_id " +
                  "WHERE tint.intervention_concept_id IS NOT NULL " +
                  "GROUP BY tint.intervention_concept_id, tint.mesh_term " +
                  "ORDER BY trial_count DESC " +
                  "LIMIT 20";
            results = jdbcTemplate.queryForList(sql);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("patterns", results);
        response.put("interventionId", interventionId);
        
        return response;
    }

    /**
     * Get measurement distributions
     */
    public Map<String, Object> getMeasurementDistributions(String conceptId) {
        String sql;
        List<Map<String, Object>> results;
        
        if (conceptId != null && !conceptId.isEmpty()) {
            sql = "SELECT min, max, unit, COUNT(*) as frequency " +
                  "FROM ec_measurement " +
                  "WHERE concept_id = ? " +
                  "AND min IS NOT NULL AND max IS NOT NULL " +
                  "GROUP BY min, max, unit " +
                  "ORDER BY frequency DESC";
            results = jdbcTemplate.queryForList(sql, conceptId);
        } else {
            sql = "SELECT concept_id, concept_name, " +
                  "MIN(min) as min_value, MAX(max) as max_value, " +
                  "AVG(min) as avg_min, AVG(max) as avg_max, " +
                  "COUNT(*) as measurement_count " +
                  "FROM ec_measurement " +
                  "WHERE min IS NOT NULL AND max IS NOT NULL " +
                  "GROUP BY concept_id, concept_name " +
                  "HAVING measurement_count > 5 " +
                  "ORDER BY measurement_count DESC " +
                  "LIMIT 20";
            results = jdbcTemplate.queryForList(sql);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("distributions", results);
        response.put("conceptId", conceptId);
        
        return response;
    }

    /**
     * Get temporal criteria patterns
     */
    public Map<String, Object> getTemporalCriteriaPatterns(String domain) {
        // This would require temporal data - using trial start dates as proxy
        String sql = "SELECT YEAR(ti.start_date) as year, " +
                    "ac.concept_name, COUNT(*) as usage_count " +
                    "FROM ec_all_criteria ac " +
                    "JOIN ctgov_trial_info ti ON ac.nctid = ti.nct_id " +
                    "WHERE ac.domain = ? " +
                    "AND ti.start_date IS NOT NULL " +
                    "AND YEAR(ti.start_date) >= 2020 " +
                    "GROUP BY YEAR(ti.start_date), ac.concept_name " +
                    "HAVING usage_count > 2 " +
                    "ORDER BY year, usage_count DESC";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, domain);
        
        Map<String, Object> response = new HashMap<>();
        response.put("temporalPatterns", results);
        response.put("domain", domain);
        
        return response;
    }

    /**
     * Get geographic distribution (placeholder - would need location data)
     */
    public Map<String, Object> getGeographicDistribution() {
        // Placeholder implementation - would need location/facility data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Geographic data not available in current schema");
        response.put("countries", new ArrayList<>());
        
        return response;
    }

    /**
     * Get predictive insights
     */
    public Map<String, Object> getPredictiveInsights(String conditionId) {
        // Simple predictive analysis based on historical completion rates
        String sql = "SELECT ti.phase, " +
                    "COUNT(*) as total_trials, " +
                    "COUNT(CASE WHEN ti.overall_status = 'Completed' THEN 1 END) as completed_trials, " +
                    "ROUND(COUNT(CASE WHEN ti.overall_status = 'Completed' THEN 1 END) * 100.0 / COUNT(*), 2) as completion_rate " +
                    "FROM ctgov_trial_info ti ";
        
        if (conditionId != null && !conditionId.isEmpty()) {
            sql += "JOIN ctgov_trial_condition tc ON ti.nct_id = tc.nct_id " +
                   "WHERE tc.condition_concept_id = ? ";
        } else {
            sql += "WHERE 1=1 ";
        }
        
        sql += "AND ti.phase IS NOT NULL " +
               "GROUP BY ti.phase " +
               "ORDER BY ti.phase";
        
        List<Map<String, Object>> results;
        if (conditionId != null && !conditionId.isEmpty()) {
            results = jdbcTemplate.queryForList(sql, conditionId);
        } else {
            results = jdbcTemplate.queryForList(sql);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("completionRates", results);
        response.put("conditionId", conditionId);
        
        return response;
    }

    /**
     * Get criteria trends over time
     */
    public Map<String, Object> getCriteriaTrends(String criteriaId, int months) {
        String sql = "SELECT DATE_FORMAT(ti.start_date, '%Y-%m') as month, " +
                    "COUNT(DISTINCT ac.nctid) as usage_count " +
                    "FROM ec_all_criteria ac " +
                    "JOIN ctgov_trial_info ti ON ac.nctid = ti.nct_id " +
                    "WHERE ti.start_date >= DATE_SUB(NOW(), INTERVAL ? MONTH) ";
        
        if (criteriaId != null && !criteriaId.isEmpty()) {
            sql += "AND ac.concept_id = ? ";
        }
        
        sql += "GROUP BY DATE_FORMAT(ti.start_date, '%Y-%m') " +
               "ORDER BY month";
        
        List<Map<String, Object>> results;
        if (criteriaId != null && !criteriaId.isEmpty()) {
            results = jdbcTemplate.queryForList(sql, months, criteriaId);
        } else {
            results = jdbcTemplate.queryForList(sql, months);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("trends", results);
        response.put("criteriaId", criteriaId);
        response.put("months", months);
        
        return response;
    }

    /**
     * Get recruitment patterns
     */
    public Map<String, Object> getRecruitmentPatterns() {
        String sql = "SELECT overall_status, " +
                    "COUNT(*) as trial_count, " +
                    "AVG(DATEDIFF(COALESCE(completion_date, NOW()), start_date)) as avg_duration " +
                    "FROM ctgov_trial_info " +
                    "WHERE start_date IS NOT NULL " +
                    "GROUP BY overall_status " +
                    "ORDER BY trial_count DESC";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        
        Map<String, Object> response = new HashMap<>();
        response.put("recruitmentPatterns", results);
        
        return response;
    }

    /**
     * Get condition co-occurrence analysis
     */
    public Map<String, Object> getConditionCooccurrenceAnalysis(String conditionId, int limit) {
        String sql = "SELECT tc2.condition_concept_id, tc2.mesh_term, " +
                    "COUNT(DISTINCT tc1.nct_id) as cooccurrence_count " +
                    "FROM ctgov_trial_condition tc1 " +
                    "JOIN ctgov_trial_condition tc2 ON tc1.nct_id = tc2.nct_id " +
                    "WHERE tc1.condition_concept_id = ? " +
                    "AND tc2.condition_concept_id != ? " +
                    "GROUP BY tc2.condition_concept_id, tc2.mesh_term " +
                    "ORDER BY cooccurrence_count DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, conditionId, conditionId, limit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cooccurringConditions", results);
        response.put("baseConditionId", conditionId);
        
        return response;
    }

    /**
     * Get search suggestions
     */
    public List<Map<String, Object>> getSearchSuggestions(String query, String type) {
        String sql;
        String searchPattern = "%" + query.toLowerCase() + "%";
        
        switch (type.toLowerCase()) {
            case "condition":
                sql = "SELECT DISTINCT condition_concept_id as id, mesh_term as name " +
                      "FROM ctgov_trial_condition " +
                      "WHERE LOWER(mesh_term) LIKE ? " +
                      "ORDER BY mesh_term " +
                      "LIMIT 10";
                break;
            case "intervention":
                sql = "SELECT DISTINCT intervention_concept_id as id, mesh_term as name " +
                      "FROM ctgov_trial_intervention " +
                      "WHERE LOWER(mesh_term) LIKE ? " +
                      "ORDER BY mesh_term " +
                      "LIMIT 10";
                break;
            default:
                sql = "SELECT DISTINCT concept_id as id, concept_name as name " +
                      "FROM ec_all_criteria " +
                      "WHERE LOWER(concept_name) LIKE ? " +
                      "ORDER BY concept_name " +
                      "LIMIT 10";
        }
        
        return jdbcTemplate.queryForList(sql, searchPattern);
    }

    /**
     * Get data quality metrics
     */
    public Map<String, Object> getDataQualityMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Completeness metrics
        String trialCompleteness = "SELECT " +
                                  "COUNT(*) as total_trials, " +
                                  "COUNT(start_date) as trials_with_start_date, " +
                                  "COUNT(completion_date) as trials_with_completion_date, " +
                                  "COUNT(phase) as trials_with_phase " +
                                  "FROM ctgov_trial_info";
        
        Map<String, Object> completeness = jdbcTemplate.queryForMap(trialCompleteness);
        
        // Criteria quality
        String criteriaQuality = "SELECT " +
                                "COUNT(*) as total_criteria, " +
                                "COUNT(CASE WHEN concept_id != 'unmapped' THEN 1 END) as mapped_criteria, " +
                                "COUNT(score) as criteria_with_scores " +
                                "FROM ec_all_criteria";
        
        Map<String, Object> quality = jdbcTemplate.queryForMap(criteriaQuality);
        
        metrics.put("completeness", completeness);
        metrics.put("quality", quality);
        
        return metrics;
    }
}
