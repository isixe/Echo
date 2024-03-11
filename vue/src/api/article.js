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

export function getArticleListByGroupId(params) {
  return request({
    url: '/article/getList',
    method: 'get',
    params
  })
}

export function getArticleDraftList(params) {
  return request({
    url: '/article/getDraftList',
    method: 'get',
    params
  })
}

export function getArticleListByKeyword(params) {
  return request({
    url: '/article/queryAll',
    method: 'get',
    params
  })
}

export function getArticleListByTitle(params) {
  return request({
    url: '/article/queryAllByTitle',
    method: 'get',
    params
  })
}

export function getArticleListByCategoryId(params) {
  return request({
    url: '/article/queryAllByCategoryId',
    method: 'get',
    params
  })
}

export function getArticleListByTagName(params) {
  return request({
    url: '/article/queryAllByTagName',
    method: 'get',
    params
  })
}

export function getArticleListByUserId(params) {
  return request({
    url: '/article/queryAllByUserId',
    method: 'get',
    params
  })
}

export function getActiveArticleListByKeyword(params) {
  return request({
    url: '/article/queryAllActive',
    method: 'get',
    params
  })
}

export function getHotActiveArticleListByKeyword(params) {
  return request({
    url: '/article/queryAllHotActive',
    method: 'get',
    params
  })
}

export async function getUserRank() {
  return request({
    url: '/article/getUserRankList',
    method: 'get'
  })
}

export async function putArticleGroup(params) {
  return request({
    url: '/article/group',
    method: 'put',
    params
  })
}

export async function removeArticleGroup(params) {
  return request({
    url: '/article/group',
    method: 'put',
    params
  })
}

export function getUnGroupArticleByUserId(params) {
  return request({
    url: '/article/queryUnGroupByUserId',
    method: 'get',
    params
  })
}
