<template>
  <div class="container">
    <a-button type="primary" class="editable-add-btn" @click="() => (showAddModal = !showAddModal)"
      >新增
    </a-button>
    <a-button type="primary" class="editable-add-btn" danger @click="handleMutiDelete"
      >批量删除
    </a-button>
    <a-table
      :row-selection="rowSelection"
      :loading="loading"
      :columns="columns"
      rowKey="id"
      :data-source="dataSource"
      :pagination="pagination"
      :scroll="{ x: 1000 }"
    >
      <template #bodyCell="{ column, text, record }">
        {{ text }}
        <template v-if="column.dataIndex === 'action'">
          <span>
            <span>
              <a @click="showEdit(record)">编辑</a>
            </span>
            <a-divider type="vertical" />
            <a-popconfirm title="确认删除?" @confirm="handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
        </template>
      </template>
    </a-table>
  </div>

  <template>
    <a-modal v-model:open="showAddModal" width="800px" title="新增文章">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="newData" :rules="rules">
          <a-form-item name="userId" label="用户">
            <author-select v-model="newData.userId" />
          </a-form-item>
          <a-form-item name="content" label="评论内容">
            <a-textarea v-model:value="newData.content" />
          </a-form-item>
          <a-form-item name="articleId" label="关联文章">
            <article-select v-model="newData.articleId" />
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (showAddModal = !showAddModal)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="handleAddOk"
          >发布</a-button
        >
      </template>
    </a-modal>
  </template>

  <template>
    <a-modal ref="editModal" v-model:open="showEditModal" width="800px" title="更新文章">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="editData" :rules="rules">
          <a-form-item name="userId" label="用户">
            <a-select v-model:value="editData.userId" :options="userOptions" disabled />
          </a-form-item>
          <a-form-item name="content" label="评论内容">
            <a-textarea v-model:value="editData.content" />
          </a-form-item>
          <a-form-item name="articleId" label="关联文章">
            <a-select v-model:value="editData.articleId" :options="articleOptions" disabled />
          </a-form-item>
          <a-form-item name="createdTime" label="创建时间">
            <a-date-picker v-model:value="editData.createdTime" disabled />
            <a-form-item-rest>
              <a-time-picker
                v-model:value="editData.createdTime"
                style="margin-left: 10px"
                disabled
              />
            </a-form-item-rest>
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (showEditModal = !showEditModal)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="handleEditOk"
          >发布</a-button
        >
      </template>
    </a-modal>
  </template>
</template>

<script setup>
import dayjs from 'dayjs'
import { DateTime } from 'luxon'
import { Modal } from 'ant-design-vue'
import { createVNode } from 'vue'
import { message } from 'ant-design-vue'
import { add, update, remove, getCommentArticleListByKeyword } from '@/api/articleComment'
import { authorSelect, articleSelect } from './components'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const searchText = defineModel('search')
const useSearch = defineModel('useSearch')
useSearch.value = true

//table
const loading = ref(true)
const dataSource = ref([])
const selectedKeys = ref([])

const params = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: null
})

const rules = {
  content: [
    {
      required: true,
      trigger: 'blur',
      message: '评论内容不能为空'
    }
  ],
  userId: [
    {
      required: true,
      trigger: 'blur',
      message: '作者不能为空'
    }
  ],
  articleId: [
    {
      required: true,
      trigger: 'blur',
      message: '文章不能为空'
    }
  ]
}

// form
const form = ref()
const showAddModal = ref(false)
const showEditModal = ref(false)

const newData = reactive({
  userId: null,
  articleId: null,
  content: ''
})

const userOptions = ref([])
const articleOptions = ref([])
const editData = reactive({
  id: null,
  content: '',
  articleId: null,
  articleTitle: '',
  userId: null,
  userName: '',
  rootCommentId: null,
  parentCommentId: null,
  createdTime: null
})

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

