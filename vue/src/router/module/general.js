import Home from '@/views/page-home'

const generalRoutes = {
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
      name: 'profile',
      component: () => import('@/views/page-home/user/profile')
    }
  ]
}

export default generalRoutes
