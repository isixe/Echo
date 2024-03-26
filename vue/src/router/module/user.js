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
    name: 'Home',
    component: () => import('@/views/page-home'),
    redirect: '/article',
    children: [
      {
        path: '/collection',
        name: 'userCollection',
        component: () => import('@/views/page-home/user/UserCollection')
      },
      {
        path: '/history',
        name: 'userHistory',
        component: () => import('@/views/page-home/user/UserHistory')
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
          },
          {
            path: '/settings/personalize',
            name: 'setPersonalize',
            component: () => import('@/views/page-home/user/settings/SetPersonalize')
          }
        ]
      }
    ]
  }
]

export default userRoutes
