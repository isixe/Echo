<template>
  <a-layout>
    <the-header></the-header>
    <a-layout class="content">
      <the-menu></the-menu>
      <the-main></the-main>
    </a-layout>
    <the-footer></the-footer>
  </a-layout>
</template>

<script setup>
import { TheHeader, TheMain, TheFooter } from '@/views/page-home/components'
import { TheMenu } from './components'
</script>

<style scoped>
.content {
  display: flex;
  flex-direction: row;
}
</style>
