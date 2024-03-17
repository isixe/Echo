import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/reactionCommentArticle',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/reactionCommentArticle',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/reactionCommentArticle',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/reactionCommentArticle',
    method: 'get',
    params
  })
}

export function getByCommentArticleIdAndUserId(params) {
  return request({
    url: '/reactionCommentArticle/getByCommentArticleIdAndUserId',
    method: 'get',
    params
  })
}
