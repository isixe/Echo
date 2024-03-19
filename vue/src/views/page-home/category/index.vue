<template>
  <div class="container">
    <div class="category-header">分类 ：{{ category }}</div>
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
import { get } from '@/api/category'
import { getArticleListByCategoryId } from '@/api/article'
import { getQuestionListByCategoryId } from '@/api/question'

const route = useRoute()
const router = useRouter()
const articleFullList = ref([])
const questionFullList = ref([])
const category = ref()
const selectedKey = ref([])

const initLoading = ref(true)
const loading = ref(false)
const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  categoryId: route.params.id
})

onMounted(() => {
  window.addEventListener('scroll', scrollBottom, true)

  get({ id: route.params.id }).then((res) => {
    category.value = res.data.categoryName
  })

  selectedKey.value = route.query.tab ? [route.query.tab] : ['article']
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

watch(selectedKey, (value) => {
  articleFullList.value = []
  questionFullList.value = []
  params.pageNum = 1
  getDataSource(value[0])
})

const getDataSource = (tab) => {
  switch (tab) {
    case 'article':
      router.push({ path: '/category/' + route.params.id, query: { tab: 'article' } })
      getArticleDataSource()
      break
    case 'question':
      router.push({ path: '/category/' + route.params.id, query: { tab: 'question' } })
      getQuestionDataSource()
      break
    default:
      selectedKey.value = ['article']
  }
}

const getArticleDataSource = () => {
  getArticleListByCategoryId(params).then((res) => {
    articleFullList.value = articleFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
  })
}

const getQuestionDataSource = () => {
  getQuestionListByCategoryId(params).then((res) => {
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
  padding-bottom: 15px;
}

.category-header {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  padding: 15px 0;
}

.conent-box {
  margin: 0 40px;
  background-color: #ffffff;
  border-radius: 4px;
}
</style>
