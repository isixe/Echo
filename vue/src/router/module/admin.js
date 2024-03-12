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
      component: () => import('@/views/page-admin/system/ArticleList')
    },
    {
      path: '/system/questionList',
      name: 'questionList',
      component: () => import('@/views/page-admin/system/QuestionList')
    },
    {
      path: '/system/categoryList',
      name: 'categoryList',
      component: () => import('@/views/page-admin/system/CategoryList')
    },
    {
      path: '/system/collectionArticleList',
      name: 'collectionArticleList',
      component: () => import('@/views/page-admin/system/CollectionArticleList')
    },
    {
      path: '/system/collectionQuestionList',
      name: 'collectionQuestionList',
      component: () => import('@/views/page-admin/system/CollectionQuestionList')
    },
    {
      path: '/system/commentArticleList',
      name: 'commentArticleList',
      component: () => import('@/views/page-admin/system/CommentArticleList')
    },
    {
      path: '/system/commentQuestionList',
      name: 'commentQuestionList',
      component: () => import('@/views/page-admin/system/CommentQuestionList')
    },
    {
      path: '/permission/roleList',
      name: 'roleList',
      component: () => import('@/views/page-admin/permission/RoleList')
    }
  ]
}

export default adminRoutes
