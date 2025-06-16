import api from './index'

export const criteriaApi = {
  // Get criteria count by criteria ID
  getCriteriaCount(criteriaId, include) {
    return api.get(`/all-criteria/get-cri-count/${criteriaId}/${include}`)
  }
}

export const criteriaStatsApi = {
  // Get criteria count by phase
  getCriteriaCountByPhase(criteriaId, include) {
    return api.get(`/common-criteria-stats/criteria-phase/${criteriaId}/${include}`)
  }
}

export const measurementApi = {
  // Get measurements by concept ID
  getMeasurementsByConceptId(conceptId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/measurement/page/concept_id/${conceptId}/${currentPage}/${size}`)
    }
    return api.get(`/measurement/page/concept_id/${conceptId}`)
  },

  // Get measurements by concept name
  getMeasurementsByConceptName(conceptName, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/measurement/page/concept_name/${conceptName}/${currentPage}/${size}`)
    }
    return api.get(`/measurement/page/concept_name/${conceptName}`)
  },

  // Get measurements by NCT ID
  getMeasurementsByNctId(nctId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/measurement/page/concept_nctid/${nctId}/${currentPage}/${size}`)
    }
    return api.get(`/measurement/page/concept_nctid/${nctId}`)
  },

  // Get measurement values by concept ID with range
  getMeasurementValues(conceptId, userMax, userMin) {
    return api.get(`/measurement/values/concept_id/${conceptId}/${userMax}/${userMin}`)
  }
}

export const drugApi = {
  // Get drugs by concept ID
  getDrugsByConceptId(conceptId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/drug/page/concept_id/${conceptId}/${currentPage}/${size}`)
    }
    return api.get(`/drug/page/concept_id/${conceptId}`)
  },

  // Get drugs by concept name
  getDrugsByConceptName(conceptName, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/drug/page/concept_name/${conceptName}/${currentPage}/${size}`)
    }
    return api.get(`/drug/page/concept_name/${conceptName}`)
  },

  // Get drugs by NCT ID
  getDrugsByNctId(nctId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/drug/page/concept_nctid/${nctId}/${currentPage}/${size}`)
    }
    return api.get(`/drug/page/concept_nctid/${nctId}`)
  }
}

export const observationApi = {
  // Get observations by concept ID
  getObservationsByConceptId(conceptId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/observation/page/concept_id/${conceptId}/${currentPage}/${size}`)
    }
    return api.get(`/observation/page/concept_id/${conceptId}`)
  },

  // Get observations by concept name
  getObservationsByConceptName(conceptName, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/observation/page/concept_name/${conceptName}/${currentPage}/${size}`)
    }
    return api.get(`/observation/page/concept_name/${conceptName}`)
  },

  // Get observations by NCT ID
  getObservationsByNctId(nctId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/observation/page/concept_nctid/${nctId}/${currentPage}/${size}`)
    }
    return api.get(`/observation/page/concept_nctid/${nctId}`)
  }
}

export const procedureApi = {
  // Get procedures by concept ID
  getProceduresByConceptId(conceptId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/procedure/page/concept_id/${conceptId}/${currentPage}/${size}`)
    }
    return api.get(`/procedure/page/concept_id/${conceptId}`)
  },

  // Get procedures by concept name
  getProceduresByConceptName(conceptName, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/procedure/page/concept_name/${conceptName}/${currentPage}/${size}`)
    }
    return api.get(`/procedure/page/concept_name/${conceptName}`)
  },

  // Get procedures by NCT ID
  getProceduresByNctId(nctId, currentPage = 1, size = 10) {
    if (currentPage && size) {
      return api.get(`/procedure/page/concept_nctid/${nctId}/${currentPage}/${size}`)
    }
    return api.get(`/procedure/page/concept_nctid/${nctId}`)
  }
}
