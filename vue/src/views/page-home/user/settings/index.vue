<template>
  <a-layout class="content">
    <the-menu></the-menu>
    <the-main></the-main>
  </a-layout>
</template>

<script setup>
import { TheMain } from '@/views/page-home/components'
import { TheMenu } from './components'
</script>

<style scoped>
.content {
  display: flex;
  flex-direction: row;
}
</style>
