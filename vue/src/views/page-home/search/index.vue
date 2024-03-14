<template>
  <div class="nav-bar">
    <div class="inner-bar">
      <a-menu
        class="nav-menu"
        v-model:selectedKeys="selectedKey"
        mode="horizontal"
        :items="items"
      />
      <div>
        <a
          class="operation-filter"
          @click="() => (showFilter = !showFilter)"
          v-show="selectedKey != 'user'"
          ><FilterOutlined /> 筛选</a
        >
      </div>
    </div>
  </div>
  <div class="nav-bar" v-show="showFilter">
    <div class="filter-option">
      <router-link
        :to="{ path: 'search', query: { type: search.type, q: search.q } }"
        :class="!search.sort ? 'active' : ''"
      >
        综合排序
      </router-link>
      <router-link
        :to="{ path: 'search', query: { type: search.type, sort: 'likeCount', q: search.q } }"
        :class="search.sort == 'likeCount' ? 'active' : ''"
        >最多点赞</router-link
      >
      <router-link
        :to="{ path: 'search', query: { type: search.type, sort: 'updateTime', q: search.q } }"
        :class="search.sort == 'updateTime' ? 'active' : ''"
      >
        最新发布
      </router-link>
    </div>
  </div>

  <div class="search-result">
    <div class="search-content">
      <template v-if="selectedKey == 'article' && articleData && articleData.length > 0">
        <div v-for="item in articleData" :key="item.id">
          <router-link :to="'/article/' + item.id">
            <article-entry-item :item="item"></article-entry-item
          ></router-link>
        </div>
      </template>
      <template v-else-if="selectedKey == 'question'">
        <template v-if="questionData">
          <div v-for="item in questionData" :key="item.id">
            <router-link :to="'/question/' + item.id" style="display: flex; width: 100%">
              <question-entry-item :item="item"></question-entry-item>
            </router-link>
          </div>
        </template>
        <template v-else> <a-empty style="padding-bottom: 30px" /></template>
      </template>
      <template v-else-if="selectedKey == 'user' && userData && userData.length > 0">
        <div v-for="item in userData" :key="item.id">
          <router-link :to="'/user/' + item.id" style="display: flex; width: 100%">
            <user-entry-item :item="item"></user-entry-item>
          </router-link>
        </div>
      </template>
      <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
      <div class="pagination" v-show="total > 15">
        <a-pagination
          v-model:current="current"
          :showSizeChanger="false"
          show-quick-jumper
          :total="total"
          @change="onChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ArticleEntryItem, QuestionEntryItem, UserEntryItem } from '@/views/page-home/components'
import { getActiveArticleListByKeyword } from '@/api/article'
import { getActiveQuestionListByKeyword } from '@/api/question'
import { getUserListByName } from '@/api/user'

const route = useRoute()
const router = useRouter()

const articleData = ref()
const questionData = ref()
const userData = ref()

const selectedKey = ref(['article'])
const showFilter = ref(false)

const search = reactive({
  type: route.query.type,
  sort: route.query.sort,
  q: route.query.q
})

const current = ref(1)
const total = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  keyword: route.query.q,
  sort: route.query.sort
})

const onChange = (pageNumber) => {
  params.pageNum = pageNumber
  getDataSource(search.type)
  window.scrollTo(0, 0)
}

onMounted(() => {
  const type = route.query.type ? route.query.type : 'article'

  selectedKey.value = [type]
  search.type = type

  getDataSource(search.type)
})

watch(
  () => route.query,
  () => {
    const type = route.query.type ? route.query.type : search.type

    if (!route.query.type) {
      return router.push({ path: '/search', query: { type, q: route.query.q } })
    }

    Object.keys(search).forEach((key) => {
      search[key] = route.query[key]
    })

    search.type = search.type ? search.type : type

    params.keyword = search.q
    params.sort = search.sort

    getDataSource(search.type)
  }
)

watch(selectedKey, (key) => {
  switch (key[0]) {
    case 'article':
      router.push({ path: '/search', query: { type: 'article', q: route.query.q } })
      break
    case 'question':
      router.push({ path: '/search', query: { type: 'question', q: route.query.q } })
      break
    case 'user':
      router.push({ path: '/search', query: { type: 'user', q: route.query.q } })
      break
  }
})

const getDataSource = (type) => {
  switch (type) {
    case 'article':
      getArticleDataSource()
      break
    case 'question':
      getQuestionDataSource()
      break
    case 'user':
      getUserDataSource()
      break
    default:
      getArticleDataSource()
  }
}

const getArticleDataSource = () => {
  getActiveArticleListByKeyword(params).then((res) => {
    articleData.value = res.data.records
    current.value = res.data.current
    total.value = res.data.total
  })
}

const getQuestionDataSource = () => {
  getActiveQuestionListByKeyword(params).then((res) => {
    questionData.value = res.data.records
    current.value = res.data.current
    total.value = res.data.total
  })
}

const getUserDataSource = () => {
  getUserListByName(params).then((res) => {
    userData.value = res.data.records
    current.value = res.data.current
    total.value = res.data.total
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
  },
  {
    key: 'user',
    label: '用户'
  }
])
</script>

<style scoped>
.nav-bar {
  display: flex;
  justify-content: center;
}

.inner-bar {
  width: 1000px;
  border-radius: 4px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
}

.search-result {
  display: flex;
  justify-content: center;
  margin: 15px 0;
}

.search-content {
  width: 1000px;
  background-color: #ffffff;
}

.operation-filter {
  color: #666;
}

.operation-filter:hover {
  color: #4d45e5;
}

.filter-option {
  background-color: #ffffff;
  width: 1000px;
  padding: 15px;
}

.filter-option a {
  border-radius: 4px;
  padding: 8px 12px;
  margin-right: 10px;
  font-size: 14px;
  color: #666;
}

.filter-option .active {
  background-color: rgba(23, 114, 246, 0.1);
}

.filter-option a:hover {
  background-color: rgba(23, 114, 246, 0.1);
  color: #4d45e5;
  cursor: pointer;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 15px;
  padding-bottom: 15px;
}
</style>
