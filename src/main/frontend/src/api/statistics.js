import api from './index'

/**
 * Statistics API service
 */
export const statisticsApi = {
  // Dashboard statistics
  getDashboardStats() {
    return api.get('/statistics/dashboard')
  },

  // Domain distribution
  getDomainDistribution() {
    return api.get('/statistics/domain-distribution')
  },

  // Inclusion vs exclusion statistics
  getInclusionExclusionStats() {
    return api.get('/statistics/inclusion-exclusion')
  },

  // Trial phase distribution
  getPhaseDistribution() {
    return api.get('/statistics/phase-distribution')
  },

  // Temporal trends
  getTemporalTrends(startYear = '2020', endYear = '2024') {
    return api.get(`/statistics/temporal-trends?startYear=${startYear}&endYear=${endYear}`)
  },

  // Top conditions
  getTopConditions(limit = 10) {
    return api.get(`/statistics/top-conditions?limit=${limit}`)
  },

  // Top criteria
  getTopCriteria(limit = 10) {
    return api.get(`/statistics/top-criteria?limit=${limit}`)
  },

  // Condition-criteria relationships
  getConditionCriteriaRelationships(conditionId = null, limit = 50) {
    const params = new URLSearchParams({ limit })
    if (conditionId) params.append('conditionId', conditionId)
    return api.get(`/statistics/condition-criteria-relationships?${params}`)
  },

  // Measurement statistics
  getMeasurementStats(conceptId = null) {
    const params = conceptId ? `?conceptId=${conceptId}` : ''
    return api.get(`/statistics/measurement-stats${params}`)
  },

  // Trial status distribution
  getTrialStatusDistribution() {
    return api.get('/statistics/trial-status')
  },

  // Demographics data
  getDemographics() {
    return api.get('/statistics/demographics')
  },

  // Concept frequency
  getConceptFrequency(domain = 'condition') {
    return api.get(`/statistics/concept-frequency?domain=${domain}`)
  },

  // Search analytics
  getSearchAnalytics() {
    return api.get('/statistics/search-analytics')
  }
}

/**
 * Analytics API service
 */
export const analyticsApi = {
  // Trial timeline analysis
  getTrialTimeline(conditionId = null, startYear = '2020', endYear = '2024') {
    const params = new URLSearchParams({ startYear, endYear })
    if (conditionId) params.append('conditionId', conditionId)
    return api.get(`/analytics/trial-timeline?${params}`)
  },

  // Criteria correlation matrix
  getCriteriaCorrelation(domain = null, limit = 20) {
    const params = new URLSearchParams({ limit })
    if (domain) params.append('domain', domain)
    return api.get(`/analytics/criteria-correlation?${params}`)
  },

  // Phase progression analysis
  getPhaseProgression(conditionId = null) {
    const params = conditionId ? `?conditionId=${conditionId}` : ''
    return api.get(`/analytics/phase-progression${params}`)
  },

  // Criteria complexity analysis
  getCriteriaComplexity() {
    return api.get('/analytics/criteria-complexity')
  },

  // Condition similarity analysis
  getConditionSimilarity(conditionId, limit = 10) {
    return api.get(`/analytics/condition-similarity?conditionId=${conditionId}&limit=${limit}`)
  },

  // Intervention patterns
  getInterventionPatterns(interventionId = null) {
    const params = interventionId ? `?interventionId=${interventionId}` : ''
    return api.get(`/analytics/intervention-patterns${params}`)
  },

  // Measurement distributions
  getMeasurementDistributions(conceptId = null) {
    const params = conceptId ? `?conceptId=${conceptId}` : ''
    return api.get(`/analytics/measurement-distributions${params}`)
  },

  // Temporal criteria patterns
  getTemporalCriteriaPatterns(domain = 'condition') {
    return api.get(`/analytics/temporal-criteria-patterns?domain=${domain}`)
  },

  // Geographic distribution
  getGeographicDistribution() {
    return api.get('/analytics/geographic-distribution')
  },

  // Predictive insights
  getPredictiveInsights(conditionId = null) {
    const params = conditionId ? `?conditionId=${conditionId}` : ''
    return api.get(`/analytics/predictive-insights${params}`)
  },

  // Criteria trends
  getCriteriaTrends(criteriaId = null, months = 12) {
    const params = new URLSearchParams({ months })
    if (criteriaId) params.append('criteriaId', criteriaId)
    return api.get(`/analytics/criteria-trends?${params}`)
  },

  // Recruitment patterns
  getRecruitmentPatterns() {
    return api.get('/analytics/recruitment-patterns')
  },

  // Condition co-occurrence
  getConditionCooccurrence(conditionId, limit = 10) {
    return api.get(`/analytics/condition-cooccurrence?conditionId=${conditionId}&limit=${limit}`)
  },

  // Search suggestions
  getSearchSuggestions(query, type = 'condition') {
    return api.get(`/analytics/search-suggestions?query=${encodeURIComponent(query)}&type=${type}`)
  },

  // Data quality metrics
  getDataQualityMetrics() {
    return api.get('/analytics/data-quality')
  }
}

export default {
  statistics: statisticsApi,
  analytics: analyticsApi
}
