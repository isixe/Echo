import axios from 'axios'
import router from '@/router'
import { message } from 'ant-design-vue'
import adminRoutes from '@/router/module/admin'
import userRoutes from '@/router/module/user'
import { useUserStore } from '@/stores/user'
import { useAdminStore } from '@/stores/admin'

//create instance
const instance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  withCredentials: true,
  timeout: 5000
})

//set request interceptors
instance.interceptors.request.use(
  (config) => {
    //set header token(Authentication)
    // const tokens = {
    //   user: useUserStore().tokenValue,
    //   admin: useAdminStore().tokenValue
    // }

    // if (Object.values(tokens).every((value) => !value)) {
    //   return config
    // }

    // if (tokens.user) {
    //   config.headers['satokenuser'] = tokens.user
    // }

    // if (tokens.admin) {
    //   config.headers['satoken'] = tokens.admin
    // }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

//set response interceptors
instance.interceptors.response.use(
  (response) => {
    const data = response.data
    if (response.status === 200) {
      return data
    }

    return Promise.reject(data.msg)
  },
  (error) => {
    if (!error.response) {
      message.error('网络错误，请稍后重试')
      return Promise.reject(error)
    }

    //get all routes path
    const extractPaths = (routes) => {
      return routes.map((route) =>
        route.children ? [route.path, ...extractPaths(route.children)] : route.path
      )
    }

    //get route hit
    const isRoleRoutes = (routes) => {
      const pathList = extractPaths([routes]).flat()
      const currentPath = router.currentRoute.value.path
      return pathList.includes(currentPath)
    }

    const response = error.response
    const data = response.data
    switch (response.status) {
      case 401:
        if (isRoleRoutes(adminRoutes)) {
          useAdminStore().resetAction()
          router.push('/admin/login')
        }

        useUserStore().resetAction()
        router.push('/login')
        message.error('登录验证过期，请重新登录')
        break
      default:
        message.error(data.msg)
    }
    return Promise.reject(data)
  }
)

export default instance
