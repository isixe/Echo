<template>
  <div class="main">
    <div class="sidebar-left" v-if="data">
      <div class="sticky-card">
        <div class="group-card" v-if="data.articleGroupName">
          <div class="group-title">合集</div>
          <div class="group-content" v-show="groupArticleList">
            <div v-for="article in groupArticleList" :key="article.id">
              <template v-if="data && data.id === article.id">
                <a :href="'/article/' + article.id">
                  <div class="group-item item-active">
                    <span>
                      <BookOutlined style="margin-right: 3px; margin-top: 5px" />
                    </span>
                    <span>
                      {{ article.title }}
                    </span>
                  </div>
                </a>
              </template>
              <template v-else>
                <a :href="'/article/' + article.id">
                  <div class="group-item">
                    <BookOutlined style="margin-right: 3px; margin-top: 5px" />
                    {{ article.title }}
                  </div>
                </a>
              </template>
            </div>
          </div>
        </div>
        <div class="contents-card">
          <div class="contents-title">目录</div>
          <div class="contents-content">
            <div id="toc-content"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="container" v-if="data">
      <div>
        <template v-if="data.featuredPic">
          <div class="featuredPic">
            <img :src="data.featuredPic" alt="头图" />
          </div>
          <div class="title">
            <div>
              <h1>{{ data.title }}</h1>
            </div>
            <div class="item-message">
              <router-link :to="'/user/' + data.userId" class="user-info">
                <a-avatar
                  class="user-avatar"
                  :src="data.avatar"
                  :size="20"
                  :style="{ marginRight: '10px', marginTop: '-3px' }"
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
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <template v-if="data.userId === userId">
                <router-link :to="{ path: '/article/edit', query: { id: data.id } }">
                  <EditOutlined /> 编辑
                </router-link>
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <a @click="deleteArticle()"> <DeleteOutlined /> 删除 </a>
              </template>
            </div>
          </div>
        </template>

        <template v-else>
          <div class="featuredPic-thin"></div>
          <div class="title-thin">
            <h1>{{ data.title }}</h1>
            <div class="item-message">
              <router-link :to="'/user/' + data.userId" class="user-info">
                <a-avatar
                  class="user-avatar"
                  :src="data.avatar"
                  :size="20"
                  :style="{ marginRight: '10px', marginTop: '-3px' }"
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
              <template v-if="data.userId === userId">
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <router-link :to="{ path: '/article/edit', query: { id: data.id } }">
                  <EditOutlined /> 编辑
                </router-link>
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <a @click="deleteArticle()"> <DeleteOutlined /> 删除 </a>
              </template>
            </div>
          </div>
        </template>
      </div>
      <div class="content">
        <div class="article-text-top"></div>
        <div class="article-text-footer">
          <div class="tags-line">
            <span class="tag-title">文章标签：</span>
            <template v-if="data.tag">
              <a-tag
                class="tag"
                v-for="tag in data.tag.split(',')"
                :key="tag"
                :color="tag.length < 3 ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
              >
                <router-link
                  :to="{ path: '/tag', query: { tagName: tag, tab: 'article' } }"
                  target="_blank"
                  >{{ tag }}</router-link
                >
              </a-tag>
            </template>
            <template v-else> 暂无标签 </template>
          </div>

          <div class="article-heart">
            <div class="praise-box" v-if="thumbData && thumbData.id" @click="unThumbArticle()">
              <LikeFilled style="font-size: 28px; color: #4d45e5" />
            </div>
            <div class="praise-box" v-else>
              <LikeOutlined @click="thumbArticle()" style="font-size: 28px; color: #ccc" />
            </div>
            <p class="pv-box">{{ thumbCount }} 人已点赞</p>
            <div class="posts-affix-box">
              <div class="article-action-box">
                <div class="action-left">
                  <p>{{ data.title }}</p>
                </div>
                <div class="action-right">
                  <span class="action-hover"
                    ><a @click="shareArticle()"><ShareAltOutlined /></a
                  ></span>
                  <span class="action-hover">
                    <a v-if="collectData && collectData.id" @click="unCollectArticle()">
                      <StarFilled style="color: #4d45e5" />
                    </a>
                    <a v-else @click="collectArticle()">
                      <StarOutlined />
                    </a>
                  </span>
                  <span class="action-hover">
                    <a v-if="thumbData && thumbData.id" @click="unThumbArticle()">
                      <LikeFilled style="color: #4d45e5" />
                    </a>
                    <a v-else @click="thumbArticle()">
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
            >发布评论</a-button
          >
        </div>
        <div>
          <template v-for="comment in commentData" :key="comment">
            <the-article-comment-item
              :comment="comment"
              v-model="data.userId"
              @onCommentUpdate="queryComment()"
            >
            </the-article-comment-item>
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
                  class="user-subscribe-btn"
                  type="primary"
                  style="background-color: #4d45e5; border-radius: 3px"
                  @click="setUserSubscribe()"
                  >关注</a-button
                >
                <a-button
                  v-else-if="followId"
                  class="user-subscribe-btn"
                  type="primary"
                  style="background-color: #ccc; border-radius: 3px"
                  @click="removeUserSubscribe()"
                  >取消关注</a-button
                >
              </template>
              <template v-else>
                <a-button
                  class="user-subscribe-btn"
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
import tocbot from 'tocbot'
import { createVNode } from 'vue'
import { get, remove, getArticleListByGroupId } from '@/api/article'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { add, getCommentArticleRootListByArticleId } from '@/api/article-comment'
import {
  add as setCollect,
  remove as unCollect,
  getCollectByUserAndArticle
} from '@/api/collection-article'
import {
  add as setThumb,
  remove as unThumb,
  getTotalArticleThumbByArticleId,
  getArticleThumbByArticleIdAndUserId
} from '@/api/article-thumb'
import { useUserStore } from '@/stores/user'
import { TheArticleCommentItem } from '../components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { add as setSubscribe, remove as unSubscribe, getByUserIdAndFollowId } from '@/api/follow'
import { set } from '@/api/history-article'

