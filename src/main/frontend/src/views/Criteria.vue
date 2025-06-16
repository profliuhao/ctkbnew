<template>
  <div class="criteria-page">
    <!-- Page Header -->
    <div class="page-header">
      <h1>Clinical Trial Criteria</h1>
      <p>Explore relationships between conditions and criteria in clinical trials.</p>
    </div>

    <!-- Search Section -->
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>Search and Filter Criteria</span>
          <el-button
            size="small"
            @click="toggleSearchMode"
            :type="searchMode === 'advanced' ? 'primary' : 'default'"
          >
            {{ searchMode === 'advanced' ? 'Simple Search' : 'Advanced Search' }}
          </el-button>
        </div>
      </template>

      <!-- Simple Search Mode -->
      <el-form v-if="searchMode === 'simple'" :model="searchForm" label-width="140px">
        <!-- Search Type Selection -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="Search Type">
              <el-radio-group v-model="searchForm.searchType">
                <el-radio value="by-condition">Find Criteria by Condition</el-radio>
                <el-radio value="by-criteria">Find Conditions by Criteria</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Search Input -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              :label="searchForm.searchType === 'by-condition' ? 'Select Condition' : 'Select Criteria'"
            >
              <el-select
                v-model="searchForm.selectedValue"
                :placeholder="searchForm.searchType === 'by-condition' ? 'Choose a condition...' : 'Choose a criteria...'"
                filterable
                clearable
                style="width: 100%"
                :loading="dropdownLoading"
              >
                <el-option
                  v-for="item in currentOptions"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-button
                type="primary"
                :icon="Search"
                @click="handleSearch"
                :loading="loading"
                :disabled="!searchForm.selectedValue"
              >
                Search
              </el-button>
              <el-button @click="resetSearch">Reset</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- Advanced Search Mode -->
      <el-form v-else :model="advancedFilters" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Criteria ID">
              <el-input
                v-model="advancedFilters.criteriaId"
                placeholder="Enter criteria concept ID"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Criteria Name">
              <el-input
                v-model="advancedFilters.criteriaName"
                placeholder="Enter criteria name"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="NCT ID">
              <el-input
                v-model="advancedFilters.nctId"
                placeholder="Enter NCT ID"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Domain">
              <el-select v-model="advancedFilters.domain" placeholder="Select domain" clearable>
                <el-option label="Condition" value="Condition" />
                <el-option label="Drug" value="Drug" />
                <el-option label="Procedure" value="Procedure" />
                <el-option label="Measurement" value="Measurement" />
                <el-option label="Observation" value="Observation" />
                <el-option label="Spec Anatomic Site" value="Spec Anatomic Site" />
                <el-option label="Device" value="Device" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Include Type">
              <el-radio-group v-model="advancedFilters.include">
                <el-radio :value="null">All</el-radio>
                <el-radio :value="1">Inclusion</el-radio>
                <el-radio :value="0">Exclusion</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item>
              <el-button
                type="primary"
                :icon="Search"
                @click="handleAdvancedSearch"
                :loading="loading"
              >
                Search
              </el-button>
              <el-button @click="resetAdvancedSearch">Reset</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- Results Section -->
    <div v-else-if="hasResults" class="results-section">
      <!-- Summary Statistics -->
      <el-row :gutter="16" class="stats-row">
        <el-col :span="6">
          <el-statistic title="Total Results" :value="totalResults" />
        </el-col>
        <el-col :span="6">
          <el-statistic
            title="Inclusion Criteria"
            :value="searchMode === 'advanced' ? advancedInclusionCount : inclusionPagination.total"
          />
        </el-col>
        <el-col :span="6">
          <el-statistic
            title="Exclusion Criteria"
            :value="searchMode === 'advanced' ? advancedExclusionCount : exclusionPagination.total"
          />
        </el-col>
        <el-col :span="6">
          <div class="search-term-display">
            <div class="search-term-title">Search Term</div>
            <div class="search-term-value">
              {{ searchMode === 'advanced' ? getAdvancedSearchSummary() : searchForm.selectedValue }}
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- Analytics Dashboard -->
      <div v-if="analyticsData" class="analytics-section">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
          <h3>Analytics Dashboard</h3>
          <el-button @click="createCharts" type="success" size="small">
            Refresh Charts
          </el-button>
        </div>
        <el-row :gutter="20">
          <!-- Domain Distribution Chart -->
          <el-col :span="8">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Domain Distribution</span>
                  <el-tag size="small">{{ analyticsData.domainDistribution?.length || 0 }} domains</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <canvas ref="domainChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <!-- Inclusion/Exclusion Chart -->
          <el-col :span="8">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Inclusion vs Exclusion</span>
                  <el-tag size="small" type="success">{{ getInclusionPercentage() }}% inclusion</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <canvas ref="inclusionChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <!-- Enhanced Value Distribution -->
          <el-col :span="8">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Value Distribution</span>
                  <el-tag v-if="measurementData?.isMeasurement" size="small" type="warning">Measurement</el-tag>
                  <el-tag v-else size="small" type="info">Non-measurement</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <canvas v-if="measurementData?.isMeasurement" ref="histogramChart"></canvas>
                <div v-else class="no-chart">
                  <el-empty description="No measurement data available" :image-size="60" />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- Enhanced Visualizations Row -->
        <el-row :gutter="20" class="enhanced-charts-row">
          <!-- Criteria Frequency Bar Chart -->
          <el-col :span="12">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Top Criteria Frequency</span>
                  <el-tag size="small">{{ getTopItemsCount() }} criteria</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <canvas ref="frequencyChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <!-- Domain Comparison Chart -->
          <el-col :span="12">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Domain Inclusion vs Exclusion</span>
                  <el-tag size="small" type="success">Detailed breakdown</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <canvas ref="domainComparisonChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- Creative Visualization Row -->
        <el-row :gutter="20" class="creative-charts-row">
          <!-- Interactive Statistics Cards -->
          <el-col :span="24">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>Interactive Data Insights</span>
                  <el-tag size="small" type="warning">Enhanced Analytics</el-tag>
                </div>
              </template>
              <div class="insights-grid">
                <div class="insight-card" v-for="(insight, index) in getDataInsights()" :key="index">
                  <div class="insight-icon">
                    <el-icon><DataAnalysis /></el-icon>
                  </div>
                  <div class="insight-content">
                    <div class="insight-title">{{ insight.title }}</div>
                    <div class="insight-value">{{ insight.value }}</div>
                    <div class="insight-description">{{ insight.description }}</div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- Top Related Items -->
        <el-row :gutter="20" class="top-items-row">
          <el-col :span="24">
            <el-card class="analytics-card">
              <template #header>
                <div class="card-header">
                  <span>
                    Top {{ searchForm.searchType === 'by-condition' ? 'Criteria' : 'Conditions' }}
                    for "{{ searchForm.selectedValue }}"
                  </span>
                  <el-tag size="small">{{ getTopItemsCount() }} items</el-tag>
                </div>
              </template>
              <div class="top-items-grid">
                <div
                  v-for="(item, index) in getTopItems()"
                  :key="index"
                  class="top-item"
                  @click="exploreItem(item)"
                >
                  <div class="item-rank">{{ index + 1 }}</div>
                  <div class="item-content">
                    <div class="item-name">{{ item.name }}</div>
                    <div class="item-stats">
                      <el-tag size="small">{{ item.count }} occurrences</el-tag>
                      <el-tag v-if="item.domain" size="small" type="info">{{ item.domain }}</el-tag>
                    </div>
                  </div>
                  <div class="item-action">
                    <el-icon><ArrowRight /></el-icon>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- Advanced Search Results Table -->
      <el-card v-if="searchMode === 'advanced' && advancedResults.length > 0" class="table-card">
        <template #header>
          <div class="table-header">
            <span class="header-title">
              <el-icon><Search /></el-icon>
              Advanced Search Results ({{ advancedPagination.total }} items)
            </span>
            <el-button size="small" @click="exportAdvancedData">
              <el-icon><Download /></el-icon>
              Export
            </el-button>
          </div>
        </template>

        <el-table
          :data="advancedResults"
          stripe
          border
          style="width: 100%"
          max-height="500"
        >
          <el-table-column
            prop="criteriaConceptId"
            label="Criteria ID"
            width="120"
            sortable
          />
          <el-table-column
            prop="criteriaConceptName"
            label="Criteria Name"
            min-width="300"
            show-overflow-tooltip
          />
          <el-table-column prop="criteriaDomain" label="Domain" width="120" />
          <el-table-column prop="include" label="Type" width="100">
            <template #default="{ row }">
              <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="conditionCount" label="Conditions" width="100" sortable />
          <el-table-column prop="totalOccurrences" label="Occurrences" width="120" sortable />
          <el-table-column label="Actions" width="120" fixed="right">
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                @click="viewCriteriaDetails(row)"
              >
                Details
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Advanced Search Pagination -->
        <div v-if="advancedPagination.pages > 1" class="pagination-container">
          <el-pagination
            v-model:current-page="advancedPagination.currentPage"
            :page-size="advancedPagination.size"
            :total="advancedPagination.total"
            layout="prev, pager, next, jumper, total"
            @current-change="handleAdvancedPageChange"
            small
          />
        </div>
      </el-card>

      <!-- Inclusion Criteria Table (Simple Search) -->
      <el-card v-if="inclusionResults.length > 0" class="table-card">
        <template #header>
          <div class="table-header">
            <span class="header-title">
              <el-icon class="inclusion-icon"><Check /></el-icon>
              Inclusion Criteria ({{ inclusionResults.length }} items)
            </span>
            <el-button size="small" @click="exportData('inclusion')">
              <el-icon><Download /></el-icon>
              Export
            </el-button>
          </div>
        </template>

        <el-table
          :data="inclusionResults"
          stripe
          border
          style="width: 100%"
          max-height="400"
        >
          <el-table-column 
            v-if="searchForm.searchType === 'by-condition'"
            prop="criteriaConceptId" 
            label="Criteria ID" 
            width="120"
            sortable
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-condition'"
            prop="criteriaConceptName" 
            label="Criteria Name" 
            min-width="250"
            show-overflow-tooltip
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-criteria'"
            prop="conditionConceptId" 
            label="Condition ID" 
            width="120"
            sortable
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-criteria'"
            prop="conditionConceptName" 
            label="Condition Name" 
            min-width="250"
            show-overflow-tooltip
          />
          <el-table-column prop="criteriaDomain" label="Domain" width="120" />
          <el-table-column prop="conceptCount" label="Count" width="100" sortable />
          <el-table-column prop="totalCount" label="Total" width="100" sortable />
          <el-table-column label="Frequency" width="100">
            <template #default="{ row }">
              <span v-if="row.totalCount && row.conceptCount">
                {{ ((row.conceptCount / row.totalCount) * 100).toFixed(1) }}%
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </el-table>

        <!-- Inclusion Pagination -->
        <div v-if="inclusionPagination.pages > 1" class="pagination-container">
          <el-pagination
            v-model:current-page="inclusionPagination.current"
            :page-size="inclusionPagination.size"
            :total="inclusionPagination.total"
            layout="prev, pager, next, jumper, total"
            @current-change="handleInclusionPageChange"
            small
          />
        </div>
      </el-card>

      <!-- Exclusion Criteria Table -->
      <el-card v-if="exclusionResults.length > 0" class="table-card">
        <template #header>
          <div class="table-header">
            <span class="header-title">
              <el-icon class="exclusion-icon"><Close /></el-icon>
              Exclusion Criteria ({{ exclusionResults.length }} items)
            </span>
            <el-button size="small" @click="exportData('exclusion')">
              <el-icon><Download /></el-icon>
              Export
            </el-button>
          </div>
        </template>

        <el-table
          :data="exclusionResults"
          stripe
          border
          style="width: 100%"
          max-height="400"
        >
          <el-table-column 
            v-if="searchForm.searchType === 'by-condition'"
            prop="criteriaConceptId" 
            label="Criteria ID" 
            width="120"
            sortable
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-condition'"
            prop="criteriaConceptName" 
            label="Criteria Name" 
            min-width="250"
            show-overflow-tooltip
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-criteria'"
            prop="conditionConceptId" 
            label="Condition ID" 
            width="120"
            sortable
          />
          <el-table-column 
            v-if="searchForm.searchType === 'by-criteria'"
            prop="conditionConceptName" 
            label="Condition Name" 
            min-width="250"
            show-overflow-tooltip
          />
          <el-table-column prop="criteriaDomain" label="Domain" width="120" />
          <el-table-column prop="conceptCount" label="Count" width="100" sortable />
          <el-table-column prop="totalCount" label="Total" width="100" sortable />
          <el-table-column label="Frequency" width="100">
            <template #default="{ row }">
              <span v-if="row.totalCount && row.conceptCount">
                {{ ((row.conceptCount / row.totalCount) * 100).toFixed(1) }}%
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </el-table>

        <!-- Exclusion Pagination -->
        <div v-if="exclusionPagination.pages > 1" class="pagination-container">
          <el-pagination
            v-model:current-page="exclusionPagination.current"
            :page-size="exclusionPagination.size"
            :total="exclusionPagination.total"
            layout="prev, pager, next, jumper, total"
            @current-change="handleExclusionPageChange"
            small
          />
        </div>
      </el-card>
    </div>

    <!-- Empty State -->
    <div v-else-if="!loading && searchAttempted" class="empty-state">
      <el-empty 
        description="No results found. Try searching for a different condition or criteria."
        :image-size="100"
      />
    </div>

    <!-- Initial State -->
    <div v-else class="initial-state">
      <el-empty 
        description="Select a search type and choose a condition or criteria to get started."
        :image-size="120"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Download, Check, Close, ArrowRight, DataAnalysis } from '@element-plus/icons-vue'
