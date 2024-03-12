<template>
  <a-select
    v-model:value="articleId"
    show-search
    placeholder="选择关联文章"
    style="width: 300px"
    :options="articleOptions"
    @focus="onArticleFocus"
    :filter-option="() => true"
    @popupScroll="onPopupScroll"
    @search="onArticleSearch"
  ></a-select>
</template>

<script setup>
import { getArticleListByTitle } from '@/api/article'

const articleId = defineModel()
const articleOptions = ref([])

const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

const onArticleSearch = (value) => {
  params.keyword = value
  params.pageNum = 1
  getArticleListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleOptions.value = data.map((article) => ({ value: article.id, label: article.title }))
  })
}

const onPopupScroll = (e) => {
  const { scrollTop, offsetHeight, scrollHeight } = e.target
  if (scrollTop + offsetHeight !== scrollHeight || params.pageNum >= pages.value) {
    return
  }

  params.pageNum += 1
  getArticleListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleOptions.value.push(
      ...data.map((article) => ({
        value: article.id,
        label: article.title
      }))
    )
  })
}

const onArticleFocus = () => {
  params.pageNum = 1
  getArticleListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleOptions.value = data.map((article) => ({
      value: article.id,
      label: article.title
    }))

    articleId.value = articleId.value ? parseInt(articleId.value) : ''
  })
}
</script>
