<template>
	<a-layout-content class="container">
		<RouterView :searchText="searchText"></RouterView>
	</a-layout-content>
</template>

<script setup>
const { searchText } = defineProps(['searchText'])
</script>

<style scoped>
.container {
	margin: 10px;
	box-sizing: border-box;
}
</style>