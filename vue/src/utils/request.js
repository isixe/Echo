import axios from 'axios'
import { message } from 'ant-design-vue'
// import { useUserStore } from '@/stores/user'
// import { useAdminStore } from '@/stores/admin'

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

    const response = error.response
    const data = response.data
    message.error(data.msg)
    return Promise.reject(data)
  }
)

export default instance
