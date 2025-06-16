<template>
  <div class="test-page">
    <h1>API Test Page</h1>
    
    <div class="test-section">
      <h2>Dashboard Stats</h2>
      <button @click="testDashboardStats" :disabled="loading">Test Dashboard Stats</button>
      <pre v-if="dashboardData">{{ JSON.stringify(dashboardData, null, 2) }}</pre>
    </div>
    
    <div class="test-section">
      <h2>Domain Distribution</h2>
      <button @click="testDomainDistribution" :disabled="loading">Test Domain Distribution</button>
      <pre v-if="domainData">{{ JSON.stringify(domainData, null, 2) }}</pre>
    </div>
    
    <div class="test-section">
      <h2>Top Conditions</h2>
      <button @click="testTopConditions" :disabled="loading">Test Top Conditions</button>
      <pre v-if="conditionsData">{{ JSON.stringify(conditionsData, null, 2) }}</pre>
    </div>
    
    <div v-if="error" class="error">
      <h3>Error:</h3>
      <pre>{{ error }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { statisticsApi } from '@/api/statistics'

const loading = ref(false)
const dashboardData = ref(null)
const domainData = ref(null)
const conditionsData = ref(null)
const error = ref(null)

const testDashboardStats = async () => {
  loading.value = true
  error.value = null
  try {
    console.log('Testing dashboard stats...')
    const response = await statisticsApi.getDashboardStats()
    console.log('Dashboard stats response:', response)
    dashboardData.value = response
  } catch (err) {
    console.error('Dashboard stats error:', err)
    error.value = err.message || err.toString()
  } finally {
    loading.value = false
  }
}

const testDomainDistribution = async () => {
  loading.value = true
  error.value = null
  try {
    console.log('Testing domain distribution...')
    const response = await statisticsApi.getDomainDistribution()
    console.log('Domain distribution response:', response)
    domainData.value = response
  } catch (err) {
    console.error('Domain distribution error:', err)
    error.value = err.message || err.toString()
  } finally {
    loading.value = false
  }
}

const testTopConditions = async () => {
  loading.value = true
  error.value = null
  try {
    console.log('Testing top conditions...')
    const response = await statisticsApi.getTopConditions(5)
    console.log('Top conditions response:', response)
    conditionsData.value = response
  } catch (err) {
    console.error('Top conditions error:', err)
    error.value = err.message || err.toString()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.test-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.test-section h2 {
  margin-top: 0;
}

button {
  background: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 15px;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

pre {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 12px;
}

.error {
  background: #fef0f0;
  border: 1px solid #fbc4c4;
  color: #f56c6c;
  padding: 15px;
  border-radius: 4px;
}
</style>
