import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/reactionCommentQuestion',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/reactionCommentQuestion',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/reactionCommentQuestion',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/reactionCommentQuestion',
    method: 'get',
    params
  })
}

export function getByCommentQuestionIdAndUserId(params) {
  return request({
    url: '/reactionCommentQuestion/getByCommentQuestionIdAndUserId',
    method: 'get',
    params
  })
}
