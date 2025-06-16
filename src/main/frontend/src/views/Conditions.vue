<template>
  <div class="conditions">
    <!-- Page Header -->
    <div class="page-header">
      <h1>Conditions</h1>
      <p>Browse and search clinical trial conditions</p>
    </div>

    <!-- Search and Filters -->
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>Search & Filters</span>
          <el-button @click="resetFilters" size="small">Reset</el-button>
        </div>
      </template>
      <el-form :model="searchFilters" label-width="120px" class="search-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Concept ID">
              <el-input
                v-model="searchFilters.conceptId"
                placeholder="Enter concept ID"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Concept Name">
              <el-input
                v-model="searchFilters.conceptName"
                placeholder="Enter concept name"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="NCT ID">
              <el-input
                v-model="searchFilters.nctId"
                placeholder="Enter NCT ID"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Domain">
              <el-select v-model="searchFilters.domain" placeholder="Select domain" clearable>
                <el-option label="Condition" value="Condition" />
                <!-- Note: Only Condition domain is available in ec_condition table -->
                <!-- Other domains (Drug, Procedure, etc.) are in separate tables -->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Include/Exclude">
              <el-select v-model="searchFilters.include" placeholder="Select type">
                <el-option label="Inclusion" :value="1" />
                <el-option label="Exclusion" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item>
              <el-button type="primary" @click="handleSearch" :loading="loading">
                <el-icon><Search /></el-icon>
                Search
              </el-button>
              <el-button @click="loadAllConditions">
                <el-icon><Refresh /></el-icon>
                Load All
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- Global Database Statistics -->
    <el-row :gutter="20" class="overview-stats">
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic
            title="Total Database Conditions"
            :value="globalStats.totalDatabaseConditions"
            :precision="0"
          >
            <template #suffix>
              <el-icon style="vertical-align: -0.125em">
                <TrendCharts />
              </el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic
            title="Most Common Condition"
            :value="globalStats.topConditionName"
            value-style="font-size: 14px;"
          >
            <template #suffix>
              <el-icon style="vertical-align: -0.125em">
                <Search />
              </el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic
            title="Active Trials"
            :value="globalStats.activeTrials"
            :precision="0"
          >
            <template #suffix>
              <el-icon style="vertical-align: -0.125em; color: #67c23a">
                <ArrowRight />
              </el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <el-statistic
            title="Criteria Coverage"
            :value="globalStats.criteriaCoverage"
            suffix="%"
            :precision="1"
          >
            <template #suffix>
              <span>%</span>
              <el-icon style="vertical-align: -0.125em; color: #f6d55c">
                <TrendCharts />
              </el-icon>
            </template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- Enhanced Analytics Dashboard -->
    <div class="analytics-section">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h3>Analytics Dashboard</h3>
        <div>
          <el-button @click="refreshAnalytics" type="primary" size="small" :loading="analyticsLoading">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
          <el-button @click="toggleAnalyticsView" type="default" size="small">
            {{ showDetailedAnalytics ? 'Simple View' : 'Detailed View' }}
          </el-button>
        </div>
      </div>

      <!-- Primary Charts Row -->
      <el-row :gutter="20">
        <!-- Top Conditions by Frequency -->
        <el-col :span="12">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Top Conditions by Frequency</span>
                <el-tag size="small">{{ topConditions.length }} conditions</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="topConditionsChart"></canvas>
            </div>
          </el-card>
        </el-col>

        <!-- Top Condition Types -->
        <el-col :span="6">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Top Condition Types</span>
                <el-tag size="small">{{ getTopConditionTypes().length }} types</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="domainChart"></canvas>
            </div>
          </el-card>
        </el-col>

        <!-- Inclusion vs Exclusion -->
        <el-col :span="6">
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
      </el-row>

      <!-- Detailed Analytics Row (when enabled) -->
      <el-row v-if="showDetailedAnalytics" :gutter="20" class="detailed-analytics-row">
        <!-- Score Distribution -->
        <el-col :span="8">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Quality Score Distribution</span>
                <el-tag size="small" type="warning">Score Analysis</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="scoreChart"></canvas>
            </div>
          </el-card>
        </el-col>

        <!-- Trials by Year -->
        <el-col :span="8">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Trials by Year</span>
                <el-tag size="small" type="info">Temporal Analysis</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="nctChart"></canvas>
            </div>
          </el-card>
        </el-col>

        <!-- Temporal Analysis -->
        <el-col :span="8">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Temporal Trends</span>
                <el-tag size="small" type="primary">Time Series</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="temporalChart"></canvas>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Interactive Insights -->
      <el-row :gutter="20" class="insights-row">
        <el-col :span="24">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Data Insights & Quick Actions</span>
                <el-tag size="small" type="warning">Interactive</el-tag>
              </div>
            </template>
            <div class="insights-grid">
              <div class="insight-card" v-for="(insight, index) in getDataInsights()" :key="index" @click="handleInsightClick(insight)">
                <div class="insight-icon">
                  <el-icon><component :is="insight.icon" /></el-icon>
                </div>
                <div class="insight-content">
                  <div class="insight-title">{{ insight.title }}</div>
                  <div class="insight-value">{{ insight.value }}</div>
                  <div class="insight-description">{{ insight.description }}</div>
                </div>
                <div class="insight-action">
                  <el-icon><ArrowRight /></el-icon>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Top Concepts -->
      <el-row :gutter="20" class="top-concepts-row">
        <el-col :span="24">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Top Concepts by Frequency</span>
                <el-tag size="small">{{ getUniqueConceptsCount() }} unique concepts</el-tag>
              </div>
            </template>
            <div class="concepts-grid">
              <div
                v-for="(concept, index) in getTopConcepts()"
                :key="concept.conceptId"
                class="concept-item"
                @click="exploreConcept(concept)"
              >
                <div class="concept-rank">{{ index + 1 }}</div>
                <div class="concept-content">
                  <div class="concept-name">{{ concept.conceptName }}</div>
                  <div class="concept-stats">
                    <el-tag size="small">ID: {{ concept.conceptId }}</el-tag>
                    <el-tag size="small" type="info">{{ concept.domain }}</el-tag>
                    <el-tag size="small" :type="concept.include === 1 ? 'success' : 'danger'">
                      {{ concept.include === 1 ? 'Inclusion' : 'Exclusion' }}
                    </el-tag>
                    <el-tag size="small" type="warning">Score: {{ concept.score }}</el-tag>
                  </div>
                </div>
                <div class="concept-action">
                  <el-icon><ArrowRight /></el-icon>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- Results Table -->
    <el-card class="results-card">
      <template #header>
        <div class="card-header">
          <span>Results ({{ pagination.total }} total)</span>
          <div class="header-actions">
            <el-button
              size="small"
              @click="toggleAnalyticsView"
              :type="showDetailedAnalytics ? 'primary' : 'default'"
            >
              <el-icon><TrendCharts /></el-icon>
              {{ showDetailedAnalytics ? 'Hide Detailed' : 'Show Detailed' }} Analytics
            </el-button>
            <el-button size="small" @click="exportData">
              <el-icon><Download /></el-icon>
              Export
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        :data="conditions"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
        @row-click="handleRowClick"
        class="conditions-table"
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
        <el-table-column label="Actions" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click.stop="viewDetails(row)"
            >
              View
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useConditionsStore } from '@/stores/conditions'
import { commonConditionsApi } from '@/api/conditions'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Download, TrendCharts, ArrowRight } from '@element-plus/icons-vue'
import { Chart, registerables } from 'chart.js'

