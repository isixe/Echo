<template>
  <a-menu v-model:selectedKeys="selectedKeys" :openKeys="parentKey" theme="dark" mode="inline">
    <a-menu-item key="/dashboard">
      <router-link to="/dashboard">
        <DashboardOutlined />
        <span>控制台</span>
      </router-link>
    </a-menu-item>

    <a-sub-menu key="/system">
      <template #title>
        <span>
          <SettingOutlined />
          <span>系统管理</span>
        </span>
      </template>

      <a-menu-item key="/system/userList">
        <router-link to="/system/userList"> 用户管理 </router-link>
      </a-menu-item>
      <a-menu-item key="/system/articleList">
        <router-link to="/system/articleList">文章管理</router-link>
      </a-menu-item>
      <a-menu-item key="/system/questionList">
        <router-link to="/system/questionList"> 问答管理 </router-link>
      </a-menu-item>
      <a-menu-item key="/system/categoryList">
        <router-link to="/system/categoryList"> 分类管理 </router-link>
      </a-menu-item>
      <a-menu-item key="/system/collectionArticleList"
        ><router-link to="/system/collectionArticleList">文章收藏管理</router-link></a-menu-item
      >
      <a-menu-item key="/system/collectionQuestionList"
        ><router-link to="/system/collectionQuestionList">问答收藏管理</router-link></a-menu-item
      >
      <a-menu-item key="/system/commentArticleList">
        <router-link to="/system/commentArticleList">文章评论管理</router-link>
      </a-menu-item>
      <a-menu-item key="7"
        ><router-link to="/system/commentQuestionList">问答评论管理</router-link></a-menu-item
      >
    </a-sub-menu>
    <a-sub-menu key="/permission">
      <template #title>
        <span>
          <KeyOutlined />
          <span>权限管理</span>
        </span>
      </template>
      <a-menu-item key="/permission/roleList">
        <router-link to="/permission/roleList"> 管理员管理 </router-link>
      </a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="/admin">
      <template #title>
        <span>
          <UserOutlined />
          <span>个人中心</span>
        </span>
      </template>
      <a-menu-item key="/admin/info">
        <router-link to="/admin/info">信息中心</router-link>
      </a-menu-item>
    </a-sub-menu>
  </a-menu>
</template>

<script setup>
const route = useRoute()
const selectedKeys = ref([route.path])
const parentKey = ref([`/${route.path.split('/')[1]}`])

watch(
  () => route.path,
  (newPath) => {
    parentKey.value = [`/${route.path.split('/')[1]}`]
    selectedKeys.value = [newPath]
  }
)
</script>
