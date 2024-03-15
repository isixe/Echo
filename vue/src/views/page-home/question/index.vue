<template>
  <!-- <div class="nav-box">
        <div class="category-tags-container">
            <div class="categories">
                <span class="tag-category">
                    <ClockCircleOutlined /> 历史
                </span>
                <span class="tag-category">
                    <PlusOutlined /> 关注
                </span>
                <span class="tag-category">
                    <HeartOutlined /> 收藏
                </span>
                <span class="tag-category">
                    <LikeOutlined /> 点赞
                </span>
            </div>
        </div>
    </div> -->
  <div class="content-box">
    <div class="article-container">
      <a-layout>
        <a-layout-content class="article-items-container">
          <a-menu
            class="nav-menu"
            v-model:selectedKeys="selectedKey"
            mode="horizontal"
            :items="items"
          />
          <a-list :loading="initLoading" item-layout="horizontal" :data-source="fullList">
            <template #renderItem="{ item }">
              <router-link :to="'/question/' + item.id" style="display: flex; width: 100%">
                <question-entry-item :item="item"></question-entry-item>
              </router-link>
            </template>
          </a-list>
        </a-layout-content>
        <a-layout-sider class="sidebar-container" width="320px">
          <div class="notice-card">
            <div class="notice-title">
              <span>公告🔉</span>
            </div>
            <div class="notice-content">
              <router-link class="notice-item" to="/question/3246543">
                <span class="article-title"> 功能维护中... </span>
                <span class="article-time"> 2024-02-01 </span>
              </router-link>
            </div>
          </div>
          <div class="sticky-card">
            <div class="rank-card">
              <div class="rank-title">
                <span
                  >提问榜
                  <font-awesome-icon
                    :icon="['fas', 'ranking-star']"
                    style="color: #c9c9c9; font-size: 14px; margin-left: 0"
                  />
                </span>
              </div>
              <div class="rank-content">
                <router-link
                  class="rank-item"
                  :to="'/user/' + rankItem.id"
                  v-for="rankItem in rankList"
                  :key="rankItem.id"
                >
                  <div class="user-info">
                    <a-avatar class="user-avatar" :size="35" :src="rankItem.avatar">
                      <template #icon>
                        <UserOutlined />
                      </template>
                    </a-avatar>
                    <div class="user-content">
                      <span class="user-name">{{ rankItem.name }}</span>
                      <span class="user-desc">{{ rankItem.description }}</span>
                    </div>
                  </div>
                  <div class="article-count">{{ rankItem.rankSum }}</div>
                </router-link>
              </div>
            </div>
            <div class="recommend-category-card">
              <div class="recommend-title">
                <span>
                  推荐分类
                  <font-awesome-icon
                    :icon="['fas', 'tags']"
                    style="color: #c9c9c9; font-size: 14px; margin-left: 0"
                  />
                </span>
              </div>
              <div class="recommend-content">
                <template v-for="category in recommendCategory" :key="category.id">
                  <router-link
                    :to="{ path: '/category/' + category.id, query: { tab: 'question' } }"
                  >
                    {{ category.categoryName }}
                  </router-link>
                </template>
              </div>
            </div>
          </div>
        </a-layout-sider>
      </a-layout>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { QuestionEntryItem } from '@/views/page-home/components'
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  getActiveQuestionListByKeyword,
  getUserRank,
  getSubscribeQuestionByUserId
} from '@/api/question'
import { getCategoryListByKeyword } from '@/api/category'
import { faTags, faBullhorn, faRankingStar } from '@fortawesome/free-solid-svg-icons'
library.add(faTags, faBullhorn, faRankingStar)

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const selectedKey = ref(['latest'])
const recommendCategory = ref([])

const initLoading = ref(true)
const loading = ref(false)
const fullList = ref([])
const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  sort: 'updateTime',
  userId: store.id
})

const rankList = ref([])

