<template>
  <a-select
    v-model:value="userId"
    show-search
    placeholder="选择作者"
    style="width: 300px"
    :options="userOptions"
    :filter-option="filterOption"
    @search="authorSearch"
  ></a-select>
</template>

<script setup>
import { getUserListByName } from '@/api/user'

const userId = defineModel()

const userOptions = ref([])

const filterOption = (input, option) => option.label.indexOf(input) >= 0

const authorSearch = (value) => {
  if (value === null || value === '') {
    return
  }
  getUserListByName({ userName: value }).then((res) => {
    const data = res.data
    userOptions.value = data.map((user) => ({ value: user.id, label: user.name }))
  })
}
</script>
