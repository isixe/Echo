<template>
  <div class="entry-item">
    <div class="item-left">
      <div class="item-left-message">
        <p>{{ item.commentCount }}</p>
        <p>回复</p>
      </div>
      <div class="item-left-message">
        <p>{{ item.pvCount }}</p>
        <p>阅读</p>
      </div>
    </div>
    <div class="item-main">
      <div>
        <p class="item-title">{{ item.title }}</p>
      </div>
      <div class="item-message">
        <div class="item-message-left">
          <router-link
            :to="{ path: '/category/' + item.categoryId, query: { tab: 'question' } }"
            class="category-container"
          >
            {{ item.category }}
          </router-link>
        </div>
        <div class="item-message-right">
          <router-link :to="'/user/' + item.userId" class="user-info">
            <a-avatar
              class="user-avatar"
              :src="item.avatar"
              :size="20"
              :style="{ marginRight: '10px' }"
            >
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
            <span class="user-name">{{ item.author }}</span>
          </router-link>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
          <span>{{ updateTime }}提问</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'
dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const props = defineProps(['item'])
const { item } = toRefs(props)

const updateTime = dayjs(item.value.updateTime).fromNow()
</script>

<style scoped>
.entry-item {
  width: 100%;
  position: relative;
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.entry-item:hover {
  background-color: #f7f7ff;
}

.item-left {
  margin-left: 5px;
  display: flex;
  flex-direction: column;
  border-radius: 4px;
  width: 150px;
  display: flex;
  flex-direction: row;
  padding-left: 10px;
}

.item-left-message {
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #666;
  border: solid 1px #4d45e5;
  border-radius: 4px;
  margin-right: 15px;
}

.item-left-message:last-child {
  border: solid 1px #000000;
}

.item-main {
  flex: 1;
  padding: 0 15px;
}

.item-message {
  display: flex;
  color: #666;
  align-items: center;
  justify-content: space-between;
}

.user-name:hover,
.item-title:hover,
.category-container:hover,
.item-message-left span:hover {
  color: #4d45e5;
}

.item-message-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  color: #666;
}

.category-container {
  background-color: #f1f1ff;
  border-radius: 4px;
  padding: 3px 10px;
  font-size: 14px;
  color: #666;
}

.item-title {
  font-weight: 700;
  font-size: 20px;
  color: #000000;
  flex: none;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-bottom: 5px;
}

@media screen and (max-width: 800px) {
  .entry-item {
    position: relative;
    padding: 5px 0;
  }

  .item-title {
    font-size: 18px;
  }
}

@media screen and (max-width: 700px) {
  .item-message-right .ant-divider-vertical:nth-child(4) {
    display: none;
  }

  .category-container {
    font-size: 12px;
    margin-left: 10px;
    padding: 2px 5px;
  }

  .item-message {
    align-items: normal;
    flex-direction: column;
    padding-bottom: 5px;
    font-size: 12px;
    padding-left: 10px;
    padding-right: 10px;
  }

  .item-title {
    padding: 5px 10px;
    margin-bottom: 0;
  }

  .item-message-left {
    margin-top: 5px;
    position: absolute;
    right: 10px;
  }
}
</style>
