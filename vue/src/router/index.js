import { createRouter, createWebHistory } from 'vue-router'
import generalRoutes from './module/general'
import adminRoutes from './module/admin'
import userRoutes from './module/user'
import { useUserStore } from '@/stores/user'
import { useAdminStore } from '@/stores/admin'

export let constantRoutes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/page-home/login')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/page-home/register')
  },
  {
    path: '/admin/login',
    name: 'adminLogin',
    component: () => import('@/views/page-admin/login')
  },
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/error/403')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404')
  },
  { path: '/:pathMatch(.*)*', name: 'NoFound', redirect: '/404', hidden: true }
]

constantRoutes = constantRoutes.concat(generalRoutes).concat(userRoutes)

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes
})

export function resetRouter() {
  const newRouter = createRouter({
    history: createWebHistory('/'),
    routes: constantRoutes,
    scrollBehavior: () => ({ left: 0, top: 0 })
  })

  router.matcher = newRouter.matcher
}

router.beforeEach((to, from, next) => {
  const path = to.path

  if (path === '/login' || path === '/admin/login') {
    return next()
  }

  //get token exists
  const tokens = {
    user: useUserStore().tokenValue == null,
    admin: useAdminStore().tokenValue == null
  }

  //get all routes path
  const extractPaths = (routes) => {
    return routes.map((route) =>
      route.children ? [route.path, ...extractPaths(route.children)] : route.path
    )
  }

  //get route hit
  const isRoleRoutes = (routes) => {
    const pathList = extractPaths(routes).flat()
    return pathList.includes(path)
  }

  const user = isRoleRoutes(userRoutes)
  const admin = isRoleRoutes([adminRoutes])

  //redirect to login page
  if (tokens.user && user) {
    return next('/login')
  }

  if (tokens.admin && admin) {
    return next('/admin/login')
  }

  return next()
})

router.addRoute(adminRoutes)
export default router
