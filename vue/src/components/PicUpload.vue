<template>
  <a-upload
    :v-model="data"
    class="pic-upload"
    list-type="picture-card"
    :show-upload-list="false"
    :max-count="1"
    :customRequest="customAddUpload"
    :before-upload="beforeUpload"
    @change="handleChange"
  >
    <img
      v-if="data"
      :src="data"
      alt="pic"
      width="415"
      height="150"
      style="object-fit: cover; border-radius: 6px"
    />
    <div v-else>
      <loading-outlined v-if="uploadLoading"></loading-outlined>
      <plus-outlined v-else></plus-outlined>
      <div class="ant-upload-text">上传</div>
    </div>
  </a-upload>
</template>

<script setup>
import { message } from 'ant-design-vue'
import { uploadPic } from '@/api/file'

const data = defineModel()
const uploadLoading = ref(false)

const beforeUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('仅支持JPG和PNG格式!')
  }

  return isJpgOrPng
}

const handleChange = (info) => {
  if (info.file.status === 'uploading') {
    return (uploadLoading.value = true)
  }

  if (info.file.status === 'error') {
    message.error('上传失败')
  }
  uploadLoading.value = false
}

const customAddUpload = (e) => {
  uploadPic({
    file: e.file
  })
    .then((res) => {
      message.success('上传成功')
      data.value = res.data
      e.onSuccess(res.data, e)
    })
    .catch((err) => {
      e.onError(err)
    })
}
</script>

<style scoped>
.pic-upload > :global(.ant-upload.ant-upload-select.ant-upload-select-picture-card) {
  width: 415px !important;
  height: 152px !important;
}
</style>
