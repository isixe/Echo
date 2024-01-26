<template>
	<a-layout>
		<SideBar :isCollapsed="isCollapsed"></SideBar>
		<a-layout>
			<Header :isCollapsed="isCollapsed" @iconClick="() => isCollapsed = !isCollapsed"></Header>
			<Main></Main>
		</a-layout>
	</a-layout>
</template>

<script setup>
import { SideBar, Header, Main } from "./components"

const isCollapsed = ref(false)
</script>