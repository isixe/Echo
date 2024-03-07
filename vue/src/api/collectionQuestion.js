import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/collectionQuestion',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/collectionQuestion',
    method: 'delete',
    data
  })
}

export function getCollectionQuestionListByKeyword(params) {
  return request({
    url: '/collectionQuestion/queryAll',
    method: 'get',
    params
  })
}

export function getCollectByUserAndQuestion(params) {
  return request({
    url: '/collectionQuestion/getCollect',
    method: 'get',
    params
  })
}
