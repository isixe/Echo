<template>
  <a-select
    v-model:value="categoryId"
    show-search
    placeholder="请选择类别"
    style="width: 300px"
    :options="categoryOptions"
    :filter-option="filterOption"
    @search="categorySearch"
  >
    <template #dropdownRender="{ menuNode: menu }">
      <v-nodes :vnodes="menu" />
      <a-divider style="margin: 4px 0" />
      <a-space style="padding: 4px 8px">
        <a-input ref="inputRef" v-model:value="categoryInput" placeholder="请输入类别" />
        <a-button type="text" @click="addCategoryItem">
          <template #icon>
            <plus-outlined />
          </template>
          新建类别
        </a-button>
      </a-space>
    </template>
  </a-select>
</template>

<script setup>
import { message } from 'ant-design-vue'
import { add, getCategoryByName, getCategoryListByName } from '@/api/category'

const categoryId = defineModel('categoryId')
const categoryName = defineModel('categoryName')

const categoryInput = ref('')
const categoryOptions = ref([])

onMounted(() => {
  if (!categoryName.value) {
    return
  }

  categoryOptions.value.push({
    value: categoryId.value,
    label: categoryName.value
  })
  categoryOptions.value = [
    ...new Map(categoryOptions.value.map((item) => [item.id, item])).values()
  ]
})

const filterOption = (input, option) => option.label.indexOf(input) >= 0

const categorySearch = (value) => {
  if (value === null || value === '') {
    return
  }
  getCategoryListByName({ categoryName: value }).then((res) => {
    const data = res.data
    categoryOptions.value = data.map((category) => ({
      value: category.id,
      label: category.categoryName
    }))
  })
}

const inputRef = ref()
const addCategoryItem = async (e) => {
  e.preventDefault()

  const inputValue = categoryInput.value

  const formData = new FormData()
  formData.append('categoryName', inputValue)
  await add(formData).then(() => {
    message.success('添加成功')
  })

  getCategoryByName({ categoryName: inputValue })
    .then((res) => {
      const data = res.data
      categoryOptions.value.push({
        value: data.id,
        label: data.categoryName
      })

      categoryId.value = data.id
      categoryOptions.value = [
        ...new Map(categoryOptions.value.map((item) => [item.id, item])).values()
      ]
    })
    .then(() => {
      categoryInput.value = ''
      setTimeout(() => {
        inputRef.value?.focus()
      }, 0)
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
