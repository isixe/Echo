import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/follow',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/follow',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/follow',
    method: 'get',
    params
  })
}

export function getByUserIdAndFollowId(params) {
  return request({
    url: '/follow/getByUserIdAndFollowId',
    method: 'get',
    params
  })
}
