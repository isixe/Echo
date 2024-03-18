import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/historyArticle',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/historyArticle',
    method: 'delete',
    data
  })
}