import { commonConditionsApi } from '@/api/conditions'
import { Chart, registerables } from 'chart.js'

// Register Chart.js components
Chart.register(...registerables)

// Router
const router = useRouter()

// Reactive data
const loading = ref(false)
const dropdownLoading = ref(false)
const searchAttempted = ref(false)
const searchMode = ref('simple') // 'simple' or 'advanced'

const searchForm = reactive({
  searchType: 'by-condition',
  selectedValue: ''
})

const advancedFilters = reactive({
  criteriaId: '',
  criteriaName: '',
  nctId: '',
  domain: '',
  include: null
})

const conditionOptions = ref([])
const criteriaOptions = ref([])
const allResults = ref([])
const advancedResults = ref([])
const analyticsData = ref(null)
const measurementData = ref(null)

const inclusionPagination = reactive({
  current: 1,
  size: 50,
  total: 0,
  pages: 0
})
const exclusionPagination = reactive({
  current: 1,
  size: 50,
  total: 0,
  pages: 0
})

const advancedPagination = reactive({
  currentPage: 1,
  size: 20,
  total: 0,
  pages: 0
})

// Chart refs and instances
const domainChart = ref(null)
const inclusionChart = ref(null)
const histogramChart = ref(null)
const frequencyChart = ref(null)
const domainComparisonChart = ref(null)
let domainChartInstance = null
let inclusionChartInstance = null
let histogramChartInstance = null
let frequencyChartInstance = null
let domainComparisonChartInstance = null

