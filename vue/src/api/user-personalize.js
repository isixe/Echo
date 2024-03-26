import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/personalize',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/personalize',
    method: 'put',
    data
  })
}

export function getByUserId(params) {
  return request({
    url: '/personalize',
    method: 'get',
    params
  })
}
