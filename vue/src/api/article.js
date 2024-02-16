import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/article',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/article',
    method: 'put',
    data
  })
}

export function remove(data) {
  return request({
    url: '/article',
    method: 'delete',
    data
  })
}

export function get(params) {
  return request({
    url: '/article',
    method: 'get',
    params
  })
}

export function getArticleList(params) {
  return request({
    url: '/article/queryAll',
    method: 'get',
    params
  })
}

export async function getUserRank() {
  return request({
    url: '/article/userRank',
    method: 'get'
  })
}
