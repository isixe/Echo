<template>
	<a-layout>
		<SideBar :isCollapsed="isCollapsed"></SideBar>
		<a-layout>
			<Header :isCollapsed="isCollapsed" :searchEnabled="searchEnabled" @iconClick="() => isCollapsed = !isCollapsed"
				@onSearch="(text) => searchText = text">
			</Header>
			<Main :searchText="searchText" @enableSearch="(e) => searchEnabled = e"></Main>
		</a-layout>
	</a-layout>
</template>

<script setup>
import { SideBar, Header, Main } from './components'

const searchText = ref()
const searchEnabled = ref(true)

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