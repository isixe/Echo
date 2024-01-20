import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/page-home'

export const constantRoutes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: () => import('@/views/page-admin'),
    hidden: true
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/page-home/login'),
    hidden: true
  },
  {
    path: '/admin/login',
    name: 'adminLogin',
    component: () => import('@/views/page-admin/login'),
    hidden: true
  },
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/error/403'),
    hidden: true
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFund',
    redirect: '/404',
    hidden: true
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes
})

export default router
