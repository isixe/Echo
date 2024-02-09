<template>
	<a-layout-content class="main">
		<RouterView v-model:search="searchText" v-model:useSearch="isSearch"></RouterView>
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