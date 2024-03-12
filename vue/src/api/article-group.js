import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/groupArticle',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/groupArticle',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/groupArticle',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/groupArticle',
    method: 'get',
    params
  })
}

export function getArticleGroupListByKeyword(params) {
  return request({
    url: '/groupArticle/queryAll',
    method: 'get',
    params
  })
}

export function getArticleGroupListByUserId(params) {
  return request({
    url: '/groupArticle/queryAllByUserId',
    method: 'get',
    params
  })
}

export function getArticleGroupByUserIdAndGroupName(params) {
  return request({
    url: '/groupArticle/getByUserIdAndGroupName',
    method: 'get',
    params
  })
}
