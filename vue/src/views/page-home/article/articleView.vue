<template>
  <div class="main">
    <div class="sidebar-left">
      <div class="group-card" v-if="data && data.articleGroupName">
        <div class="group-title">合集</div>
        <div class="group-content"></div>
      </div>
      <div class="title-card"></div>
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
              <RouterLink :to="'/user/' + data.userId" class="user-info">
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
              </RouterLink>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <span> <EyeOutlined /> {{ data.pvCount }} </span>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <span>{{ data.updateTime }}</span>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <RouterLink :to="'/category/' + data.categoryId" class="category-container">
                {{ data.category }}
              </RouterLink>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <template v-if="data.userId === userId">
                <RouterLink :to="{ path: '/article/edit', query: { id: data.id } }">
                  编辑
                </RouterLink>
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <a @click="deleteArticle()"> 删除 </a>
              </template>
            </div>
          </div>
        </template>

        <template v-else>
          <div class="featuredPic-thin"></div>
          <div class="title-thin">
            <h1>{{ data.title }}</h1>
            <div class="item-message">
              <RouterLink :to="'/user/' + data.userId" class="user-info">
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
              </RouterLink>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <span> <EyeOutlined /> {{ data.pvCount }} </span>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <span>{{ data.updateTime }}</span>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <RouterLink :to="'/category/' + data.categoryId" class="category-container">
                {{ data.category }}
              </RouterLink>
              <template v-if="data.userId === userId">
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <RouterLink :to="{ path: '/article/edit', query: { id: data.id } }">
                  编辑
                </RouterLink>
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <a> 删除 </a>
              </template>
            </div>
          </div>
        </template>
      </div>
      <div class="content">
        <div class="content-top" v-html="data.content"></div>
        <div class="content-footer">
          <div class="tags-line">
            <span class="tag-title">文章标签：</span>
            <template v-if="data.tag">
              <a-tag
                class="tag"
                v-for="tag in data.tag.split(',')"
                :key="tag"
                :color="tag.length < 3 ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
              >
                <a href="/tag?name=tag" target="_blank">{{ tag }}</a>
              </a-tag>
            </template>
            <template v-else> 暂无标签 </template>
          </div>

          <div class="article-heart">
            <div class="praise-box">
              <LikeOutlined style="font-size: 28px; color: #ccc" />
            </div>
            <p class="pv-box">{{ data.pvCount }} 人已点赞</p>
            <div class="posts-affix-box">
              <div class="article-action-box">
                <div class="action-left">
                  <p>{{ data.title }}</p>
                </div>
                <div class="action-right">
                  <span class="action-hover"><ShareAltOutlined /></span>
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
            >发布评论</a-button
          >
        </div>
        <div>
          <template v-for="comment in commentData" :key="comment">
            <article-comment-item
              :comment="comment"
              v-model="data.userId"
              @onCommentUpdate="queryComment()"
            />
          </template>
        </div>
      </div>
    </div>

    <div class="sidebar-right"></div>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { get } from '@/api/article'
import { remove } from '@/api/article'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { add, getCommentArticleRootList } from '@/api/articleComment'
import { useUserStore } from '@/stores/user'
import { ArticleCommentItem } from '../components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const store = useUserStore()
const userId = store.id
const route = useRoute()
const data = ref()
const postContent = ref()
const commentData = ref()

onMounted(async () => {
  await get({ id: route.params.id }).then((res) => {
    data.value = res.data
  })

  queryComment()
})

const queryComment = () => {
  getCommentArticleRootList({ articleId: data.value.id }).then((res) => {
    commentData.value = res.data
  })
}

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

const postComment = () => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('articleId', data.value.id)
  formData.append('content', postContent.value)
  add(formData).then(() => {
    message.success('发布成功')
    queryComment()
  })
}
</script>

<style scoped>
.main {
  display: flex;
}

.sidebar-left,
.sidebar-right {
  width: 250px;
  min-height: 100vh;
  margin: 10px;
}

.group-card,
.title-card {
  background-color: #ffffff;
  margin-bottom: 15px;
  border-radius: 4px;
  height: 200px;
}

.group-title {
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

.container {
  flex: 1;
  margin: 10px auto;
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

.tags-line .tag {
  font-size: 12px;
  margin-right: 10px;
}

.article-heart {
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

.article-action-box {
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
