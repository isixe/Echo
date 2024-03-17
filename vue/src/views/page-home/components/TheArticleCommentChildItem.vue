<template>
  <div class="child-info">
    <div class="comment-message">
      <router-link :to="'/user/' + child.userId">
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
      </router-link>
    </div>
    <div class="comment-user-info">
      <div class="base-info">
        <span class="user-name g-hover">
          <router-link :to="'/user/' + child.userId">
            {{ child.userName }}
            <span v-show="child.userId == authorId" style="color: #ccc">[作者]</span>
          </router-link>
          <template v-if="child.parentCommentId && child.userId !== child.parentUserId">
            <CaretRightOutlined style="color: #ccc" />
            <router-link :to="'/user/' + child.parentUserId">
              @{{ child.parentUserName }}
              <span v-show="child.parentUserId == authorId" style="color: #ccc">[作者]</span>
            </router-link>
          </template>
        </span>
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
        <span class="comment-time">{{ child.createdTime }}</span>
        <template v-if="store.id == child.userId">
          <a-divider type="vertical" style="height: 15px; top: 0; background-color: #e1cee7" />
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
        <template v-if="reactionData && reactionData.status">
          <a @click="removeCommentReaction()"
            ><span> <LikeFilled style="color: #4d45e5" /> {{ child.likeCount }} </span></a
          >
        </template>
        <template v-else>
          <a @click="likeComment(child.id)"
            ><span> <LikeOutlined /> {{ child.likeCount }} </span></a
          >
        </template>
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
        <template v-if="reactionData && reactionData.status != null && !reactionData.status">
          <a @click="removeCommentReaction()"
            ><span> <DislikeFilled style="color: #4d45e5" /> {{ child.dislikeCount }} </span></a
          >
        </template>
        <template v-else>
          <a @click="dislikeComment(child.id)"
            ><span> <DislikeOutlined /> {{ child.dislikeCount }} </span></a
          >
        </template>
        <a-divider type="vertical" style="height: 15px; top: 0; background-color: #efeeee" />
        <a
          ><span @click="() => (showChildReplay = showChildReplay == child.id ? 0 : child.id)">
            <MessageOutlined /> 0
          </span></a
        >
        <div class="replay-child-info-box" v-show="showChildReplay == child.id">
          <a-textarea v-model:value="childContent" :autoSize="true" />
          <div class="button-box">
            <a-button class="relay_commetn_btn" @click="postChildComment(child.id)">回复</a-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { add, remove } from '@/api/article-comment'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import {
  add as setReaction,
  update as updateReaction,
  remove as removeReaction,
  getByCommentArticleIdAndUserId
} from '@/api/reaction-comment-article'

const store = useUserStore()

const props = defineProps(['child', 'comment', 'authorId'])
const { child, comment, authorId } = toRefs(props)
const emit = defineEmits(['onChildCommentUpdate'])

const childContent = ref('')
const showChildReplay = ref(0)
const reactionData = ref()

onMounted(() => {
  queryReactionData()
})

const queryReactionData = () => {
  const param = {
    userId: store.id,
    commentArticleId: child.value.id
  }
  getByCommentArticleIdAndUserId(param).then((res) => {
    reactionData.value = res.data
  })
}

const postChildComment = (childId) => {
  const formData = new FormData()
  formData.append('userId', store.id)
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
    emit('onChildCommentUpdate')
    childContent.value = ''
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
        emit('onChildCommentUpdate')
      })
    }
  })
}

const likeComment = (commentId) => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('commentArticleId', commentId)
  formData.append('status', 1)

  if (reactionData.value.status == null) {
    setReaction(formData).then(() => {
      queryReactionData()
      emit('onChildCommentUpdate')
    })
    return
  }

  formData.append('id', reactionData.value.id)
  updateReaction(formData).then(() => {
    queryReactionData()
    emit('onChildCommentUpdate')
  })
}

const dislikeComment = (commentId) => {
  const formData = new FormData()
  formData.append('userId', store.id)
  formData.append('commentArticleId', commentId)
  formData.append('status', 0)

  if (reactionData.value.status == null) {
    setReaction(formData).then(() => {
      queryReactionData()
      emit('onChildCommentUpdate')
    })
    return
  }

  formData.append('id', reactionData.value.id)
  updateReaction(formData).then(() => {
    queryReactionData()
    emit('onChildCommentUpdate')
  })
}

const removeCommentReaction = () => {
  const formData = new FormData()
  console.log(reactionData.value.id)
  formData.append('id', reactionData.value.id)
  removeReaction(formData).then(() => {
    queryReactionData()
    emit('onChildCommentUpdate')
  })
}
</script>

<style scope>
.child-info {
  background-color: #f7f7fc;
  padding: 10px;
  border-radius: 5px;
  display: flex;
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
