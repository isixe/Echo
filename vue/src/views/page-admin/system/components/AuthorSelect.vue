<template>
  <a-select
    v-model:value="userId"
    show-search
    placeholder="选择作者"
    style="width: 300px"
    :options="userOptions"
    @focus="onUserFocus"
    :filter-option="() => true"
    @popupScroll="onPopupScroll"
    @search="onAuthorSearch"
  ></a-select>
</template>

<script setup>
import { getUserListByName } from '@/api/user'

const userId = defineModel()
const userOptions = ref([])

const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  keyword: ''
})

const onAuthorSearch = (value) => {
  params.keyword = value
  params.pageNum = 1
  getUserListByName(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    userOptions.value = data.map((user) => ({ value: user.id, label: user.name }))
  })
}

const onPopupScroll = (e) => {
  const { scrollTop, offsetHeight, scrollHeight } = e.target
  if (scrollTop + offsetHeight !== scrollHeight || params.pageNum >= pages.value) {
    return
  }

  params.pageNum += 1
  getUserListByName(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    userOptions.value.push(
      ...data.map((user) => ({
        value: user.id,
        label: user.name
      }))
    )
  })
}

const onUserFocus = () => {
  params.pageNum = 1
  getUserListByName(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    userOptions.value = data.map((user) => ({
      value: user.id,
      label: user.name
    }))

    userId.value = userId.value ? parseInt(userId.value) : ''
  })
}
</script>
