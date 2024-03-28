<template>
  <div class="container">
    <div class="left">
      <div class="left-item">
        <label>头图：</label>
        <pic-upload v-model="data.featuredPic"></pic-upload>
      </div>
      <div class="left-item">
        <label>标签：</label>
        <tag-input v-model="data.tag"></tag-input>
      </div>
      <div class="left-item">
        <label>类别：</label>
        <category-select
          v-model:categoryId="data.categoryId"
          v-model:categoryName="data.category"
        ></category-select>
      </div>
      <div class="left-item">
        <label>分组：</label>
        <article-group-select
          v-model:userId="data.userId"
          v-model:articleGroupId="data.articleGroupId"
          v-model:articleGroupName="data.articleGroupName"
        ></article-group-select>
      </div>
    </div>
    <div class="right">
      <a-input
        class="title"
        v-model:value="data.title"
        placeholder="文章标题"
        show-count
        :maxlength="42"
      />
      <article-editor class="right-content" v-model="data.content" />
      <div class="right-footer">
        <div class="right-footer-left">
          <template v-if="data.status">
            <a-date-picker v-model:value="data.publishTime" disabled />
            <a-time-picker v-model:value="data.publishTime" style="margin-left: 10px" disabled />
          </template>
          <template v-else>
            <a-date-picker v-model:value="data.publishTime" />
            <a-time-picker v-model:value="data.publishTime" style="margin-left: 10px" />
          </template>
        </div>

        <div class="right-footer-right">
          <template v-if="data.content && data.status !== 1 && !data.publishTime">
            <a-button
              type="primary"
              style="background-color: #008000; margin-right: 10px"
              @click="draftArticle()"
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
              @click="postArticle()"
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
    </div>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import { DateTime } from 'luxon'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { get, add, update } from '@/api/article'
import { TagInput, CategorySelect, ArticleGroupSelect, PicUpload } from '@/components'
import ArticleEditor from '@/components/ArticleEditor'

const loading = ref()
const route = useRoute()
const router = useRouter()
const data = reactive({
  id: '',
  category: '',
  status: null,
  articleGroupName: '',
  title: '',
  featuredPic: '',
  summary: '',
  content: '',
  userId: '',
  categoryId: '',
  tag: '',
  articleGroupId: '',
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

const draftArticle = () => {
  data.summary = data.content.replace(/<[^>]+>|\n/g, '').substring(0, 120)
  const formData = new FormData()
  Object.keys(data).forEach((key) => {
    formData.append(key, data[key])
  })
  formData.delete('publishTime')

  if (data.status === 0) {
    updateArticle(formData)
  } else {
    formData.set('status', 0)
    addArticle(formData)
  }
}

const postArticle = () => {
  data.summary = data.content.replace(/<[^>]+>|\n/g, '').substring(0, 120)
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
    addArticle(formData)
    return
  }

  if (!data.status) {
    formData.set('status', 1)
    updateArticle(formData)
    return
  }

  if (data.id && data.status) {
    updateArticle(formData)
    return
  }

  formData.set('status', 1)
  addArticle(formData)
}

const addArticle = (formData) => {
  add(formData)
    .then(() => {
      message.success('发布成功')
      loading.value = false
      router.push('/article')
    })
    .catch(() => {
      loading.value = false
    })
}

const updateArticle = (formData) => {
  update(formData)
    .then(() => {
      message.success('更新成功')
      router.push('/article')
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
  display: flex;
}

.left {
  flex: 1;
}

.left-item {
  margin-bottom: 15px;
  display: flex;
  flex-direction: row;
}

.left-item span {
  margin-left: 5px;
}

.left-item label {
  width: 55px;
  margin-right: 10px;
}

.right {
  width: auto;
}

.right-content {
  margin-bottom: 10px;
}

.right .title {
  height: 40px;
  font-size: 16px;
  border-radius: 0;
}

.right-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 25px;
}

@media screen and (max-width: 1000px) {
  .container {
    flex-direction: column;
  }

  .right-footer {
    display: block;
  }

  .right-footer-right {
    margin-top: 10px;
  }
}

@media screen and (max-width: 800px) {
  .left-item {
    display: block;
  }

  .left-item span {
    margin-left: 0;
  }

  :global(.ant-upload.ant-upload-select.ant-upload-select-picture-card) {
    width: 100% !important;
  }

  :global(.ant-upload img) {
    width: 100% !important;
  }
}
</style>
