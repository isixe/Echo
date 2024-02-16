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

export function add(data) {
  return request({
    url: '/admin',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/admin',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/admin',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/admin',
    method: 'get',
    params
  })
}

export function getRoleListByKeyword(params) {
  return request({
    url: '/admin/queryAll',
    method: 'get',
    params
  })
}
