import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/historyQuestion',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/historyQuestion',
    method: 'delete',
    data
  })
}
