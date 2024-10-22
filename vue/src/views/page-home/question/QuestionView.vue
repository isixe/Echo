<template>
  <div class="main">
    <div class="container" v-if="data">
      <div class="content">
        <div class="title">
          <h1>{{ data.title }}</h1>
          <div class="item-message">
            <router-link :to="'/user/' + data.userId" class="user-info">
              <a-avatar
                class="user-avatar"
                :src="data.avatar"
                :size="20"
                :style="{ marginRight: '10px', marginTop: '10px' }"
              >
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
              <span class="user-name">{{ data.author }}</span>
            </router-link>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <span> <EyeOutlined /> {{ data.pvCount }} </span>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <span>{{ data.updateTime }}</span>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <router-link :to="'/category/' + data.categoryId" class="category-container">
              <FolderOutlined /> {{ data.category }}
            </router-link>
            <template v-if="data.userId == userId">
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <router-link :to="{ path: '/question/edit', query: { id: data.id } }">
                <EditOutlined /> 编辑
              </router-link>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <a @click="deleteQuestion()"><DeleteOutlined /> 删除 </a>
            </template>
          </div>
        </div>
        <div class="question-text-top"></div>
        <div class="question-text-footer">
          <div class="tags-line">
            <span class="tag-title">问题标签：</span>
            <template v-if="data.tag">
              <a-tag
                class="tag"
                v-for="tag in data.tag.split(',')"
                :key="tag"
                :color="tag.length < 3 ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
              >
                <router-link
                  :to="{ path: '/tag', query: { tagName: tag, tab: 'question' } }"
                  target="_blank"
                  >{{ tag }}</router-link
                >
              </a-tag>
            </template>
            <template v-else> 暂无标签 </template>
          </div>

          <div class="question-heart">
            <div class="posts-affix-box">
              <div class="question-action-box">
                <div class="action-left">
                  <p>{{ data.title }}</p>
                </div>
                <div class="action-right">
                  <span class="action-hover"
                    ><a @click="shareQuesion()"><ShareAltOutlined /></a
                  ></span>
                  <span class="action-hover">
                    <a v-if="collectData && collectData.id" @click="unCollectQuestion()">
                      <StarFilled style="color: #4d45e5" />
                    </a>
                    <a v-else @click="collectQuestion()"><StarOutlined /></a
                  ></span>
                  <span class="action-hover">
                    <a v-if="thumbData && thumbData.id" @click="unThumbQuestion()">
                      <LikeFilled style="color: #4d45e5" />
                    </a>
                    <a v-else @click="thumbQuestion()">
                      <LikeOutlined />
                    </a>
                  </span>
                  <span class="action-hover"
                    ><a href="#comment"><MessageOutlined /></a
                  ></span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="comment-box" id="comment">
        <a-textarea v-model:value="postContent" />
        <div class="button-box">
          <a-button type="primary" style="background-color: #4d45e5" @click="postComment()"
            >回复问答</a-button
          >
        </div>
        <div>
          <template v-for="comment in commentData" :key="comment">
            <the-question-comment-item
              :comment="comment"
              v-model="data.userId"
              @onCommentUpdate="queryComment()"
            ></the-question-comment-item>
          </template>
        </div>
        <div class="pagination" v-show="total > 15">
          <a-pagination
            v-model:current="current"
            :showSizeChanger="false"
            show-quick-jumper
            :total="total"
            :pageSize="params.pageSize"
            @change="onChange"
          />
        </div>
      </div>
    </div>

    <div class="sidebar-right">
      <div class="sticky-card">
        <div class="user-card" v-if="data && data.userId">
          <div class="user-title">关于作者</div>
          <div class="user-content">
            <div class="user-box">
              <router-link :to="'/user/' + data.userId" class="user-info">
                <a-avatar
                  class="user-avatar"
                  :src="data.avatar"
                  :size="40"
                  :style="{ marginRight: '10px', marginTop: '-3px' }"
                >
                  <template #icon>
                    <UserOutlined />
                  </template>
                </a-avatar>
                <span class="user-name">{{ data.author }}</span>
              </router-link>
              <template v-if="data && store.id != data.userId">
                <a-button
                  v-if="!followId"
                  class="user-follow-btn"
                  type="primary"
                  style="background-color: #4d45e5; border-radius: 3px"
                  @click="setUserSubscribe()"
                  >关注</a-button
                >
                <a-button
                  v-else-if="followId"
                  class="user-follow-btn"
                  type="primary"
                  style="background-color: #ccc; border-radius: 3px"
                  @click="removeUserSubscribe()"
                  >取消关注</a-button
                >
              </template>
              <template v-else>
                <a-button
                  class="user-follow-btn"
                  type="primary"
                  style="background-color: #ccc; border-radius: 3px"
                  disabled
                  >关注</a-button
                >
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { get, remove } from '@/api/question'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { add, getCommentQuestionRootListByQuestionId } from '@/api/question-comment'
import { useUserStore } from '@/stores/user'
import { TheQuestionCommentItem } from '../components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import {
  add as collect,
  remove as unCollect,
  getCollectByUserAndQuestion
} from '@/api/collection-question'
import {
  add as setThumb,
  remove as unThumb,
  getQuestionThumbByArticleIdAndUserId
} from '@/api/question-thumb'
import { add as setSubscribe, remove as unSubscribe, getByUserIdAndFollowId } from '@/api/follow'
import { set } from '@/api/history-question'

