<template>
  <template v-for="tag in stateTags.tags" :key="tag">
    <a-tag :closable="true" @close="close(tag)">
      {{ tag }}
    </a-tag>
  </template>
  <a-input
    v-if="stateTags.inputVisible"
    ref="tagInputRef"
    v-model:value="stateTags.inputValue"
    type="text"
    size="small"
    :style="{ width: '78px' }"
    @blur="inputConfirm"
    @keyup.enter="inputConfirm"
  />
  <a-tag v-else style="background: #fff; border-style: dashed" @click="showInput">
    <plus-outlined />
    新标签
  </a-tag>
</template>

<script setup>
const tags = defineModel()

const tagInputRef = ref()
const stateTags = reactive({
  tags: [],
  inputVisible: false,
  inputValue: ''
})

const dataSync = (tags) => (stateTags.tags = tags ? tags.split(',') : (stateTags.tags = []))

onMounted(() => dataSync(tags.value))
watch(tags, () => dataSync(tags.value))
watch(stateTags, () => (tags.value = stateTags.tags.join()))

const inputConfirm = () => {
  const inputValue = stateTags.inputValue
  let tags = stateTags.tags
  if (inputValue && tags.indexOf(inputValue) === -1) {
    tags = [...tags, inputValue]
  }
  Object.assign(stateTags, {
    tags,
    inputVisible: false,
    inputValue: ''
  })
}

const close = (removedTag) => {
  const tags = stateTags.tags.filter((tag) => tag !== removedTag)
  stateTags.tags = tags
}

const showInput = () => {
  stateTags.inputVisible = true
  nextTick(() => {
    tagInputRef.value.focus()
  })
}
</script>
