<template>
	<a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
		<a-menu-item key="1">
			<DashboardOutlined />
			<span>控制台</span>
		</a-menu-item>

		<a-sub-menu key="sub1">
			<template #title>
				<span>
					<SettingOutlined />
					<span>系统管理</span>
				</span>
			</template>
			<a-menu-item key="2">用户管理</a-menu-item>
			<a-menu-item key="3">文章管理</a-menu-item>
			<a-menu-item key="4">帖子管理</a-menu-item>
			<a-menu-item key="5">问答管理</a-menu-item>
			<a-menu-item key="6">分类管理</a-menu-item>
			<a-menu-item key="7">评论管理</a-menu-item>
			<a-menu-item key="8">收藏管理</a-menu-item>
		</a-sub-menu>
		<a-sub-menu key="sub2">
			<template #title>
				<span>
					<KeyOutlined />
					<span>权限管理</span>
				</span>
			</template>
			<RouterLink to="/permission/roleList">
				<a-menu-item key="10">管理员管理</a-menu-item>
			</RouterLink>
		</a-sub-menu>
		<a-sub-menu key="sub3">
			<template #title>
				<span>
					<UserOutlined />
					<span>个人中心</span>
				</span>
			</template>
			<a-menu-item key="11">信息中心</a-menu-item>
		</a-sub-menu>
	</a-menu>
</template>

<script setup>
const selectedKeys = ref(['1']);
</script>