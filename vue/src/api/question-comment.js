import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/commentQuestion',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/commentQuestion',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/commentQuestion',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/commentQuestion',
    method: 'get',
    params
  })
}

export function getCommentQuestionRootListByQuestionId(params) {
  return request({
    url: '/commentQuestion/queryAllRootCommentByQuestionId',
    method: 'get',
    params
  })
}

export function getCommentQuestionChildListByRootId(params) {
  return request({
    url: '/commentQuestion/getChildCommentListByRootId',
    method: 'get',
    params
  })
}

export function getCommentQuestionListByKeyword(params) {
  return request({
    url: '/commentQuestion/queryAll',
    method: 'get',
    params
  })
}