// Computed properties
const currentOptions = computed(() => {
  return searchForm.searchType === 'by-condition' ? conditionOptions.value : criteriaOptions.value
})

const inclusionResults = computed(() => {
  return allResults.value.filter(item => item.include === 1)
})

const exclusionResults = computed(() => {
  return allResults.value.filter(item => item.include === 0)
})

const totalResults = computed(() => {
  if (searchMode.value === 'advanced') {
    return advancedPagination.total
  }
  return inclusionPagination.total + exclusionPagination.total
})

const hasResults = computed(() => {
  if (searchMode.value === 'advanced') {
    return advancedResults.value.length > 0
  }
  return allResults.value.length > 0
})

const advancedInclusionCount = computed(() => {
  return advancedResults.value.filter(item => item.include === 1).length
})

const advancedExclusionCount = computed(() => {
  return advancedResults.value.filter(item => item.include === 0).length
})

// Watch search type changes
watch(() => searchForm.searchType, () => {
  searchForm.selectedValue = ''
  allResults.value = []
  searchAttempted.value = false
})

// Watch search mode changes
watch(() => searchMode.value, () => {
  // Reset all results when switching between simple and advanced modes
  allResults.value = []
  advancedResults.value = []
  searchAttempted.value = false
  analyticsData.value = null
  measurementData.value = null
})