// Register Chart.js components
Chart.register(...registerables)

const router = useRouter()
const route = useRoute()
const conditionsStore = useConditionsStore()

// Reactive data
const loading = ref(false)
const conditions = ref([])
const showAnalytics = ref(true) // Always show analytics now
const showDetailedAnalytics = ref(false)
const analyticsLoading = ref(false)
const topConditions = ref([])
const globalStats = reactive({
  totalDatabaseConditions: 0,
  topConditionName: 'Loading...',
  activeTrials: 0,
  criteriaCoverage: 0
})
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const searchFilters = reactive({
  conceptId: '',
  conceptName: '',
  nctId: '',
  domain: '',
  include: 1
})

// Chart refs and instances
const domainChart = ref(null)
const inclusionChart = ref(null)
const scoreChart = ref(null)
const topConditionsChart = ref(null)
const nctChart = ref(null)
const temporalChart = ref(null)
let domainChartInstance = null
let inclusionChartInstance = null
let scoreChartInstance = null
let topConditionsChartInstance = null
let nctChartInstance = null
let temporalChartInstance = null

// Watch store changes
watch(
  () => conditionsStore.getConditions,
  (newConditions) => {
    conditions.value = newConditions
  }
)

watch(
  () => conditionsStore.getPagination,
  (newPagination) => {
    Object.assign(pagination, newPagination)
  }
)

