<template>
  <a-select
    v-model:value="articleGroupId"
    placeholder="请选择合集"
    style="width: 300px"
    show-search
    :options="articleGroupOptions"
    @focus="onArticleGroupFocus"
    @popupScroll="onPopupScroll"
    :filter-option="() => true"
    @search="onGroupSearch"
  >
    <template #dropdownRender="{ menuNode: menu }">
      <v-nodes :vnodes="menu" />
      <a-divider style="margin: 4px 0" />
      <a-space style="padding: 4px 8px">
        <a-input ref="inputRef" v-model:value="articleGroupInput" placeholder="请输入合集名称" />
        <a-button type="text" @click.prevent="addGroupItem">
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
} from '@/api/article-group'

const userId = defineModel('userId')
const articleGroupId = defineModel('articleGroupId')
const articleGroupName = defineModel('articleGroupName')

const inputRef = ref()
const articleGroupInput = ref('')
const articleGroupOptions = ref([])
const pages = ref(0)

const params = reactive({
  pageNum: 1,
  pageSize: 15,
  userId: userId,
  keyword: ''
})

onMounted(() => {
  if (!articleGroupId.value || !articleGroupName.value) {
    return
  }

  articleGroupOptions.value.push({
    value: articleGroupId.value,
    label: articleGroupName.value
  })
})

watch(articleGroupName, () => {
  articleGroupOptions.value.push({
    value: articleGroupId.value,
    label: articleGroupName.value
  })
  articleGroupOptions.value = [
    ...new Map(articleGroupOptions.value.map((item) => [item.id, item])).values()
  ]
})

const queryGroupData = () => {
  getArticleGroupListByUserId(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleGroupOptions.value.push(
      ...data.map((group) => ({
        value: group.id,
        label: group.name
      }))
    )
  })
}

const onArticleGroupFocus = () => {
  if (!userId.value) {
    return message.warning('请先选择作者')
  }

  params.pageNum = 1
  getArticleGroupListByUserId(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleGroupOptions.value = data.map((group) => ({
      value: group.id,
      label: group.name
    }))

    articleGroupId.value = articleGroupId.value ? parseInt(articleGroupId.value) : ''
  })
}

const onPopupScroll = (e) => {
  const { scrollTop, offsetHeight, scrollHeight } = e.target
  if (scrollTop + offsetHeight !== scrollHeight || params.pageNum >= pages.value) {
    return
  }
  params.pageNum += 1
  queryGroupData()
}

const onGroupSearch = (value) => {
  params.keyword = value
  params.pageNum = 1
  getArticleGroupListByUserId(params).then((res) => {
    const data = res.data.records
    pages.value = res.data.pages
    articleGroupOptions.value = data.map((group) => ({
      value: group.id,
      label: group.name
    }))
  })
}

const addGroupItem = () => {
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