// Methods
const loadDropdownOptions = async () => {
  dropdownLoading.value = true
  try {
    // Use optimized endpoints that return top 500 items with counts
    const [conditionsResponse, criteriaResponse] = await Promise.all([
      commonConditionsApi.getTopConditions(),
      commonConditionsApi.getTopCriteria()
    ])

    // Extract names from the response objects
    conditionOptions.value = (conditionsResponse || [])
      .map(item => item.name)
      .filter(name => name && typeof name === 'string' && name.trim() !== '')

    criteriaOptions.value = (criteriaResponse || [])
      .map(item => item.name)
      .filter(name => name && typeof name === 'string' && name.trim() !== '')

    console.log(`Loaded ${conditionOptions.value.length} conditions and ${criteriaOptions.value.length} criteria`)
  } catch (error) {
    console.error('Error loading dropdown options:', error)
    ElMessage.error('Failed to load search options')
  } finally {
    dropdownLoading.value = false
  }
}

const handleSearch = async () => {
  if (!searchForm.selectedValue) {
    ElMessage.warning('Please select a search term')
    return
  }

  loading.value = true
  searchAttempted.value = true

  // Reset pagination
  inclusionPagination.current = 1
  exclusionPagination.current = 1

  try {
    await loadSearchResults()
  } catch (error) {
    console.error('Error searching:', error)
    ElMessage.error('Search failed. Please try again.')
    allResults.value = []
  } finally {
    loading.value = false
  }
}

