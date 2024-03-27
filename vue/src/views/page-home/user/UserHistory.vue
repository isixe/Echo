<template>
  <div class="container">
    <div class="collection-header">历史记录</div>
    <div class="collection-container">
      <a-menu
        class="nav-menu"
        v-model:selectedKeys="selectedKey"
        mode="horizontal"
        :items="items"
      />
      <template v-if="selectedKey == 'article' && articleFullList && articleFullList.length > 0">
        <div class="group-article-list" v-for="item in articleFullList" :key="item.id">
          <router-link :to="'/article/' + item.articleId" class="entry-item-box">
            <article-entry-item :item="item"></article-entry-item>
          </router-link>
          <a class="article-remove" @click="deleteArticleHistory(item.id)"
            ><DeleteOutlined />&nbsp;删除历史</a
          >
        </div>
      </template>
      <template
        v-else-if="selectedKey == 'question' && questionFullList && questionFullList.length > 0"
      >
        <div class="group-article-list" v-for="item in questionFullList" :key="item.id">
          <router-link :to="'/question/' + item.questionId" class="entry-item-box">
            <question-entry-item :item="item"></question-entry-item>
          </router-link>
          <a class="article-remove" @click="deleteQuestionHistory(item.id)"
            ><DeleteOutlined />&nbsp;删除历史</a
          >
        </div>
      </template>
      <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
    </div>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { ArticleEntryItem, QuestionEntryItem } from '@/views/page-home/components'
import { remove as removeArticleHistory, getArticleHistoryByUserId } from '@/api/history-article'
import { remove as removeQuestionHistory, getQuestionHistoryByUserId } from '@/api/history-question'
import { useUserStore } from '@/stores/user'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const store = useUserStore()
const articleFullList = ref([])
const questionFullList = ref([])
const selectedKey = ref(['article'])

const initLoading = ref(true)
const loading = ref(false)
const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  userId: store.id
})

onMounted(() => {
  window.addEventListener('scroll', scrollBottom, true)

  getDataSource(selectedKey.value)
})

onBeforeUnmount(() => window.removeEventListener('scroll', scrollBottom, true))

const scrollBottom = () => {
  if (initLoading.value || loading.value || params.pageNum >= pages.value) {
    return
  }

  const windowHeight = document.body.scrollHeight
  const scrollHeight = window.innerHeight + window.scrollY
  if (windowHeight - scrollHeight >= 1) {
    return
  }

  onLoadMore()
}

const onLoadMore = () => {
  loading.value = true
  params.pageNum += 1
  getDataSource(selectedKey.value)
  nextTick(() => {
    window.dispatchEvent(new Event('resize'))
  })
}

watch(selectedKey, () => {
  articleFullList.value = []
  questionFullList.value = []
  params.pageNum = 1
  getDataSource(selectedKey.value)
})

const getDataSource = (tab) => {
  switch (tab[0]) {
    case 'article':
      getArticleDataSource()
      break
    case 'question':
      getQuestionDataSource()
      break
  }
}

const getArticleDataSource = () => {
  getArticleHistoryByUserId(params).then((res) => {
    articleFullList.value = articleFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
    console.log(res.data)
  })
}

const getQuestionDataSource = () => {
  getQuestionHistoryByUserId(params).then((res) => {
    questionFullList.value = questionFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
    console.log(res.data)
  })
}

const deleteArticleHistory = (historyId) => {
  Modal.confirm({
    title: `确定要删除历史记录吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除后，文章将会从历史记录中移除',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', historyId)
      removeArticleHistory(formData).then(() => {
        message.success('删除历史记录成功')
        articleFullList.value = []
        questionFullList.value = []
        params.pageNum = 1
        getDataSource(selectedKey.value)
      })
    }
  })
}

const deleteQuestionHistory = (historyId) => {
  Modal.confirm({
    title: `确定要删除历史记录吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除后，问答将会从历史记录中移除',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', historyId)
      removeQuestionHistory(formData).then(() => {
        message.success('删除历史记录成功')
        articleFullList.value = []
        questionFullList.value = []
        params.pageNum = 1
        getDataSource(selectedKey.value)
      })
    }
  })
}

router.afterEach((to, from, next) => window.scrollTo(0, 0))

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
  padding: 0 200px;
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

@media screen and (max-width: 1000px) {
  .container {
    padding: 0;
  }
}

@media screen and (max-width: 800px) {
  .container {
    padding: 0;
  }

  .article-remove {
    font-size: 14px;
  }

  .item-title {
    width: 90%;
  }
}
</style>