const router = useRouter()
const route = useRoute()

const store = useUserStore()
const userId = store.id
const postContent = ref('')

const data = ref()
const groupArticleList = ref([])
const thumbCount = ref(0)
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

tocbot.init({
  tocSelector: '#toc-content',
  contentSelector: '.article-text-top',
  headingSelector: 'h1, h2, h3, h4, h5, h6'
})

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

  document.querySelector('.article-text-top').innerHTML = doc.body.innerHTML
  tocbot.refresh()

  getArticleListByGroupId({ groupId: data.value.articleGroupId }).then((res) => {
    groupArticleList.value = res.data
  })

  getArticleThumbCount()
  getCollect()
  getArticleThumb()
  queryComment()
  logArticleHistory()
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

const getArticleThumbCount = () => {
  getTotalArticleThumbByArticleId({ articleId: data.value.id }).then((res) => {
    thumbCount.value = res.data
  })
}

const getCollect = () => {
  const params = {
    userId: store.id,
    articleId: data.value.id
  }
  getCollectByUserAndArticle(params).then((res) => (collectData.value = res.data))
}

const getArticleThumb = () => {
  const params = {
    userId: store.id,
    articleId: data.value.id
  }
  getArticleThumbByArticleIdAndUserId(params).then((res) => (thumbData.value = res.data))
}

const queryComment = () => {
  const param = {
    pageNum: params.pageNum,
    pageSize: params.pageSize,
    articleId: data.value.id
  }
  getCommentArticleRootListByArticleId(param).then((res) => {
    commentData.value = res.data.records
    current.value = res.data.current
    total.value = res.data.total
  })
}