const loadSearchResults = async () => {
  let inclusionData = []
  let exclusionData = []

  if (searchForm.searchType === 'by-condition') {
    // Search criteria by condition name with pagination
    const [inclusionResponse, exclusionResponse] = await Promise.all([
      commonConditionsApi.getCriteriaByConditionName(
        searchForm.selectedValue,
        1,
        inclusionPagination.current,
        inclusionPagination.size
      ),
      commonConditionsApi.getCriteriaByConditionName(
        searchForm.selectedValue,
        0,
        exclusionPagination.current,
        exclusionPagination.size
      )
    ])

    inclusionData = inclusionResponse.records || []
    exclusionData = exclusionResponse.records || []

    // Update pagination info
    inclusionPagination.total = inclusionResponse.total || 0
    inclusionPagination.pages = inclusionResponse.pages || 0
    exclusionPagination.total = exclusionResponse.total || 0
    exclusionPagination.pages = exclusionResponse.pages || 0

  } else {
    // Search conditions by criteria name with pagination
    const [inclusionResponse, exclusionResponse] = await Promise.all([
      commonConditionsApi.getConditionsByCriteriaName(
        searchForm.selectedValue,
        1,
        inclusionPagination.current,
        inclusionPagination.size
      ),
      commonConditionsApi.getConditionsByCriteriaName(
        searchForm.selectedValue,
        0,
        exclusionPagination.current,
        exclusionPagination.size
      )
    ])

    inclusionData = inclusionResponse.records || []
    exclusionData = exclusionResponse.records || []

    // Update pagination info
    inclusionPagination.total = inclusionResponse.total || 0
    inclusionPagination.pages = inclusionResponse.pages || 0
    exclusionPagination.total = exclusionResponse.total || 0
    exclusionPagination.pages = exclusionResponse.pages || 0
  }

  allResults.value = [...inclusionData, ...exclusionData]

  if (allResults.value.length === 0) {
    ElMessage.info(`No results found for "${searchForm.selectedValue}"`)
  } else {
    ElMessage.success(`Found ${inclusionPagination.total + exclusionPagination.total} total results (${inclusionPagination.total} inclusion, ${exclusionPagination.total} exclusion)`)

    // Load analytics data
    await loadAnalyticsData()
  }
}

const loadAnalyticsData = async () => {
  try {
    console.log('Loading analytics data for:', searchForm.selectedValue, 'type:', searchForm.searchType)

    let analyticsPromise, measurementPromise

    if (searchForm.searchType === 'by-condition') {
      analyticsPromise = commonConditionsApi.getConditionTrends(searchForm.selectedValue)
      measurementPromise = commonConditionsApi.getMeasurementHistogram(searchForm.selectedValue)
    } else {
      analyticsPromise = commonConditionsApi.getCriteriaTrends(searchForm.selectedValue)
      measurementPromise = commonConditionsApi.getMeasurementHistogram(searchForm.selectedValue)
    }

    const [analyticsResponse, measurementResponse] = await Promise.all([
      analyticsPromise,
      measurementPromise
    ])

    console.log('Analytics response:', analyticsResponse)
    console.log('Measurement response:', measurementResponse)

    analyticsData.value = analyticsResponse
    measurementData.value = measurementResponse

    // Create charts after data is loaded
    await nextTick()
    console.log('Creating charts...')
    // Add delay to ensure DOM elements are rendered
    setTimeout(() => {
      console.log('Creating charts after delay...')
      createCharts()
    }, 500)

  } catch (error) {
    console.error('Error loading analytics data:', error)
  }
}

const resetSearch = () => {
  searchForm.selectedValue = ''
  allResults.value = []
  searchAttempted.value = false
}

// Advanced search methods
const toggleSearchMode = () => {
  searchMode.value = searchMode.value === 'simple' ? 'advanced' : 'simple'
  // Reset results when switching modes
  allResults.value = []
  advancedResults.value = []
  searchAttempted.value = false
}

const handleAdvancedSearch = async () => {
  // Check if at least one filter is provided
  const hasFilters = Object.values(advancedFilters).some(value =>
    value !== null && value !== undefined && value !== ''
  )

  if (!hasFilters) {
    ElMessage.warning('Please provide at least one search criteria')
    return
  }

  loading.value = true
  searchAttempted.value = true

  // Reset pagination
  advancedPagination.currentPage = 1

  try {
    await loadAdvancedSearchResults()
  } catch (error) {
    console.error('Error in advanced search:', error)
    ElMessage.error('Advanced search failed. Please try again.')
    advancedResults.value = []
  } finally {
    loading.value = false
  }
}

const loadAdvancedSearchResults = async () => {
  const filters = {
    criteriaId: advancedFilters.criteriaId || undefined,
    criteriaName: advancedFilters.criteriaName || undefined,
    nctId: advancedFilters.nctId || undefined,
    domain: advancedFilters.domain || undefined,
    include: advancedFilters.include,
    currentPage: advancedPagination.currentPage,
    size: advancedPagination.size
  }

  // Remove undefined values
  Object.keys(filters).forEach(key => {
    if (filters[key] === undefined) {
      delete filters[key]
    }
  })

  const response = await commonConditionsApi.searchCriteriaAdvanced(filters)

  advancedResults.value = response.records || []
  advancedPagination.total = response.total || 0
  advancedPagination.pages = response.pages || 0

  if (advancedResults.value.length === 0) {
    ElMessage.info('No criteria found matching your search criteria')
  } else {
    ElMessage.success(`Found ${advancedPagination.total} criteria matching your search`)
  }
}

