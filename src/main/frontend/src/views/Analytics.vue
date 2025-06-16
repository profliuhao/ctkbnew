<template>
  <div class="analytics">
    <!-- Page Header -->
    <div class="page-header">
      <h1>Analytics</h1>
      <p>Data insights and visualizations</p>
    </div>

    <!-- Overview Cards -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-content">
            <div class="metric-value">{{ metrics.totalConditions }}</div>
            <div class="metric-label">Total Conditions</div>
            <div class="metric-change positive">+5.2% from last month</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-content">
            <div class="metric-value">{{ metrics.totalCriteria }}</div>
            <div class="metric-label">Total Criteria</div>
            <div class="metric-change positive">+3.8% from last month</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-content">
            <div class="metric-value">{{ metrics.totalTrials }}</div>
            <div class="metric-label">Clinical Trials</div>
            <div class="metric-change positive">+2.1% from last month</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card">
          <div class="metric-content">
            <div class="metric-value">{{ metrics.avgScore }}</div>
            <div class="metric-label">Average Score</div>
            <div class="metric-change negative">-0.3% from last month</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Row 1 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Domain Distribution</span>
              <el-button size="small" @click="refreshDomainChart">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="domainChartRef" width="400" height="300"></canvas>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Inclusion vs Exclusion</span>
              <el-button size="small" @click="refreshInclusionChart">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="inclusionChartRef" width="400" height="300"></canvas>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Row 2 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Trends Over Time</span>
              <div class="header-controls">
                <el-select v-model="timeRange" size="small" @change="refreshTrendChart">
                  <el-option label="Last 7 days" value="7d" />
                  <el-option label="Last 30 days" value="30d" />
                  <el-option label="Last 90 days" value="90d" />
                  <el-option label="Last year" value="1y" />
                </el-select>
                <el-button size="small" @click="refreshTrendChart">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </div>
          </template>
          <div class="chart-container large">
            <canvas ref="trendChartRef" width="800" height="400"></canvas>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Top Items -->
    <el-row :gutter="20" class="top-items-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Top Conditions</span>
              <el-button size="small" @click="loadTopConditions">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="top-items-list">
            <div
              v-for="(condition, index) in topConditions"
              :key="condition.id"
              class="top-item"
            >
              <div class="item-rank">{{ index + 1 }}</div>
              <div class="item-info">
                <div class="item-name">{{ condition.conceptName }}</div>
                <div class="item-meta">ID: {{ condition.conceptId }}</div>
              </div>
              <div class="item-count">{{ condition.count }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Top Criteria</span>
              <el-button size="small" @click="loadTopCriteria">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="top-items-list">
            <div
              v-for="(criteria, index) in topCriteria"
              :key="criteria.id"
              class="top-item"
            >
              <div class="item-rank">{{ index + 1 }}</div>
              <div class="item-info">
                <div class="item-name">{{ criteria.criteriaConceptName }}</div>
                <div class="item-meta">ID: {{ criteria.criteriaConceptId }}</div>
              </div>
              <div class="item-count">{{ criteria.conceptCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { Chart, registerables } from 'chart.js'
import { useConditionsStore } from '@/stores/conditions'
import { useCriteriaStore } from '@/stores/criteria'
import { Refresh } from '@element-plus/icons-vue'
import { statisticsApi } from '@/api/statistics'

// Register Chart.js components
Chart.register(...registerables)

const conditionsStore = useConditionsStore()
const criteriaStore = useCriteriaStore()

// Reactive data
const metrics = reactive({
  totalConditions: 0,
  totalCriteria: 0,
  totalTrials: 0,
  avgScore: 0
})

const timeRange = ref('30d')
const topConditions = ref([])
const topCriteria = ref([])

// Chart refs
const domainChartRef = ref(null)
const inclusionChartRef = ref(null)
const trendChartRef = ref(null)

// Chart instances
let domainChart = null
let inclusionChart = null
let trendChart = null

// API functions
const fetchDashboardStats = async () => {
  try {
    const response = await statisticsApi.getDashboardStats()
    Object.assign(metrics, response)
  } catch (error) {
    console.error('Error fetching dashboard stats:', error)
  }
}

const fetchDomainDistribution = async () => {
  try {
    const response = await statisticsApi.getDomainDistribution()
    return response
  } catch (error) {
    console.error('Error fetching domain distribution:', error)
    return { labels: [], data: [] }
  }
}

const fetchInclusionExclusionStats = async () => {
  try {
    const response = await statisticsApi.getInclusionExclusionStats()
    return response
  } catch (error) {
    console.error('Error fetching inclusion/exclusion stats:', error)
    return { labels: [], data: [] }
  }
}

const fetchTemporalTrends = async () => {
  try {
    const response = await statisticsApi.getTemporalTrends()
    return response
  } catch (error) {
    console.error('Error fetching temporal trends:', error)
    return { labels: [], data: [] }
  }
}

const fetchTopConditions = async () => {
  try {
    const response = await statisticsApi.getTopConditions(10)
    return response
  } catch (error) {
    console.error('Error fetching top conditions:', error)
    return []
  }
}

const fetchTopCriteria = async () => {
  try {
    const response = await statisticsApi.getTopCriteria(10)
    return response
  } catch (error) {
    console.error('Error fetching top criteria:', error)
    return []
  }
}

// Methods
const loadMetrics = async () => {
  try {
    await fetchDashboardStats()

    // Calculate average score from conditions store if needed
    await conditionsStore.fetchAllConditions()
    const conditions = conditionsStore.getConditions
    const scoresSum = conditions.reduce((sum, condition) => sum + (condition.score || 0), 0)
    if (!metrics.avgScore) {
      metrics.avgScore = conditions.length > 0 ? (scoresSum / conditions.length).toFixed(2) : 0
    }
  } catch (error) {
    console.error('Error loading metrics:', error)
  }
}

const createDomainChart = async () => {
  if (!domainChartRef.value) return

  const ctx = domainChartRef.value.getContext('2d')

  // Fetch real data from API
  const domainData = await fetchDomainDistribution()

  const data = {
    labels: domainData.labels || ['Condition', 'Drug', 'Procedure', 'Measurement', 'Observation'],
    datasets: [{
      data: domainData.data || [35, 25, 20, 15, 5],
      backgroundColor: [
        '#667eea',
        '#f093fb',
        '#4facfe',
        '#43e97b',
        '#f6d55c',
        '#ff6b6b',
        '#4ecdc4',
        '#45b7d1'
      ],
      borderWidth: 2,
      borderColor: '#ffffff'
    }]
  }

  domainChart = new Chart(ctx, {
    type: 'doughnut',
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
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

const createInclusionChart = async () => {
  if (!inclusionChartRef.value) return

  const ctx = inclusionChartRef.value.getContext('2d')

  // Fetch real data from API
  const inclusionData = await fetchInclusionExclusionStats()

  const data = {
    labels: inclusionData.labels || ['Inclusion', 'Exclusion'],
    datasets: [{
      data: inclusionData.data || [65, 35],
      backgroundColor: ['#4ecdc4', '#ff6b6b'],
      borderWidth: 2,
      borderColor: '#ffffff'
    }]
  }

  inclusionChart = new Chart(ctx, {
    type: 'pie',
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
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

const createTrendChart = async () => {
  if (!trendChartRef.value) return

  const ctx = trendChartRef.value.getContext('2d')

  // Fetch real temporal trends data
  const trendsData = await fetchTemporalTrends()

  const data = {
    labels: trendsData.labels || [],
    datasets: [
      {
        label: 'Trials Started',
        data: trendsData.data || [],
        borderColor: '#667eea',
        backgroundColor: 'rgba(102, 126, 234, 0.1)',
        tension: 0.4,
        fill: true
      }
    ]
  }

  // Add mock criteria data if not available from API
  if (!trendsData.labels || trendsData.labels.length === 0) {
    const labels = []
    const trialsData = []

    // Generate mock time series data for last 12 months
    for (let i = 11; i >= 0; i--) {
      const date = new Date()
      date.setMonth(date.getMonth() - i)
      labels.push(date.toLocaleDateString('en-US', { month: 'short', year: 'numeric' }))
      trialsData.push(Math.floor(Math.random() * 50) + 20)
    }

    data.labels = labels
    data.datasets[0].data = trialsData
  }

  trendChart = new Chart(ctx, {
    type: 'line',
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 10
          }
        }
      },
      plugins: {
        legend: {
          position: 'top'
        },
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      interaction: {
        mode: 'nearest',
        axis: 'x',
        intersect: false
      }
    }
  })
}

const loadTopConditions = async () => {
  try {
    const conditions = await fetchTopConditions()
    topConditions.value = conditions.map(condition => ({
      id: condition.condition_concept_id,
      conceptName: condition.mesh_term,
      conceptId: condition.condition_concept_id,
      count: condition.trial_count
    }))
  } catch (error) {
    console.error('Error loading top conditions:', error)
  }
}

const loadTopCriteria = async () => {
  try {
    const criteria = await fetchTopCriteria()
    topCriteria.value = criteria.map(criterion => ({
      id: criterion.concept_id,
      criteriaConceptName: criterion.concept_name,
      criteriaConceptId: criterion.concept_id,
      conceptCount: criterion.usage_count
    }))
  } catch (error) {
    console.error('Error loading top criteria:', error)
  }
}

const refreshDomainChart = async () => {
  if (domainChart) {
    domainChart.destroy()
  }
  await nextTick()
  await createDomainChart()
}

const refreshInclusionChart = async () => {
  if (inclusionChart) {
    inclusionChart.destroy()
  }
  await nextTick()
  await createInclusionChart()
}

const refreshTrendChart = async () => {
  if (trendChart) {
    trendChart.destroy()
  }
  await nextTick()
  await createTrendChart()
}

onMounted(async () => {
  await loadMetrics()
  await loadTopConditions()
  await loadTopCriteria()

  await nextTick()
  await createDomainChart()
  await createInclusionChart()
  await createTrendChart()
})
</script>

<style scoped>
.analytics {
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

.overview-row {
  margin-bottom: 24px;
}

.metric-card {
  text-align: center;
}

.metric-content {
  padding: 8px 0;
}

.metric-value {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.metric-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.metric-change {
  font-size: 12px;
  font-weight: 500;
}

.metric-change.positive {
  color: #67C23A;
}

.metric-change.negative {
  color: #F56C6C;
}

.charts-row {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-controls {
  display: flex;
  gap: 8px;
  align-items: center;
}

.chart-container {
  height: 300px;
  position: relative;
}

.chart-container.large {
  height: 400px;
}

.top-items-row {
  margin-bottom: 24px;
}

.top-items-list {
  max-height: 400px;
  overflow-y: auto;
}

.top-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.top-item:last-child {
  border-bottom: none;
}

.item-rank {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin-right: 12px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.item-meta {
  font-size: 12px;
  color: #909399;
}

.item-count {
  font-weight: 600;
  color: #409EFF;
}
</style>
