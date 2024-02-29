<template>
  <div class="entry-item">
    <RouterLink :to="'/article/' + item.id">
      <div class="item-message">
        <div class="item-message-left">
          <RouterLink :to="'/user/' + item.userId" class="user-info">
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
          </RouterLink>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
          <span>{{ updateTime }}</span>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
          <RouterLink :to="'/category/' + item.categoryId" class="category-container">
            {{ item.category }}
          </RouterLink>
        </div>
        <div class="item-message-right">
          <span> <EyeOutlined /> {{ item.pvCount }} </span>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
          <span> <LikeOutlined /> {{ item.likeCount }} </span>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
          <span> <MessageOutlined /> 0 </span>
        </div>
      </div>
      <div>
        <p class="item-title">{{ item.title }}</p>
      </div>
      <div class="item-content">
        <div class="item-content-left">
          <p class="item-summary">
            {{ item.summary }}
          </p>
        </div>
        <div class="item-content-right">
          <img class="entry-item-picture" v-if="item.featuredPic" :src="item.featuredPic" />
        </div>
      </div>
    </RouterLink>
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
  position: relative;
}

.entry-item:hover {
  background-color: #f7f7ff;
}

.item-message {
  display: flex;
  padding: 15px;
  color: #666;
  align-items: center;
  justify-content: space-between;
}

.user-name:hover,
.item-title:hover,
.category-container:hover,
.item-message-right span:hover {
  color: #4d45e5;
}

.item-message-left {
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
  padding: 0 15px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 80%;
}

.item-content {
  padding: 10px 15px 15px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  color: #666;
}

.item-content-right {
  display: flex;
  align-items: center;
}

.item-summary {
  flex: none;
  line-height: 28px;
  height: 56px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.entry-item-picture {
  border-radius: 4px;
  height: 105px;
  width: 140px;
  margin-top: -45px;
}

@media screen and (max-width: 1200px) {
  .item-title {
    width: 70%;
  }
}

@media screen and (max-width: 800px) {
  .item-title {
    font-size: 18px;
    width: 85%;
  }

  .item-content {
    padding: 0 10px 10px;
    display: flex;
    align-items: center;
  }

  .entry-item-picture {
    width: 85px;
    height: 55px;
    margin-top: 12px;
  }
}

@media screen and (max-width: 700px) {
  .item-message-left .ant-divider-vertical:nth-child(4) {
    display: none;
  }

  .category-container {
    font-size: 12px;
    position: absolute;
    top: 130px;
    right: 10px;
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
    width: 80%;
  }

  .item-content {
    padding-bottom: 40px;
  }

  .item-message-right {
    margin-top: 5px;
    position: absolute;
    top: 128px;
  }

  .entry-item-picture {
    width: 85px;
    height: 55px;
    margin-top: 12px;
  }

  .item-summary {
    height: 44px;
    line-height: 22px;
  }

  .entry-item-picture {
    margin: 0;
    width: 62px;
    height: 44px;
  }
}
</style>
