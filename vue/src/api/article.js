import request from '@/utils/request'

export async function getUserRank() {
  return request({
    url: '/article/userRank',
    method: 'get'
  })
}