const router = useRouter()
const store = useUserStore()
const userId = store.id
const route = useRoute()
const postContent = ref('')

const data = ref()
const collectData = ref()
const thumbData = ref()
const commentData = ref([])

const current = ref(1)
const total = ref(0)
const params = reactive({
  pageNum: 1,
  pageSize: 15
})

const followId = ref()

onMounted(async () => {
  await get({ id: route.params.id }).then((res) => {
    data.value = res.data
    checkFollow()
  })

  const parser = new DOMParser()
  const doc = parser.parseFromString(data.value.content, 'text/html')
  const headings = doc.querySelectorAll('h1, h2, h3, h4, h5, h6')
  const headingIds = {}
  const tagCounts = {}
  headings.forEach((heading) => {
    const tagName = heading.tagName.toLowerCase()
    const baseId = `toc-${tagName}`

    tagCounts[tagName] = !tagCounts[tagName] ? 1 : tagCounts[tagName] + 1

    const id = tagCounts[tagName] > 1 ? `${baseId}-${tagCounts[tagName]}` : baseId

    headingIds[id] = true
    heading.setAttribute('id', id)
  })

  document.querySelector('.question-text-top ').innerHTML = doc.body.innerHTML

  getCollect()
  getQuestioinThumb()
  queryComment()
  logQuestionHistory()
})

//get
const checkFollow = () => {
  const params = {
    userId: store.id,
    followUserId: data.value.userId
  }
  getByUserIdAndFollowId(params).then((res) => {
    followId.value = res.data.id
  })
}

const queryComment = () => {
  const param = {
    pageNum: params.pageNum,
    pageSize: params.pageSize,
    questionId: data.value.id
  }

  getCommentQuestionRootListByQuestionId(param).then((res) => {
    commentData.value = res.data.records
    current.value = res.data.current
    total.value = res.data.total
  })
}

const getQuestioinThumb = () => {
  const params = {
    userId: store.id,
    questionId: data.value.id
  }

  getQuestionThumbByArticleIdAndUserId(params).then((res) => (thumbData.value = res.data))
}

const getCollect = () => {
  const params = {
    userId: store.id,
    questionId: data.value.id
  }
  getCollectByUserAndQuestion(params).then((res) => {
    collectData.value = res.data
  })
}

