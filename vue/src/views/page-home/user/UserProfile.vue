<template>
  <div class="user-profile-container">
    <div class="user-info-box" v-if="user">
      <div class="avatar-content">
        <img class="user-avatar" v-if="user.avatar" :src="user.avatar" alt="avatar" />
      </div>
      <div class="info-content">
        <div class="user-name">{{ user.name }}</div>

        <div class="user-detail">
          <span>加入时间：{{ user.createdTime }}</span>
          <span>最后活跃时间：{{ user.lastActiveTime }}</span>
        </div>
        <div class="user-desc" v-if="user.description">{{ user.description }}</div>
        <div class="user-desc" v-else>这个人很懒，什么都没有写...</div>
      </div>
      <div class="footer-content" v-if="user && store.id != user.id">
        <a-button
          v-if="!followId"
          class="user-follow-btn"
          type="primary"
          style="background-color: #4d45e5"
          @click="setUserFollow()"
          >关注</a-button
        >
        <a-button
          v-else-if="followId"
          class="user-follow-btn"
          type="primary"
          style="background-color: #ccc"
          @click="removeUserFollow()"
          >取消关注</a-button
        >
      </div>
    </div>
    <div class="user-dynamic-box">
      <a-layout-content class="dynamic-entry-content">
        <a-tabs class="nav-menu" v-model:activeKey="activeKey">
          <a-tab-pane key="article" :tab="'文章 ' + data.articleTotal">
            <a-list :loading="initLoading" item-layout="horizontal" :data-source="fullList">
              <template #renderItem="{ item }">
                <router-link :to="'/article/' + item.id">
                  <article-entry-item :item="item"></article-entry-item>
                </router-link>
              </template>
            </a-list>
          </a-tab-pane>
          <a-tab-pane key="group" :tab="'合集 ' + data.articleGroupTotal">
            <div
              class="group-box-set"
              v-if="user && store.id == user.id"
              @click="() => (showAddModal = !showAddModal)"
            >
              <span><PlusOutlined /> 添加合集</span>
            </div>

            <a-list :loading="initLoading" item-layout="horizontal" :data-source="fullList">
              <template #renderItem="{ item }">
                <router-link :to="'/article/group/' + item.id"
                  ><a-card class="group-box" :title="item.name">
                    <template #extra v-if="store.id == user.id">
                      <a @click.prevent="deleteGroup(item.id)"><DeleteOutlined /> 删除</a>
                    </template>
                    <p class="item-summary">{{ item.description }}</p>
                  </a-card>
                </router-link>
              </template>
            </a-list>
          </a-tab-pane>
          <a-tab-pane key="question" :tab="'问答 ' + data.questionTotal">
            <a-list :loading="initLoading" item-layout="horizontal" :data-source="fullList">
              <template #renderItem="{ item }">
                <router-link :to="'/question/' + item.id" style="display: flex; width: 100%">
                  <question-entry-item :item="item"></question-entry-item>
                </router-link>
              </template>
            </a-list>
          </a-tab-pane>
          <a-tab-pane key="follow" tab="关注">
            <div class="follow-tab">
              <a
                :class="followTab == 'follow' ? 'active-tab' : ''"
                @click="() => switchFollow('follow')"
              >
                他关注的人
              </a>
              <a
                :class="followTab == 'follower' ? 'active-tab' : ''"
                @click="() => switchFollow('follower')"
              >
                关注他的人
              </a>
            </div>
            <a-list :loading="initLoading" item-layout="horizontal" :data-source="fullList">
              <template #renderItem="{ item }">
                <router-link :to="'/user/' + item.id" style="display: flex; width: 100%">
                  <user-entry-item :item="item"></user-entry-item>
                </router-link>
              </template>
            </a-list>
          </a-tab-pane>
        </a-tabs>
      </a-layout-content>
      <a-layout-sider class="dynamic-side-content" width="320px">
        <div class="follow-card">
          <div class="subscirbe-content">
            <div>关注了</div>
            <div class="subscirbe-count">{{ data.followCount }}</div>
          </div>
          <div class="subscirbe-content">
            <div>关注者</div>
            <span class="subscirbe-count">{{ data.followerCount }}</span>
          </div>
        </div>
      </a-layout-sider>
    </div>
  </div>

  <template>
    <a-modal v-model:open="showAddModal" width="700px" title="新建集合">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="newData" :rules="rules">
          <a-form-item name="name" label="集合名">
            <a-input
              v-model:value="newData.name"
              placeholder="请输入集合名"
              show-count
              :maxlength="42"
            />
          </a-form-item>
          <a-form-item name="description" label="描述">
            <a-textarea
              v-model:value="newData.description"
              placeholder="请输入描述"
              show-count
              :maxlength="120"
            />
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (showAddModal = !showAddModal)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="addGroup">提交</a-button>
      </template>
    </a-modal>
  </template>