//delete
const deleteArticle = () => {
  Modal.confirm({
    title: `确定要删除文章吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '文章删除后，不能恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', data.value.id)
      remove(formData).then(() => {
        message.success('删除成功')
        router.push('/article')
      })
    }
  })
}

//history
const logArticleHistory = () => {
  if (!userId) {
    return
  }
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('articleId', route.params.id)
  set(formData)
}

//comment
const postComment = () => {
  if (!userId) {
    message.warning('请先登录')
    return router.push('/login')
  }

  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('articleId', data.value.id)
  formData.append('content', postContent.value)
  add(formData).then(() => {
    message.success('发布成功')
    queryComment()
    postContent.value = null
  })
}

//collection
const collectArticle = () => {
  if (!userId) {
    message.warning('请先登录')
    return router.push('/login')
  }
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('articleId', data.value.id)
  setCollect(formData).then(() => getCollect())
}

const unCollectArticle = () => {
  const formData = new FormData()
  formData.append('id', collectData.value.id)
  unCollect(formData).then(() => {
    collectData.value = null
  })
}

//thumb
const thumbArticle = () => {
  if (!userId) {
    message.warning('请先登录')
    return router.push('/login')
  }
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('articleId', data.value.id)
  setThumb(formData).then(() => {
    getArticleThumbCount()
    getArticleThumb()
  })
}

const unThumbArticle = () => {
  const formData = new FormData()
  formData.append('id', thumbData.value.id)
  unThumb(formData).then(() => {
    thumbData.value = null
    getArticleThumbCount()
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
  })
}

//other
const shareArticle = () => {
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
  padding: 10px 0;
}

.sidebar-left,
.sidebar-right {
  width: 250px;
  min-height: 100vh;
  margin: 0 10px;
}

.sticky-card {
  position: sticky;
  top: 10px;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.group-card,
.user-card {
  background-color: #ffffff;
  margin-bottom: 10px;
  max-height: 50vh;
}

.contents-card {
  background-color: #ffffff;
  border-radius: 4px;
  flex: 1;
}

.group-title,
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
  border-radius: 4px;
  position: relative;
}

.content {
  background-color: #ffffff;
  padding: 30px 30px 0 30px;
  border-radius: 4px;
}

.title {
  width: 100%;
  height: 165px;
  background-color: rgba(241, 241, 255, 0.829);
  padding: 0 30px;
  position: absolute;
  top: 140px;
  word-break: break-all;
  display: flex;
  flex-direction: column;
}

.title div:first-child {
  height: 110px;
  display: flex;
  align-items: center;
}

.content > :global(.content img) {
  max-width: 100% !important;
}

.featuredPic {
  height: 300px;
  background-color: #ffffff;
}

.featuredPic img {
  width: 100%;
  padding: 10px;
  margin: auto;
  height: 300px;
  object-fit: cover;
  border-radius: 15px;
}

.featuredPic-thin {
  height: 170px;
  background-color: #ffffff;
}

.title-thin {
  width: 100%;
  height: 120px;
  background-color: rgba(241, 241, 255, 0.829);
  padding: 0 30px;
  position: absolute;
  top: 40px;
}

.item-message {
  height: 40px;
  display: flex;
  align-items: center;
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

.article-text-footer {
  margin: 15px 0;
}

.tags-line .tag {
  font-size: 12px;
  margin-right: 10px;
}

.article-heart {
  text-align: center;
}

.tags-line {
  margin-bottom: 15px;
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

.pv-box {
  margin-bottom: 30px;
}

.praise-box span {
  margin-top: 8px;
}

.article-action-box {
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
  padding: 30px 30px 20px 30px;
  background-color: #ffffff;
  border-radius: 4px;
}

.comment-box .button-box {
  text-align: right;
  margin: 20px 0;
}

.group-content {
  padding: 5px 15px 10px 15px;
  font-size: 14px;
  overflow: scroll;
  max-height: 360px;
}

.group-item {
  padding: 5px 5px 5px 0;
  display: flex;
  flex-direction: row;
}

.item-active {
  background-color: #f7f7ff;
  color: #000;
}

.group-content a {
  color: #666;
}

.group-content a:hover {
  color: #4d45e5;
}

.group-item a:hover {
  color: #4d45e5;
}

.group-item:hover {
  background-color: #f7f7ff;
}

#toc-content {
  padding: 0 15px 15px 15px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

.action-left {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

:global(ol.toc-list) {
  padding-left: 20px;
}

:global(#toc-content > ol > li a) {
  color: #666;
}

:global(#toc-content > ol > li a:hover) {
  color: #4d45e5;
}

:global(#toc-content > ol > li.toc-list-item.is-active-li > a) {
  color: #4d45e5;
}

:global(#toc-content > ol > li.toc-list-item) {
  padding: 3px 0;
}

:global(#toc-content > ol > li.toc-list-item.is-active-li) {
  background: #f7f7ff;
}

:global(.article-text-top > table) {
  margin: 0 auto;
  border-collapse: collapse;
  margin-bottom: 15px;
  font-size: 15px;
}

:global(.article-text-top tr:first-child) {
  background-color: #eaeef2 !important;
  font-weight: 700;
}

:global(.article-text-top tr:nth-child(odd)) {
  background-color: #f6f8fa;
}

:global(.article-text-top td) {
  border: 1px solid #ccc;
  padding: 5px 2px;
}

:global(.article-text-top p),
:global(.article-text-top li),
:global(.article-text-top span) {
  line-height: 1.8;
  margin: 0 0 15px;
  font-size: 16px;
}

:global(.article-text-top h1),
:global(.article-text-top h2),
:global(.article-text-top h3),
:global(.article-text-top h4),
:global(.article-text-top h5),
:global(.article-text-top h6) {
  margin: 0 0 15px;
}

:global(.article-text-top pre) {
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

:global(.article-text-top pre code) {
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

@media screen and (max-width: 1200px) {
  .sidebar-left {
    display: none;
  }

  .container {
    margin-left: 10px;
  }
}

@media screen and (max-width: 800px) {
  .sidebar-right {
    display: none;
  }

  .title h1,
  .title-thin h1 {
    font-size: 22px;
  }

  .item-message {
    font-size: 14px;
  }

  .tags-line,
  .action-left p,
  .pv-box {
    font-size: 16px;
  }

  .main {
    display: block;
  }
}

@media screen and (max-width: 700px) {
  .title h1,
  .title-thin h1 {
    font-size: 20px;
  }

  .item-message {
    font-size: 12px;
  }

  .tags-line,
  .action-left p,
  .pv-box {
    font-size: 14px;
  }

  .praise-box {
    width: 45px;
    height: 45px;
  }

  .item-message {
    flex-wrap: wrap;
  }

  .title {
    height: 120px;
    top: 90px;
  }

  .title div:first-child {
    height: 60px;
  }

  .featuredPic {
    height: 200px;
  }

  .featuredPic img {
    height: 200px;
  }

  :global(.article-text-top pre) {
    max-width: 420px;
  }

  :global(.article-text-top p),
  :global(.article-text-top li),
  :global(.article-text-top span) {
    line-height: 1.4;
    margin: 0 0 10px;
    font-size: 14px;
  }
}
</style>
