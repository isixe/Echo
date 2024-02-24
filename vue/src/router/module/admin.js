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
      path: '/system/userList',
      name: 'userList',
      component: () => import('@/views/page-admin/system/userList')
    },
    {
      path: '/system/articleList',
      name: 'articleList',
      component: () => import('@/views/page-admin/system/articleList')
    },
    {
      path: '/permission/roleList',
      name: 'roleList',
      component: () => import('@/views/page-admin/permission/roleList')
    },
    {
      path: '/system/commentArticleList',
      name: 'commentArticleList',
      component: () => import('@/views/page-admin/system/commentArticle')
    }
  ]
}

export default adminRoutes
