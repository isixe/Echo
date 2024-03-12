<template>
  <div class="draft-header">我的草稿箱</div>
  <div class="draft-container">
    <a-menu class="nav-menu" v-model:selectedKeys="selectedKey" mode="horizontal" :items="items" />
    <template v-if="selectedKey == 'article' && articleFullList && articleFullList.length > 0">
      <div class="article-entry-list">
        <div class="article-entry" v-for="item in articleFullList" :key="item.id">
          <div class="entry-item">
            <RouterLink :to="{ path: 'article/edit', query: { id: item.id } }">
              <div>
                <p class="item-title">{{ item.title }}</p>
              </div>
              <div class="item-content">
                <div class="item-content-left">
                  <p class="item-summary">
                    {{ item.summary }}
                  </p>
                </div>
                <div class="item-content-right">
                  <img class="entry-item-picture" v-if="item.featuredPic" :src="item.featuredPic" />
                </div>
              </div>
            </RouterLink>
          </div>
          <div class="entry-option" @click="onArticleDraftDelete(item.id)"><a>删除</a></div>
        </div>
      </div>
    </template>
    <template v-if="selectedKey == 'question' && questionFullList && questionFullList.length > 0">
      <div class="question-entry-list">
        <div class="question-entry" v-for="item in questionFullList" :key="item.id">
          <div class="entry-item">
            <RouterLink :to="{ path: 'question/edit', query: { id: item.id } }">
              <div>
                <p class="item-title">{{ item.title }}</p>
              </div>
            </RouterLink>
          </div>
          <div class="entry-option" @click="onQuestionDraftDelete(item.id)"><a>删除</a></div>
        </div>
      </div>
    </template>
    <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { remove as removeArticle, getArticleDraftList } from '@/api/article'
import { remove as removeQuestion, getQuestionDraftList } from '@/api/question'

import { message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

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

watch(selectedKey, () => {
  articleFullList.value = []
  questionFullList.value = []
  params.pageNum = 1
  getDataSource(selectedKey.value)
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
  getArticleDraftList(params).then((res) => {
    articleFullList.value = articleFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
  })
}

const getQuestionDataSource = () => {
  getQuestionDraftList(params).then((res) => {
    questionFullList.value = questionFullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
  })
}

const onArticleDraftDelete = (id) => {
  Modal.confirm({
    title: `确定要删除文章草稿吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除文章草稿后，文章草稿无法恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', id)
      removeArticle(formData).then(() => {
        message.success('删除成功')
        getArticleDataSource()
      })
    }
  })
}

const onQuestionDraftDelete = (id) => {
  Modal.confirm({
    title: `确定要删除问答草稿吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '移除问答草稿后，问答草稿无法恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', id)
      removeQuestion(formData).then(() => {
        message.success('删除成功')
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
.draft-header {
  margin-top: 15px;
  text-align: center;
  font-size: 20px;
  font-weight: 700;
}

.draft-container {
  margin: 15px 40px;
  border-radius: 4px;
}

.article-entry,
.question-entry {
  margin-top: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
  display: flex;
}

.entry-item {
  padding: 15px;
  flex: 1;
}

.entry-item a {
  text-decoration: none;
}

.entry-item:hover {
  background-color: #f7f7ff;
}

.entry-option {
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.entry-option a:hover {
  color: #4d45e5;
  cursor: pointer;
}

.user-name:hover,
.item-title:hover,
.category-container:hover {
  color: #4d45e5;
}

.user-info {
  display: flex;
  align-items: center;
  color: #666;
}

.item-title {
  width: 80%;
  padding-bottom: 10px;
  font-weight: 700;
  font-size: 20px;
  color: #000000;
  flex: none;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.item-content {
  display: flex;
  justify-content: space-between;
  color: #666;
}

.item-content {
  display: flex;
  justify-content: space-between;
  color: #666;
}

.item-content-right {
  display: flex;
  align-items: center;
}

.item-summary {
  flex: none;
  line-height: 28px;
  height: 56px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.entry-item-picture {
  border-radius: 4px;
  height: 105px;
  width: 140px;
  margin-top: -45px;
}

.article-entry-list,
.question-entry-list {
  display: grid;
  grid-column-gap: 15px;
  grid-template-columns: 1fr 1fr;
}

@media screen and (max-width: 1200px) {
  .item-title {
    width: 70%;
  }
}

@media screen and (max-width: 800px) {
  .item-title {
    font-size: 18px;
    width: 85%;
  }

  .item-content {
    padding: 0 10px 10px;
    display: flex;
    align-items: center;
  }

  .entry-item-picture {
    width: 85px;
    height: 55px;
    margin-top: 12px;
  }
}

@media screen and (max-width: 700px) {
  .item-title {
    padding: 5px 10px;
    width: 80%;
  }

  .item-content {
    padding-bottom: 40px;
  }

  .item-message-right {
    margin-top: 5px;
    position: absolute;
    top: 128px;
  }

  .entry-item-picture {
    width: 85px;
    height: 55px;
    margin-top: 12px;
  }

  .entry-item-picture {
    margin: 0;
    width: 62px;
    height: 44px;
  }
}
</style>
