<template>
  <div class="dashboard">
    <!-- Page Header -->
    <div class="page-header">
      <h1>Dashboard</h1>
      <p>Clinical Trial Knowledge Base Overview</p>
    </div>

    <!-- Statistics Cards -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon conditions">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalConditions }}</div>
              <div class="stat-label">Total Conditions</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon criteria">
              <el-icon><List /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalCriteria }}</div>
              <div class="stat-label">Total Criteria</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon trials">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalTrials }}</div>
              <div class="stat-label">Clinical Trials</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon concepts">
              <el-icon><Connection /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ formatNumber(stats.totalInterventions) }}</div>
              <div class="stat-label">Interventions</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Quick Search -->
    <el-row :gutter="20" class="search-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Quick Search</span>
            </div>
          </template>
          <div class="search-content">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-input
                  v-model="searchQuery.conceptId"
                  placeholder="Search by Concept ID"
                  clearable
                  @keyup.enter="handleSearch"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="8">
                <el-input
                  v-model="searchQuery.conceptName"
                  placeholder="Search by Concept Name"
                  clearable
                  @keyup.enter="handleSearch"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="8">
                <el-input
                  v-model="searchQuery.nctId"
                  placeholder="Search by NCT ID"
                  clearable
                  @keyup.enter="handleSearch"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </el-col>
            </el-row>
            <div class="search-actions">
              <el-button type="primary" @click="handleSearch" :loading="searching">
                <el-icon><Search /></el-icon>
                Search
              </el-button>
              <el-button @click="clearSearch">Clear</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Recent Activity -->
    <el-row :gutter="20" class="activity-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Recent Conditions</span>
              <el-button type="text" @click="$router.push('/conditions')">View All</el-button>
            </div>
          </template>
          <div class="activity-list">
            <div v-if="recentConditions.length === 0" class="empty-state">
              <el-empty description="No recent conditions" />
            </div>
            <div v-else>
              <div
                v-for="condition in recentConditions"
                :key="condition.id"
                class="activity-item"
                @click="viewCondition(condition)"
              >
                <div class="activity-info">
                  <div class="activity-title">{{ condition.conceptName }}</div>
                  <div class="activity-meta">
                    ID: {{ condition.conceptId }} | NCT: {{ condition.nctid }}
                  </div>
                </div>
                <el-icon class="activity-arrow"><ArrowRight /></el-icon>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Quick Actions</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button
              type="primary"
              size="large"
              class="action-button"
              @click="$router.push('/conditions')"
            >
              <el-icon><Document /></el-icon>
              Browse Conditions
            </el-button>
            <el-button
              type="success"
              size="large"
              class="action-button"
              @click="$router.push('/criteria')"
            >
              <el-icon><List /></el-icon>
              Explore Criteria
            </el-button>
            <el-button
              type="warning"
              size="large"
              class="action-button"
              @click="$router.push('/analytics')"
            >
              <el-icon><TrendCharts /></el-icon>
              View Analytics
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Section -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Domain Distribution</span>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="domainChart" width="400" height="300"></canvas>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Inclusion vs Exclusion Criteria</span>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="inclusionChart" width="400" height="300"></canvas>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Additional Charts Row -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Trial Phase Distribution</span>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="phaseChart" width="800" height="400"></canvas>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Top Lists Section -->
    <el-row :gutter="20" class="insights-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Top Conditions by Trial Count</span>
            </div>
          </template>
          <div class="top-list">
            <div v-if="topConditions.length === 0" class="empty-state">
              <el-empty description="No data available" />
            </div>
            <div v-else>
              <div
                v-for="(condition, index) in topConditions.slice(0, 8)"
                :key="condition.condition_concept_id"
                class="list-item"
              >
                <span class="rank">{{ index + 1 }}</span>
                <div class="item-content">
                  <div class="item-name">{{ condition.mesh_term }}</div>
                  <div class="item-meta">{{ condition.trial_count }} trials</div>
                </div>
                <div class="item-progress">
                  <el-progress
                    :percentage="topConditions.length > 0 ? Math.round((condition.trial_count / topConditions[0].trial_count) * 100) : 0"
                    :show-text="false"
                    :stroke-width="6"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Most Used Criteria</span>
            </div>
          </template>
          <div class="top-list">
            <div v-if="topCriteria.length === 0" class="empty-state">
              <el-empty description="No data available" />
            </div>
            <div v-else>
              <div
                v-for="(criteria, index) in topCriteria.slice(0, 8)"
                :key="criteria.concept_id"
                class="list-item"
              >
                <span class="rank">{{ index + 1 }}</span>
                <div class="item-content">
                  <div class="item-name">{{ criteria.concept_name }}</div>
                  <div class="item-meta">{{ criteria.usage_count }} uses</div>
                </div>
                <div class="item-progress">
                  <el-progress
                    :percentage="topCriteria.length > 0 ? Math.round((criteria.usage_count / topCriteria[0].usage_count) * 100) : 0"
                    :show-text="false"
                    :stroke-width="6"
                    color="#f56c6c"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useConditionsStore } from '@/stores/conditions'
