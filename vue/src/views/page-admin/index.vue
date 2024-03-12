<template>
  <a-layout>
    <the-sidebar v-model="isCollapsed"></the-sidebar>
    <a-layout>
      <the-header
        v-model:collapsed="isCollapsed"
        v-model:useSearch="isSearch"
        v-model:search="searchText"
      >
      </the-header>
      <the-main v-model:search="searchText" v-model:useSearch="isSearch"></the-main>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { TheSidebar, TheHeader, TheMain } from './components'

const searchText = ref()
const isSearch = ref(true)

const isCollapsed = ref(false)
if (window.innerWidth < 1000) {
  isCollapsed.value = true
}

window.onresize = () => {
  if (window.innerWidth < 1000) {
    return (isCollapsed.value = true)
  }
  isCollapsed.value = false
}
</script>
