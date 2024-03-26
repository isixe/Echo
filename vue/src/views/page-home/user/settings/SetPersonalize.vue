<template>
  <div class="personalize-container">
    <p class="personalize-title">个性化数据源设置</p>
    <a-form class="profile-form" ref="form" :model="state">
      <a-form-item name="useCommented" label="使用评论过的文章和问答">
        <a-switch v-model:checked="state.useCommented" @click="updatePersonalize()">
          <template #checkedChildren><check-outlined /></template>
          <template #unCheckedChildren><close-outlined /></template>
        </a-switch>
      </a-form-item>
      <a-form-item name="useCollection" label="使用收藏的文章和问答">
        <a-switch v-model:checked="state.useCollection" @click="updatePersonalize()">
          <template #checkedChildren><check-outlined /></template>
          <template #unCheckedChildren><close-outlined /></template>
        </a-switch>
      </a-form-item>
      <a-form-item name="useThumb" label="使用点赞过的文章和问答">
        <a-switch v-model:checked="state.useThumb" @click="updatePersonalize()">
          <template #checkedChildren><check-outlined /></template>
          <template #unCheckedChildren><close-outlined /></template>
        </a-switch>
      </a-form-item>
      <a-form-item name="useHistory" label="使用阅读历史的文章和问答">
        <a-switch v-model:checked="state.useHistory" @click="updatePersonalize()">
          <template #checkedChildren><check-outlined /></template>
          <template #unCheckedChildren><close-outlined /></template>
        </a-switch>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup>
import { message } from 'ant-design-vue'
import { add, update, getByUserId } from '@/api/user-personalize'
import { useUserStore } from '@/stores/user'

const store = useUserStore()

const state = reactive({
  useCommented: true,
  useCollection: true,
  useThumb: true,
  useHistory: true
})

const stateId = ref('')

onMounted(() => queryPersonalize())

const queryPersonalize = () => {
  getByUserId({ userId: store.id }).then((res) => {
    if (res.data.id) {
      stateId.value = res.data.id
      Object.keys(state).forEach((key) => {
        state[key] = res.data[key]
      })
    }
  })
}

const updatePersonalize = () => {
  if (!state.useCommented && !state.useCollection && !state.useThumb && !state.useHistory) {
    message.warning('请至少保留一项')
    queryPersonalize()
    return
  }

  const formData = new FormData()
  formData.append('userId', store.id)
  Object.keys(state).forEach((key) => {
    formData.append(key, state[key])
  })

  if (!stateId.value) {
    add(formData).then(() => {
      message.success('保存成功')
      queryPersonalize()
    })
    return
  }

  formData.append('id', stateId.value)
  update(formData).then(() => {
    message.success('保存成功')
  })
}
</script>

<style scoped>
.personalize-container {
  background-color: #ffffff;
  padding: 20px;
  min-height: 90vh;
  flex-wrap: wrap;
}

.personalize-title {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 15px;
}
</style>
