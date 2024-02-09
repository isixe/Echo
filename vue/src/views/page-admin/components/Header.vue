<template>
    <a-layout-header>
        <div class="header-left">
            <div class="menu">
                <menu-unfold-outlined v-if="isCollapsed" class="trigger" @click="() => isCollapsed = !isCollapsed" />
                <menu-fold-outlined v-else class="trigger" @click="() => isCollapsed = !isCollapsed" />
            </div>
            <div class="breadcrumb">
                <a-breadcrumb>
                    <a-breadcrumb-item>首页</a-breadcrumb-item>
                </a-breadcrumb>
            </div>
        </div>

        <div class="header-right">
            <div class="header-right-search" v-if="useSearch">
                <a-input v-model:value="text" placeholder="请输入关键词" @pressEnter="() => searchText = text">
                    <template #suffix>
                        <SearchOutlined @click="() => searchText = text" style="color: rgba(0, 0, 0, 0.45)" />
                    </template>
                </a-input>
            </div>
            <div class="header-right-info">
                <a-popover placement="bottom" trigger="click">
                    <template #content>
                        <a-list :dataSource="data">
                            <!-- <template #renderItem="{ item }">
                                <a-list-item>
                                    <a-list-item-meta description="这是一条消息内容">
                                        <template #title>
                                            <a href="#">{{ item.title }}</a>
                                        </template>
                                        <template #avatar>
                                            <a-avatar
                                                src="https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png" />
                                        </template>
                                    </a-list-item-meta>
                                </a-list-item>
                            </template> -->
                        </a-list>
                    </template>
                    <a-badge dot class="header-badge">
                        <BellOutlined class="info-icon" />
                    </a-badge>
                </a-popover>
            </div>
            <a-dropdown placement="bottomRight" overlayClassName="dropdown-menu">
                <div class="user-info">
                    <a-avatar class="user-avatar" :size="26" :src="avatar">
                        <template #icon>
                            <UserOutlined />
                        </template>
                    </a-avatar>
                    <span class="user-name">{{ name }}</span>
                </div>
                <template #overlay>
                    <a-menu>
                        <a-menu-item @click="handleToUserCenter">
                            <UserOutlined />
                            个人中心
                        </a-menu-item>
                        <a-menu-item @click="handleLoginOut">
                            <LoginOutlined />
                            退出
                        </a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
        </div>

    </a-layout-header>
</template>

<script setup>
import { get } from '@/api/admin'
import { createVNode } from 'vue';
import { Modal } from 'ant-design-vue'
import { useAdminStore } from '@/stores/admin'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

const text = ref('')
const name = ref('')
const avatar = ref('')
const router = useRouter()
const isCollapsed = defineModel('collapsed')
const useSearch = defineModel('useSearch')
const searchText = defineModel('search')

onMounted(() => {
    const store = useAdminStore()
    get({ id: store.id }).then((res) => {
        name.value = res.data.name
        avatar.value = res.data.avatar
    })
})

const handleLoginOut = () => {
    const modal = Modal.confirm({
        title: '提示',
        icon: createVNode(ExclamationCircleOutlined),
        content: '您确定要退出系统吗？',
        okText: '确认',
        cancelText: '取消',
        centered: true,
        async onOk() {
            const store = useAdminStore()
            await store.logoutAction()
                .then(() => {
                    router.push('/admin/login')
                }).catch(() => {
                    modal.destroy()
                })
        },
    });
}

const handleToUserCenter = () => {
    // router.push('/userCenter')
}
</script>

<style scoped>
.ant-layout-header {
    background: #fff;
    padding: 0 15px;
    height: 48px;
    line-height: 48px;
    display: flex;
    z-index: 100;
    right: 0;
    top: 0;
    transition: left 0.2s;
    box-shadow: 0 1px 4px #eee;
}

.header-left {
    display: flex;
    align-items: center;
}

.header-left .menu {
    width: 15px;
}

.trigger {
    cursor: pointer;
    transition: color 0.3s;
}

.trigger:hover {
    color: #1890ff;
}

.header-left .breadcrumb {
    line-height: 48px;
    margin-left: 20px;
}


.header-right {
    margin-left: auto;
    display: flex;
    align-items: center;
}

.header-right-info {
    margin-right: 20px;
    height: 48px;
    line-height: 48px;
}

.header-badge {
    height: 48px;
    line-height: 48px;
}

.ant-badge-dot {
    top: 14px;
}

.user-info {
    cursor: pointer;
    display: flex;
    align-items: center;
}

.user-info .user-avatar {
    margin-right: 8px;
    border: 1px solid #666;
    border-radius: 50%;
}

.user-info .user-name {
    vertical-align: middle;
    width: 64px;
    line-height: 64px;
    height: 64px;
    overflow: hidden;
    text-overflow: ellipsis;
}

.header-right {
    margin-left: auto;
    display: flex;
}

.header-right .header-right-info {
    margin-right: 20px;
    height: 48px;
    line-height: 48px;
}

.header-right .header-right-info .header-badge {
    height: 48px;
    line-height: 48px;
}

.header-right .header-right-info .header-badge :deep(.ant-badge-dot) {
    top: 14px;
}

.header-right .header-right-info .info-icon {
    font-size: 16px;
    vertical-align: middle;
    padding: 0 4px;
    cursor: pointer;
}

.header-right-search {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 48px;
    margin-right: 10px;
}

@media screen and (max-width: 1000px) {
    .user-info .user-name {
        display: none
    }
}
</style>