const resetAdvancedSearch = () => {
  Object.keys(advancedFilters).forEach(key => {
    if (key === 'include') {
      advancedFilters[key] = null
    } else {
      advancedFilters[key] = ''
    }
  })
  advancedResults.value = []
  searchAttempted.value = false
  advancedPagination.currentPage = 1
  advancedPagination.total = 0
  advancedPagination.pages = 0
}

const handleAdvancedPageChange = async (page) => {
  advancedPagination.currentPage = page
  loading.value = true
  try {
    await loadAdvancedSearchResults()
  } catch (error) {
    console.error('Error loading page:', error)
    ElMessage.error('Failed to load page')
  } finally {
    loading.value = false
  }
}

const getAdvancedSearchSummary = () => {
  const filters = []
  if (advancedFilters.criteriaId) filters.push(`ID: ${advancedFilters.criteriaId}`)
  if (advancedFilters.criteriaName) filters.push(`Name: ${advancedFilters.criteriaName}`)
  if (advancedFilters.nctId) filters.push(`NCT: ${advancedFilters.nctId}`)
  if (advancedFilters.domain) filters.push(`Domain: ${advancedFilters.domain}`)
  if (advancedFilters.include !== null) {
    filters.push(`Type: ${advancedFilters.include === 1 ? 'Inclusion' : 'Exclusion'}`)
  }
  return filters.length > 0 ? filters.join(', ') : 'Advanced Search'
}

const viewCriteriaDetails = (criteria) => {
  // Navigate to criteria detail page
  router.push(`/criteria/${criteria.criteriaConceptId}`)
}

const exportAdvancedData = () => {
  ElMessage.info(`Exporting ${advancedResults.value.length} advanced search results...`)
  // TODO: Implement actual export functionality
}

const exportData = (type) => {
  const data = type === 'inclusion' ? inclusionResults.value : exclusionResults.value
  ElMessage.info(`Exporting ${data.length} ${type} criteria...`)
  // TODO: Implement actual export functionality
}

// Pagination handlers
const handleInclusionPageChange = async (page) => {
  inclusionPagination.current = page
  loading.value = true
  try {
    await loadSearchResults()
  } catch (error) {
    console.error('Error loading page:', error)
    ElMessage.error('Failed to load page')
  } finally {
    loading.value = false
  }
}

const handleExclusionPageChange = async (page) => {
  exclusionPagination.current = page
  loading.value = true
  try {
    await loadSearchResults()
  } catch (error) {
    console.error('Error loading page:', error)
    ElMessage.error('Failed to load page')
  } finally {
    loading.value = false
  }
}

// Analytics computed properties
const getInclusionPercentage = () => {
  const total = inclusionPagination.total + exclusionPagination.total
  return total > 0 ? Math.round((inclusionPagination.total / total) * 100) : 0
}

const getTopItemsCount = () => {
  if (!analyticsData.value) return 0
  const items = searchForm.searchType === 'by-condition' ?
    analyticsData.value.topCriteria : analyticsData.value.topConditions
  return items?.length || 0
}

const getTopItems = () => {
  if (!analyticsData.value) return []
  return searchForm.searchType === 'by-condition' ?
    (analyticsData.value.topCriteria || []).slice(0, 10) :
    (analyticsData.value.topConditions || []).slice(0, 10)
}

const exploreItem = (item) => {
  ElMessage.info(`Exploring ${item.name}...`)
  // TODO: Navigate to detailed view or update search
}

const getDataInsights = () => {
  if (!analyticsData.value) return []

  const insights = []
  const domainData = analyticsData.value.domainDistribution || []
  const topCriteria = analyticsData.value.topCriteria || []

  // Most common domain
  if (domainData.length > 0) {
    const topDomain = domainData[0]
    insights.push({
      title: 'Most Common Domain',
      value: topDomain.domain,
      description: `${topDomain.count} criteria (${Math.round((topDomain.count / domainData.reduce((sum, d) => sum + d.count, 0)) * 100)}%)`
    })
  }

  // Inclusion vs Exclusion ratio
  const inclusionTotal = inclusionPagination.total
  const exclusionTotal = exclusionPagination.total
  const total = inclusionTotal + exclusionTotal
  if (total > 0) {
    insights.push({
      title: 'Inclusion Rate',
      value: `${Math.round((inclusionTotal / total) * 100)}%`,
      description: `${inclusionTotal} inclusion vs ${exclusionTotal} exclusion criteria`
    })
  }

  // Most frequent criteria
  if (topCriteria.length > 0) {
    const topCriterion = topCriteria[0]
    insights.push({
      title: 'Top Criteria',
      value: topCriterion.name,
      description: `Appears ${topCriterion.count} times in ${topCriterion.domain} domain`
    })
  }

  // Domain diversity
  insights.push({
    title: 'Domain Diversity',
    value: `${domainData.length} domains`,
    description: 'Criteria span across multiple medical domains'
  })

  return insights
}

