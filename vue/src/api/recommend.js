import request from '@/utils/request'

export function getArticleRecommendListByUserId(params) {
  return request({
    url: '/recommend/article',
    method: 'get',
    timeout: 30000,
    params
  })
}

export function getQuestionRecommendListByUserId(params) {
  return request({
    url: '/recommend/question',
    method: 'get',
    timeout: 30000,
    params
  })
}
