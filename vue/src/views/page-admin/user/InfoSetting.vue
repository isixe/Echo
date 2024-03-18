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
          <a-input v-model:value="editData.name" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item name="email" label="邮箱">
          <a-input v-model:value="editData.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item name="password" label="密码">
          <a-input-password
            v-model:value="editData.password"
            placeholder="请输入密码"
            autocomplete="new-password"
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
import { DateTime } from 'luxon'
import { get, update } from '@/api/admin'
import { message } from 'ant-design-vue'
import { uploadAvatar } from '@/api/file'
import { useAdminStore } from '@/stores/admin'

const form = ref()
const store = useAdminStore()
const loading = ref(false)
const formState = reactive({
  layout: 'vertical'
})

const editData = reactive({
  id: '',
  name: '',
  password: '',
  email: '',
  avatar: '',
  lastActiveTime: '',
  createdTime: ''
})

const rules = {
  name: [
    {
      min: 4,
      max: 16,
      required: true,
      trigger: 'blur',
      message: '用户名长度不能小于4个字符，大于16个字符'
    }
  ],
  password: [{ min: 6, required: true, trigger: 'blur', message: '密码不能小于6位' }],
  email: [
    {
      pattern: '^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$',
      required: false,
      trigger: 'blur',
      message: '邮箱格式不正确'
    }
  ]
}

onMounted(() => {
  queryData()
})

const queryData = () => {
  get({ id: store.id }).then((res) => {
    Object.keys(editData).forEach((key) => {
      editData[key] = res.data[key]
    })
    editData.lastActiveTime = DateTime.fromSQL(editData.lastActiveTime).toFormat(
      "yyyy-MM-dd'T'HH:mm:ss"
    )
    editData.createdTime = DateTime.fromSQL(editData.createdTime).toFormat("yyyy-MM-dd'T'HH:mm:ss")
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

      update(formData)
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
</style>
