<template>
  <div class="search">
    <!-- Page Header -->
    <div class="page-header">
      <h1>Search Results</h1>
      <p>Search results for your query</p>
    </div>

    <!-- Search Summary -->
    <el-card class="search-summary">
      <div class="summary-content">
        <div class="search-query">
          <strong>Search Query:</strong>
          <el-tag v-if="query.conceptId" type="primary" class="query-tag">
            Concept ID: {{ query.conceptId }}
          </el-tag>
          <el-tag v-if="query.conceptName" type="success" class="query-tag">
            Concept Name: {{ query.conceptName }}
          </el-tag>
          <el-tag v-if="query.nctId" type="warning" class="query-tag">
            NCT ID: {{ query.nctId }}
          </el-tag>
        </div>
        <div class="search-actions">
          <el-button @click="modifySearch">Modify Search</el-button>
          <el-button type="primary" @click="newSearch">New Search</el-button>
        </div>
      </div>
    </el-card>

    <!-- Search Results Tabs -->
    <el-card class="results-card">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <!-- Conditions Tab -->
        <el-tab-pane label="Conditions" name="conditions">
          <div class="tab-content">
            <div class="results-header">
              <span class="results-count">{{ conditionsResults.length }} conditions found</span>
              <el-button size="small" @click="exportConditions">
                <el-icon><Download /></el-icon>
                Export
              </el-button>
            </div>
            
            <el-table
              :data="conditionsResults"
              v-loading="loadingConditions"
              stripe
              border
              style="width: 100%"
              @row-click="viewConditionDetail"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="conceptId" label="Concept ID" width="120" />
              <el-table-column prop="conceptName" label="Concept Name" min-width="200" show-overflow-tooltip />
              <el-table-column prop="nctid" label="NCT ID" width="120" />
              <el-table-column prop="domain" label="Domain" width="120" />
              <el-table-column prop="include" label="Type" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                    {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="score" label="Score" width="100">
                <template #default="{ row }">
                  <span v-if="row.score">{{ row.score.toFixed(2) }}</span>
                  <span v-else>-</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- Measurements Tab -->
        <el-tab-pane label="Measurements" name="measurements">
          <div class="tab-content">
            <div class="results-header">
              <span class="results-count">{{ measurementsResults.length }} measurements found</span>
              <el-button size="small" @click="exportMeasurements">
                <el-icon><Download /></el-icon>
                Export
              </el-button>
            </div>
            
            <el-table
              :data="measurementsResults"
              v-loading="loadingMeasurements"
              stripe
              border
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="conceptId" label="Concept ID" width="120" />
              <el-table-column prop="conceptName" label="Concept Name" min-width="200" show-overflow-tooltip />
              <el-table-column prop="nctid" label="NCT ID" width="120" />
              <el-table-column prop="domain" label="Domain" width="120" />
              <el-table-column prop="unit" label="Unit" width="100" />
              <el-table-column prop="min" label="Min" width="80" />
              <el-table-column prop="max" label="Max" width="80" />
            </el-table>
          </div>
        </el-tab-pane>

        <!-- Drugs Tab -->
        <el-tab-pane label="Drugs" name="drugs">
          <div class="tab-content">
            <div class="results-header">
              <span class="results-count">{{ drugsResults.length }} drugs found</span>
              <el-button size="small" @click="exportDrugs">
                <el-icon><Download /></el-icon>
                Export
              </el-button>
            </div>
            
            <el-table
              :data="drugsResults"
              v-loading="loadingDrugs"
              stripe
              border
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="conceptId" label="Concept ID" width="120" />
              <el-table-column prop="conceptName" label="Concept Name" min-width="200" show-overflow-tooltip />
              <el-table-column prop="nctid" label="NCT ID" width="120" />
              <el-table-column prop="domain" label="Domain" width="120" />
              <el-table-column prop="include" label="Type" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                    {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- Procedures Tab -->
        <el-tab-pane label="Procedures" name="procedures">
          <div class="tab-content">
            <div class="results-header">
              <span class="results-count">{{ proceduresResults.length }} procedures found</span>
              <el-button size="small" @click="exportProcedures">
                <el-icon><Download /></el-icon>
                Export
              </el-button>
            </div>
            
            <el-table
              :data="proceduresResults"
              v-loading="loadingProcedures"
              stripe
              border
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="conceptId" label="Concept ID" width="120" />
              <el-table-column prop="conceptName" label="Concept Name" min-width="200" show-overflow-tooltip />
              <el-table-column prop="nctid" label="NCT ID" width="120" />
              <el-table-column prop="domain" label="Domain" width="120" />
              <el-table-column prop="include" label="Type" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                    {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { conditionsApi } from '@/api/conditions'
import { measurementApi, drugApi, procedureApi } from '@/api/criteria'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// Reactive data
const activeTab = ref('conditions')
const query = reactive({
  conceptId: '',
  conceptName: '',
  nctId: ''
})

const conditionsResults = ref([])
const measurementsResults = ref([])
const drugsResults = ref([])
const proceduresResults = ref([])

const loadingConditions = ref(false)
const loadingMeasurements = ref(false)
const loadingDrugs = ref(false)
const loadingProcedures = ref(false)

// Methods
const searchConditions = async () => {
  loadingConditions.value = true
  try {
    let response
    if (query.conceptId) {
      response = await conditionsApi.getConditionsByConceptId(query.conceptId)
    } else if (query.conceptName) {
      response = await conditionsApi.getConditionsByConceptName(query.conceptName)
    } else if (query.nctId) {
      response = await conditionsApi.getConditionsByNctId(query.nctId)
    }
    
    conditionsResults.value = response?.records || []
  } catch (error) {
    console.error('Error searching conditions:', error)
    conditionsResults.value = []
  } finally {
    loadingConditions.value = false
  }
}

const searchMeasurements = async () => {
  loadingMeasurements.value = true
  try {
    let response
    if (query.conceptId) {
      response = await measurementApi.getMeasurementsByConceptId(query.conceptId)
    } else if (query.conceptName) {
      response = await measurementApi.getMeasurementsByConceptName(query.conceptName)
    } else if (query.nctId) {
      response = await measurementApi.getMeasurementsByNctId(query.nctId)
    }
    
    measurementsResults.value = response?.records || []
  } catch (error) {
    console.error('Error searching measurements:', error)
    measurementsResults.value = []
  } finally {
    loadingMeasurements.value = false
  }
}

const searchDrugs = async () => {
  loadingDrugs.value = true
  try {
    let response
    if (query.conceptId) {
      response = await drugApi.getDrugsByConceptId(query.conceptId)
    } else if (query.conceptName) {
      response = await drugApi.getDrugsByConceptName(query.conceptName)
    } else if (query.nctId) {
      response = await drugApi.getDrugsByNctId(query.nctId)
    }
    
    drugsResults.value = response?.records || []
  } catch (error) {
    console.error('Error searching drugs:', error)
    drugsResults.value = []
  } finally {
    loadingDrugs.value = false
  }
}

const searchProcedures = async () => {
  loadingProcedures.value = true
  try {
    let response
    if (query.conceptId) {
      response = await procedureApi.getProceduresByConceptId(query.conceptId)
    } else if (query.conceptName) {
      response = await procedureApi.getProceduresByConceptName(query.conceptName)
    } else if (query.nctId) {
      response = await procedureApi.getProceduresByNctId(query.nctId)
    }
    
    proceduresResults.value = response?.records || []
  } catch (error) {
    console.error('Error searching procedures:', error)
    proceduresResults.value = []
  } finally {
    loadingProcedures.value = false
  }
}

const handleTabChange = (tabName) => {
  activeTab.value = tabName
  
  // Load data for the selected tab if not already loaded
  switch (tabName) {
    case 'measurements':
      if (measurementsResults.value.length === 0) {
        searchMeasurements()
      }
      break
    case 'drugs':
      if (drugsResults.value.length === 0) {
        searchDrugs()
      }
      break
    case 'procedures':
      if (proceduresResults.value.length === 0) {
        searchProcedures()
      }
      break
  }
}

const viewConditionDetail = (condition) => {
  router.push(`/conditions/${condition.id}`)
}

const modifySearch = () => {
  router.push({
    path: '/conditions',
    query: route.query
  })
}

const newSearch = () => {
  router.push('/dashboard')
}

const exportConditions = () => {
  ElMessage.info('Export functionality will be implemented soon')
}

const exportMeasurements = () => {
  ElMessage.info('Export functionality will be implemented soon')
}

const exportDrugs = () => {
  ElMessage.info('Export functionality will be implemented soon')
}

const exportProcedures = () => {
  ElMessage.info('Export functionality will be implemented soon')
}

const initializeSearch = () => {
  const routeQuery = route.query
  query.conceptId = routeQuery.conceptId || ''
  query.conceptName = routeQuery.conceptName || ''
  query.nctId = routeQuery.nctId || ''
}

const performSearch = async () => {
  // Always search conditions first
  await searchConditions()
  
  // If user is on a different tab, load that data too
  if (activeTab.value !== 'conditions') {
    handleTabChange(activeTab.value)
  }
}

onMounted(async () => {
  initializeSearch()
  await performSearch()
})
</script>

<style scoped>
.search {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 16px;
}

.search-summary {
  margin-bottom: 24px;
}

.summary-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-query {
  display: flex;
  align-items: center;
  gap: 12px;
}

.query-tag {
  margin-left: 8px;
}

.search-actions {
  display: flex;
  gap: 8px;
}

.results-card {
  margin-bottom: 24px;
}

.tab-content {
  padding: 16px 0;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.results-count {
  font-weight: 500;
  color: #303133;
}

:deep(.el-table__row) {
  cursor: pointer;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}
</style>
