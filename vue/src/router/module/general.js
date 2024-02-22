import Home from '@/views/page-home'

const generalRoutes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    redirect: '/article',
    children: [
      {
        path: '/article',
        name: 'article',
        component: () => import('@/views/page-home/article')
      },
      {
        path: '/user/:id',
        name: 'userProfile',
        component: () => import('@/views/page-home/user/UserProfile')
      },
      {
        path: '/article/:id',
        name: 'articleView',
        component: () => import('@/views/page-home/article/articleView')
      },

      {
        path: '/test',
        name: 'test',
        component: () => import('@/views/page-home/user/testView')
      }
    ]
  },
  {
    path: '/article/edit',
    name: 'articleEdit',
    component: () => import('@/views/page-home/article/articleEdit')
  }
]

export default generalRoutes
