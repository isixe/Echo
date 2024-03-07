import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/collectionArticle',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/collectionArticle',
    method: 'delete',
    data
  })
}

export function getCollectionArticleListByKeyword(params) {
  return request({
    url: '/collectionArticle/queryAll',
    method: 'get',
    params
  })
}