watch(
  () => conditionsStore.isLoading,
  (newLoading) => {
    loading.value = newLoading
  }
)

// Methods
const handleSearch = async () => {
  pagination.current = 1
  await performSearch()
}

const performSearch = async () => {
  const { conceptId, conceptName, nctId, domain, include } = searchFilters

  // If no search criteria, load all conditions
  if (!conceptId && !conceptName && !nctId && !domain && include === '') {
    await conditionsStore.fetchAllConditions()
    return
  }

  // Use advanced search to include all filters
  const filters = {
    conceptId: conceptId || undefined,
    conceptName: conceptName || undefined,
    nctId: nctId || undefined,
    domain: domain || undefined,
    include: include !== '' ? include : undefined,
    currentPage: pagination.current,
    size: pagination.size
  }

  // Remove undefined values
  Object.keys(filters).forEach(key => {
    if (filters[key] === undefined || filters[key] === '') {
      delete filters[key]
    }
  })

  await conditionsStore.searchConditionsAdvanced(filters)
}

const loadAllConditions = async () => {
  resetFilters()
  await conditionsStore.fetchAllConditions()
}

const resetFilters = () => {
  searchFilters.conceptId = ''
  searchFilters.conceptName = ''
  searchFilters.nctId = ''
  searchFilters.domain = ''
  searchFilters.include = 1
  pagination.current = 1
}

const handleRowClick = (row) => {
  viewDetails(row)
}

const viewDetails = (condition) => {
  console.log('Viewing details for condition:', condition)
  console.log('Condition ID:', condition.id)

  if (!condition.id) {
    ElMessage.error('Condition ID is missing')
    return
  }

  router.push(`/conditions/${condition.id}`)
}

const handleSizeChange = (newSize) => {
  pagination.size = newSize
  pagination.current = 1
  performSearch()
}

const handleCurrentChange = (newPage) => {
  pagination.current = newPage
  performSearch()
}

const exportData = () => {
  // TODO: Implement export functionality
  ElMessage.info('Export functionality will be implemented soon')
}

// Enhanced Analytics methods
const toggleAnalyticsView = () => {
  showDetailedAnalytics.value = !showDetailedAnalytics.value
  if (showDetailedAnalytics.value) {
    setTimeout(() => createCharts(), 100)
  }
}

const refreshAnalytics = async () => {
  analyticsLoading.value = true
  try {
    await loadTopConditions()
    await loadGlobalStats()
    await nextTick()
    createCharts()
  } catch (error) {
    console.error('Error refreshing analytics:', error)
    ElMessage.error('Failed to refresh analytics')
  } finally {
    analyticsLoading.value = false
  }
}

const loadTopConditions = async () => {
  try {
    // Use the existing API to get top conditions
    const response = await commonConditionsApi.getTopConditions()
    topConditions.value = response || []
  } catch (error) {
    console.error('Error loading top conditions:', error)
    topConditions.value = []
  }
}

const loadGlobalStats = async () => {
  try {
    // Get total database conditions count
    const allConditionsResponse = await conditionsStore.fetchAllConditions()
    globalStats.totalDatabaseConditions = conditionsStore.getPagination.total || 0

    // Get top condition name from our loaded data
    if (topConditions.value.length > 0) {
      globalStats.topConditionName = topConditions.value[0].name
    }

    // Calculate active trials (unique NCT IDs)
    const uniqueTrials = new Set()
    conditions.value.forEach(condition => {
      if (condition.nctid) {
        uniqueTrials.add(condition.nctid)
      }
    })
    globalStats.activeTrials = uniqueTrials.size

    // Calculate criteria coverage (percentage of conditions with criteria)
    const conditionsWithCriteria = topConditions.value.reduce((sum, condition) => sum + condition.criteriaCount, 0)
    const totalPossibleCriteria = globalStats.totalDatabaseConditions * 10 // Rough estimate
    globalStats.criteriaCoverage = totalPossibleCriteria > 0 ?
      Math.min((conditionsWithCriteria / totalPossibleCriteria) * 100, 100) : 0

  } catch (error) {
    console.error('Error loading global stats:', error)
    // Set fallback values
    globalStats.totalDatabaseConditions = conditions.value.length
    globalStats.topConditionName = 'Unknown'
    globalStats.activeTrials = 0
    globalStats.criteriaCoverage = 0
  }
}

