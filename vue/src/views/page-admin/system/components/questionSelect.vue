<template>
  <a-select
    v-model:value="questionId"
    show-search
    placeholder="选择关联问答"
    style="width: 300px"
    :options="questionOptions"
    :filter-option="filterOption"
    @search="authorSearch"
  ></a-select>
</template>

<script setup>
import { getQuestionListByTitle } from '@/api/question'

const questionId = defineModel()

const questionOptions = ref([])

const filterOption = (input, option) => option.label.indexOf(input) >= 0

const authorSearch = (value) => {
  if (value === null || value === '') {
    return
  }
  getQuestionListByTitle({ title: value }).then((res) => {
    const data = res.data
    questionOptions.value = data.map((question) => ({ value: question.id, label: question.title }))
  })
}
</script>
