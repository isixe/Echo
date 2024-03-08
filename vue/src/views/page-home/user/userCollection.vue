<template>
  <div class="container">
    <div class="collection-header">我的收藏</div>
    <div class="collection-container">
      <a-menu
        class="nav-menu"
        v-model:selectedKeys="selectedKey"
        mode="horizontal"
        :items="items"
      />
      <template v-if="selectedKey == 'article'">
        <template v-if="articleData && articleData.length > 0">
          <div class="group-article-list" v-for="item in articleData" :key="item.id">
            <RouterLink :to="'/article/' + item.id" class="entry-item-box">
              <article-entry-item :item="item"></article-entry-item>
            </RouterLink>
            <a class="article-remove" @click="removeArticle(item.id)"
              ><DeleteOutlined />&nbsp;取消收藏</a
            >
          </div>
        </template>
        <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
      </template>
      <template v-else-if="selectedKey == 'question'">
        <template v-if="questionData && questionData.length > 0">
          <div class="group-article-list" v-for="item in questionData" :key="item.id">
            <RouterLink :to="'/question/' + item.id" class="entry-item-box">
              <question-entry-item :item="item"></question-entry-item>
            </RouterLink>
            <a class="article-remove" @click="removeQuestion(item.id)"
              ><DeleteOutlined />&nbsp;取消收藏</a
            >
          </div>
        </template>
        <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
      </template>
    </div>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { ArticleEntryItem, QuestionEntryItem } from '@/views/page-home/components'
import {
  remove as unCollectArticle,
  getCollectionArticleListByUserId
} from '@/api/collectionArticle'
import {
  remove as unCollectQuestion,
  getCollectionQuestionListByUserId
} from '@/api/collectionQuestion'
import { useUserStore } from '@/stores/user'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const store = useUserStore()
const articleData = ref()
const questionData = ref()
const selectedKey = ref(['article'])
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  userId: store.id
})

onMounted(() => {
  getArticleDataSource()
  getQuestionDataSource()
})

const getArticleDataSource = () => {
  getCollectionArticleListByUserId(params).then((res) => {
    articleData.value = res.data.records
  })
}

const getQuestionDataSource = () => {
  getCollectionQuestionListByUserId(params).then((res) => {
    questionData.value = res.data.records
  })
}

watch(selectedKey, (key) => {
  switch (key[0]) {
    case 'article':
      getArticleDataSource(key[0])
      break
    case 'question':
      getQuestionDataSource(key[0])
      break
  }
})

const removeArticle = (collectionId) => {
  Modal.confirm({
    title: `确定要取消收藏吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除后，文章将会从您的收藏中移除',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', collectionId)
      unCollectArticle(formData).then(() => {
        message.success('取消收藏成功')
        getArticleDataSource()
      })
    }
  })
}

const removeQuestion = (collectionId) => {
  Modal.confirm({
    title: `确定要取消收藏吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除后，问答将会从您的收藏中移除',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', collectionId)
      unCollectQuestion(formData).then(() => {
        message.success('取消收藏成功')
        getQuestionDataSource()
      })
    }
  })
}

const items = ref([
  {
    key: 'article',
    label: '文章'
  },
  {
    key: 'question',
    label: '问答'
  }
])
</script>

<style scoped>
a {
  text-decoration: none;
}

.container {
  padding: 15px 200px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.collection-container {
  background-color: #ffffff;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.collection-header {
  padding: 15px;
  text-align: center;
  font-size: 20px;
  font-weight: 700;
}

.collection-container {
  margin: 15px 40px;
  background-color: #ffffff;
  border-radius: 4px;
}

.group-article-list {
  display: flex;
  justify-content: space-between;
}

.entry-item-box {
  flex: 1;
}

.article-remove {
  width: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  color: #666;
  cursor: pointer;
}

.article-remove:hover {
  color: #4d45e5;
}
</style>
