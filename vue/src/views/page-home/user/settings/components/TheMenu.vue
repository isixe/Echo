<template>
  <a-menu
    class="settings-menu"
    v-model:selectedKeys="selectedKeys"
    style="width: 256px"
    mode="vertical"
    :items="items"
  />
</template>
<script setup>
import { UserOutlined, SmileOutlined } from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()
const selectedKeys = ref(['/settings/profile'])

const items = ref([
  {
    key: '/settings/profile',
    icon: () => h(UserOutlined),
    label: '个人信息',
    title: 'profile'
  },
  {
    key: '/settings/personalize',
    icon: () => h(SmileOutlined),
    label: '个性化设置',
    title: 'personalize'
  }
])

onMounted(() => {
  selectedKeys.value = route.path ? [route.path] : ['/settings/profile']
})

watch(selectedKeys, (key) => {
  router.push(key[0])
})
</script>

<style scoped>
.settings-menu {
  min-height: 90vh;
}

:global(.settings-menu .ant-menu-item-selected) {
  background-color: #f1f1ff;
}
</style>