//delete
const deleteQuestion = () => {
  Modal.confirm({
    title: `确定要删除问答吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '问答删除后，不能恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', data.value.id)
      remove(formData).then(() => {
        message.success('删除成功')
        router.push('/question')
      })
    }
  })
}

//history
const logQuestionHistory = () => {
  if (!userId) {
    return
  }
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('questionId', route.params.id)
  set(formData)
}

//comment
const postComment = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('questionId', data.value.id)
  formData.append('content', postContent.value)
  add(formData).then(() => {
    message.success('发布成功')
    queryComment()
    postContent.value = ''
  })
}

//collection
const collectQuestion = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('questionId', data.value.id)
  collect(formData).then(() => getCollect())
}

const unCollectQuestion = () => {
  const formData = new FormData()
  formData.append('id', collectData.value.id)
  unCollect(formData).then(() => {
    collectData.value = null
  })
}

//thumb
const thumbQuestion = () => {
  if (!userId) {
    message.warning('请先登录')
    return router.push('/login')
  }
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('questionId', data.value.id)
  setThumb(formData).then(() => {
    getQuestioinThumb()
  })
}

const unThumbQuestion = () => {
  const formData = new FormData()
  formData.append('id', thumbData.value.id)
  unThumb(formData).then(() => {
    thumbData.value = null
  })
}

//follow
const setUserSubscribe = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('followUserId', data.value.userId)
  setSubscribe(formData).then(() => checkFollow())
}

const removeUserSubscribe = () => {
  const formData = new FormData()
  formData.append('id', followId.value)
  unSubscribe(formData).then(() => {
    followId.value = null
    console.log(followId.value)
  })
}

//other
const shareQuesion = () => {
  navigator.clipboard
    .writeText(window.location.href)
    .then(() => {
      message.success('URL已成功复制到剪贴板: ' + window.location.href)
    })
    .catch(() => {
      message.error('复制失败')
    })
}

const onChange = (pageNumber) => {
  params.pageNum = pageNumber
  queryComment()
  document.querySelector('.comment-box').scrollIntoView(true)
}
</script>

<style scoped>
.main {
  display: flex;
  padding: 15px 0;
}

.sidebar-right {
  width: 250px;
  min-height: 100vh;
  margin: 0 10px 10px 0;
}

.sticky-card {
  position: sticky;
  top: 10px;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.user-card {
  background-color: #ffffff;
  margin-bottom: 10px;
  max-height: 50vh;
}

.user-avatar {
  margin-top: 0 !important;
}

.contents-card {
  background-color: #ffffff;
  margin-bottom: 15px;
  border-radius: 4px;
  flex: 1;
}

.user-title,
.contents-title {
  font-weight: 700;
  font-size: 16px;
  padding: 0 15px;
  border-bottom: 1px solid #f4f4f4;
  height: 54px;
  display: flex;
  align-items: center;
}

.title-card {
  position: sticky;
  top: 0;
}

.user-content {
  padding: 15px;
}

.user-box {
  display: flex;
  justify-content: space-between;
}

.user-name {
  color: #666;
}
.user-name:hover {
  color: #4d45e5;
  cursor: pointer;
}

.container {
  flex: 1;
  margin: 0 15px;
  border-radius: 4px;
  position: relative;
}

.content {
  background-color: #ffffff;
  padding: 30px 30px 0 30px;
  border-radius: 4px;
}

.title h1 {
  font-weight: 500;
  margin: 0;
  font-size: 24px;
}

.item-message {
  height: 40px;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.item-message,
.item-message a {
  color: #666;
}

.item-message a:hover {
  color: #4d45e5;
}

.item-message span {
  margin-bottom: 0;
}

.ant-divider-vertical {
  margin: 0 15px;
}

.question-text-footer {
  margin: 15px 0;
}

.tags-line {
  margin-bottom: 16px;
}

.tags-line .tag {
  font-size: 14px;
  margin-right: 10px;
}

.question-heart {
  text-align: center;
}

.praise-box {
  width: 50px;
  height: 50px;
  margin: auto;
  border-radius: 50%;
  border: 1px solid #ccc;
  cursor: pointer;
  margin-bottom: 10px;
}

.question-action-box {
  display: flex;
  justify-content: space-between;
  padding-bottom: 15px;
}

.action-right {
  display: flex;
  flex-direction: row;
}

.action-hover span {
  margin: 0 5px;
  padding: 10px;
  line-height: 0;
  border-radius: 4px;
  background-color: #f1f1ff;
}

.action-hover a {
  color: #000000;
}

.action-hover a:hover {
  color: #4d45e5;
  cursor: pointer;
}

.comment-box {
  padding: 30px;
  background-color: #ffffff;
  border-radius: 4px;
}

.comment-box .button-box {
  text-align: right;
  margin: 20px 0;
}

.item-active {
  background-color: #f7f7ff;
  color: #000;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

:global(.question-text-top > img) {
  max-width: 100% !important;
}

:global(.question-text-top > table) {
  margin: 0 auto;
  border-collapse: collapse;
  margin-bottom: 15px;
  font-size: 15px;
}

:global(.question-text-top tr:first-child) {
  background-color: #eaeef2 !important;
  font-weight: 700;
}

:global(.question-text-top tr:nth-child(odd)) {
  background-color: #f6f8fa;
}

:global(.question-text-top td) {
  border: 1px solid #ccc;
  padding: 5px 2px;
}

:global(.question-text-top p),
:global(.question-text-top li),
:global(.question-text-top span) {
  line-height: 1.8;
  font-size: 16px;
}

:global(.question-text-top h1),
:global(.question-text-top h2),
:global(.question-text-top h3),
:global(.question-text-top h4),
:global(.question-text-top h5),
:global(.question-text-top h6) {
  margin: 0 0 15px;
}

:global(.question-text-top pre) {
  max-width: 670px;
  background-color: #f5f2f0;
  padding: 10px;
  border-radius: 4px 4px;
  display: block;
  font-size: 14px;
  border-color: #e8e8e8;
  text-indent: 0;
  margin-bottom: 15px;
  overflow: scroll;
}

:global(.question-text-top pre code) {
  word-wrap: normal;
  font-family:
    Consolas,
    Monaco,
    Andale Mono,
    Ubuntu Mono,
    monospace;
  -webkit-hyphens: none;
  hyphens: none;
  line-height: 1.5;
  margin: 0.5em 0;
  overflow: auto;
  padding: 1em;
  -moz-tab-size: 4;
  -o-tab-size: 4;
  tab-size: 4;
  text-align: left;
  text-shadow: 0 1px #fff;
  white-space: pre;
  word-break: normal;
  word-spacing: normal;
}

@media screen and (max-width: 800px) {
  .sidebar-right {
    display: none;
  }

  .title h1 {
    font-size: 22px;
  }
}

@media screen and (max-width: 700px) {
  .title h1 {
    font-size: 20px;
  }

  .item-message,
  .tags-line {
    font-size: 12px;
  }

  .action-left p {
    display: none;
  }

  :global(.question-text-top p),
  :global(.question-text-top li),
  :global(.question-text-top span) {
    line-height: 1.4;
    font-size: 14px;
  }

  :global(.article-text-top pre) {
    max-width: 420px;
  }
}
</style>
