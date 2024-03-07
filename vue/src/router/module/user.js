const userRoutes = [
  {
    path: '/article/edit',
    name: 'articleEdit',
    component: () => import('@/views/page-home/article/articleEdit')
  },
  {
    path: '/question/edit',
    name: 'questionEdit',
    component: () => import('@/views/page-home/question/questionEdit')
  },
  {
    path: '/draft',
    name: 'userDraft',
    component: () => import('@/views/page-home/user/userDraft')
  },
  {
    path: '/collection',
    name: 'userCollection',
    component: () => import('@/views/page-home/user/userCollection')
  }
]

export default userRoutes
