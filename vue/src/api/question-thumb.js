import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/questionThumb',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/questionThumb',
    method: 'delete',
    data
  })
}

export function getQuestionThumbByArticleIdAndUserId(params) {
  return request({
    url: '/questionThumb/getByQuestionIdAndUserId',
    method: 'get',
    params
  })
}
