import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/question',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/question',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/question',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/question',
    method: 'get',
    params
  })
}

export function getQuestionDraftList(params) {
  return request({
    url: '/question/queryAllDraftByUserId',
    method: 'get',
    params
  })
}

export function getQuestionListByKeyword(params) {
  return request({
    url: '/question/queryAll',
    method: 'get',
    params
  })
}

export function getQuestionListByTitle(params) {
  return request({
    url: '/question/queryAllByTitle',
    method: 'get',
    params
  })
}

export function getQuestionListByCategoryId(params) {
  return request({
    url: '/question/queryAllByCategoryId',
    method: 'get',
    params
  })
}

export function getQuestionListByTagName(params) {
  return request({
    url: '/question/queryAllByTagName',
    method: 'get',
    params
  })
}

export function getQuestionListByUserId(params) {
  return request({
    url: '/question/queryAllByUserId',
    method: 'get',
    params
  })
}

export function getActiveQuestionListByKeyword(params) {
  return request({
    url: '/question/queryAllActive',
    method: 'get',
    params
  })
}

export function getHotActiveQuestionListByKeyword(params) {
  return request({
    url: '/question/queryAllHotActive',
    method: 'get',
    params
  })
}

export async function getUserRank() {
  return request({
    url: '/question/getUserRankList',
    method: 'get'
  })
}
