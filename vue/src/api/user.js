import request from '@/utils/request'

export async function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/user',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/user',
    method: 'get',
    params
  })
}

export function getUserList(params) {
  return request({
    url: '/user/queryAll',
    method: 'get',
    params
  })
}
