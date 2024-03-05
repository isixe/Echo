<template>
  <div class="draft-header">我的草稿箱</div>
  <div class="container">
    <a-menu class="nav-menu" v-model:selectedKeys="selectedKey" mode="horizontal" :items="items" />
    <template v-if="selectedKey == 'article'">
      <div class="article-entry-list">
        <div v-for="item in articleData" :key="item.id">
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
        </div>
      </div>
    </template>
    <template v-if="selectedKey == 'question'">
      <div class="question-entry-list">
        <div v-for="item in questionData" :key="item.id">
          <div class="entry-item">
            <RouterLink :to="{ path: 'question/edit', query: { id: item.id } }">
              <div>
                <p class="item-title">{{ item.title }}</p>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { getArticleDraftList } from '@/api/article'
import { getQuestionDraftList } from '@/api/question'

const store = useUserStore()

const articleData = ref()
const questionData = ref()
const selectedKey = ref(['article'])

onMounted(() => {
  getArticleDataSource()
})

const getArticleDataSource = () => {
  getArticleDraftList({ userId: store.id }).then((res) => {
    articleData.value = res.data
  })
}

const getQuestionDataSource = () => {
  getQuestionDraftList({ userId: store.id }).then((res) => {
    questionData.value = res.data
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

.container {
  margin: 15px 40px;
  border-radius: 4px;
}

.entry-item {
  margin-top: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 15px;
}

.entry-item a {
  text-decoration: none;
}

.entry-item:hover {
  background-color: #f7f7ff;
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
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  color: #666;
}

.item-content {
  border-bottom: 1px solid #ebeef5;
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
