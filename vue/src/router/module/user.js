const userRoutes = [
  {
    path: '/article/edit',
    name: 'articleEdit',
    component: () => import('@/views/page-home/article/ArticleEdit')
  },
  {
    path: '/question/edit',
    name: 'questionEdit',
    component: () => import('@/views/page-home/question/QuestionEdit')
  },
  {
    path: '/draft',
    name: 'userDraft',
    component: () => import('@/views/page-home/user/UserDraft')
  },
  {
    path: '/collection',
    name: 'userCollection',
    component: () => import('@/views/page-home/user/UserCollection')
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import('@/views/page-home/user/settings'),
    redirect: '/settings/profile',
    children: [
      {
        path: '/settings/profile',
        name: 'setProfile',
        component: () => import('@/views/page-home/user/settings/SetProfile')
      }
    ]
  }
]

export default userRoutes
