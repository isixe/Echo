<template>
  <div class="profile-container">
    <a-form
      class="profile-form"
      ref="form"
      :layout="formState.layout"
      :model="editData"
      :rules="rules"
    >
      <div class="form-left">
        <a-form-item name="name" label="用户名">
          <a-input
            v-model:value="editData.name"
            placeholder="请输入用户名"
            autocomplete="new-password"
          />
        </a-form-item>
        <a-form-item name="email" label="邮箱">
          <a-input v-model:value="editData.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item name="description" label="简介">
          <a-textarea
            v-model:value="editData.description"
            placeholder="请输入简介"
            show-count
            :maxlength="60"
          />
        </a-form-item>
      </div>
      <div class="form-right">
        <a-form-item name="avatar" label="头像">
          <div class="upload-avatar">
            <a-avatar class="user-avatar" :size="200" :src="editData.avatar">
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
          </div>
          <div style="margin-top: 15px" class="upload-btn">
            <a-upload
              :before-upload="beforeUpload"
              :max-count="1"
              :showUploadList="false"
              listType="picture"
              :customRequest="avatarUploadAction"
            >
              <a-button>
                <upload-outlined />
                选择文件
              </a-button>
            </a-upload>
          </div>
        </a-form-item>
      </div>
    </a-form>
    <a-button
      key="submit"
      type="primary"
      style="background-color: #4d45e5"
      :loading="loading"
      @click="handleEditOk"
      >保存</a-button
    >
  </div>
</template>

<script setup>
import { get, updateProfile } from '@/api/user'
import { message } from 'ant-design-vue'
import { uploadAvatar } from '@/api/file'
import { useUserStore } from '@/stores/user'

const store = useUserStore()

const editData = reactive({
  id: store.id,
  name: '',
  email: '',
  description: '',
  avatar: ''
})

const loading = ref(false)
const form = ref()
const formState = reactive({
  layout: 'vertical'
})

onMounted(() => {
  queryData()
})

const queryData = () => {
  get({ id: store.id }).then((res) => {
    Object.keys(editData).forEach((key) => {
      editData[key] = res.data[key]
    })
  })
}

const handleEditOk = () => {
  loading.value = true
  form.value
    .validate()
    .then(async () => {
      const formData = new FormData()
      Object.keys(editData).forEach((key) => {
        formData.append(key, editData[key])
      })
      updateProfile(formData)
        .then(() => {
          message.success('修改成功')
          loading.value = false
          queryData()
        })
        .catch(() => {
          loading.value = false
        })
    })
    .catch(() => {
      loading.value = false
    })
}

//upload
const beforeUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('仅支持JPG和PNG格式!')
  }

  return isJpgOrPng
}

const avatarUploadAction = (info) => {
  const formData = new FormData()
  formData.append('file', info.file)
  uploadAvatar(formData).then((res) => {
    message.success('上传成功')
    editData.avatar = res.data
  })
}
</script>

<style scoped>
.profile-container {
  background-color: #ffffff;
  padding: 20px;
  min-height: 90vh;
  flex-wrap: wrap;
}

.profile-form {
  display: flex;
}

.form-left {
  flex: 1;
  padding-right: 20px;
}

.form-right {
  width: 300px;
  display: flex;
  justify-content: center;
}

.upload-avatar,
.upload-btn {
  display: flex;
  justify-content: center;
}

@media screen and (max-width: 800px) {
  .user-avatar {
    width: 150px !important;
    height: 150px !important;
  }

  .form-right {
    width: auto;
  }
}
</style>