import { statisticsApi } from '@/api/statistics'
import Chart from 'chart.js/auto'
import {
  Document,
  List,
  DataAnalysis,
  Connection,
  Search,
  ArrowRight,
  TrendCharts
} from '@element-plus/icons-vue'

const router = useRouter()
const conditionsStore = useConditionsStore()

// Reactive data
const stats = reactive({
  totalConditions: 0,
  totalCriteria: 0,
  totalTrials: 0,
  activeTrials: 0,
  totalInterventions: 0
})

const searchQuery = reactive({
  conceptId: '',
  conceptName: '',
  nctId: ''
})

const recentConditions = ref([])
const searching = ref(false)
const loading = ref(false)
const lastUpdated = ref('')

// New data for enhanced dashboard
const topConditions = ref([])
const topCriteria = ref([])
const domainDistribution = ref({})
const phaseDistribution = ref({})
const inclusionExclusionStats = ref({})
const dataQuality = ref({ completeness: 0 })

// Chart refs
const domainChart = ref(null)
const inclusionChart = ref(null)
const phaseChart = ref(null)

// Chart instances
let domainChartInstance = null
let inclusionChartInstance = null
let phaseChartInstance = null

// Methods
const handleSearch = async () => {
  if (!searchQuery.conceptId && !searchQuery.conceptName && !searchQuery.nctId) {
    return
  }

  searching.value = true
  try {
    // Navigate to search results with query parameters
    const query = {}
    if (searchQuery.conceptId) query.conceptId = searchQuery.conceptId
    if (searchQuery.conceptName) query.conceptName = searchQuery.conceptName
    if (searchQuery.nctId) query.nctId = searchQuery.nctId

    router.push({ path: '/search', query })
  } finally {
    searching.value = false
  }
}

const clearSearch = () => {
  searchQuery.conceptId = ''
  searchQuery.conceptName = ''
  searchQuery.nctId = ''
}

const viewCondition = (condition) => {
  router.push(`/conditions/${condition.id}`)
}

// API functions
const fetchDashboardStats = async () => {
  try {
    const response = await statisticsApi.getDashboardStats()
    Object.assign(stats, response)
  } catch (error) {
    console.error('Error fetching dashboard stats:', error)
  }
}

const fetchTopConditions = async () => {
  try {
    const response = await statisticsApi.getTopConditions(10)
    topConditions.value = response
  } catch (error) {
    console.error('Error fetching top conditions:', error)
  }
}

const fetchTopCriteria = async () => {
  try {
    const response = await statisticsApi.getTopCriteria(10)
    topCriteria.value = response
  } catch (error) {
    console.error('Error fetching top criteria:', error)
  }
}

const fetchDomainDistribution = async () => {
  try {
    const response = await statisticsApi.getDomainDistribution()
    domainDistribution.value = response
  } catch (error) {
    console.error('Error fetching domain distribution:', error)
  }
}

const fetchPhaseDistribution = async () => {
  try {
    const response = await statisticsApi.getPhaseDistribution()
    phaseDistribution.value = response
  } catch (error) {
    console.error('Error fetching phase distribution:', error)
  }
}

const fetchInclusionExclusionStats = async () => {
  try {
    const response = await statisticsApi.getInclusionExclusionStats()
    inclusionExclusionStats.value = response
  } catch (error) {
    console.error('Error fetching inclusion/exclusion stats:', error)
  }
}

const loadDashboardData = async () => {
  loading.value = true
  try {
    // Load all dashboard data in parallel
    await Promise.all([
      fetchDashboardStats(),
      fetchTopConditions(),
      fetchTopCriteria(),
      fetchDomainDistribution(),
      fetchPhaseDistribution(),
      fetchInclusionExclusionStats(),
      conditionsStore.fetchAllConditions()
    ])

    // Load recent conditions from store
    recentConditions.value = conditionsStore.getConditions.slice(0, 5)

    // Update last updated time
    lastUpdated.value = new Date().toLocaleString()

    console.log('Dashboard data loaded:', {
      stats: stats,
      topConditions: topConditions.value,
      topCriteria: topCriteria.value,
      domainDistribution: domainDistribution.value
    })

    // Create charts after data is loaded
    await createAllCharts()
  } catch (error) {
    console.error('Error loading dashboard data:', error)
  } finally {
    loading.value = false
  }
}

