<template>
  <div class="main">
    <div class="container" v-if="data">
      <div class="content">
        <div class="title">
          <h1>{{ data.title }}</h1>
          <div class="item-message">
            <RouterLink :to="'/user/' + data.userId" class="user-info">
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
            </RouterLink>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <span> <EyeOutlined /> {{ data.pvCount }} </span>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <span>{{ data.updateTime }}</span>
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <RouterLink :to="'/category/' + data.categoryId" class="category-container">
              <FolderOutlined /> {{ data.category }}
            </RouterLink>
            <template v-if="data.userId == userId">
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <RouterLink :to="{ path: '/question/edit', query: { id: data.id } }">
                <EditOutlined /> 编辑
              </RouterLink>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <a @click="deleteQuestion()"><DeleteOutlined /> 删除 </a>
            </template>
          </div>
        </div>
        <div class="content-top"></div>
        <div class="content-footer">
          <div class="tags-line">
            <span class="tag-title">问题标签：</span>
            <template v-if="data.tag">
              <a-tag
                class="tag"
                v-for="tag in data.tag.split(',')"
                :key="tag"
                :color="tag.length < 3 ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
              >
                <RouterLink :to="{ path: '/tag', query: { tagName: tag } }" target="_blank">{{
                  tag
                }}</RouterLink>
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
                  <span class="action-hover"><StarOutlined /></span>
                  <span class="action-hover"><LikeOutlined /></span>
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
            <question-comment-item
              :comment="comment"
              v-model="data.userId"
              @onCommentUpdate="queryComment()"
            />
          </template>
        </div>
      </div>
    </div>

    <div class="sidebar-right">
      <div class="sticky-card">
        <div class="user-card" v-if="data && data.userId">
          <div class="user-title">关于作者</div>
          <div class="user-content">
            <div class="user-box">
              <RouterLink :to="'/user/' + data.userId" class="user-info">
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
              </RouterLink>
              <a-button
                class="user-subscribe-btn"
                type="primary"
                style="background-color: #4d45e5; border-radius: 3px"
                >关注</a-button
              >
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
import { add, getCommentQuestionRootList } from '@/api/questionComment'
import { useUserStore } from '@/stores/user'
import { QuestionCommentItem } from '../components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const store = useUserStore()
const userId = store.id
const route = useRoute()
const data = ref()
const postContent = ref('')
const commentData = ref([])

onMounted(async () => {
  await get({ id: route.params.id }).then((res) => {
    data.value = res.data
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

  document.querySelector('.content-top').innerHTML = doc.body.innerHTML
  queryComment()
})

const queryComment = () => {
  getCommentQuestionRootList({ questionId: data.value.id }).then((res) => {
    commentData.value = res.data
  })
}

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

const postComment = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('questionId', data.value.id)
  formData.append('content', postContent.value)
  add(formData).then(() => {
    message.success('发布成功')
    queryComment()
  })
}

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
</script>

<style scoped>
.main {
  display: flex;
}

.sidebar-right {
  width: 250px;
  min-height: 100vh;
  margin: 10px;
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
  margin: 10px 0 10px 10px;
  border-radius: 4px;
  position: relative;
}

.content {
  background-color: #ffffff;
  padding: 30px 30px 0 30px;
  border-radius: 4px;
}

.content > :global(.content img) {
  max-width: 100% !important;
}

.title h1 {
  font-weight: 500;
}

.item-message {
  height: 40px;
}

.item-message,
.item-message a {
  color: #666;
}

.item-message a:hover {
  color: #4d45e5;
}

.ant-divider-vertical {
  margin: 0 15px;
}

.content-footer {
  margin: 15px 0;
}

.tags-line {
  margin-bottom: 15px;
}

.tags-line .tag {
  font-size: 12px;
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
</style>

<style>
.content table {
  margin: 0 auto;
  border-collapse: collapse;
  margin-bottom: 15px;
  font-size: 15px;
}

.content tr:first-child {
  background-color: #eaeef2 !important;
  font-weight: 700;
}

.content tr:nth-child(odd) {
  background-color: #f6f8fa;
}

.content td {
  border: 1px solid #ccc;
  padding: 5px 2px;
}

.content p,
.content li,
.content span {
  line-height: 1.8;
  margin: 0 0 15px;
  font-size: 16px;
}

.content h1,
.content h2,
.content h3,
.content h4,
.content h5,
.content h6 {
  margin: 0 0 15px;
}

.content pre {
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

.content pre code {
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
</style>
