import api from './index'

export const conditionsApi = {
  // Get all conditions with pagination (using working direct SQL endpoint)
  getAllConditions() {
    return api.get('/condition/page/all-direct')
  },

  // Get conditions by concept ID (using working direct SQL endpoint)
  getConditionsByConceptId(conceptId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/condition/page/concept_id-direct/${conceptId}/${currentPage}/${size}`)
    }
    return api.get(`/condition/page/concept_id-direct/${conceptId}`)
  },

  // Get conditions by concept name (using working direct SQL endpoint)
  getConditionsByConceptName(conceptName, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/condition/page/concept_name-direct/${conceptName}/${currentPage}/${size}`)
    }
    return api.get(`/condition/page/concept_name-direct/${conceptName}`)
  },

  // Get conditions by NCT ID (using working direct SQL endpoint)
  getConditionsByNctId(nctId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/condition/page/concept_nctid-direct/${nctId}/${currentPage}/${size}`)
    }
    return api.get(`/condition/page/concept_nctid-direct/${nctId}`)
  },

  // Advanced search with filters
  searchConditionsAdvanced(filters) {
    return api.get('/condition/search-advanced', { params: filters })
  },

  // Get condition by ID
  getConditionById(id) {
    return api.get(`/condition/id/${id}`)
  }
}

export const commonConditionsApi = {
  // Get criteria by condition name (using working direct SQL endpoint with pagination)
  getCriteriaByConditionName(conceptName, include, page = 1, size = 50) {
    return api.get(`/common-condition/get-criteria-name-direct/${conceptName}/${include}/${page}/${size}`)
  },

  // Get conditions by criteria name (using working direct SQL endpoint with pagination)
  getConditionsByCriteriaName(conceptName, include, page = 1, size = 50) {
    return api.get(`/common-condition/get-conditions-name-direct/${conceptName}/${include}/${page}/${size}`)
  },

  // Get top conditions with precomputed counts
  getTopConditions() {
    return api.get('/common-condition/get-top-conditions')
  },

  // Get top criteria with precomputed counts
  getTopCriteria() {
    return api.get('/common-condition/get-top-criteria')
  },

  // Analytics endpoints
  getCriteriaTrends(criteriaName) {
    return api.get(`/common-condition/analytics/criteria-trends/${encodeURIComponent(criteriaName)}`)
  },

  getConditionTrends(conditionName) {
    return api.get(`/common-condition/analytics/condition-trends/${encodeURIComponent(conditionName)}`)
  },

  getMeasurementHistogram(criteriaName) {
    return api.get(`/common-condition/analytics/measurement-histogram/${encodeURIComponent(criteriaName)}`)
  },

  // Criteria search endpoints
  searchCriteriaAdvanced(filters) {
    return api.get('/common-condition/search-criteria-advanced', { params: filters })
  },

  searchCriteriaByConceptId(conceptId, currentPage = 1, size = 20) {
    return api.get(`/common-condition/search-criteria/concept-id/${encodeURIComponent(conceptId)}`, {
      params: { currentPage, size }
    })
  },

  searchCriteriaByConceptName(conceptName, currentPage = 1, size = 20) {
    return api.get(`/common-condition/search-criteria/concept-name/${encodeURIComponent(conceptName)}`, {
      params: { currentPage, size }
    })
  },

  searchCriteriaByNctId(nctId, currentPage = 1, size = 20) {
    return api.get(`/common-condition/search-criteria/nct-id/${encodeURIComponent(nctId)}`, {
      params: { currentPage, size }
    })
  },

  // Get criteria detail by ID
  getCriteriaDetail(criteriaId) {
    return api.get(`/common-condition/criteria-detail/${encodeURIComponent(criteriaId)}`)
  },

  // Get criteria by condition ID
  getCriteriaByConditionId(conceptId, include) {
    return api.get(`/common-condition/get-criteria-id/${conceptId}/${include}`)
  },

  // Get criteria by condition ID and domain
  getCriteriaByConditionIdAndDomain(conceptId, include, domain) {
    return api.get(`/common-condition/get-criteria-id/${conceptId}/${include}/${domain}`)
  },

  // Get conditions by criteria ID
  getConditionsByCriteriaId(conceptId, include) {
    return api.get(`/common-condition/get-conditions-id/${conceptId}/${include}`)
  },

  // Get all criteria names
  getAllCriteriaNames() {
    return api.get('/common-condition/get-all-criteria-names')
  },

  // Get all condition names
  getAllConditionNames() {
    return api.get('/common-condition/get-all-condition-names')
  },

  // Get criterion ID by name
  getCriterionIdByName(name) {
    return api.get(`/common-condition/get-criterion-id-by-name/${name}`)
  },

  // Get condition ID by name
  getConditionIdByName(name) {
    return api.get(`/common-condition/get-condition-id-by-name/${name}`)
  },

  // Get by condition ID and criteria ID
  getByConditionIdAndCriteriaId(conditionId, criteriaId, include) {
    return api.get(`/common-condition/${conditionId}/${criteriaId}/${include}`)
  }
}
