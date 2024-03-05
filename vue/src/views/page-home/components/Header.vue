<template>
  <a-layout-header>
    <div class="header-box">
      <div class="header-left">
        <div class="logo">
          <img src="../../../assets/svg/logo.svg" width="40" height="40" />
        </div>
        <div class="header-route">
          <RouterLink to="/"> Echo </RouterLink>
          <RouterLink to="/article"> 文章 </RouterLink>
          <RouterLink to="/question"> 问答 </RouterLink>
        </div>
      </div>
      <div class="header-right">
        <a-input
          class="search-box"
          placeholder="请输入关键词"
          style="width: 200px"
          @pressEnter="onSearch"
        >
          <template #suffix>
            <SearchOutlined style="color: rgba(0, 0, 0, 0.45)" />
          </template>
        </a-input>
        <a-dropdown>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" style="display: grid">
                <RouterLink to="/article/edit">
                  <font-awesome-icon
                    :icon="['fas', 'pen-to-square']"
                    style="width: 20px; margin-left: -5px"
                  />
                  写文章
                </RouterLink>
              </a-menu-item>
              <a-menu-item key="2" style="display: grid">
                <RouterLink to="/question/edit">
                  <font-awesome-icon
                    :icon="['fas', 'question']"
                    style="width: 20px; margin-left: -5px"
                  />
                  提问题
                </RouterLink>
              </a-menu-item>
              <a-menu-item key="2" style="display: grid">
                <RouterLink to="/draft">
                  <font-awesome-icon
                    :icon="['fas', 'box-archive']"
                    style="width: 20px; margin-left: -5px"
                  />
                  草稿箱
                </RouterLink>
              </a-menu-item>
            </a-menu>
          </template>
          <a-button type="primary" style="background-color: #4d45e5">
            发布
            <DownOutlined style="font-size: 10px; font-weight: 100" />
          </a-button>
        </a-dropdown>
        <template v-if="user">
          <a-dropdown placement="bottomRight" overlayClassName="dropdown-menu">
            <div class="user-info">
              <a-avatar class="user-avatar" :size="30" :src="user.avatar">
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
              <span class="user-name">{{ user.name }}</span>
            </div>
            <template #overlay>
              <a-menu>
                <RouterLink :to="'/user/' + user.id">
                  <a-menu-item> <UserOutlined />&nbsp; 我的主页 </a-menu-item>
                </RouterLink>
                <!-- <a-menu-item> <ClockCircleOutlined />&nbsp; 浏览历史 </a-menu-item> -->
                <a-menu-item> <HeartOutlined />&nbsp; 我的收藏 </a-menu-item>
                <a-menu-item @click="handleToUserCenter">
                  <SettingOutlined />&nbsp; 个人中心
                </a-menu-item>
                <a-menu-item @click="handleLoginOut"> <LoginOutlined />&nbsp; 退出 </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <template v-else>
          <a-button style="padding: 0 20px" @click="() => router.push('/login')">登录</a-button>
        </template>
      </div>
      <div class="header-right-mobile">
        <span class="search-box">
          <SearchOutlined
            style="color: rgba(0, 0, 0, 0.45); font-size: 18px; cursor: pointer; margin-right: 15px"
          />
        </span>
        <span class="menu-box">
          <MenuOutlined style="color: rgba(0, 0, 0, 0.45); font-size: 18px; cursor: pointer" />
        </span>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup>
import router from '@/router'
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { get } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { library } from '@fortawesome/fontawesome-svg-core'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { faPenToSquare, faQuestion, faBoxArchive } from '@fortawesome/free-solid-svg-icons'
library.add(faPenToSquare, faQuestion, faBoxArchive)

const user = ref()
const store = useUserStore()

onMounted(() => {
  if (store.id) {
    get({ id: store.id }).then((result) => {
      user.value = result.data
    })
  }
})

const onSearch = () => {}

const handleLoginOut = () => {
  const modal = Modal.confirm({
    title: '提示',
    icon: createVNode(ExclamationCircleOutlined),
    content: '您确定要退出吗？',
    okText: '确认',
    cancelText: '取消',
    centered: true,
    async onOk() {
      await store
        .logoutAction()
        .then(() => {
          router.push('/login')
        })
        .catch(() => {
          modal.destroy()
        })
    }
  })
}

const handleToUserCenter = () => {
  // router.push('/userCenter')
}
</script>

<style scoped>
.ant-layout-header {
  height: 64px;
  background-color: #ffffff;
  border-bottom: 1px solid #e8eff9;
  box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.05);
  padding: 0;
}

.header-box {
  align-items: center;
  display: flex;
  height: 64px;
  justify-content: space-between;
  margin: 0 auto;
  max-width: 1200px;
}

.header-left {
  align-items: center;
  display: flex;
}

.header-right {
  align-items: center;
  display: flex;
}

.header-right-mobile {
  display: none;
}

.logo {
  cursor: pointer;
  font-size: 20px;
  height: 40px;
  margin-right: 10px;
  vertical-align: middle;
  width: 40px;
}

.header-route a {
  color: #666;
  cursor: pointer;
  display: inline-block;
  font-size: 16px;
  margin-right: 30px;
  outline: none;
}

.router-link-exact-active {
  color: #4d45e5 !important;
}

.header-right button {
  margin-left: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  margin-left: 15px;
}

.user-avatar {
  border-radius: 50%;
  border: 1px solid #666;
  margin-right: 5px;
}

.user-name {
  width: 64px;
  line-height: 64px;
  height: 64px;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media screen and (max-width: 1200px) {
  .ant-layout-header {
    padding: 0 10px;
  }
}

@media screen and (max-width: 800px) {
  .header-box,
  .ant-layout-header {
    height: 50px;
  }

  .header-right {
    display: none;
  }

  .header-right-mobile {
    display: block;
  }

  .header-route a:not(.router-link-exact-active) {
    display: none;
  }
}
</style>
