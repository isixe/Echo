<template>
  <a-select
    v-model:value="questionId"
    show-search
    placeholder="选择关联问答"
    style="width: 300px"
    :options="questionOptions"
    @focus="onQuestionFocus"
    :filter-option="() => true"
    @popupScroll="onPopupScroll"
    @search="onQuestionSearch"
  ></a-select>
</template>

<script setup>
import { getQuestionListByTitle } from '@/api/question'

const questionId = defineModel()
const questionOptions = ref([])

const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  keyword: ''
})

const onQuestionSearch = (value) => {
  params.keyword = value
  params.pageNum = 1
  getQuestionListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    questionOptions.value = data.map((question) => ({ value: question.id, label: question.title }))
  })
}

const onPopupScroll = (e) => {
  const { scrollTop, offsetHeight, scrollHeight } = e.target
  if (scrollTop + offsetHeight !== scrollHeight || params.pageNum >= pages.value) {
    return
  }

  params.pageNum += 1
  getQuestionListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    questionOptions.value.push(
      ...data.map((question) => ({
        value: question.id,
        label: question.title
      }))
    )
  })
}

const onQuestionFocus = () => {
  params.pageNum = 1
  getQuestionListByTitle(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    questionOptions.value = data.map((question) => ({
      value: question.id,
      label: question.title
    }))

    questionId.value = questionId.value ? parseInt(questionId.value) : null
  })
}
</script>
