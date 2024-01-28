import request from '@/utils/request'

export async function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

export function getRoleList(params) {
  return request({
    url: '/admin/queryAll',
    method: 'get',
    params
  })
}
