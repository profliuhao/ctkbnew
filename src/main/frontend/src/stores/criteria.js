import { defineStore } from 'pinia'
import { criteriaApi, criteriaStatsApi } from '@/api/criteria'
import { commonConditionsApi } from '@/api/conditions'

export const useCriteriaStore = defineStore('criteria', {
  state: () => ({
    criteria: [],
    criteriaNames: [],
    conditionNames: [],
    criteriaStats: {},
    loading: false,
    searchFilters: {
      criteriaId: '',
      criteriaName: '',
      conditionId: '',
      conditionName: '',
      include: 1,
      domain: ''
    }
  }),

  getters: {
    isLoading: (state) => state.loading,
    getCriteria: (state) => state.criteria,
    getCriteriaNames: (state) => state.criteriaNames,
    getConditionNames: (state) => state.conditionNames,
    getCriteriaStats: (state) => state.criteriaStats,
    getSearchFilters: (state) => state.searchFilters
  },

  actions: {
    async fetchAllCriteriaNames() {
      this.loading = true
      try {
        const response = await commonConditionsApi.getAllCriteriaNames()
        // Filter out null, undefined, and empty values
        this.criteriaNames = (response || []).filter(name =>
          name != null && name !== '' && typeof name === 'string'
        )
      } catch (error) {
        console.error('Error fetching criteria names:', error)
        this.criteriaNames = []
      } finally {
        this.loading = false
      }
    },

    async fetchAllConditionNames() {
      this.loading = true
      try {
        const response = await commonConditionsApi.getAllConditionNames()
        // Filter out null, undefined, and empty values
        this.conditionNames = (response || []).filter(name =>
          name != null && name !== '' && typeof name === 'string'
        )
      } catch (error) {
        console.error('Error fetching condition names:', error)
        this.conditionNames = []
      } finally {
        this.loading = false
      }
    },

    async fetchCriteriaByConditionName(conditionName, include = 1) {
      this.loading = true
      try {
        const response = await commonConditionsApi.getCriteriaByConditionName(conditionName, include)
        // Handle both old array format and new pagination format
        if (response && response.records) {
          this.criteria = response.records || []
        } else {
          this.criteria = response || []
        }
      } catch (error) {
        console.error('Error fetching criteria by condition name:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    },

    async fetchCriteriaByConditionId(conditionId, include = 1, domain = null) {
      this.loading = true
      try {
        let response
        if (domain) {
          response = await commonConditionsApi.getCriteriaByConditionIdAndDomain(conditionId, include, domain)
        } else {
          response = await commonConditionsApi.getCriteriaByConditionId(conditionId, include)
        }
        // Handle both old array format and new pagination format
        if (response && response.records) {
          this.criteria = response.records || []
        } else {
          this.criteria = response || []
        }
      } catch (error) {
        console.error('Error fetching criteria by condition ID:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    },

    async fetchConditionsByCriteriaName(criteriaName, include = 1) {
      this.loading = true
      try {
        const response = await commonConditionsApi.getConditionsByCriteriaName(criteriaName, include)
        this.criteria = response || []
      } catch (error) {
        console.error('Error fetching conditions by criteria name:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    },

    async fetchConditionsByCriteriaId(criteriaId, include = 1) {
      this.loading = true
      try {
        const response = await commonConditionsApi.getConditionsByCriteriaId(criteriaId, include)
        this.criteria = response || []
      } catch (error) {
        console.error('Error fetching conditions by criteria ID:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    },

    async fetchCriteriaCount(criteriaId, include = 1) {
      try {
        const response = await criteriaApi.getCriteriaCount(criteriaId, include)
        return response
      } catch (error) {
        console.error('Error fetching criteria count:', error)
        return 0
      }
    },

    async fetchCriteriaCountByPhase(criteriaId, include = 1) {
      try {
        const response = await criteriaStatsApi.getCriteriaCountByPhase(criteriaId, include)
        this.criteriaStats[criteriaId] = response || {}
        return response
      } catch (error) {
        console.error('Error fetching criteria count by phase:', error)
        return {}
      }
    },

    updateSearchFilters(filters) {
      this.searchFilters = { ...this.searchFilters, ...filters }
    },

    clearCriteria() {
      this.criteria = []
      this.criteriaStats = {}
    },

    async fetchBothInclusionAndExclusionCriteria(conditionName) {
      this.loading = true
      try {
        const [inclusionResponse, exclusionResponse] = await Promise.all([
          commonConditionsApi.getCriteriaByConditionName(conditionName, 1),
          commonConditionsApi.getCriteriaByConditionName(conditionName, 0)
        ])

        // Handle both old array format and new pagination format
        const inclusionData = inclusionResponse?.records || inclusionResponse || []
        const exclusionData = exclusionResponse?.records || exclusionResponse || []

        // Combine both inclusion and exclusion results
        this.criteria = [
          ...inclusionData,
          ...exclusionData
        ]
      } catch (error) {
        console.error('Error fetching both inclusion and exclusion criteria:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    },

    async fetchBothInclusionAndExclusionConditions(criteriaName) {
      this.loading = true
      try {
        const [inclusionResponse, exclusionResponse] = await Promise.all([
          commonConditionsApi.getConditionsByCriteriaName(criteriaName, 1),
          commonConditionsApi.getConditionsByCriteriaName(criteriaName, 0)
        ])

        // Combine both inclusion and exclusion results
        this.criteria = [
          ...(inclusionResponse || []),
          ...(exclusionResponse || [])
        ]
      } catch (error) {
        console.error('Error fetching both inclusion and exclusion conditions:', error)
        this.criteria = []
      } finally {
        this.loading = false
      }
    }
  }
})
