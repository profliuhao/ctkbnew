<template>
  <div class="criteria-detail">
    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- Main Content -->
    <div v-else-if="criteria" class="detail-content">
      <!-- Header Section -->
      <el-card class="header-card">
        <div class="header-content">
          <div class="header-left">
            <el-button @click="goBack" :icon="ArrowLeft" type="default">
              Back to Criteria
            </el-button>
          </div>
          <div class="header-center">
            <h1 class="criteria-title">{{ criteria.criteriaConceptName }}</h1>
            <div class="criteria-meta">
              <el-tag size="large" type="primary">ID: {{ criteria.criteriaConceptId }}</el-tag>
              <el-tag size="large" type="info">{{ criteria.criteriaDomain }}</el-tag>
              <el-tag size="large" :type="getInclusionRatio() > 50 ? 'success' : 'warning'">
                {{ getInclusionRatio() }}% Inclusion
              </el-tag>
            </div>
          </div>
          <div class="header-right">
            <el-button @click="refreshData" :icon="Refresh" type="primary">
              Refresh
            </el-button>
            <el-button @click="createCharts" type="success" style="margin-left: 8px;">
              Refresh Charts
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- Statistics Overview -->
      <el-row :gutter="16" class="stats-overview">
        <el-col :span="6">
          <el-card class="stat-card">
            <el-statistic
              title="Total Conditions"
              :value="criteria.totalConditions"
              :precision="0"
            >
              <template #suffix>
                <el-icon style="vertical-align: -0.125em">
                  <List />
                </el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <el-statistic
              title="Total Occurrences"
              :value="criteria.totalOccurrences"
              :precision="0"
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
              title="Inclusion Uses"
              :value="criteria.inclusionCount"
              :precision="0"
            >
              <template #suffix>
                <el-icon style="vertical-align: -0.125em; color: #67c23a">
                  <Check />
                </el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <el-statistic
              title="Exclusion Uses"
              :value="criteria.exclusionCount"
              :precision="0"
            >
              <template #suffix>
                <el-icon style="vertical-align: -0.125em; color: #f56c6c">
                  <Close />
                </el-icon>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
      </el-row>

      <!-- Analytics Dashboard -->
      <el-row :gutter="20" class="analytics-row">
        <!-- Domain Distribution Chart -->
        <el-col :span="8">
          <el-card class="analytics-card">
            <template #header>
              <div class="card-header">
                <span>Domain Distribution</span>
                <el-tag size="small">{{ domainDistribution.length }} domains</el-tag>
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
                <span>Usage Type Distribution</span>
                <el-tag size="small" type="success">{{ getInclusionRatio() }}% inclusion</el-tag>
              </div>
            </template>
            <div class="chart-container">
              <canvas ref="inclusionChart"></canvas>
            </div>
          </el-card>
        </el-col>

        <!-- Measurement Histogram (if applicable) -->
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

      <!-- Related Conditions Table -->
      <el-card class="related-card">
        <template #header>
          <div class="card-header">
            <span>Related Conditions ({{ relatedConditions.length }} items)</span>
            <div class="header-actions">
              <el-button size="small" @click="exportData">
                <el-icon><Download /></el-icon>
                Export
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          :data="paginatedConditions"
          stripe
          border
          style="width: 100%"
          max-height="500"
        >
          <el-table-column 
            prop="conditionConceptId" 
            label="Condition ID" 
            width="120"
            sortable
          />
          <el-table-column 
            prop="conditionConceptName" 
            label="Condition Name" 
            min-width="300"
            show-overflow-tooltip
          />
          <el-table-column prop="include" label="Usage Type" width="120">
            <template #default="{ row }">
              <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="occurrenceCount" label="Occurrences" width="120" sortable />
          <el-table-column prop="totalCount" label="Total Count" width="120" sortable />
          <el-table-column label="Frequency" width="100">
            <template #default="{ row }">
              <span v-if="row.totalCount && criteria.totalOccurrences">
                {{ ((row.totalCount / criteria.totalOccurrences) * 100).toFixed(1) }}%
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="Actions" width="120" fixed="right">
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                @click="viewConditionDetails(row)"
              >
                Details
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="relatedConditions.length"
            layout="prev, pager, next, jumper, total"
            @current-change="handlePageChange"
            size="small"
          />
        </div>
      </el-card>
    </div>

    <!-- Error State -->
    <div v-else class="error-state">
      <el-result
        icon="warning"
        title="Criteria Not Found"
        :sub-title="`The criteria with ID ${route.params.id} could not be found.`"
      >
        <template #extra>
          <el-button type="primary" @click="goBack">Go Back</el-button>
          <el-button @click="loadCriteriaDetail">Retry</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { commonConditionsApi } from '@/api/conditions'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft,
  Refresh,
  List,
  Search,
  Download,
  Check,
  Close
} from '@element-plus/icons-vue'
import { Chart, registerables } from 'chart.js'

// Register Chart.js components
Chart.register(...registerables)

// Router
const router = useRouter()
const route = useRoute()

// Reactive data
const loading = ref(false)
const criteria = ref(null)
const relatedConditions = ref([])
const domainDistribution = ref([])
const inclusionBreakdown = ref([])
const measurementData = ref(null)

// Pagination
const currentPage = ref(1)
const pageSize = ref(20)

// Chart refs and instances
const domainChart = ref(null)
const inclusionChart = ref(null)
const histogramChart = ref(null)
let domainChartInstance = null
let inclusionChartInstance = null
let histogramChartInstance = null

// Computed properties
const paginatedConditions = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return relatedConditions.value.slice(start, end)
})

