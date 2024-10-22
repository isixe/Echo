import request from '@/utils/request'

export function set(data) {
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

export function getArticleHistoryByUserId(params) {
  return request({
    url: '/historyArticle/queryAllByUserId',
    method: 'get',
    params
  })
}