// Utility functions
const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString()
}

const getActiveTrialPercentage = () => {
  if (!stats.totalTrials || !stats.activeTrials) return '0'
  return Math.round((stats.activeTrials / stats.totalTrials) * 100)
}

const calculateGrowth = (type) => {
  // Mock growth calculation - in real app, this would compare with previous period
  return Math.floor(Math.random() * 15) + 1
}

const getLatestTrialDate = () => {
  // Mock function - would get actual latest trial date from API
  return 'January 2024'
}

const getDemographicSummary = () => {
  // Mock function - would get actual demographic summary
  return 'Mixed age groups, all genders'
}

const refreshData = async () => {
  await loadDashboardData()
}

// Chart creation functions
const createDomainChart = () => {
  console.log('Creating domain chart with data:', domainDistribution.value)
  if (!domainChart.value || !domainDistribution.value.labels) {
    console.log('Domain chart creation skipped - missing data or element')
    return
  }

  if (domainChartInstance) {
    domainChartInstance.destroy()
  }

  const ctx = domainChart.value.getContext('2d')
  domainChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: domainDistribution.value.labels,
      datasets: [{
        data: domainDistribution.value.data,
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
    },
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

const createInclusionChart = () => {
  if (!inclusionChart.value || !inclusionExclusionStats.value.labels) return

  if (inclusionChartInstance) {
    inclusionChartInstance.destroy()
  }

  const ctx = inclusionChart.value.getContext('2d')
  inclusionChartInstance = new Chart(ctx, {
    type: 'pie',
    data: {
      labels: inclusionExclusionStats.value.labels,
      datasets: [{
        data: inclusionExclusionStats.value.data,
        backgroundColor: ['#ff6b6b', '#4ecdc4'],
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
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

const createPhaseChart = () => {
  if (!phaseChart.value || !phaseDistribution.value.labels) return

  if (phaseChartInstance) {
    phaseChartInstance.destroy()
  }

  const ctx = phaseChart.value.getContext('2d')
  phaseChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: phaseDistribution.value.labels,
      datasets: [{
        label: 'Number of Trials',
        data: phaseDistribution.value.data,
        backgroundColor: 'rgba(102, 126, 234, 0.8)',
        borderColor: 'rgba(102, 126, 234, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 1
          }
        }
      },
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return `${context.label}: ${context.parsed.y} trials`
            }
          }
        }
      }
    }
  })
}

const createAllCharts = async () => {
  await nextTick()
  createDomainChart()
  createInclusionChart()
  createPhaseChart()
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
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

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
  color: white;
}

.stat-icon.conditions {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.criteria {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.trials {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.concepts {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.search-row {
  margin-bottom: 24px;
}

.search-content {
  padding: 8px 0;
}

.search-actions {
  margin-top: 16px;
  text-align: center;
}

.activity-row {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: between;
  align-items: center;
}

.activity-list {
  max-height: 300px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s;
}

.activity-item:hover {
  background-color: #f8f9fa;
  padding-left: 8px;
  padding-right: 8px;
  border-radius: 4px;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.activity-meta {
  font-size: 12px;
  color: #909399;
}

.activity-arrow {
  color: #c0c4cc;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
}

.empty-state {
  text-align: center;
  padding: 20px 0;
}

/* New styles for enhanced dashboard */
.charts-row {
  margin-bottom: 24px;
}

.chart-container {
  height: 300px;
  position: relative;
}

.insights-row {
  margin-bottom: 24px;
}

.top-list {
  max-height: 400px;
  overflow-y: auto;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.list-item:last-child {
  border-bottom: none;
}

.rank {
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  margin-right: 12px;
  flex-shrink: 0;
}

.item-content {
  flex: 1;
  margin-right: 12px;
}

.item-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
  font-size: 14px;
  line-height: 1.4;
}

.item-meta {
  font-size: 12px;
  color: #909399;
}

.item-progress {
  width: 100px;
  flex-shrink: 0;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.refresh-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.refresh-btn {
  background: #409eff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
}

.refresh-btn:hover {
  background: #337ecc;
}

.refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.last-updated {
  font-size: 12px;
  color: #909399;
}

.loading-state {
  text-align: center;
  padding: 60px 0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Enhanced stat cards */
.stat-card {
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

/* Responsive design */
@media (max-width: 768px) {
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .refresh-controls {
    width: 100%;
    justify-content: space-between;
  }

  .chart-container {
    height: 250px;
  }

  .item-progress {
    width: 80px;
  }
}
</style>
