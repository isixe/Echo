<template>
  <div class="user-container">
    <div class="user-info">
      <div class="user-container-left">
        <a-avatar class="user-avatar" :size="50" :src="item.avatar">
          <template #icon>
            <UserOutlined />
          </template>
        </a-avatar>
        <div class="user-content">
          <span class="user-entry-name">{{ item.name }}</span>
          <span class="user-desc">{{ item.description }}</span>
          <div class="user-message">
            <span>{{ item.articleCount }} 发文数</span>
            <span>{{ item.questionCount }} 问答数</span>
            <span>{{ item.followCount }} 关注者</span>
          </div>
        </div>
      </div>

      <div class="user-container-right">
        <template v-if="item && store.id != item.id">
          <a-button
            v-if="!followId"
            class="user-follow-btn"
            type="primary"
            style="background-color: #4d45e5; border-radius: 3px"
            @click.prevent="setUserSubscribe()"
            >关注</a-button
          >
          <a-button
            v-else-if="followId"
            class="user-follow-btn"
            type="primary"
            style="background-color: #ccc; border-radius: 3px"
            @click.prevent="removeUserSubscribe()"
            >取消关注</a-button
          >
        </template>
        <template v-else>
          <a-button
            class="user-follow-btn"
            type="primary"
            style="background-color: #ccc; border-radius: 3px"
            disabled
            >关注</a-button
          >
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { add as setSubscribe, remove as unSubscribe, getByUserIdAndFollowId } from '@/api/follow'

const store = useUserStore()
const props = defineProps(['item'])
const { item } = toRefs(props)
console.log(item.value)
const followId = ref()

onMounted(() => {
  checkFollow()
})

const checkFollow = () => {
  const params = {
    userId: store.id,
    followUserId: item.value.id
  }
  getByUserIdAndFollowId(params).then((res) => {
    followId.value = res.data.id
  })
}

const setUserSubscribe = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('followUserId', item.value.id)
  setSubscribe(formData).then(() => checkFollow())
}

const removeUserSubscribe = () => {
  const formData = new FormData()
  formData.append('id', followId.value)
  unSubscribe(formData).then(() => {
    followId.value = null
    console.log(followId.value)
  })
}
</script>

<style scope>
.user-container {
  padding: 10px 20px;
  width: 100%;
  border-bottom: 1px solid #ebeef5;
}

.user-container:hover {
  background-color: #f7f7ff;
}

.user-container-left {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  color: #666;
  justify-content: space-between;
}

.user-entry-name {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 5px;
}

.user-desc {
  margin-bottom: 5px;
}

.user-entry-name:hover,
.user-content:hover {
  color: #4d45e5;
}

.user-content {
  display: flex;
  flex-direction: column;
  margin: 0 10px;
}

.user-message span {
  margin-right: 15px;
}
</style>
