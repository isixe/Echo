<template v-if="comment">
  <div class="comment-main-container root-info group-main">
    <div class="comment-message">
      <RouterLink :to="'/user/' + comment.userId">
        <a-avatar
          class="user-avatar"
          :src="comment.avatar"
          :size="30"
          :style="{ marginRight: '10px', marginTop: '-3px' }"
        >
          <template #icon>
            <UserOutlined />
          </template>
        </a-avatar>
      </RouterLink>
    </div>
    <div class="comment-user-info">
      <div class="base-info">
        <RouterLink :to="'/user/' + comment.userId">
          <span class="user-name g-hover"
            >{{ comment.userName }}
            <span v-show="comment.userId == authorId" style="color: #ccc">[作者]</span>
          </span>
        </RouterLink>
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
        <span class="comment-time">{{ comment.createdTime }}</span>
        <template v-if="store.id == comment.userId">
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
          <a class="comment-delete" @click="deleteComment(comment.id)">删除</a>
        </template>
      </div>
      <div class="ex-info">
        <p>{{ comment.content }}</p>
      </div>
      <div class="footer-info">
        <a @click="likeComment(comment.id)"
          ><span> <LikeOutlined /> {{ comment.likeCount }} </span></a
        >
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
        <a @click="dislikeComment(comment.id)"
          ><span> <DislikeOutlined /> {{ comment.dislikeCount }} </span></a
        >
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
        <a
          ><span @click="() => (showRootReplay = !showRootReplay)"> <MessageOutlined /> 0 </span></a
        >
        <div class="replay-root-info-box" v-show="showRootReplay">
          <a-textarea v-model:value="rootContent" :autoSize="true" />
          <div class="button-box">
            <a-button class="relay_commetn_btn" @click="postRootComment(comment.id)">回复</a-button>
          </div>
        </div>
      </div>

      <template v-for="child in childComments" :key="child.id">
        <div class="child-info">
          <div class="comment-message">
            <RouterLink :to="'/user/' + child.userId">
              <a-avatar
                class="user-avatar"
                :src="child.avatar"
                :size="30"
                :style="{ marginRight: '10px', marginTop: '-3px' }"
              >
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
            </RouterLink>
          </div>
          <div class="comment-user-info">
            <div class="base-info">
              <span class="user-name g-hover">
                <RouterLink :to="'/user/' + child.userId">
                  {{ child.userName }}
                  <span v-show="child.userId == authorId" style="color: #ccc">[作者]</span>
                </RouterLink>
                <template v-if="child.parentCommentId && child.userId !== child.parentUserId">
                  <CaretRightOutlined style="color: #ccc" />
                  <RouterLink :to="'/user/' + child.parentUserId">
                    @{{ child.parentUserName }}
                    <span v-show="child.parentUserId == authorId" style="color: #ccc">[作者]</span>
                  </RouterLink>
                </template>
              </span>
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
              <span class="comment-time">{{ child.createdTime }}</span>
              <template v-if="store.id == child.userId">
                <a-divider
                  type="vertical"
                  style="height: 15px; top: 0; background-color: #e1cee7"
                />
                <a class="comment-delete" @click="deleteComment(child.id)">删除</a>
              </template>
            </div>
            <div class="ex-info">
              <p>{{ child.content }}</p>
              <p class="quote-comment" v-show="child.parentCommentId !== comment.id">
                {{ child.parentContent }}
              </p>
            </div>
            <div class="footer-info">
              <a @click="likeComment(child.id)"
                ><span> <LikeOutlined /> {{ child.likeCount }} </span></a
              >
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
              <a @click="dislikeComment(child.id)"
                ><span> <DislikeOutlined /> {{ child.dislikeCount }} </span></a
              >
              <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
              <a
                ><span
                  @click="() => (showChildReplay = showChildReplay == child.id ? 0 : child.id)"
                >
                  <MessageOutlined /> 0
                </span></a
              >
              <div class="replay-child-info-box" v-show="showChildReplay == child.id">
                <a-textarea v-model:value="childContent" :autoSize="true" />
                <div class="button-box">
                  <a-button class="relay_commetn_btn" @click="postChildComment(child.id)"
                    >回复</a-button
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>
<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { add, remove, getCommentArticleChildList } from '@/api/articleComment'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const store = useUserStore()
const props = defineProps(['comment'])
const { comment } = toRefs(props)
const authorId = defineModel()
const emit = defineEmits(['onCommentUpdate'])

const childComments = ref()
const showRootReplay = ref(false)
const showChildReplay = ref(0)
const rootContent = ref('')
const childContent = ref('')

onMounted(() => {
  queryChildCommentList()
})

const queryChildCommentList = () => {
  getCommentArticleChildList({ rootId: comment.value.id }).then((res) => {
    childComments.value = res.data
  })
}

const deleteComment = (id) => {
  Modal.confirm({
    title: `确定要删除评论吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '评论删除后，不能恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      const formData = new FormData()
      formData.append('id', id)
      remove(formData).then(() => {
        message.success('删除成功')
        emit('onCommentUpdate')
      })
    }
  })
}

const postRootComment = (parentId) => {
  const formData = new FormData()
  formData.append('content', rootContent.value)
  formData.append('parentCommentId', parentId)
  postComment(formData)
}

const postChildComment = (childId) => {
  const formData = new FormData()
  formData.append('parentCommentId', childId)
  formData.append('content', childContent.value)
  postComment(formData)
}

const postComment = (formData) => {
  formData.append('userId', store.id)
  formData.append('rootCommentId', comment.value.id)
  formData.append('articleId', comment.value.articleId)
  add(formData).then(() => {
    message.success('回复成功')
    emit('onCommentUpdate')
    rootContent.value = ''
    childContent.value = ''
    queryChildCommentList()
  })
}

const likeComment = (commentId) => {
  alert('支持')
}

const dislikeComment = (commentId) => {
  alert('不支持')
}
</script>

<style scoped>
.comment-main-container {
  border-bottom: 1px solid #f4f4f4;
  display: flex;
  padding: 15px 0;
}

.comment-user-info {
  flex: 1;
  font-size: 15px;
}

.base-info {
  color: #ccc;
  margin-bottom: 5px;
}

.user-name {
  color: #000000;
}

.comment-delete,
.footer-info a {
  color: #ccc;
}

.user-name a {
  color: #000000;
}
.comment-delete:hover,
.footer-info a:hover,
.user-name a:hover {
  color: #4d45e5;
}

.ex-info {
  color: #000000;
  margin-bottom: 5px;
}

.footer-info {
  font-size: 16px;
  color: #ccc;
}

.child-info {
  background-color: #f7f7fc;
  padding: 10px;
  border-radius: 5px;
  display: flex;
}

.replay-root-info-box {
  margin-top: 10px;
  background-color: #f4f4f4;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.replay-child-info-box {
  margin-top: 10px;
  background-color: #ffffff;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.button-box {
  text-align: right;
  margin-top: 4px;
  margin-top: 7px;
}

.relay_commetn_btn {
  border-color: #4d45e5;
  color: #4d45e5;
  text-shadow: none;
  padding: 0px 10px;
  height: 26px;
  border-radius: 3px;
}

.quote-comment {
  padding: 5px;
  margin: 5px 0;
  border-radius: 4px;
  background-color: #e8e8f6;
}
</style>
