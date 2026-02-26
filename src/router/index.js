import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PortalView from '../views/PortalView.vue'
import ArticleDetailView from '../views/ArticleDetailView.vue'
import LifeView from '../views/LifeView.vue'
import ArchiveView from '../views/ArchiveView.vue'
import AboutView from '../views/AboutView.vue'
import MessageView from '../views/MessageView.vue'

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
      component: LifeView
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
      path: '/archive',
      name: 'archive',
      component: ArchiveView
    },
    {
      path: '/archive/:year/:month',
      name: 'archive-by-date',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView
    },
    {
      path: '/message',
      name: 'message',
      component: MessageView
    },
    {
      path: '/article/:id',
      name: 'article',
      component: ArticleDetailView
    }
  ]
})

export default router