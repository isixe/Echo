<template>
  <a-select
    v-model:value="articleId"
    show-search
    placeholder="选择关联文章"
    style="width: 300px"
    :options="articleOptions"
    :filter-option="filterOption"
    @search="authorSearch"
  ></a-select>
</template>

<script setup>
import { getArticleListByName } from '@/api/article'

const articleId = defineModel()

const articleOptions = ref([])

const filterOption = (input, option) => option.label.indexOf(input) >= 0

const authorSearch = (value) => {
  if (value === null || value === '') {
    return
  }
  getArticleListByName({ title: value }).then((res) => {
    const data = res.data
    articleOptions.value = data.map((article) => ({ value: article.id, label: article.title }))
  })
}
</script>