const getDataInsights = () => {
  const insights = []

  // Database coverage insight
  const databaseCoverage = globalStats.totalDatabaseConditions > 0 ?
    Math.round((conditions.value.length / globalStats.totalDatabaseConditions) * 100) : 0

  insights.push({
    title: 'Database Coverage',
    value: `${databaseCoverage}%`,
    description: `${conditions.value.length} of ${globalStats.totalDatabaseConditions} conditions`,
    icon: 'TrendCharts',
    action: 'view-all-conditions',
    data: 'all'
  })

  // Unique trials insight
  const uniqueTrials = new Set(conditions.value.map(c => c.nctid).filter(id => id))
  insights.push({
    title: 'Unique Trials',
    value: uniqueTrials.size,
    description: 'Distinct clinical trials',
    icon: 'Search',
    action: 'explore-trials',
    data: Array.from(uniqueTrials)
  })

  // Most studied condition
  const topCondition = topConditions.value.length > 0 ? topConditions.value[0] : null
  if (topCondition) {
    insights.push({
      title: 'Most Studied',
      value: topCondition.name.length > 15 ? topCondition.name.substring(0, 15) + '...' : topCondition.name,
      description: `${topCondition.criteriaCount} criteria`,
      icon: 'ArrowRight',
      action: 'explore-condition',
      data: topCondition.name
    })
  }

  // Research focus insight
  const inclusionCount = conditions.value.filter(c => c.include === 1).length
  const exclusionCount = conditions.value.filter(c => c.include === 0).length
  const focusType = inclusionCount > exclusionCount ? 'Inclusion-focused' :
                   exclusionCount > inclusionCount ? 'Exclusion-focused' : 'Balanced'

  insights.push({
    title: 'Research Focus',
    value: focusType,
    description: `${inclusionCount} inclusion, ${exclusionCount} exclusion`,
    icon: 'Refresh',
    action: 'analyze-focus',
    data: { inclusion: inclusionCount, exclusion: exclusionCount }
  })

  return insights
}

const handleInsightClick = (insight) => {
  switch (insight.action) {
    case 'view-all-conditions':
      resetFilters()
      loadAllConditions()
      ElMessage.success('Loading all conditions from database')
      break
    case 'explore-trials':
      ElMessage.info(`Found ${insight.data.length} unique trials`)
      // Could navigate to trials page or show trial details
      break
    case 'explore-condition':
      searchFilters.conceptName = insight.data
      handleSearch()
      ElMessage.success(`Exploring condition: ${insight.data}`)
      break
    case 'analyze-focus':
      const { inclusion, exclusion } = insight.data
      ElMessage.info(`Research focus analysis: ${inclusion} inclusion vs ${exclusion} exclusion criteria`)
      break
  }
}

const getDomainCount = () => {
  const domains = new Set(conditions.value.map(c => c.domain).filter(d => d))
  return domains.size
}

const getTopConditionTypes = () => {
  // Group conditions by concept name and count occurrences
  const typeMap = new Map()

  conditions.value.forEach(condition => {
    if (condition.conceptName) {
      const key = condition.conceptName
      if (typeMap.has(key)) {
        typeMap.get(key).count++
      } else {
        typeMap.set(key, {
          name: condition.conceptName,
          count: 1
        })
      }
    }
  })

  return Array.from(typeMap.values())
    .sort((a, b) => b.count - a.count)
    .slice(0, 8) // Top 8 for better visualization
}

const getInclusionPercentage = () => {
  const total = conditions.value.length
  const inclusion = conditions.value.filter(c => c.include === 1).length
  return total > 0 ? Math.round((inclusion / total) * 100) : 0
}

const getUniqueConceptsCount = () => {
  const concepts = new Set(conditions.value.map(c => c.conceptId).filter(id => id))
  return concepts.size
}

const getTopConcepts = () => {
  // Group by concept and count occurrences
  const conceptMap = new Map()

  conditions.value.forEach(condition => {
    const key = condition.conceptId
    if (key && condition.conceptName) {
      if (conceptMap.has(key)) {
        conceptMap.get(key).count++
      } else {
        conceptMap.set(key, {
          conceptId: condition.conceptId,
          conceptName: condition.conceptName,
          domain: condition.domain,
          include: condition.include,
          score: condition.score,
          count: 1
        })
      }
    }
  })

  return Array.from(conceptMap.values())
    .sort((a, b) => b.count - a.count)
    .slice(0, 10)
}