//set pagination
const pagination = reactive({
  defaultPageSize: 10,
  current: params.pageNum,
  pageSize: params.pageSize,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共有 ${total} 条数据`,
  onShowSizeChange: (current, pageSize) => {
    loading.value = true
    params.pageSize = pageSize
    queryData(params)
    return (pagination.pageSize = pageSize)
  },
  onChange: (page) => {
    loading.value = true
    params.pageNum = page
    queryData(params)
    return (pagination.current = page)
  }
})

onMounted(() => {
  queryData(params)
})

//get response
const queryData = async (params) => {
  return await getCommentArticleListByKeyword(params).then((res) => {
    const data = res.data
    dataSource.value = data.records
    pagination.total = data.total
    loading.value = false
  })
}

//add
const handleAddOk = () => {
  loading.value = true

  form.value.validate().then(async () => {
    const formData = new FormData()
    Object.keys(newData).forEach((key) => {
      formData.append(key, newData[key])
    })

    add(formData)
      .then(() => {
        message.success('发布成功')
        queryData(params)

        Object.keys(newData).forEach((key) => {
          newData[key] = ''
        })
        loading.value = false
        showAddModal.value = false
      })
      .catch(() => {
        loading.value = false
      })
  })
}

//delete
const handleDelete = (id) => {
  const formData = new FormData()
  formData.append('id', id)
  remove(formData).then(() => {
    message.success('删除成功')
    queryData(params)
  })
}

const handleMutiDelete = () => {
  const length = selectedKeys.value.length
  if (!length > 0) {
    return
  }

  Modal.confirm({
    title: `确定要批量删除${length}条数据?`,
    icon: createVNode(ExclamationCircleOutlined),
    content: '数据删除后，不能恢复',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk() {
      selectedKeys.value.forEach((key) => {
        handleDelete(key)
      })
    }
  })
}

//edit
const showEdit = async (record) => {
  Object.keys(editData).forEach((key) => {
    editData[key] = record[key]
  })

  editData['createdTime'] = dayjs(editData['createdTime'], 'YYYY-MM-DD HH:mm:ss')
  userOptions.value = [{ value: editData.userId, label: editData.userName }]
  articleOptions.value = [{ value: editData.articleId, label: editData.articleTitle }]

  showEditModal.value = true
}

const handleEditOk = () => {
  loading.value = true
  form.value.validate().then(async () => {
    const formData = new FormData()
    Object.keys(editData).forEach((key) => {
      formData.append(key, editData[key])
    })

    formData.set(
      'createdTime',
      DateTime.fromJSDate(editData.createdTime.$d).toFormat("yyyy-MM-dd'T'HH:mm:ss")
    )

    update(formData)
      .then(() => {
        message.success('更新成功')
        queryData(params)
      })
      .then(() => {
        showEditModal.value = false
        loading.value = false
      })
      .catch(() => {
        loading.value = false
      })
  })
}

//select items
const rowSelection = {
  onChange: (selectedRowKeys) => {
    selectedKeys.value = selectedRowKeys
  }
}

//search
watch(searchText, (newValue) => {
  params.keyword = newValue
  queryData(params)
})

//set table head
const columns = [
  {
    title: '用户名',
    dataIndex: 'userName',
    sorter: (a, b) => (a.userName.length - b.userName.length > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '顶级评论ID',
    dataIndex: 'rootCommentId',
    width: 130
  },
  {
    title: '父级评论ID',
    dataIndex: 'parentCommentId',
    width: 130
  },
  {
    title: '评论',
    dataIndex: 'content',
    sorter: (a, b) => (a.content.length - b.content.length > 0 ? 1 : -1),
    width: 300
  },
  {
    title: '相关文章',
    dataIndex: 'articleTitle',
    sorter: (a, b) => (a.articleTitle.length - b.articleTitle.length > 0 ? 1 : -1),
    width: 200
  },
  {
    title: '支持数',
    dataIndex: 'likeCount',
    sorter: (a, b) => (a.likeCount - b.likeCount > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '否定数',
    dataIndex: 'dislikeCount',
    sorter: (a, b) => (a.dislikeCount - b.dislikeCount > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '创建时间',
    dataIndex: 'createdTime',
    sorter: (a, b) => (new Date(a.createdTime) - new Date(b.createdTime) ? 1 : -1),
    width: 160
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 120
  }
]
</script>

<style scoped>
.container {
  background-color: #ffffff;
  padding: 10px;
}

.editable-add-btn {
  margin-bottom: 8px;
  margin-right: 5px;
}

.form-container {
  padding: 20px;
  background-color: #ffffff;
  max-height: 55vh;
  overflow-y: scroll;
}

.ant-form {
  margin-top: 20px;
}

.summary {
  overflow: hidden;
  width: 100%;
  font-size: 13px;
  text-overflow: ellipsis;
  display: block;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.title {
  overflow: hidden;
  font-size: 13px;
  width: 100%;
  text-overflow: ellipsis;
  display: block;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.tag {
  margin: 3px;
}

.pic-upload > :global(.ant-upload.ant-upload-select.ant-upload-select-picture-card) {
  width: 415px !important;
  height: 152px !important;
}
</style>
