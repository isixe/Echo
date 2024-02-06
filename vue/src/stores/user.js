import { defineStore } from 'pinia'
import { login, logout } from '@/api/user'

export const useUserStore = defineStore(
  'user',
  () => {
    const id = ref()
    const tokenName = ref()
    const tokenValue = ref()
    const tokenTimeout = ref()

    const loginAction = async (data) => {
      const formData = new URLSearchParams()
      formData.append('name', data.name)
      formData.append('password', data.password)
      formData.append('rememberMe', data.rememberMe)

      await login(formData).then((response) => {
        id.value = response.data.loginId
        tokenName.value = response.data.tokenName
        tokenValue.value = response.data.tokenValue
        tokenTimeout.value = response.data.tokenTimeout
      })
    }

    const logoutAction = async () => {
      await logout().then(() => resetAction())
    }

    const resetAction = () => {
      id.value = null
      tokenName.value = null
      tokenValue.value = null
      tokenTimeout.value = null
    }

    return {
      id,
      tokenName,
      tokenValue,
      tokenTimeout,
      loginAction,
      logoutAction,
      resetAction
    }
  },
  {
    persist: true
  }
)
