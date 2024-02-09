<template>
	<a-layout>
		<SideBar v-model="isCollapsed"></SideBar>
		<a-layout>
			<Header v-model:collapsed="isCollapsed" v-model:useSearch="isSearch" v-model:search="searchText">
			</Header>
			<Main v-model:search="searchText" v-model:useSearch="isSearch"></Main>
		</a-layout>
	</a-layout>
</template>

<script setup>
import { SideBar, Header, Main } from './components'

const searchText = ref()
const isSearch = ref(true)

const isCollapsed = ref(false)
if (window.innerWidth < 1000) {
	isCollapsed.value = true
}

window.onresize = () => {
	if (window.innerWidth < 1000) {
		return isCollapsed.value = true
	}
	isCollapsed.value = false
}

</script>