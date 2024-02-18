<template>
  <a-select
    v-model:value="articleGroupId"
    placeholder="请选择合集"
    style="width: 300px"
    show-search
    :options="articleGroupOptions"
    :filter-option="filterOption"
    @focus="articleGroupFocus"
  >
    <template #dropdownRender="{ menuNode: menu }">
      <v-nodes :vnodes="menu" />
      <a-divider style="margin: 4px 0" />
      <a-space style="padding: 4px 8px">
        <a-input ref="inputRef" v-model:value="articleGroupInput" placeholder="请输入合集名称" />
        <a-button type="text" @click="addGroupItem">
          <template #icon>
            <plus-outlined />
          </template>
          新建合集
        </a-button>
      </a-space>
    </template>
  </a-select>
</template>

<script setup>
import { message } from 'ant-design-vue'
import {
  add,
  getArticleGroupListByUserId,
  getArticleGroupByUserIdAndGroupName
} from '@/api/articleGroup'
import { watch } from 'vue'

const itemId = defineModel('itemId')
const userId = defineModel('userId')
const articleGroupId = defineModel('articleGroupId')
const articleGroupName = defineModel('articleGroupName')

const inputRef = ref()
const articleGroupInput = ref('')
const articleGroupOptions = ref([])

onMounted(() => {
  articleGroupOptions.value.push({
    value: articleGroupId.value,
    label: articleGroupName.value
  })
})

watch(itemId, () => {
  articleGroupOptions.value.push({
    value: articleGroupId.value,
    label: articleGroupName.value
  })

  articleGroupOptions.value = [
    ...new Map(articleGroupOptions.value.map((item) => [item.id, item])).values()
  ]
})

const addGroupItem = (e) => {
  e.preventDefault()
  if (!userId.value) {
    return message.warning('请先选择作者')
  }
  const inputValue = articleGroupInput.value
  const formData = new FormData()
  formData.append('userId', userId.value)
  formData.append('name', inputValue)
  add(formData)
    .then(() => {
      message.success('添加成功')
    })
    .then(() => {
      const params = { userId: userId.value, groupName: inputValue }
      getArticleGroupByUserIdAndGroupName(params)
        .then((res) => {
          const data = res.data
          articleGroupOptions.value.push({
            value: data.id,
            label: data.name
          })

          articleGroupId.value = data.id
          articleGroupOptions.value = [
            ...new Map(articleGroupOptions.value.map((item) => [item.id, item])).values()
          ]
        })
        .then(() => {
          articleGroupInput.value = ''
          setTimeout(() => {
            inputRef.value?.focus()
          }, 0)
        })
    })
}

const articleGroupFocus = () => {
  if (!userId.value) {
    return message.warning('请先选择作者')
  }
  getArticleGroupListByUserId({ userId: userId.value }).then((res) => {
    const data = res.data
    articleGroupOptions.value = data.map((group) => ({
      value: group.id,
      label: group.name
    }))
  })
}

const filterOption = (input, option) => option.label.indexOf(input) >= 0

const VNodes = defineComponent({
  props: {
    vnodes: {
      type: Object,
      required: true
    }
  },
  render() {
    return this.vnodes
  }
})
</script>
