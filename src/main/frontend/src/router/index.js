import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: {
      title: 'Dashboard'
    }
  },
  {
    path: '/conditions',
    name: 'Conditions',
    component: () => import('@/views/Conditions.vue'),
    meta: {
      title: 'Conditions'
    }
  },
  {
    path: '/conditions/:id',
    name: 'ConditionDetail',
    component: () => import('@/views/ConditionDetail.vue'),
    meta: {
      title: 'Condition Detail'
    }
  },
  {
    path: '/criteria',
    name: 'Criteria',
    component: () => import('@/views/Criteria.vue'),
    meta: {
      title: 'Criteria'
    }
  },
  {
    path: '/criteria/:id',
    name: 'CriteriaDetail',
    component: () => import('@/views/CriteriaDetail.vue'),
    meta: {
      title: 'Criteria Detail'
    }
  },
  {
    path: '/analytics',
    name: 'Analytics',
    component: () => import('@/views/Analytics.vue'),
    meta: {
      title: 'Analytics'
    }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/Search.vue'),
    meta: {
      title: 'Search Results'
    }
  },
  {
    path: '/test',
    name: 'Test',
    component: () => import('@/views/Test.vue'),
    meta: {
      title: 'API Test'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard to update page title
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - CTKB`
  }
  next()
})

export default router
