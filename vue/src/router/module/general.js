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
        path: '/category/:id',
        name: 'category',
        component: () => import('@/views/page-home/category')
      },
      {
        path: '/tag',
        name: 'tag',
        component: () => import('@/views/page-home/tag')
      },
      {
        path: '/test',
        name: 'test',
        component: () => import('@/views/page-home/user/testView')
      }
    ]
  }
]

export default generalRoutes