</template>

<script setup>
import { get, getFollowUserByUserId, getFollowerUserByUserId } from '@/api/user'
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { ArticleEntryItem, QuestionEntryItem, UserEntryItem } from '@/views/page-home/components'
import { getArticleListByUserId } from '@/api/article'
import { getQuestionListByUserId } from '@/api/question'
import { add, remove, getArticleGroupListByUserId } from '@/api/article-group'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { add as setFollow, remove as unFollow, getByUserIdAndFollowId } from '@/api/follow'

const user = ref()
const route = useRoute()
const router = useRouter()
const store = useUserStore()

const data = reactive({
  articleTotal: 0,
  questionTotal: 0,
  articleGroupTotal: 0,
  followCount: 0,
  followerCount: 0
})
const activeKey = ref('')
const followTab = ref('follow')

const initLoading = ref(true)
const loading = ref(false)
const fullList = ref([])
const pages = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15,
  userId: route.params.id
})

const followId = ref()

const form = ref()
const showAddModal = ref(false)

const newData = reactive({
  userId: store.id,
  name: '',
  description: ''
})

onMounted(() => {
  window.addEventListener('scroll', scrollBottom, true)

  dataLoader()
})

const dataLoader = () => {
  get({ id: route.params.id })
    .then((result) => {
      user.value = result.data
      checkFollow()
    })
    .catch(() => {
      router.push('/404')
    })

  getArticleListByUserId(params).then((res) => (data.articleTotal = res.data.total))
  getArticleGroupListByUserId(params).then((res) => (data.articleGroupTotal = res.data.total))
  getQuestionListByUserId(params).then((res) => (data.questionTotal = res.data.total))
  getFollowUserByUserId(params).then((res) => (data.followCount = res.data.total))
  getFollowerUserByUserId(params).then((res) => (data.followerCount = res.data.total))

  let tab = route.query.tab ? route.query.tab : 'article'
  activeKey.value = tab
}

onBeforeUnmount(() => window.removeEventListener('scroll', scrollBottom, true))

const scrollBottom = () => {
  if (initLoading.value || loading.value || params.pageNum >= pages.value) {
    return
  }

  const windowHeight = document.body.scrollHeight
  const scrollHeight = window.innerHeight + window.scrollY
  if (windowHeight - scrollHeight >= 1) {
    return
  }

  onLoadMore()
}

const onLoadMore = () => {
  loading.value = true
  params.pageNum += 1
  let tab = route.query.tab ? route.query.tab : 'article'
  getDataSource(tab)
  nextTick(() => {
    window.dispatchEvent(new Event('resize'))
  })
}

watch(
  () => route.params.id,
  (newId) => {
    params.userId = newId
    dataLoader()
    activeKey.value = 'article'
  }
)

watch(activeKey, (tab) => {
  fullList.value = []
  params.pageNum = 1
  switch (tab) {
    case 'article':
      router.replace({ path: '/user/' + route.params.id, query: { tab: 'article' } })
      break
    case 'group':
      router.replace({ path: '/user/' + route.params.id, query: { tab: 'group' } })
      break
    case 'question':
      router.replace({ path: '/user/' + route.params.id, query: { tab: 'question' } })
      break
    case 'follow':
      router.replace({ path: '/user/' + route.params.id, query: { tab: 'follow' } })
      break
  }
  getDataSource(tab)
})

