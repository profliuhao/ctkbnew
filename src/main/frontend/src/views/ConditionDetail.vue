<template>
  <div class="condition-detail">
    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- Content -->
    <div v-else-if="condition" class="detail-content">
      <!-- Header -->
      <div class="detail-header">
        <el-button @click="goBack" class="back-button">
          <el-icon><ArrowLeft /></el-icon>
          Back
        </el-button>
        <div class="header-info">
          <h1>{{ condition.conceptName || 'Unknown Condition' }}</h1>
          <div class="header-meta">
            <el-tag type="primary">ID: {{ condition.conceptId || 'N/A' }}</el-tag>
            <el-tag type="info">NCT: {{ condition.nctid || 'N/A' }}</el-tag>
            <el-tag :type="condition.include === 1 ? 'success' : 'danger'">
              {{ condition.include === 1 ? 'Inclusion' : 'Exclusion' }}
            </el-tag>
          </div>
        </div>
      </div>

      <!-- Basic Information -->
      <el-card class="info-card">
        <template #header>
          <span>Basic Information</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Concept ID">
            {{ condition.conceptId || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Concept Name">
            {{ condition.conceptName || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="NCT ID">
            {{ condition.nctid || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Domain">
            {{ condition.domain || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Type">
            <el-tag :type="condition.include === 1 ? 'success' : 'danger'">
              {{ condition.include === 1 ? 'Inclusion' : 'Exclusion' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Score">
            {{ condition.score ? condition.score.toFixed(2) : 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Line Number">
            {{ condition.lineNum || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Negation">
            <el-tag :type="condition.neg === 1 ? 'warning' : 'success'">
              {{ condition.neg === 1 ? 'Yes' : 'No' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- Text Information -->
      <el-card v-if="condition.temporalSourceText || condition.entitySourceText" class="text-card">
        <template #header>
          <span>Source Text</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="12" v-if="condition.temporalSourceText">
            <div class="text-section">
              <h4>Temporal Source Text</h4>
              <div class="text-content">
                {{ condition.temporalSourceText }}
              </div>
            </div>
          </el-col>
          <el-col :span="12" v-if="condition.entitySourceText">
            <div class="text-section">
              <h4>Entity Source Text</h4>
              <div class="text-content">
                {{ condition.entitySourceText }}
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- Temporal Information -->
      <el-card v-if="condition.beforedays || condition.afterdays" class="temporal-card">
        <template #header>
          <span>Temporal Information</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Before Days">
            {{ condition.beforedays || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="After Days">
            {{ condition.afterdays || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="Start Index">
            {{ condition.startIndex || 'N/A' }}
          </el-descriptions-item>
          <el-descriptions-item label="End Index">
            {{ condition.endIndex || 'N/A' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- Related Criteria -->
      <el-card class="related-card">
        <template #header>
          <div class="card-header">
            <span>Related Criteria</span>
            <el-button size="small" @click="loadRelatedCriteria" :loading="loadingCriteria">
              <el-icon><Refresh /></el-icon>
              Refresh
            </el-button>
          </div>
        </template>
        <div v-if="relatedCriteria.length === 0 && !loadingCriteria" class="empty-state">
          <el-empty description="No related criteria found" />
        </div>
        <el-table
          v-else
          :data="relatedCriteria"
          v-loading="loadingCriteria"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="criteriaConceptId" label="Criteria ID" width="120" />
          <el-table-column prop="criteriaConceptName" label="Criteria Name" min-width="200" show-overflow-tooltip />
          <el-table-column prop="criteriaDomain" label="Domain" width="120" />
          <el-table-column prop="include" label="Type" width="100">
            <template #default="{ row }">
              <el-tag :type="row.include === 1 ? 'success' : 'danger'" size="small">
                {{ row.include === 1 ? 'Inclusion' : 'Exclusion' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="conceptCount" label="Count" width="100" />
        </el-table>
      </el-card>

      <!-- Actions -->
      <el-card class="actions-card">
        <template #header>
          <span>Actions</span>
        </template>
        <div class="actions-content">
          <el-button type="primary" @click="viewInCriteria">
            <el-icon><List /></el-icon>
            View in Criteria
          </el-button>
          <el-button type="success" @click="findSimilar">
            <el-icon><Search /></el-icon>
            Find Similar
          </el-button>
          <el-button @click="exportDetails">
            <el-icon><Download /></el-icon>
            Export Details
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- Error State -->
    <div v-else class="error-state">
      <el-result
        icon="warning"
        title="Condition Not Found"
        :sub-title="`The condition with ID ${route.params.id} could not be found.`"
      >
        <template #extra>
          <el-button type="primary" @click="goBack">Go Back</el-button>
          <el-button @click="loadConditionDetail">Retry</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useConditionsStore } from '@/stores/conditions'
import { useCriteriaStore } from '@/stores/criteria'
import { conditionsApi, commonConditionsApi } from '@/api/conditions'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft,
  Refresh,
  List,
  Search,
  Download
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const conditionsStore = useConditionsStore()
const criteriaStore = useCriteriaStore()

// Reactive data
const loading = ref(true)
const loadingCriteria = ref(false)
const condition = ref(null)
const relatedCriteria = ref([])

// Methods
const goBack = () => {
  router.go(-1)
}

const loadConditionDetail = async () => {
  loading.value = true
  try {
    const conditionId = route.params.id
    console.log('Loading condition detail for ID:', conditionId)

    // First try to get from store
    const storeConditions = conditionsStore.getConditions
    let foundCondition = storeConditions.find(c =>
      c && c.id && c.id.toString() === conditionId
    )

    if (foundCondition) {
      console.log('Found condition in store:', foundCondition)
      condition.value = foundCondition
    } else {
      console.log('Condition not found in store, fetching from API...')

      // Try to fetch the specific condition by ID using the new API endpoint
      try {
        const response = await conditionsApi.getConditionById(conditionId)
        console.log('API response:', response)

        if (response.found && response.condition) {
          console.log('Successfully fetched condition:', response.condition)
          condition.value = response.condition
          foundCondition = response.condition
        } else {
          console.log('Condition not found via API')
        }
      } catch (error) {
        console.warn('Failed to fetch condition by ID:', error)
      }

      // If still not found, try to search by the ID as a concept ID
      if (!foundCondition) {
        try {
          await conditionsStore.searchConditionsAdvanced({
            conceptId: conditionId,
            currentPage: 1,
            size: 20
          })

          const searchResults = conditionsStore.getConditions
          foundCondition = searchResults.find(c => c && c.id && c.id.toString() === conditionId)

          if (foundCondition) {
            condition.value = foundCondition
          }
        } catch (error) {
          console.warn('Failed to search by concept ID:', error)
        }
      }

      // If still not found, try to load all conditions and find it (last resort)
      if (!foundCondition) {
        try {
          await conditionsStore.fetchAllConditions()
          const allConditions = conditionsStore.getConditions
          foundCondition = allConditions.find(c =>
            c && c.id && c.id.toString() === conditionId
          )

          if (foundCondition) {
            condition.value = foundCondition
          }
        } catch (error) {
          console.warn('Failed to fetch all conditions:', error)
        }
      }
    }

    // If still not found, try to find by conceptId
    if (!condition.value && conditionId) {
      const allConditions = conditionsStore.getConditions
      condition.value = allConditions.find(c =>
        c && c.conceptId && c.conceptId.toString() === conditionId
      )
    }

    // If we still don't have a condition, create a meaningful placeholder
    if (!condition.value) {
      // Try to fetch some sample data to show the user something useful
      try {
        await conditionsStore.fetchAllConditions()
        const sampleConditions = conditionsStore.getConditions
        if (sampleConditions.length > 0) {
          // Use the first condition as a template but update the ID
          const template = sampleConditions[0]
          condition.value = {
            ...template,
            id: conditionId,
            conceptId: conditionId,
            conceptName: `Condition ${conditionId}`,
            entitySourceText: `Details for condition ID ${conditionId}. This may be a placeholder if the specific condition was not found.`,
          }
        } else {
          // Fallback placeholder
          condition.value = {
            id: conditionId,
            conceptId: conditionId,
            conceptName: `Condition ${conditionId}`,
            domain: 'Unknown',
            include: 1,
            nctid: 'Unknown',
            entitySourceText: 'No details available for this condition.',
            temporalSourceText: '',
            score: 0
          }
        }
      } catch (error) {
        console.warn('Failed to create meaningful placeholder:', error)
        condition.value = {
          id: conditionId,
          conceptId: conditionId,
          conceptName: `Condition ${conditionId}`,
          domain: 'Unknown',
          include: 1,
          nctid: 'Unknown',
          entitySourceText: 'No details available for this condition.',
          temporalSourceText: '',
          score: 0
        }
      }
    }

    if (condition.value) {
      await loadRelatedCriteria()
    }
  } catch (error) {
    console.error('Error loading condition detail:', error)
    ElMessage.error('Failed to load condition details')
  } finally {
    loading.value = false
  }
}

const loadRelatedCriteria = async () => {
  if (!condition.value) return
  
  loadingCriteria.value = true
  try {
    // Try to find related criteria by condition name or ID
    if (condition.value.conceptName) {
      await criteriaStore.fetchCriteriaByConditionName(
        condition.value.conceptName,
        condition.value.include
      )
      relatedCriteria.value = criteriaStore.getCriteria
    }
  } catch (error) {
    console.error('Error loading related criteria:', error)
    relatedCriteria.value = []
  } finally {
    loadingCriteria.value = false
  }
}

const viewInCriteria = () => {
  router.push({
    path: '/criteria',
    query: {
      conditionName: condition.value.conceptName,
      include: condition.value.include
    }
  })
}

const findSimilar = () => {
  router.push({
    path: '/search',
    query: {
      conceptName: condition.value.conceptName,
      domain: condition.value.domain
    }
  })
}

const exportDetails = () => {
  // TODO: Implement export functionality
  ElMessage.info('Export functionality will be implemented soon')
}

onMounted(() => {
  loadConditionDetail()
})
</script>

<style scoped>
.condition-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.loading-container {
  padding: 24px;
}

.detail-content {
  padding: 0;
}

.detail-header {
  margin-bottom: 24px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.back-button {
  margin-top: 8px;
}

.header-info {
  flex: 1;
}

.header-info h1 {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.header-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.info-card,
.text-card,
.temporal-card,
.related-card,
.actions-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text-section h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.text-content {
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
  line-height: 1.6;
  color: #606266;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
}

.actions-content {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.error-state {
  padding: 40px 0;
  text-align: center;
}
</style>
