import { defineStore } from 'pinia'
import { conditionsApi, commonConditionsApi } from '@/api/conditions'

export const useConditionsStore = defineStore('conditions', {
  state: () => ({
    conditions: [],
    currentCondition: null,
    loading: false,
    pagination: {
      current: 1,
      size: 10,
      total: 0
    },
    searchFilters: {
      conceptId: '',
      conceptName: '',
      nctId: '',
      domain: '',
      include: 1
    }
  }),

  getters: {
    isLoading: (state) => state.loading,
    getConditions: (state) => state.conditions,
    getCurrentCondition: (state) => state.currentCondition,
    getPagination: (state) => state.pagination,
    getSearchFilters: (state) => state.searchFilters
  },

  actions: {
    async fetchAllConditions() {
      this.loading = true
      try {
        const response = await conditionsApi.getAllConditions()
        this.conditions = response.records || []
        this.pagination = {
          current: response.current || 1,
          size: response.size || 10,
          total: response.total || 0
        }
      } catch (error) {
        console.error('Error fetching conditions:', error)
        this.conditions = []
      } finally {
        this.loading = false
      }
    },

    async fetchConditionsByConceptId(conceptId, currentPage = 1, size = 10) {
      this.loading = true
      try {
        const response = await conditionsApi.getConditionsByConceptId(conceptId, currentPage, size)
        this.conditions = response.records || []
        this.pagination = {
          current: response.current || 1,
          size: response.size || 10,
          total: response.total || 0
        }
      } catch (error) {
        console.error('Error fetching conditions by concept ID:', error)
        this.conditions = []
      } finally {
        this.loading = false
      }
    },

    async fetchConditionsByConceptName(conceptName, currentPage = 1, size = 10) {
      this.loading = true
      try {
        const response = await conditionsApi.getConditionsByConceptName(conceptName, currentPage, size)
        this.conditions = response.records || []
        this.pagination = {
          current: response.current || 1,
          size: response.size || 10,
          total: response.total || 0
        }
      } catch (error) {
        console.error('Error fetching conditions by concept name:', error)
        this.conditions = []
      } finally {
        this.loading = false
      }
    },

    async fetchConditionsByNctId(nctId, currentPage = 1, size = 10) {
      this.loading = true
      try {
        const response = await conditionsApi.getConditionsByNctId(nctId, currentPage, size)
        this.conditions = response.records || []
        this.pagination = {
          current: response.current || 1,
          size: response.size || 10,
          total: response.total || 0
        }
      } catch (error) {
        console.error('Error fetching conditions by NCT ID:', error)
        this.conditions = []
      } finally {
        this.loading = false
      }
    },

    async searchConditionsAdvanced(filters) {
      this.loading = true
      try {
        const response = await conditionsApi.searchConditionsAdvanced(filters)
        this.conditions = response.records || []
        this.pagination = {
          current: response.current || 1,
          size: response.size || 10,
          total: response.total || 0
        }
      } catch (error) {
        console.error('Error searching conditions with advanced filters:', error)
        this.conditions = []
      } finally {
        this.loading = false
      }
    },

    setCurrentCondition(condition) {
      this.currentCondition = condition
    },

    updateSearchFilters(filters) {
      this.searchFilters = { ...this.searchFilters, ...filters }
    },

    updatePagination(pagination) {
      this.pagination = { ...this.pagination, ...pagination }
    },

    clearConditions() {
      this.conditions = []
      this.currentCondition = null
      this.pagination = {
        current: 1,
        size: 10,
        total: 0
      }
    }
  }
})
