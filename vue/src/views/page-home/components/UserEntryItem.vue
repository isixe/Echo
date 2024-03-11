<template>
  <div class="user-container">
    <div class="user-info">
      <a-avatar class="user-avatar" :size="45" :src="item.avatar">
        <template #icon>
          <UserOutlined />
        </template>
      </a-avatar>
      <div class="user-content">
        <span class="user-name">{{ item.name }}</span>
        <span class="user-desc">{{ item.description }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps(['item'])
const { item } = toRefs(props)
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

.user-info {
  display: flex;
  align-items: center;
  color: #666;
}

.user-name:hover,
.user-content:hover {
  color: #4d45e5;
}

.user-content {
  display: flex;
  flex-direction: column;
  margin: 0 10px;
}
</style>
