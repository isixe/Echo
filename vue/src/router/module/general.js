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
        path: '/question',
        name: 'question',
        component: () => import('@/views/page-home/question')
      },
      {
        path: '/user/:id',
        name: 'userProfile',
        component: () => import('@/views/page-home/user/userProfile')
      },
      {
        path: '/article/:id',
        name: 'articleView',
        component: () => import('@/views/page-home/article/articleView')
      },
      {
        path: '/question/:id',
        name: 'questionView',
        component: () => import('@/views/page-home/question/questionView')
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
        path: '/article/group/:id',
        name: 'articleGroup',
        component: () => import('@/views/page-home/article/articleGroup')
      }
    ]
  }
]

export default generalRoutes
