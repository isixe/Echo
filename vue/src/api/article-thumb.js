import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/articleThumb',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/articleThumb',
    method: 'delete',
    data
  })
}

export function getArticleThumbByArticleIdAndUserId(params) {
  return request({
    url: '/articleThumb/getByArticleIdAndUserId',
    method: 'get',
    params
  })
}
