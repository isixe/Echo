import { defineStore } from 'pinia'
import { login } from '@/api/admin'

export const useAdminStore = defineStore('admin', () => {
  //state
  const id = ref()
  const tokenName = ref()
  const tokenValue = ref()
  const tokenTimeout = ref()

  //action
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

  return { id, tokenName, tokenValue, tokenTimeout, loginAction }
})
