import Admin from '@/views/page-admin'

const adminRoutes = {
  path: '/admin',
  name: 'admin',
  component: Admin,
  redirect: '/dashboard',
  children: [
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/page-admin/dashboard')
    },
    {
      path: '/permission/roleList',
      name: 'roleList',
      component: () => import('@/views/page-admin/permission/roleList')
    },
    {
      path: '/permission/roleList/add',
      name: 'roleAdd',
      component: () => import('@/views/page-admin/permission/add')
    }
  ]
}

export default adminRoutes
