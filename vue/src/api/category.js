import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/category',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/category',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/category',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/category',
    method: 'get',
    params
  })
}

export function getCategoryListByKeyword(params) {
  return request({
    url: '/category/queryAll',
    method: 'get',
    params
  })
}

export function getCategoryListByName(params) {
  return request({
    url: '/category/queryByName',
    method: 'get',
    params
  })
}
