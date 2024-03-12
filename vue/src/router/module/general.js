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
        path: '/search',
        name: 'search',
        component: () => import('@/views/page-home/search')
      },
      {
        path: '/user/:id',
        name: 'userProfile',
        component: () => import('@/views/page-home/user/UserProfile')
      },
      {
        path: '/article/:id',
        name: 'articleView',
        component: () => import('@/views/page-home/article/ArticleView')
      },
      {
        path: '/question/:id',
        name: 'questionView',
        component: () => import('@/views/page-home/question/QuestionView')
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
        component: () => import('@/views/page-home/article/ArticleGroup')
      }
    ]
  }
]

export default generalRoutes
