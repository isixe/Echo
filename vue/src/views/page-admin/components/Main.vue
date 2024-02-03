<template>
	<a-layout-content class="main">
		<RouterView :searchText="searchText" @noSearch="() => $emit('enableSearch')"></RouterView>
	</a-layout-content>
</template>

<script setup>
const { searchText } = defineProps(['searchText'])
</script>

<style scoped>
.main {
	margin: 15px;
	box-sizing: border-box;
}
</style>