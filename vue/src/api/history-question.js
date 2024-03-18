import request from '@/utils/request'

export function set(data) {
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