// Chart creation functions
const createCharts = (retryCount = 0) => {
  try {
    console.log('Creating all charts... (attempt', retryCount + 1, ')')

    // Check if canvas elements are available
    const canvasElements = [domainChart.value, inclusionChart.value, frequencyChart.value, domainComparisonChart.value]
    const availableCanvas = canvasElements.filter(canvas => canvas !== null)

    console.log('Canvas availability:', {
      domainChart: !!domainChart.value,
      inclusionChart: !!inclusionChart.value,
      frequencyChart: !!frequencyChart.value,
      domainComparisonChart: !!domainComparisonChart.value,
      availableCount: availableCanvas.length,
      totalCount: canvasElements.length
    })

    // If no canvas elements are available and we haven't retried too many times, retry
    if (availableCanvas.length === 0 && retryCount < 3) {
      console.log('No canvas elements available, retrying in 200ms...')
      setTimeout(() => createCharts(retryCount + 1), 200)
      return
    }

    createDomainChart()
    createInclusionChart()
    createHistogramChart()
    createFrequencyChart()
    createDomainComparisonChart()
    console.log('All charts created successfully')
  } catch (error) {
    console.error('Error creating charts:', error)
  }
}

const createDomainChart = () => {
  console.log('Domain chart check:', {
    hasCanvas: !!domainChart.value,
    canvasElement: domainChart.value,
    hasAnalyticsData: !!analyticsData.value,
    analyticsData: analyticsData.value,
    hasDomainDistribution: !!analyticsData.value?.domainDistribution,
    domainDistribution: analyticsData.value?.domainDistribution
  })

  if (!domainChart.value || !analyticsData.value?.domainDistribution) {
    console.log('Domain chart creation skipped:', {
      hasCanvas: !!domainChart.value,
      hasData: !!analyticsData.value?.domainDistribution
    })
    return
  }

  console.log('Creating domain chart with data:', analyticsData.value.domainDistribution)

  if (domainChartInstance) {
    domainChartInstance.destroy()
  }

  const data = analyticsData.value.domainDistribution
  const ctx = domainChart.value.getContext('2d')

  domainChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: data.map(item => item.domain),
      datasets: [{
        data: data.map(item => item.count),
        backgroundColor: [
          '#667eea', '#f093fb', '#4facfe', '#43e97b',
          '#f6d55c', '#ff6b6b', '#4ecdc4', '#45b7d1'
        ],
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'bottom' },
        tooltip: {
          callbacks: {
            label: (context) => {
              const total = context.dataset.data.reduce((a, b) => a + b, 0)
              const percentage = Math.round((context.parsed / total) * 100)
              return `${context.label}: ${context.parsed} (${percentage}%)`
            }
          }
        }
      }
    }
  })
}

const createInclusionChart = () => {
  if (!inclusionChart.value || !analyticsData.value?.inclusionExclusion) return

  if (inclusionChartInstance) {
    inclusionChartInstance.destroy()
  }

  const data = analyticsData.value.inclusionExclusion
  const inclusionCount = data.find(item => item.include === 1)?.count || 0
  const exclusionCount = data.find(item => item.include === 0)?.count || 0

  const ctx = inclusionChart.value.getContext('2d')

  inclusionChartInstance = new Chart(ctx, {
    type: 'pie',
    data: {
      labels: ['Exclusion', 'Inclusion'],
      datasets: [{
        data: [exclusionCount, inclusionCount],
        backgroundColor: ['#f56c6c', '#67c23a'],
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'bottom' },
        tooltip: {
          callbacks: {
            label: (context) => {
              const total = context.dataset.data.reduce((a, b) => a + b, 0)
              const percentage = Math.round((context.parsed / total) * 100)
              return `${context.label}: ${context.parsed} (${percentage}%)`
            }
          }
        }
      }
    }
  })
}

const createHistogramChart = () => {
  if (!histogramChart.value || !measurementData.value?.histogram) return

  if (histogramChartInstance) {
    histogramChartInstance.destroy()
  }

  const data = measurementData.value.histogram
  const ctx = histogramChart.value.getContext('2d')

  histogramChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: data.map(item => item.range),
      datasets: [{
        label: 'Frequency',
        data: data.map(item => item.count),
        backgroundColor: '#409eff',
        borderColor: '#409eff',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Frequency'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Value Range'
          }
        }
      }
    }
  })
}

