import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/commentArticle',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/commentArticle',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/commentArticle',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/commentArticle',
    method: 'get',
    params
  })
}

export function getCommentArticleRootList(params) {
  return request({
    url: '/commentArticle/getRootCommentListByArticleId',
    method: 'get',
    params
  })
}

export function getCommentArticleChildList(params) {
  return request({
    url: '/commentArticle/getChildCommentListByRootId',
    method: 'get',
    params
  })
}

export function getCommentArticleListByKeyword(params) {
  return request({
    url: '/commentArticle/queryAll',
    method: 'get',
    params
  })
}
