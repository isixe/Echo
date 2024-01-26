import Admin from '@/views/page-admin'

const adminRoutes = {
  path: '/dashboard',
  name: 'dashboard',
  component: Admin,
  children: [
    {
      path: '/permission/roleList',
      name: 'roleList',
      component: () => import('@/views/page-admin/permission/roleList')
    }
  ]
}

export default adminRoutes
