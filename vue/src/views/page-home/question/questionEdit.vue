<template>
  <div class="container">
    <div class="question-header">
      <div class="left-item">
        <label>类别：</label>
        <category-select
          v-model:categoryId="data.categoryId"
          v-model:categoryName="data.category"
        />
      </div>
      <div class="left-item">
        <label>标签：</label>
        <tag-input v-model="data.tag" />
      </div>
      <div class="question-header-left">
        <template v-if="data.status">
          <a-date-picker v-model:value="data.publishTime" disabled />
          <a-time-picker v-model:value="data.publishTime" style="margin-left: 10px" disabled />
        </template>
        <template v-else>
          <a-date-picker v-model:value="data.publishTime" />
          <a-time-picker v-model:value="data.publishTime" style="margin-left: 10px" />
        </template>
      </div>

      <div class="question-header-right">
        <template v-if="data.content && data.status !== 1 && !data.publishTime">
          <a-button
            type="primary"
            style="background-color: #008000; margin-right: 10px"
            @click="draftQuestion()"
            :loading="loading"
            >保存为草稿</a-button
          >
        </template>
        <template v-else>
          <a-button
            type="primary"
            style="background-color: #ccc; margin-right: 10px; color: #ffffff"
            disabled
            >保存为草稿</a-button
          >
        </template>
        <template v-if="data.publishTime && data.content">
          <a-button
            type="primary"
            style="background-color: #4d45e5"
            @click="postQuestion()"
            :loading="loading"
            >发布</a-button
          >
        </template>

        <template v-else>
          <a-button type="primary" style="background-color: #ccc; color: #ffffff" disabled
            >发布</a-button
          >
        </template>
      </div>
    </div>
    <a-input
      class="title"
      v-model:value="data.title"
      placeholder="问答标题"
      show-count
      :maxlength="42"
    />
    <article-editor class="question-content" v-model="data.content" />
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import { DateTime } from 'luxon'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { get, add, update } from '@/api/question'
import { TagInput, CategorySelect } from '@/components'

const loading = ref()
const route = useRoute()
const router = useRouter()
const data = reactive({
  id: '',
  category: '',
  status: null,
  title: '',
  content: '',
  userId: '',
  categoryId: '',
  tag: '',
  publishTime: ''
})

onMounted(() => {
  const store = useUserStore()
  if (!data.userId) {
    data.userId = store.id
  }

  if (!route.query.id) {
    return
  }
  get({ id: route.query.id })
    .then((res) => {
      Object.keys(data).forEach((key) => {
        data[key] = res.data[key]
      })

      if (res.data.userId != store.id) {
        message.error('无访问权限')
        router.push('/403')
      }

      if (data.publishTime) {
        data.publishTime = dayjs(data.publishTime, 'YYYY-MM-DD HH:mm:ss')
      }
    })
    .catch(() => {
      router.push('/404')
    })
})

const draftQuestion = () => {
  const formData = new FormData()
  Object.keys(data).forEach((key) => {
    formData.append(key, data[key])
  })
  formData.delete('publishTime')

  if (data.status === 0) {
    updateQuestion(formData)
  } else {
    formData.set('status', 0)
    addQuestion(formData)
  }
}

const postQuestion = () => {
  const formData = new FormData()
  Object.keys(data).forEach((key) => {
    formData.append(key, data[key])
  })
  formData.set(
    'publishTime',
    DateTime.fromJSDate(data.publishTime.$d).toFormat("yyyy-MM-dd'T'HH:mm:ss")
  )

  if (!data.id && !data.status) {
    formData.set('status', 1)
    addQuestion(formData)
    return
  }

  if (!data.status) {
    formData.set('status', 1)
    updateQuestion(formData)
    return
  }

  if (data.id && data.status) {
    updateQuestion(formData)
    return
  }

  formData.set('status', 1)
  addQuestion(formData)
}

const addQuestion = (formData) => {
  add(formData)
    .then(() => {
      message.success('发布成功')
      loading.value = false
      router.push('/question')
    })
    .catch(() => {
      loading.value = false
    })
}

const updateQuestion = (formData) => {
  update(formData)
    .then(() => {
      message.success('更新成功')
      router.push('/question')
      loading.value = false
    })
    .catch(() => {
      loading.value = false
    })
}
</script>

<style scoped>
.container {
  padding: 15px;
  background-color: #ffffff;
}

.left {
  flex: 1;
}

.left-item {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.left-item span {
  margin-left: 5px;
}

.left-item label {
  width: 55px;
  margin-right: 10px;
}

.question-content {
  margin-bottom: 10px;
}

.right .title {
  height: 40px;
  font-size: 16px;
  border-radius: 0;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
}
</style>
