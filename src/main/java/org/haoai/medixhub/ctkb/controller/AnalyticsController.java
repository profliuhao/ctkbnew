package org.haoai.medixhub.ctkb.controller;

import org.haoai.medixhub.ctkb.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Advanced Analytics Controller
 * Provides complex analytical data and insights
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Get trial timeline analysis
     */
    @GetMapping("/trial-timeline")
    public Map<String, Object> getTrialTimeline(
            @RequestParam(required = false) String conditionId,
            @RequestParam(defaultValue = "2020") String startYear,
            @RequestParam(defaultValue = "2024") String endYear) {
        return analyticsService.getTrialTimelineAnalysis(conditionId, startYear, endYear);
    }

    /**
     * Get criteria correlation matrix
     */
    @GetMapping("/criteria-correlation")
    public Map<String, Object> getCriteriaCorrelation(
            @RequestParam(required = false) String domain,
            @RequestParam(defaultValue = "20") int limit) {
        return analyticsService.getCriteriaCorrelationMatrix(domain, limit);
    }

    /**
     * Get phase progression analysis
     */
    @GetMapping("/phase-progression")
    public Map<String, Object> getPhaseProgression(
            @RequestParam(required = false) String conditionId) {
        return analyticsService.getPhaseProgressionAnalysis(conditionId);
    }

    /**
     * Get eligibility criteria complexity analysis
     */
    @GetMapping("/criteria-complexity")
    public Map<String, Object> getCriteriaComplexity() {
        return analyticsService.getCriteriaComplexityAnalysis();
    }

    /**
     * Get condition similarity analysis
     */
    @GetMapping("/condition-similarity")
    public Map<String, Object> getConditionSimilarity(
            @RequestParam String conditionId,
            @RequestParam(defaultValue = "10") int limit) {
        return analyticsService.getConditionSimilarityAnalysis(conditionId, limit);
    }

    /**
     * Get intervention effectiveness patterns
     */
    @GetMapping("/intervention-patterns")
    public Map<String, Object> getInterventionPatterns(
            @RequestParam(required = false) String interventionId) {
        return analyticsService.getInterventionPatterns(interventionId);
    }

    /**
     * Get measurement value distributions
     */
    @GetMapping("/measurement-distributions")
    public Map<String, Object> getMeasurementDistributions(
            @RequestParam(required = false) String conceptId) {
        return analyticsService.getMeasurementDistributions(conceptId);
    }

    /**
     * Get temporal patterns in criteria usage
     */
    @GetMapping("/temporal-criteria-patterns")
    public Map<String, Object> getTemporalCriteriaPatterns(
            @RequestParam(defaultValue = "condition") String domain) {
        return analyticsService.getTemporalCriteriaPatterns(domain);
    }

    /**
     * Get geographic trial distribution (if location data available)
     */
    @GetMapping("/geographic-distribution")
    public Map<String, Object> getGeographicDistribution() {
        return analyticsService.getGeographicDistribution();
    }

    /**
     * Get predictive insights for trial success
     */
    @GetMapping("/predictive-insights")
    public Map<String, Object> getPredictiveInsights(
            @RequestParam(required = false) String conditionId) {
        return analyticsService.getPredictiveInsights(conditionId);
    }

    /**
     * Get criteria usage trends over time
     */
    @GetMapping("/criteria-trends")
    public Map<String, Object> getCriteriaTrends(
            @RequestParam(required = false) String criteriaId,
            @RequestParam(defaultValue = "12") int months) {
        return analyticsService.getCriteriaTrends(criteriaId, months);
    }

    /**
     * Get trial recruitment patterns
     */
    @GetMapping("/recruitment-patterns")
    public Map<String, Object> getRecruitmentPatterns() {
        return analyticsService.getRecruitmentPatterns();
    }

    /**
     * Get condition co-occurrence analysis
     */
    @GetMapping("/condition-cooccurrence")
    public Map<String, Object> getConditionCooccurrence(
            @RequestParam String conditionId,
            @RequestParam(defaultValue = "10") int limit) {
        return analyticsService.getConditionCooccurrenceAnalysis(conditionId, limit);
    }

    /**
     * Get advanced search suggestions
     */
    @GetMapping("/search-suggestions")
    public List<Map<String, Object>> getSearchSuggestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "condition") String type) {
        return analyticsService.getSearchSuggestions(query, type);
    }

    /**
     * Get data quality metrics
     */
    @GetMapping("/data-quality")
    public Map<String, Object> getDataQualityMetrics() {
        return analyticsService.getDataQualityMetrics();
    }
}
