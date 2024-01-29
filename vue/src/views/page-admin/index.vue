<template>
	<a-layout>
		<SideBar :isCollapsed="isCollapsed"></SideBar>
		<a-layout>
			<Header :isCollapsed="isCollapsed" @iconClick="() => isCollapsed = !isCollapsed"
				@onSearch="(text) => searchText = text">
			</Header>
			<Main :searchText="searchText"></Main>
		</a-layout>
	</a-layout>
</template>

<script setup>
import { SideBar, Header, Main } from './components'

const searchText = ref()
const isCollapsed = ref(false)
</script>