onMounted(() => {
  window.addEventListener('scroll', scrollBottom, true)

  getUserRank().then((res) => (rankList.value = res.data))

  let type = route.query.type ? route.query.type : 'latest'

  selectedKey.value = [type]

  getCategoryListByKeyword({ pageSize: 6 }).then((res) => {
    recommendCategory.value = res.data.records
  })
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
  let type = route.query.type ? route.query.type : 'latest'
  getDataSource(type)
  nextTick(() => {
    window.dispatchEvent(new Event('resize'))
  })
}

watch(selectedKey, (key) => {
  fullList.value = []
  params.pageNum = 1
  switch (key[0]) {
    case 'latest':
      router.push('/question')
      break
    case 'recommend':
      router.push({ path: '/question', query: { type: 'recommend' } })
      break
    case 'hot':
      router.push({ path: '/question', query: { type: 'hot' } })
      break
    case 'follow':
      router.push({ path: '/question', query: { type: 'follow' } })
      break
  }
  getDataSource(key[0])
})

const getDataSource = (type) => {
  switch (type) {
    case 'latest':
      params.sort = 'updateTime'
      getActiveQuestionListByKeyword(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    case 'recommend':
      break
    case 'hot':
      params.sort = null
      getActiveQuestionListByKeyword(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    case 'follow':
      getSubscribeQuestionByUserId(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    default:
      router.push('/question')
      selectedKey.value = ['latest']
  }
}

router.afterEach((to, from, next) => window.scrollTo(0, 0))

const items = ref([
  {
    key: 'latest',
    label: '最新'
  },
  {
    key: 'recommend',
    label: '推荐'
  },
  {
    key: 'hot',
    label: '热门'
  },
  {
    key: 'follow',
    label: '订阅'
  }
])
</script>

<style scoped>
.nav-box {
  background-color: #ffffff;
}

.content-box {
  margin-top: 15px;
  margin-bottom: 15px;
}

.category-tags-container {
  align-items: center;
  display: flex;
  height: 48px;
  justify-content: space-between;
  margin: 0 auto;
  max-width: 1200px;
  background-color: #ffffff;
}

.nav-menu {
  font-size: 16px;
  padding: 4px 0;
}

.article-container {
  margin: 0 auto;
  max-width: 1200px;
}

.tag-category {
  color: #666;
  cursor: pointer;
  font-size: 15px;
  padding: 4px 14px;
  margin-right: 10px;
}

.tag-category:hover {
  background-color: #f1f1ff;
  border-radius: 4px;
  color: #4d45e5;
}

.active-category {
  background-color: #4d45e5 !important;
  border-radius: 4px;
  color: #ffffff !important;
  font-weight: 700;
}

.article-items-container {
  border-radius: 4px;
  min-height: 80vh;
  margin-right: 15px;
  border-radius: 4px;
  background-color: #ffffff;
}

.rank-item:hover {
  background-color: #f7f7ff;
}

.sidebar-container {
  border-radius: 4px;
  background-color: #00000000;
}

.user-info {
  display: flex;
  align-items: center;
  color: #666;
}

.user-name:hover,
.notice-item:hover,
.user-content:hover {
  color: #4d45e5;
}

.notice-card,
.rank-card,
.recommend-category-card {
  background-color: #ffffff;
  border-radius: 4px;
}

.rank-card,
.recommend-category-card {
  margin-top: 15px;
}

.sticky-card {
  position: sticky;
  top: 15px;
}

.notice-title,
.rank-title,
.recommend-title {
  font-weight: 700;
  font-size: 16px;
  padding: 0 15px;
  border-bottom: 1px solid #f4f4f4;
  height: 54px;
  display: flex;
  align-items: center;
}

.notice-content,
.recommend-content {
  padding: 15px;
}

.recommend-content {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.recommend-content a {
  background-color: #f1f1ff;
  color: #666;
  border-radius: 4px;
  font-size: 13px;
  margin: 7px 0;
  padding: 3px 15px;
  text-align: center;
  width: 45%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.recommend-content a:hover {
  background-color: #6b64ee;
  color: #fff;
}

.notice-item,
.rank-item {
  display: flex;
  justify-content: space-between;
  color: #666;
  align-items: center;
}

.rank-item {
  font-size: 14px;
  padding: 10px 15px;
}

.user-content {
  display: flex;
  flex-direction: column;
  margin: 0 10px;
}

.user-desc {
  font-size: 12px;
}

.entry-item {
  flex: 1;
}

@media screen and (max-width: 1200px) {
  .nav-box,
  .content-box {
    padding: 0 10px;
  }
}

@media screen and (max-width: 800px) {
  .content-box {
    padding: 0;
  }

  .sidebar-container {
    display: none;
  }

  .article-items-container {
    margin: 0;
  }

  .content-box {
    margin-top: 10px;
  }
}
</style>
