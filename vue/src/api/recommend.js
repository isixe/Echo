import request from '@/utils/request'

export function getArticleRecommendListByUserId(params) {
  return request({
    url: '/recommend/article',
    method: 'get',
    timeout: 30000,
    params
  })
}