const exploreConcept = (concept) => {
  // Update search filters to explore this concept
  searchFilters.conceptId = concept.conceptId
  handleSearch()
  ElMessage.success(`Exploring concept: ${concept.conceptName}`)
}

// Chart creation functions
const createCharts = () => {
  setTimeout(() => {
    createTopConditionsChart()
    createDomainChart()
    createInclusionChart()
    if (showDetailedAnalytics.value) {
      createScoreChart()
      createNctChart()
      createTemporalChart()
    }
  }, 100)
}

const createDomainChart = () => {
  if (!domainChart.value || conditions.value.length === 0) return

  if (domainChartInstance) {
    domainChartInstance.destroy()
  }

  // Get top condition types
  const topTypes = getTopConditionTypes()
  if (topTypes.length === 0) return

  const ctx = domainChart.value.getContext('2d')

  domainChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: topTypes.map(type => type.name.length > 12 ? type.name.substring(0, 12) + '...' : type.name),
      datasets: [{
        data: topTypes.map(type => type.count),
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
        legend: {
          position: 'bottom',
          labels: {
            boxWidth: 12,
            font: {
              size: 10
            }
          }
        },
        tooltip: {
          callbacks: {
            title: (context) => {
              const index = context[0].dataIndex
              return topTypes[index].name
            },
            label: (context) => {
              const total = context.dataset.data.reduce((a, b) => a + b, 0)
              const percentage = Math.round((context.parsed / total) * 100)
              return `Count: ${context.parsed} (${percentage}%)`
            }
          }
        }
      }
    }
  })
}

const createInclusionChart = () => {
  if (!inclusionChart.value || conditions.value.length === 0) return

  if (inclusionChartInstance) {
    inclusionChartInstance.destroy()
  }

  const inclusionCount = conditions.value.filter(c => c.include === 1).length
  const exclusionCount = conditions.value.filter(c => c.include === 0).length

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

const createScoreChart = () => {
  if (!scoreChart.value || conditions.value.length === 0) return

  if (scoreChartInstance) {
    scoreChartInstance.destroy()
  }

  // Create score distribution histogram
  const scores = conditions.value
    .map(c => c.score)
    .filter(score => score !== null && score !== undefined)

  if (scores.length === 0) return

  // Create bins
  const bins = [0, 0.2, 0.4, 0.6, 0.8, 1.0]
  const binCounts = new Array(bins.length - 1).fill(0)
  const binLabels = []

  for (let i = 0; i < bins.length - 1; i++) {
    binLabels.push(`${bins[i].toFixed(1)} - ${bins[i + 1].toFixed(1)}`)
  }

  scores.forEach(score => {
    for (let i = 0; i < bins.length - 1; i++) {
      if (score >= bins[i] && score <= bins[i + 1]) {
        binCounts[i]++
        break
      }
    }
  })

  const ctx = scoreChart.value.getContext('2d')

  scoreChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: binLabels,
      datasets: [{
        label: 'Frequency',
        data: binCounts,
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
            text: 'Score Range'
          }
        }
      }
    }
  })
}

const createTopConditionsChart = () => {
  if (!topConditionsChart.value || topConditions.value.length === 0) return

  if (topConditionsChartInstance) {
    topConditionsChartInstance.destroy()
  }

  const data = topConditions.value.slice(0, 10)
  const ctx = topConditionsChart.value.getContext('2d')

  topConditionsChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: data.map(item => item.name.length > 15 ? item.name.substring(0, 15) + '...' : item.name),
      datasets: [{
        label: 'Criteria Count',
        data: data.map(item => item.criteriaCount || item.conditionCount || item.count || 0),
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
              return [
                `Criteria Count: ${context.parsed.y}`,
                `Inclusion: ${item.inclusionCount || 0}`,
                `Exclusion: ${item.exclusionCount || 0}`
              ]
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
            text: 'Conditions'
          }
        }
      }
    }
  })
}

