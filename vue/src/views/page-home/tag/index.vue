<template>
  <div class="category-header">标签 ：{{ tagName }}</div>
  <div class="container">
    <a-menu class="nav-menu" v-model:selectedKeys="selectedKey" mode="horizontal" :items="items" />
    <div v-for="item in data" :key="item.id">
      <article-entry-item :item="item"></article-entry-item>
    </div>
  </div>
</template>

<script setup>
import { ArticleEntryItem } from '@/views/page-home/components'
import { getArticleListByTagName } from '@/api/article'
const route = useRoute()
const data = ref()
const tagName = ref(route.query.tagName)
const selectedKey = ref(['article'])
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  tagName: route.query.tagName
})

onMounted(() => {
  getArticleDataSource()
})

const getArticleDataSource = () => {
  getArticleListByTagName(params).then((res) => {
    data.value = res.data.records
    console.log(res.data.records)
  })
}

const getQuestionDataSource = () => {}

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
.category-header {
  margin-top: 15px;
  text-align: center;
  font-size: 20px;
  font-weight: 700;
}

.container {
  margin: 15px 40px;
  background-color: #ffffff;
  border-radius: 4px;
}
</style>