const getDataSource = (type) => {
  switch (type) {
    case 'article':
      getArticleListByUserId(params).then((res) => {
        console.log(params)
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    case 'group':
      queryGroupData()
      break
    case 'question':
      getQuestionListByUserId(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    case 'follow':
      if (followTab.value == 'follower') {
        getFollowerUserByUserId(params).then((res) => {
          fullList.value = fullList.value.concat(res.data.records)
          pages.value = res.data.pages
          initLoading.value = false
          loading.value = false
        })
        break
      }
      getFollowUserByUserId(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
      })
      break
    case 'follower':
      getFollowUserByUserId(params).then((res) => {
        fullList.value = fullList.value.concat(res.data.records)
        pages.value = res.data.pages
        initLoading.value = false
        loading.value = false
        console.log(fullList.value)
      })
      break
    default:
      router.push({ path: '/user/' + route.params.id })
      activeKey.value = 'article'
  }
}

const checkFollow = () => {
  const params = {
    userId: store.id,
    followUserId: user.value.id
  }
  getByUserIdAndFollowId(params).then((res) => {
    followId.value = res.data.id
  })
}

const queryGroupData = () => {
  getArticleGroupListByUserId(params).then((res) => {
    fullList.value = fullList.value.concat(res.data.records)
    pages.value = res.data.pages
    initLoading.value = false
    loading.value = false
    data.articleGroupTotal = res.data.total
  })
}

const addGroup = () => {
  loading.value = true
  form.value
    .validate()
    .then(async () => {
      const formData = new FormData()
      Object.keys(newData).forEach((key) => {
        formData.append(key, newData[key])
      })
      await add(formData)
        .then(() => {
          message.success('新建成功')
          fullList.value = []
          params.pageNum = 1
          queryGroupData(params)
          Object.keys(newData).forEach((key) => {
            newData[key] = ''
          })
          loading.value = false
          showAddModal.value = false
        })
        .catch(() => {
          loading.value = false
          showAddModal.value = false
        })
    })
    .catch(() => {
      loading.value = false
    })
}

const deleteGroup = (groupId) => {
  Modal.confirm({
    title: `确定要删除合集吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '文章合集删除后，不能恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', groupId)
      remove(formData).then(() => {
        message.success('删除成功')
        fullList.value = []
        params.pageNum = 1
        queryGroupData()
        window.scrollTo(0, 0)
      })
    }
  })
}

const setUserFollow = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('followUserId', user.value.id)
  setFollow(formData).then(() => checkFollow())
}

const removeUserFollow = () => {
  const formData = new FormData()
  formData.append('id', followId.value)
  unFollow(formData).then(() => {
    followId.value = null
    console.log(followId.value)
  })
}

const switchFollow = (tab) => {
  console.log(tab)
  followTab.value = tab
  fullList.value = []
  params.pageNum = 1
  getDataSource('follow')
}

const formItemLayout = {
  labelCol: {
    xs: {
      span: 24
    },
    sm: {
      span: 6
    }
  },
  wrapperCol: {
    xs: {
      span: 24
    },
    sm: {
      span: 14
    }
  }
}

const rules = {
  name: [
    {
      max: 42,
      required: true,
      trigger: 'blur',
      message: '合集名称不能为空和大于16个字符'
    }
  ],
  description: [
    {
      max: 120,
      required: false,
      trigger: 'blur',
      message: '描述不能大于120个字符'
    }
  ]
}
</script>

<style scoped>
.user-profile-container {
  max-width: 1200px;
  margin: auto;
  min-height: 100%;
  padding: 15px 0;
}

.user-info-box {
  background-color: #ffffff;
  border-radius: 4px;
  padding: 20px;
  display: flex;
  flex-direction: row;
  box-sizing: border-box;
}

.user-dynamic-box {
  margin-top: 15px;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
}

.avatar-content {
  margin-right: 15px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 20px;
}

.user-detail {
  margin-bottom: 20px;
}

.user-detail span {
  margin-right: 15px;
}

.dynamic-entry-content {
  flex: 1;
  background-color: #ffffff;
  margin-right: 15px;
}

.dynamic-side-content {
  border-radius: 4px;
  background-color: #00000000;
}

.nav-menu {
  font-size: 14px;
  padding: 0 15px;
}

.footer-content {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  margin: 3px;
}

.user-follow-btn:hover {
  background-color: #6d66f1 !important;
}

.follow-card {
  background-color: #ffffff;
  border-radius: 4px;
  display: grid;
  grid-template-columns: 50% 50%;
}

.subscirbe-content {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 10px 15px;
}

.subscirbe-content:first-child {
  border-right: 1px solid #dadada;
}

.subscirbe-count {
  font-weight: 700;
  font-size: 16px;
}

.group-box {
  margin-bottom: 15px;
  border-color: #dadada;
}

.group-box a {
  color: #666;
  margin-left: 10px;
}

.group-box a:hover {
  color: #6d66f1;
}

.group-box-set {
  text-align: center;
  padding: 15px;
  border-radius: 4px;
  border: 1px dashed #666;
  border-spacing: 5px;
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.group-box:hover,
.group-box-set:hover {
  background-color: #f7f7ff;
  cursor: pointer;
}

.item-title {
  font-weight: 700;
  font-size: 20px;
  color: #000000;
  flex: none;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.item-title:hover {
  color: #4d45e5;
}

.item-summary {
  flex: none;
  line-height: 28px;
  max-height: 56px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.entry-item {
  flex: 1;
}

.follow-tab {
  margin-bottom: 15px;
}

.follow-tab a {
  margin-right: 15px;
  color: #666;
}

.follow-tab a:hover {
  cursor: pointer;
}

.follow-tab .active-tab {
  font-weight: 700;
}
</style>
