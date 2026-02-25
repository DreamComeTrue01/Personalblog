import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PortalView from '../views/PortalView.vue'
import ArticleDetailView from '../views/ArticleDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'portal',
      component: PortalView
    },
    {
      path: '/blog',
      name: 'home',
      component: HomeView
    },
    {
      path: '/life',
      name: 'life',
      component: HomeView
    },
    {
      path: '/learn',
      name: 'learn',
      component: HomeView
    },
    {
      path: '/build',
      name: 'build',
      component: HomeView
    },
    {
      path: '/archive/:year/:month',
      name: 'archive',
      component: HomeView
    },
    {
      path: '/article/:id',
      name: 'article',
      component: ArticleDetailView
    }
  ]
})

export default router