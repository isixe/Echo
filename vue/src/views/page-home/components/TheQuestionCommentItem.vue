<template v-if="comment">
  <div class="comment-main-container root-info group-main">
    <div class="info-message">
      <template v-if="reactionData && reactionData.status">
        <a @click="removeCommentReaction()"
          ><CaretUpFilled style="font-size: 24px; color: #4d45e5"
        /></a>
      </template>
      <template v-else>
        <a @click="likeComment(comment.id)"><CaretUpOutlined style="font-size: 24px" /></a>
      </template>
      <p>{{ comment.likeCount - comment.dislikeCount }}</p>
      <template v-if="reactionData && reactionData.status != null && !reactionData.status">
        <a @click="removeCommentReaction()"
          ><CaretDownFilled style="font-size: 24px; color: #4d45e5"
        /></a>
      </template>
      <template v-else>
        <a @click="dislikeComment(comment.id)"><CaretDownOutlined style="font-size: 24px" /></a>
      </template>
    </div>
    <div class="comment-message">
      <router-link :to="'/user/' + comment.userId">
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
      </router-link>
    </div>
    <div class="comment-user-info">
      <div class="base-info">
        <div>
          <router-link :to="'/user/' + comment.userId">
            <span class="user-name g-hover"
              >{{ comment.userName }}
              <span v-show="comment.userId == authorId" style="color: #ccc">[作者]</span>
            </span>
          </router-link>
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
          <span class="comment-time">{{ comment.createdTime }}</span>
          <template v-if="store.id == comment.userId">
            <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
            <a class="comment-delete" @click="deleteComment(comment.id)">删除</a>
          </template>
        </div>
        <div>
          <a class="comment-switch"
            ><span @click="() => (showRootReplay = !showRootReplay)"> <MessageOutlined /> </span
          ></a>
        </div>
      </div>
      <div class="ex-info">
        <p>{{ comment.content }}</p>
      </div>
      <div class="footer-info">
        <div class="replay-root-info-box" v-show="showRootReplay">
          <a-textarea v-model:value="rootContent" :autoSize="true" />
          <div class="button-box">
            <a-button class="relay_commetn_btn" @click="postRootComment(comment.id)">回复</a-button>
          </div>
        </div>
      </div>

      <template v-for="child in childComments" :key="child.id">
        <the-question-comment-child-item
          :child="child"
          :comment="comment"
          :authorId="authorId"
          @onChildCommentUpdate="queryChildCommentList()"
        ></the-question-comment-child-item>
      </template>
    </div>
  </div>
</template>
<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { add, remove, getCommentQuestionChildListByRootId } from '@/api/question-comment'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import {
  add as setReaction,
  update as updateReaction,
  remove as removeReaction,
  getByCommentQuestionIdAndUserId
} from '@/api/reaction-comment-question'
import { TheQuestionCommentChildItem } from '../components'

const router = useRouter()
const store = useUserStore()
const props = defineProps(['comment'])
const { comment } = toRefs(props)
const authorId = defineModel()
const emit = defineEmits(['onCommentUpdate'])

const childComments = ref()
const showRootReplay = ref(false)
const rootContent = ref('')

const reactionData = ref(0)

onMounted(() => {
  queryChildCommentList()
  queryReactionData()
})

const queryReactionData = () => {
  const param = {
    userId: store.id,
    commentQuestionId: comment.value.id
  }
  getByCommentQuestionIdAndUserId(param).then((res) => {
    reactionData.value = res.data
  })
}

const queryChildCommentList = () => {
  getCommentQuestionChildListByRootId({ rootId: comment.value.id }).then((res) => {
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
  if (!store.id) {
    message.warning('请先登录')
    return router.push('/login')
  }
  formData.append('userId', store.id)
  formData.append('rootCommentId', comment.value.id)
  formData.append('questionId', comment.value.questionId)
  add(formData).then(() => {
    message.success('回复成功')
    emit('onCommentUpdate')
    rootContent.value = ''
    childContent.value = ''
    queryChildCommentList()
  })
}

const likeComment = (commentId) => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('commentQuestionId', commentId)
  formData.append('status', 1)

  if (reactionData.value.status == null) {
    setReaction(formData).then(() => {
      queryReactionData()
      comment.value.likeCount += 1
    })
    return
  }

  formData.append('id', reactionData.value.id)
  updateReaction(formData).then(() => {
    queryReactionData()
    comment.value.likeCount += 1
  })

  if (!reactionData.value.status) {
    comment.value.dislikeCount -= 1
  }
}

const dislikeComment = (commentId) => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('commentQuestionId', commentId)
  formData.append('status', 0)

  if (reactionData.value.status == null) {
    setReaction(formData).then(() => {
      queryReactionData()
      comment.value.dislikeCount += 1
    })
    return
  }

  formData.append('id', reactionData.value.id)
  updateReaction(formData).then(() => {
    queryReactionData()
    comment.value.dislikeCount += 1
  })

  if (reactionData.value.status) {
    comment.value.likeCount -= 1
  }
}

const removeCommentReaction = () => {
  const formData = new FormData()
  formData.append('id', reactionData.value.id)
  removeReaction(formData).then(() => {
    queryReactionData()

    if (reactionData.value.status) {
      comment.value.likeCount -= 1
      return
    }

    comment.value.dislikeCount -= 1
  })
}
</script>

<style scoped>
.comment-main-container {
  border-bottom: 1px solid #f4f4f4;
  display: flex;
  padding: 15px 0;
}

.info-message {
  display: flex;
  margin-right: 10px;
  flex-direction: column;
  align-items: center;
}

.info-message a {
  color: #ccc;
}

.info-message a:hover {
  color: #4d45e5;
}

.comment-user-info {
  flex: 1;
  font-size: 15px;
}

.base-info {
  color: #ccc;
  margin-bottom: 5px;
  display: flex;
  justify-content: space-between;
}

.comment-switch {
  color: #666;
}

.comment-switch:hover {
  color: #4d45e5;
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

@media screen and (max-width: 800px) {
  .base-info,
  .ex-info {
    font-size: 14px;
  }
}
</style>