const createNctChart = () => {
  if (!nctChart.value || conditions.value.length === 0) return

  if (nctChartInstance) {
    nctChartInstance.destroy()
  }

  // Analyze NCT ID patterns (year distribution)
  const yearCounts = {}
  const uniqueTrials = new Set()

  conditions.value.forEach(condition => {
    if (condition.nctid && !uniqueTrials.has(condition.nctid)) {
      uniqueTrials.add(condition.nctid)

      // Extract year from NCT ID (format: NCT########)
      // NCT IDs starting with 00-99 map to years 2000-2099
      const match = condition.nctid.match(/NCT(\d{8})/)
      if (match) {
        const nctNumber = match[1]
        const yearPrefix = nctNumber.substring(0, 2)

        // Map NCT year prefixes to actual years
        let year
        if (yearPrefix >= '00' && yearPrefix <= '30') {
          year = '20' + yearPrefix
        } else if (yearPrefix >= '99' && yearPrefix <= '99') {
          year = '19' + yearPrefix
        } else {
          year = '20' + yearPrefix // Default to 2000s
        }

        yearCounts[year] = (yearCounts[year] || 0) + 1
      }
    }
  })

  // If no valid years found, create mock data
  if (Object.keys(yearCounts).length === 0) {
    const currentYear = new Date().getFullYear()
    for (let i = currentYear - 5; i <= currentYear; i++) {
      yearCounts[i.toString()] = Math.floor(Math.random() * 50) + 10
    }
  }

  const sortedYears = Object.keys(yearCounts).sort()
  const ctx = nctChart.value.getContext('2d')

  nctChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: sortedYears,
      datasets: [{
        label: 'Number of Trials',
        data: sortedYears.map(year => yearCounts[year]),
        backgroundColor: '#409eff',
        borderColor: '#409eff',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (context) => {
              return `Trials: ${context.parsed.y}`
            }
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Number of Trials'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Year'
          }
        }
      }
    }
  })
}

const createTemporalChart = () => {
  if (!temporalChart.value) return

  if (temporalChartInstance) {
    temporalChartInstance.destroy()
  }

  // Mock temporal data for demonstration
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']
  const inclusionData = [120, 135, 150, 140, 160, 155]
  const exclusionData = [80, 85, 90, 95, 85, 90]

  const ctx = temporalChart.value.getContext('2d')

  temporalChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: months,
      datasets: [
        {
          label: 'Inclusion Criteria',
          data: inclusionData,
          borderColor: '#67c23a',
          backgroundColor: 'rgba(103, 194, 58, 0.1)',
          fill: false,
          tension: 0.4
        },
        {
          label: 'Exclusion Criteria',
          data: exclusionData,
          borderColor: '#f56c6c',
          backgroundColor: 'rgba(245, 108, 108, 0.1)',
          fill: false,
          tension: 0.4
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'top' }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Count'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Month'
          }
        }
      }
    }
  })
}

// Initialize from route query parameters
const initializeFromQuery = () => {
  const query = route.query
  if (query.conceptId) searchFilters.conceptId = query.conceptId
  if (query.conceptName) searchFilters.conceptName = query.conceptName
  if (query.nctId) searchFilters.nctId = query.nctId
}

onMounted(async () => {
  initializeFromQuery()

  // Load initial data
  if (searchFilters.conceptId || searchFilters.conceptName || searchFilters.nctId) {
    await handleSearch()
  } else {
    await loadAllConditions()
  }

  // Load analytics data
  await loadTopConditions()
  await loadGlobalStats()

  // Create charts after data is loaded
  await nextTick()
  createCharts()
})
</script>

<style scoped>
.conditions {
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

.search-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 0;
}

.results-card {
  margin-bottom: 24px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.conditions-table {
  margin-bottom: 16px;
}

.conditions-table :deep(.el-table__row) {
  cursor: pointer;
}

.conditions-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 16px 0;
}

.analytics-section {
  margin-bottom: 24px;
}

.analytics-card {
  height: 100%;
}

.chart-container {
  height: 300px;
  position: relative;
  padding: 10px;
}

.top-concepts-row {
  margin-top: 20px;
}

.concepts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
}

.concept-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.concept-item:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.concept-rank {
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

.concept-content {
  flex: 1;
}

.concept-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  font-size: 14px;
}

.concept-stats {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.concept-action {
  color: #909399;
  transition: color 0.3s ease;
}

.concept-item:hover .concept-action {
  color: #409eff;
}

.overview-stats {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
  height: 100%;
}

.detailed-analytics-row {
  margin-top: 20px;
}

.insights-row {
  margin-bottom: 20px;
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

.insight-action {
  color: #909399;
  transition: color 0.3s ease;
}

.insight-card:hover .insight-action {
  color: #409eff;
}
</style>
