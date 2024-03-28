<template>
  <div class="container">
    <div class="category-header">标签 ：{{ tagName }}</div>
    <div class="conent-box">
      <a-menu
        class="nav-menu"
        v-model:selectedKeys="selectedKey"
        mode="horizontal"
        :items="items"
      />
      <template v-if="selectedKey == 'article' && articleFullList && articleFullList.length > 0">
        <div v-for="item in articleFullList" :key="item.id">
          <router-link :to="'/article/' + item.id">
            <article-entry-item :item="item"></article-entry-item
          ></router-link>
        </div>
      </template>
      <template
        v-else-if="selectedKey == 'question' && questionFullList && questionFullList.length > 0"
      >
        <div v-for="item in questionFullList" :key="item.id">
          <router-link :to="'/question/' + item.id" style="display: flex; width: 100%">
            <question-entry-item :item="item"></question-entry-item>
          </router-link>
        </div>
      </template>
      <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
    </div>
  </div>
</template>

<script setup>
import { ArticleEntryItem, QuestionEntryItem } from '@/views/page-home/components'
import { getArticleListByTagName } from '@/api/article'
import { getQuestionListByTagName } from '@/api/question'

const route = useRoute()
const router = useRouter()

const articleFullList = ref([])
const questionFullList = ref([])
const tagName = ref(route.query.tagName)
const selectedKey = ref([])

const initLoading = ref(true)
const loading = ref(false)
const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  tagName: route.query.tagName
})

onMounted(() => {
  window.addEventListener('scroll', scrollBottom, true)

  selectedKey.value = route.query.tab ? [route.query.tab] : ['article']
})

watch(selectedKey, (value) => {
  articleFullList.value = []
  questionFullList.value = []
  params.pageNum = 1
  getDataSource(value[0])
})

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

const getDataSource = (tab) => {
  switch (tab) {
    case 'article':
      router.push({ path: '/tag/', query: { tagName: route.query.tagName, tab: 'article' } })
      getArticleDataSource()
      break
    case 'question':
      router.push({ path: '/tag/', query: { tagName: route.query.tagName, tab: 'question' } })
      getQuestionDataSource()
      break
    default:
      selectedKey.value = ['article']
  }
}

const getArticleDataSource = () => {
  getArticleListByTagName(params).then((res) => {
    articleFullList.value = articleFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
  })
}

const getQuestionDataSource = () => {
  getQuestionListByTagName(params).then((res) => {
    questionFullList.value = questionFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
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
.container {
  padding: 15px 0;
}

.category-header {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
}

.conent-box {
  margin: 15px 150px;
  background-color: #ffffff;
  border-radius: 4px;
}

@media screen and (max-width: 800px) {
  .conent-box {
    margin: 15px 20px;
  }
}

@media screen and (max-width: 700px) {
  .conent-box {
    margin: 15px 10px;
  }
}
</style>
