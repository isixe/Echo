<template>
  <div class="group-container" v-if="data">
    <div class="group-header">
      <div>
        <p class="group-title">{{ data.name }}</p>
        <p class="group-summary">{{ data.description }}</p>
      </div>
      <div class="group-message" v-if="(data.userId = store.id)">
        <a @click="showEditor()"><EditOutlined /> 编辑</a>
        <a @click="deleteGroup(data.id)"><DeleteOutlined /> 删除</a>
      </div>
    </div>
    <div class="group-box-set" v-if="store.id && store.id == data.userId" @click="showPutter()">
      <span><PlusOutlined /> 添加文章</span>
    </div>
    <template v-if="groupArticleData && groupArticleData.length > 0">
      <div class="group-article-list" v-for="item in groupArticleData" :key="item.id">
        <RouterLink :to="'/article/' + item.id" class="entry-item-box">
          <article-entry-item :item="item"></article-entry-item>
        </RouterLink>
        <a
          class="article-remove"
          v-if="store.id && store.id == data.userId"
          @click="removeArticle(item.id)"
          ><DeleteOutlined />&nbsp;移除</a
        >
      </div>
    </template>
    <template v-else> <a-empty style="padding-bottom: 30px" /> </template>
  </div>

  <template>
    <a-modal v-model:open="open" width="700px" title="更新集合信息">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="editData" :rules="rules">
          <a-form-item name="name" label="集合名">
            <a-input
              v-model:value="editData.name"
              placeholder="请输入集合名"
              show-count
              :maxlength="42"
            />
          </a-form-item>
          <a-form-item name="description" label="描述">
            <a-textarea
              v-model:value="editData.description"
              placeholder="请输入描述"
              show-count
              :maxlength="120"
            />
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (open = !open)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="updateGroup"
          >提交</a-button
        >
      </template>
    </a-modal>
  </template>

  <template>
    <a-modal v-model:open="openPutter" width="700px" title="添加文章">
      <div class="form-container">
        <div class="group-article-list" v-for="item in userArticleData" :key="item.id">
          <label>
            <input type="checkbox" :value="item.id" />
            <article-entry-item :item="item"></article-entry-item>
          </label>
        </div>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (openPutter = !openPutter)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="putArticleToGroup()"
          >添加</a-button
        >
      </template>
    </a-modal>
  </template>
</template>

<script setup>
import { createVNode } from 'vue'
import { Modal } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import {
  putArticleGroup,
  removeArticleGroup,
  getUnGroupArticleByUserId,
  getArticleListByGroupId
} from '@/api/article'
import { get, update, remove } from '@/api/articleGroup'
import { ArticleEntryItem } from '@/views/page-home/components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()
const store = useUserStore()

const data = ref()
const groupArticleData = ref()
const userArticleData = ref()

const form = ref()
const loading = ref(false)
const open = ref(false)
const openPutter = ref(false)
const editData = reactive({
  id: null,
  userId: store.id,
  name: '',
  description: ''
})

onMounted(() => {
  getGroupDataSource()
  getArticleDataSource()
})

const getGroupDataSource = () => {
  get({ id: route.params.id })
    .then((res) => {
      data.value = res.data
    })
    .catch(() => {
      router.push('/404')
    })
}

const getArticleDataSource = () => {
  getArticleListByGroupId({ groupId: route.params.id }).then((res) => {
    groupArticleData.value = res.data
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
        router.push('/user/' + data.value.userId)
      })
    }
  })
}

const showEditor = () => {
  open.value = true
  Object.keys(editData).forEach((key) => {
    editData[key] = data.value[key]
  })
}

const updateGroup = () => {
  loading.value = true
  form.value
    .validate()
    .then(async () => {
      const formData = new FormData()
      Object.keys(editData).forEach((key) => {
        formData.append(key, editData[key])
      })
      await update(formData)
        .then(() => {
          message.success('更新成功')
          Object.keys(editData).forEach((key) => {
            editData[key] = ''
          })

          getGroupDataSource()

          loading.value = false
          open.value = false
        })
        .catch(() => {
          loading.value = false
          open.value = false
        })
    })
    .catch(() => {
      loading.value = false
    })
}

const showPutter = () => {
  const params = {
    userId: data.value.userId,
    pageNum: 1,
    pageSize: 10
  }
  getUnGroupArticleByUserId(params).then((res) => {
    userArticleData.value = res.data.records
  })

  openPutter.value = true
}

const putArticleToGroup = async () => {
  const checkboxList = document.querySelectorAll('.group-article-list>label>input')
  const checkboxValues = Array.from(checkboxList)
    .filter((checkbox) => checkbox.checked)
    .map((checkbox) => checkbox.value)

  checkboxValues.forEach(async (articleId) => {
    await putArticleGroup({ id: articleId, groupId: data.value.id })
      .then(() => {
        message.success('添加成功')
        getArticleDataSource()
      })
      .catch(() => {
        return
      })
  })

  openPutter.value = false

  checkboxList.forEach((checkbox) => {
    checkbox.checked = false
  })
}

const removeArticle = (articleId) => {
  Modal.confirm({
    title: `确定要移除文章吗?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定后，文章将会从合集中移除',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      removeArticleGroup({ id: articleId }).then((res) => {
        getArticleDataSource()
        message.success('已移除')
      })
    }
  })
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
.group-container {
  margin: 15px 200px;
  background-color: #ffffff;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.group-header {
  margin: 15px;
  background-color: #ffffff;
  border-radius: 4px;
  border-bottom: 1px solid #ccc;
  display: flex;
  justify-content: space-between;
}

.group-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 15px;
}

.group-summary {
  margin-bottom: 15px;
}

.group-message a {
  color: #666;
  margin-left: 10px;
}

.group-message a:hover {
  color: #4d45e5;
}

.group-box-set {
  text-align: center;
  padding: 15px;
  margin: 30px;
  border-radius: 4px;
  border: 1px dashed #666;
  border-spacing: 5px;
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.group-box-set:hover {
  background-color: #f7f7ff;
  cursor: pointer;
}

.group-article-list {
  display: flex;
  justify-content: space-between;
}

.group-article-list input {
  margin-right: 10px;
}

.group-article-list label {
  display: flex;
}

.entry-item-box {
  flex: 1;
}

.article-remove {
  width: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  color: #666;
}
.article-remove:hover {
  color: #4d45e5;
}
</style>
