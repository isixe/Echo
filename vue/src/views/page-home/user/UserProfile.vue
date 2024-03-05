<template>
  <div class="user-container">
    <div class="user-info-box" v-if="user">
      <div class="avatar-content">
        <img class="user-avatar" v-if="user.avatar" :src="user.avatar" alt="avatar" />
      </div>
      <div class="info-content">
        <div class="user-name">{{ user.name }}</div>

        <div class="user-detail">
          <span>加入时间：{{ user.createdTime }}</span>
          <span>最后活跃时间：{{ user.lastActiveTime }}</span>
        </div>
        <div class="user-desc" v-if="user.description">{{ user.description }}</div>
        <div class="user-desc" v-else>这个人很懒，什么都没有写...</div>
      </div>
      <div class="footer-content">
        <a-button
          v-if="!store.id == user.id"
          class="user-subscribe-btn"
          type="primary"
          style="background-color: #4d45e5"
          >关注</a-button
        >
      </div>
    </div>
    <div class="user-dynamic-box">
      <a-layout-content class="dynamic-entry-content">
        <a-tabs class="nav-menu" v-model:activeKey="activeKey">
          <a-tab-pane key="1" :tab="'文章 ' + data.articleTotal">
            <template v-for="item in data.article" :key="item.id">
              <article-entry-item :item="item"></article-entry-item>
            </template>
          </a-tab-pane>
          <a-tab-pane key="2" :tab="'问答 ' + data.questionTotal">
            <template v-for="item in data.question" :key="item.id">
              <question-entry-item :item="item"></question-entry-item>
            </template>
          </a-tab-pane>
          <a-tab-pane key="4" tab="关注">
            <div class="subscribe-container"></div>
          </a-tab-pane>
        </a-tabs>
      </a-layout-content>
      <a-layout-sider class="dynamic-side-content" width="320px">
        <div class="subscribe-card">
          <div class="subscirbe-content">
            <div>关注了</div>
            <div class="subscirbe-count">22</div>
          </div>
          <div class="subscirbe-content">
            <div>关注者</div>
            <span class="subscirbe-count">26</span>
          </div>
        </div>
      </a-layout-sider>
    </div>
  </div>
</template>

<script setup>
import { get } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { QuestionEntryItem } from '@/views/page-home/components'
import { ArticleEntryItem } from '@/views/page-home/components'
import { getArticleListByUserId } from '@/api/article'
import { getQuestionListByUserId } from '@/api/question'

const user = ref()
const route = useRoute()
const store = useUserStore()
const data = reactive({
  article: [],
  question: [],
  articleTotal: 0,
  questionTotal: 0
})
const activeKey = ref('1')
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  userId: route.params.id
})

onMounted(() => {
  get({ id: route.params.id }).then((result) => {
    user.value = result.data
  })

  getArticleListByUserId(params).then((res) => {
    data.article = res.data.records
    data.articleTotal = res.data.total
  })

  getQuestionListByUserId(params).then((res) => {
    data.question = res.data.records
    data.questionTotal = res.data.total
  })
})
</script>

<style scoped>
.user-container {
  max-width: 1200px;
  margin: auto;
  min-height: 100%;
}

.user-info-box {
  margin: 15px 0;
  background-color: #ffffff;
  border-radius: 4px;
  padding: 20px;
  display: flex;
  flex-direction: row;
  box-sizing: border-box;
}

.user-dynamic-box {
  margin: 15px 0;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
}

.avatar-content {
  margin-right: 15px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 20px;
}

.user-detail {
  margin-bottom: 20px;
}

.user-detail span {
  margin-right: 15px;
}

.dynamic-entry-content {
  background-color: #ffffff;
  margin-right: 20px;
}

.dynamic-side-content {
  border-radius: 4px;
  background-color: #00000000;
}

.nav-menu {
  font-size: 14px;
  padding: 0 15px;
}

.footer-content {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  margin: 3px;
}

.user-subscribe-btn:hover {
  background-color: #6d66f1 !important;
}

.subscribe-card {
  background-color: #ffffff;
  border-radius: 4px;
  display: grid;
  grid-template-columns: 50% 50%;
}

.subscirbe-content {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 10px 15px;
}

.subscirbe-content:first-child {
  border-right: 1px solid #dadada;
}

.subscirbe-count {
  font-weight: 700;
  font-size: 16px;
}
</style>
