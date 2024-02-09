<template>
	<a-layout-sider :collapsed="isCollapsed" :trigger="null" collapsible>

		<div class="logo">
			<router-link to="/dashboard">
				<img src="../../../assets/svg/logo.svg" />
			</router-link>
			<router-link to="/dashboard">
				<span v-if="!isCollapsed">Echo Admin</span>
			</router-link>
		</div>

		<SidebarMenu></SidebarMenu>

	</a-layout-sider>
</template>

<script setup>
import SidebarMenu from './SideBarMenu';

const isCollapsed = defineModel()
</script>

<style scoped>
.ant-layout-sider {
	min-height: 100vh;
}

.logo {
	height: 45px;
	display: flex;
	justify-content: center;
	padding: 10px 0;
	margin: 10px 0;
	overflow: hidden;
}

.logo img {
	width: 30px;
	height: 30px;
	margin: 0 5px;
}

.logo span {
	color: #ffffff;
	font-weight: 700;
	font-size: 18px;
}
</style>