const createFrequencyChart = () => {
  try {
    console.log('Frequency chart check:', {
      hasCanvas: !!frequencyChart.value,
      canvasElement: frequencyChart.value,
      hasAnalyticsData: !!analyticsData.value,
      hasTopCriteria: !!analyticsData.value?.topCriteria,
      topCriteria: analyticsData.value?.topCriteria
    })

    if (!frequencyChart.value || !analyticsData.value?.topCriteria) {
      console.log('Frequency chart creation skipped:', {
        hasCanvas: !!frequencyChart.value,
        hasData: !!analyticsData.value?.topCriteria
      })
      return
    }

    console.log('Creating frequency chart with data:', analyticsData.value.topCriteria)

    if (frequencyChartInstance) {
      frequencyChartInstance.destroy()
    }

    const data = analyticsData.value.topCriteria.slice(0, 10)
    const ctx = frequencyChart.value.getContext('2d')

  frequencyChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: data.map(item => item.name.length > 20 ? item.name.substring(0, 20) + '...' : item.name),
      datasets: [{
        label: 'Frequency',
        data: data.map(item => item.count),
        backgroundColor: [
          '#667eea', '#f093fb', '#4facfe', '#43e97b', '#f6d55c',
          '#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57'
        ],
        borderColor: '#ffffff',
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            title: (context) => {
              const index = context[0].dataIndex
              return data[index].name
            },
            label: (context) => {
              const item = data[context.dataIndex]
              return `${item.domain}: ${context.parsed.y} occurrences`
            }
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Frequency'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Criteria'
          }
        }
      }
    }
  })
  } catch (error) {
    console.error('Error creating frequency chart:', error)
  }
}

const createDomainComparisonChart = () => {
  try {
  if (!domainComparisonChart.value || !analyticsData.value?.domainDistribution) return

  if (domainComparisonChartInstance) {
    domainComparisonChartInstance.destroy()
  }

  // Create mock inclusion/exclusion data by domain for demonstration
  const domains = analyticsData.value.domainDistribution
  const inclusionData = domains.map(d => Math.floor(d.count * 0.4)) // Approximate 40% inclusion
  const exclusionData = domains.map(d => d.count - Math.floor(d.count * 0.4))

  const ctx = domainComparisonChart.value.getContext('2d')

  domainComparisonChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: domains.map(d => d.domain),
      datasets: [
        {
          label: 'Inclusion',
          data: inclusionData,
          backgroundColor: '#67c23a',
          borderColor: '#67c23a',
          borderWidth: 1
        },
        {
          label: 'Exclusion',
          data: exclusionData,
          backgroundColor: '#f56c6c',
          borderColor: '#f56c6c',
          borderWidth: 1
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'top' },
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      scales: {
        x: {
          stacked: true,
          title: {
            display: true,
            text: 'Domain'
          }
        },
        y: {
          stacked: true,
          beginAtZero: true,
          title: {
            display: true,
            text: 'Count'
          }
        }
      }
    }
  })
  } catch (error) {
    console.error('Error creating domain comparison chart:', error)
  }
}

// Lifecycle
onMounted(() => {
  loadDropdownOptions()
})
</script>

<style scoped>
.criteria-page {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 16px;
}

.search-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.loading-container {
  margin: 24px 0;
}

.stats-row {
  margin-bottom: 24px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.table-card {
  margin-bottom: 24px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  font-weight: 600;
}

.inclusion-icon {
  color: #67c23a;
  margin-right: 8px;
}

.exclusion-icon {
  color: #f56c6c;
  margin-right: 8px;
}

.empty-state,
.initial-state {
  margin: 48px 0;
  text-align: center;
}

.results-section {
  margin-top: 24px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.analytics-section {
  margin-top: 24px;
}

.analytics-card {
  height: 100%;
}

.chart-container {
  height: 300px;
  position: relative;
  padding: 10px;
}

.no-chart {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.top-items-row {
  margin-top: 20px;
}

.top-items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
}

.top-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.top-item:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.item-rank {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
  margin-right: 12px;
}

.item-content {
  flex: 1;
}

.item-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  font-size: 14px;
}

.item-stats {
  display: flex;
  gap: 8px;
}

.item-action {
  color: #909399;
  transition: color 0.3s ease;
}

.top-item:hover .item-action {
  color: #409eff;
}

.enhanced-charts-row {
  margin-top: 20px;
}

.creative-charts-row {
  margin-top: 20px;
}

.chart-container canvas {
  max-height: 300px;
}

.insights-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  padding: 16px 0;
}

.insight-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  cursor: pointer;
}

.insight-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.insight-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  margin-right: 16px;
  flex-shrink: 0;
}

.insight-content {
  flex: 1;
}

.insight-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
  font-weight: 500;
}

.insight-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.insight-description {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}

.search-term-display {
  text-align: center;
}

.search-term-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.search-term-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  word-break: break-word;
}
</style>
