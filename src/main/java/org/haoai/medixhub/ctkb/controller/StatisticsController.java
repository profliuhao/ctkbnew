package org.haoai.medixhub.ctkb.controller;

import org.haoai.medixhub.ctkb.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Statistics and Analytics Controller
 * Provides aggregated data for dashboard and analytics visualizations
 */
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Test endpoint to verify controller is working
     */
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Statistics controller is working!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Test endpoint to check condition table
     */
    @GetMapping("/test-conditions")
    public Map<String, Object> testConditions() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test basic count
            String countSql = "SELECT COUNT(*) as count FROM ec_condition LIMIT 1";
            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
            response.put("conditionCount", count);

            // Test sample data
            String sampleSql = "SELECT * FROM ec_condition LIMIT 3";
            List<Map<String, Object>> sampleData = jdbcTemplate.queryForList(sampleSql);
            response.put("sampleData", sampleData);

            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Test endpoint to check common condition table
     */
    @GetMapping("/test-common-conditions")
    public Map<String, Object> testCommonConditions() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test basic count
            String countSql = "SELECT COUNT(*) as count FROM ec_common_condition LIMIT 1";
            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
            response.put("commonConditionCount", count);

            // Test sample data
            String sampleSql = "SELECT * FROM ec_common_condition LIMIT 3";
            List<Map<String, Object>> sampleData = jdbcTemplate.queryForList(sampleSql);
            response.put("sampleData", sampleData);

            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Get dashboard summary statistics
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        return statisticsService.getDashboardStatistics();
    }

    /**
     * Get domain distribution for pie charts
     */
    @GetMapping("/domain-distribution")
    public Map<String, Object> getDomainDistribution() {
        return statisticsService.getDomainDistribution();
    }

    /**
     * Get inclusion vs exclusion statistics
     */
    @GetMapping("/inclusion-exclusion")
    public Map<String, Object> getInclusionExclusionStats() {
        return statisticsService.getInclusionExclusionStats();
    }

    /**
     * Get trial phase distribution
     */
    @GetMapping("/phase-distribution")
    public Map<String, Object> getPhaseDistribution() {
        return statisticsService.getPhaseDistribution();
    }

    /**
     * Get temporal trends (trials over time)
     */
    @GetMapping("/temporal-trends")
    public Map<String, Object> getTemporalTrends(
            @RequestParam(defaultValue = "2020") String startYear,
            @RequestParam(defaultValue = "2024") String endYear) {
        return statisticsService.getTemporalTrends(startYear, endYear);
    }

    /**
     * Get top conditions by trial count
     */
    @GetMapping("/top-conditions")
    public List<Map<String, Object>> getTopConditions(
            @RequestParam(defaultValue = "10") int limit) {
        return statisticsService.getTopConditions(limit);
    }

    /**
     * Get top criteria by usage
     */
    @GetMapping("/top-criteria")
    public List<Map<String, Object>> getTopCriteria(
            @RequestParam(defaultValue = "10") int limit) {
        return statisticsService.getTopCriteria(limit);
    }

    /**
     * Get condition-criteria relationship data for network graphs
     */
    @GetMapping("/condition-criteria-relationships")
    public Map<String, Object> getConditionCriteriaRelationships(
            @RequestParam(required = false) String conditionId,
            @RequestParam(defaultValue = "50") int limit) {
        return statisticsService.getConditionCriteriaRelationships(conditionId, limit);
    }

    /**
     * Get measurement statistics (min, max, avg values)
     */
    @GetMapping("/measurement-stats")
    public Map<String, Object> getMeasurementStats(
            @RequestParam(required = false) String conceptId) {
        return statisticsService.getMeasurementStatistics(conceptId);
    }

    /**
     * Get trial status distribution
     */
    @GetMapping("/trial-status")
    public Map<String, Object> getTrialStatusDistribution() {
        return statisticsService.getTrialStatusDistribution();
    }

    /**
     * Get age and gender demographics
     */
    @GetMapping("/demographics")
    public Map<String, Object> getDemographics() {
        return statisticsService.getDemographicsData();
    }

    /**
     * Get concept frequency heatmap data
     */
    @GetMapping("/concept-frequency")
    public Map<String, Object> getConceptFrequency(
            @RequestParam(defaultValue = "condition") String domain) {
        return statisticsService.getConceptFrequencyData(domain);
    }

    /**
     * Get search analytics and popular terms
     */
    @GetMapping("/search-analytics")
    public Map<String, Object> getSearchAnalytics() {
        return statisticsService.getSearchAnalytics();
    }

    /**
     * Get available domains in condition table
     */
    @GetMapping("/condition-domains")
    public Map<String, Object> getConditionDomains() {
        Map<String, Object> response = new HashMap<>();
        try {
            String sql = "SELECT DISTINCT domain, COUNT(*) as count FROM ec_condition GROUP BY domain ORDER BY count DESC";
            List<Map<String, Object>> domains = jdbcTemplate.queryForList(sql);
            response.put("domains", domains);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
        }
        return response;
    }
}
