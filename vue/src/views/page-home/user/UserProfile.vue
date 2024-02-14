<template>
  <div class="user-container">
    <div class="user-info-box">
      <div class="avatar-content">
        <img
          class="user-avatar"
          src="https://pic1.zhimg.com/v2-1e1363e9670078b4ae450446d0abd60a_xll.jpg?source=32738c0c"
          alt="avatar"
        />
      </div>
      <div class="info-content">
        <div class="user-name">用户名</div>

        <div class="user-detail">
          <span>加入时间：2023-2-2</span>
          <span>最后活跃时间：2023-2-3</span>
        </div>
        <div class="user-desc">这个人很懒，什么都没有写...</div>
      </div>
      <div class="footer-content">
        <a-button class="user-subscribe-btn" type="primary" style="background-color: #4d45e5"
          >关注</a-button
        >
      </div>
    </div>
    <div class="user-dynamic-box">
      <a-layout-content class="dynamic-entry-content">
        <a-tabs class="nav-menu" v-model:activeKey="activeKey">
          <a-tab-pane key="1" tab="文章 22">
            <template v-for="n in 10" :key="n">
              <EntryItem v-bind="user">{{ n }}</EntryItem>
            </template>
          </a-tab-pane>
          <a-tab-pane key="2" tab="问答 23">
            <template v-for="n in 10" :key="n">
              <EntryItem v-model="user"></EntryItem>
            </template>
          </a-tab-pane>
          <a-tab-pane key="3" tab="动态 12">
            <template v-for="n in 10" :key="n">
              <EntryItem v-model="user"></EntryItem>
            </template>
          </a-tab-pane>
          <a-tab-pane key="4" tab="关注">
            <div class="subscribe-container"></div>
          </a-tab-pane>
        </a-tabs>
      </a-layout-content>
      <a-layout-sider class="dynamic-side-content" width="320px">
        <div class="subscribe-card">
          <div class="subscirbe-content">
            <div>关注了</div>
            <div class="subscirbe-count">22</div>
          </div>
          <div class="subscirbe-content">
            <div>关注者</div>
            <span class="subscirbe-count">26</span>
          </div>
        </div>
      </a-layout-sider>
    </div>
  </div>
</template>

<script setup>
import { get } from '@/api/user'
import { EntryItem } from '@/views/page-home/components'

const user = ref()
const route = useRoute()
const activeKey = ref('1')

onMounted(() => {
  get({ id: route.params.id }).then((result) => {
    user.value = result.data
  })
})
</script>

<style scoped>
.user-container {
  max-width: 1200px;
  margin: auto;
  min-height: 100%;
}

.user-info-box {
  margin: 15px 0;
  background-color: #ffffff;
  border-radius: 4px;
  padding: 20px;
  display: flex;
  flex-direction: row;
  box-sizing: border-box;
}

.user-dynamic-box {
  margin: 15px 0;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
}

.avatar-content {
  margin-right: 15px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 20px;
}

.user-detail {
  margin-bottom: 20px;
}

.user-detail span {
  margin-right: 15px;
}

.dynamic-entry-content {
  background-color: #ffffff;
  margin-right: 20px;
}

.dynamic-side-content {
  border-radius: 4px;
  background-color: #00000000;
}

.nav-menu {
  font-size: 14px;
  padding: 0 15px;
}

.footer-content {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  margin: 3px;
}

.user-subscribe-btn:hover {
  background-color: #6d66f1 !important;
}

.subscribe-card {
  background-color: #ffffff;
  border-radius: 4px;
  display: grid;
  grid-template-columns: 50% 50%;
}

.subscirbe-content {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 10px 15px;
}

.subscirbe-content:first-child {
  border-right: 1px solid #dadada;
}

.subscirbe-count {
  font-weight: 700;
  font-size: 16px;
}
</style>
