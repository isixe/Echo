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
      path: '/system/questionList',
      name: 'questionList',
      component: () => import('@/views/page-admin/system/questionList')
    },
    {
      path: '/system/categoryList',
      name: 'categoryList',
      component: () => import('@/views/page-admin/system/categoryList')
    },
    {
      path: '/system/commentArticleList',
      name: 'commentArticleList',
      component: () => import('@/views/page-admin/system/commentArticleList')
    },
    {
      path: '/permission/roleList',
      name: 'roleList',
      component: () => import('@/views/page-admin/permission/roleList')
    }
  ]
}

export default adminRoutes
