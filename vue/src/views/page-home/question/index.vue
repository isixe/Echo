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
          <template v-for="record in dataSource" :key="record.id">
            <question-entry-item :item="record"></question-entry-item>
          </template>
        </a-layout-content>
        <a-layout-sider class="sidebar-container" width="320px">
          <div class="notice-card">
            <div class="notice-title">
              <span>公告🔉</span>
            </div>
            <div class="notice-content">
              <RouterLink class="notice-item" to="/question/3246543">
                <span class="article-title"> 功能维护中... </span>
                <span class="article-time"> 2024-02-01 </span>
              </RouterLink>
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
                <RouterLink
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
                </RouterLink>
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
                  <RouterLink :to="'/category/' + category.id">
                    {{ category.categoryName }}
                  </RouterLink>
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
import { QuestionEntryItem } from '@/views/page-home/components'
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  getActiveQuestionListByKeyword,
  getHotActiveQuestionListByKeyword,
  getUserRank
} from '@/api/question'
import { getCategoryListByKeyword } from '@/api/category'
import { faTags, faBullhorn, faRankingStar } from '@fortawesome/free-solid-svg-icons'
library.add(faTags, faBullhorn, faRankingStar)

const route = useRoute()
const router = useRouter()
const dataSource = ref([])
const selectedKey = ref(['latest'])
const recommendCategory = ref([])

const params = reactive({
  pageNum: 1,
  pageSize: 15
})

const rankList = ref([])

const getDataSource = (type) => {
  switch (type) {
    case 'latest':
      getActiveQuestionListByKeyword(params).then((res) => {
        const data = res.data
        dataSource.value = data.records
        // pagination.total = data.total
        // loading.value = false
      })
      break
    case 'recommend':
      break
    case 'hot':
      getHotActiveQuestionListByKeyword(params).then((res) => {
        const data = res.data
        dataSource.value = data.records
        // pagination.total = data.total
        // loading.value = false
      })
      break
    case 'subscribe':
      break
    default:
      router.push('/question')
      selectedKey.value = ['latest']
  }
}

onMounted(() => {
  getUserRank().then((res) => (rankList.value = res.data))

  let type = route.query.type ? route.query.type : 'latest'

  selectedKey.value = [type]
  getDataSource(type)

  getCategoryListByKeyword({ pageSize: 6 }).then((res) => {
    recommendCategory.value = res.data.records
  })
})

watch(selectedKey, (key) => {
  switch (key[0]) {
    case 'latest':
      router.push('/question')
      getDataSource(key[0])
      break
    case 'recommend':
      router.push({ path: '/question', query: { type: 'recommend' } })
      getDataSource(key[0])
      break
    case 'hot':
      router.push({ path: '/question', query: { type: 'hot' } })
      getDataSource(key[0])
      break
    case 'subscribe':
      router.push({ path: '/question', query: { type: 'subscribe' } })
      getDataSource(key[0])
      break
  }
})

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
    key: 'subscribe',
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
