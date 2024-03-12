<template>
  <a-layout-content class="main">
    <router-view v-model:search="searchText" v-model:useSearch="isSearch"></router-view>
  </a-layout-content>
</template>

<script setup>
const searchText = defineModel('search')
const isSearch = defineModel('useSearch')
</script>

<style scoped>
.main {
  margin: 15px;
  box-sizing: border-box;
}
</style>