const totalPages = computed(() => {
  return Math.ceil(relatedConditions.value.length / pageSize.value)
})

const getInclusionRatio = () => {
  if (!criteria.value) return 0
  const total = criteria.value.inclusionCount + criteria.value.exclusionCount
  return total > 0 ? Math.round((criteria.value.inclusionCount / total) * 100) : 0
}

// Methods
const goBack = () => {
  router.push('/criteria')
}

const refreshData = async () => {
  await loadCriteriaDetail()
}

const loadCriteriaDetail = async () => {
  loading.value = true
  try {
    const criteriaId = route.params.id
    console.log('Loading criteria detail for ID:', criteriaId)

    const response = await commonConditionsApi.getCriteriaDetail(criteriaId)
    console.log('API response:', response)

    if (response.found && response.criteria) {
      criteria.value = response.criteria
      relatedConditions.value = response.relatedConditions || []
      domainDistribution.value = response.domainDistribution || []
      inclusionBreakdown.value = response.inclusionBreakdown || []

      console.log('Successfully loaded criteria:', criteria.value)

      // Load measurement histogram data
      await loadMeasurementData()

      // Create charts after data is loaded
      await nextTick()
      // Add a small delay to ensure DOM is fully rendered
      setTimeout(() => {
        createCharts()
      }, 100)

    } else {
      console.log('Criteria not found via API')
      criteria.value = null
    }
  } catch (error) {
    console.error('Error loading criteria detail:', error)
    ElMessage.error('Failed to load criteria details')
    criteria.value = null
  } finally {
    loading.value = false
  }
}

const loadMeasurementData = async () => {
  if (!criteria.value) return

  try {
    const response = await commonConditionsApi.getMeasurementHistogram(criteria.value.criteriaConceptName)
    measurementData.value = response
  } catch (error) {
    console.warn('Failed to load measurement data:', error)
    measurementData.value = { isMeasurement: false }
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const viewConditionDetails = (condition) => {
  // Navigate to condition detail page
  router.push(`/conditions/${condition.conditionConceptId}`)
}

const exportData = () => {
  ElMessage.info(`Exporting ${relatedConditions.value.length} related conditions...`)
  // TODO: Implement actual export functionality
}

// Chart creation functions
const createCharts = () => {
  try {
    console.log('Creating charts for criteria detail...')
    console.log('Domain distribution data:', domainDistribution.value)
    console.log('Inclusion breakdown data:', inclusionBreakdown.value)
    console.log('Measurement data:', measurementData.value)

    createDomainChart()
    createInclusionChart()
    createHistogramChart()

    console.log('All charts created successfully')
  } catch (error) {
    console.error('Error creating charts:', error)
  }
}

const createDomainChart = () => {
  try {
    if (!domainChart.value || !domainDistribution.value.length) {
      console.log('Domain chart creation skipped:', {
        hasCanvas: !!domainChart.value,
        hasData: !!domainDistribution.value.length,
        dataLength: domainDistribution.value.length
      })
      return
    }

    console.log('Creating domain chart with data:', domainDistribution.value)

    if (domainChartInstance) {
      domainChartInstance.destroy()
    }

  const ctx = domainChart.value.getContext('2d')

  domainChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: domainDistribution.value.map(item => item.domain),
      datasets: [{
        data: domainDistribution.value.map(item => item.count),
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
  } catch (error) {
    console.error('Error creating domain chart:', error)
  }
}

const createInclusionChart = () => {
  try {
    if (!inclusionChart.value || !inclusionBreakdown.value.length) {
      console.log('Inclusion chart creation skipped:', {
        hasCanvas: !!inclusionChart.value,
        hasData: !!inclusionBreakdown.value.length,
        dataLength: inclusionBreakdown.value.length
      })
      return
    }

    console.log('Creating inclusion chart with data:', inclusionBreakdown.value)

    if (inclusionChartInstance) {
      inclusionChartInstance.destroy()
    }

  const inclusionCount = inclusionBreakdown.value.find(item => item.include === 1)?.count || 0
  const exclusionCount = inclusionBreakdown.value.find(item => item.include === 0)?.count || 0

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
  } catch (error) {
    console.error('Error creating inclusion chart:', error)
  }
}

const createHistogramChart = () => {
  try {
    if (!histogramChart.value || !measurementData.value?.histogram) {
      console.log('Histogram chart creation skipped:', {
        hasCanvas: !!histogramChart.value,
        hasData: !!measurementData.value?.histogram,
        isMeasurement: measurementData.value?.isMeasurement
      })
      return
    }

    console.log('Creating histogram chart with data:', measurementData.value.histogram)

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
  } catch (error) {
    console.error('Error creating histogram chart:', error)
  }
}

// Lifecycle
onMounted(() => {
  loadCriteriaDetail()
})
</script>

<style scoped>
.criteria-detail {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.loading-container {
  padding: 40px;
}

.header-card {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  flex: 0 0 auto;
}

.header-center {
  flex: 1;
  text-align: center;
  margin: 0 20px;
}

.header-right {
  flex: 0 0 auto;
}

.criteria-title {
  margin: 0 0 12px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.criteria-meta {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

.stats-overview {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
  height: 100%;
}

.analytics-row {
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

.no-chart {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.related-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.error-state {
  padding: 40px;
  text-align: center;
}

/* Responsive design */
@media (max-width: 768px) {
  .criteria-detail {
    padding: 10px;
  }

  .header-content {
    flex-direction: column;
    gap: 16px;
  }

  .header-center {
    margin: 0;
  }

  .criteria-meta {
    justify-content: center;
  }
}
</style>
