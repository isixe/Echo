import request from '@/utils/request'

export async function uploadAvatar(data) {
  return request({
    url: '/upload/avatar',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

export async function uploadPic(data) {
  return request({
    url: '/upload/pic',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}
