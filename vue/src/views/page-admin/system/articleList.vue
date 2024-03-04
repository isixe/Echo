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
        <div>
          <template v-if="column.dataIndex === 'featuredPic'">
            <a-image
              v-if="record.featuredPic"
              style="object-fit: cover"
              class="featured-pic"
              :width="80"
              :height="56"
              :src="record.featuredPic"
            />
            <img
              v-else
              style="object-fit: cover"
              class="featured-pic"
              width="80"
              height="56"
              src="../../../assets/png/noimage.png"
            />
          </template>
          <template v-else-if="column.dataIndex === 'title'">
            <div class="title">
              {{ record.title }}
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'summary'">
            <div class="summary">
              {{ record.summary }}
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'tag' && record.tag">
            <a-tag
              class="tag"
              v-for="tag in record.tag.split(',')"
              :key="tag"
              :color="tag.length < 3 ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
            >
              {{ tag }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <div class="status">
              <a-tag color="green" v-if="record.status === 1">已发布</a-tag>
              <a-tag color="orange" v-else>未发布</a-tag>
            </div>
          </template>
          <template v-else>
            {{ text }}
          </template>
        </div>

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
          <a-form-item name="featuredPic" label="头图">
            <pic-upload v-model="newData.featuredPic" />
          </a-form-item>
          <a-form-item name="title" label="标题">
            <a-input
              v-model:value="newData.title"
              placeholder="文章标题"
              show-count
              :maxlength="42"
            />
          </a-form-item>
          <a-form-item name="content" label="正文">
            <article-editor v-model="newData.content" />
          </a-form-item>
          <a-form-item name="userId" label="作者">
            <author-select v-model="newData.userId" />
          </a-form-item>
          <a-form-item name="categoryId" label="类别">
            <category-select v-model:categoryId="newData.categoryId" />
          </a-form-item>
          <a-form-item name="tag" label="标签">
            <tag-input v-model="newData.tag" />
          </a-form-item>
          <a-form-item name="articleGroupId" label="分组">
            <article-group-select
              v-model:userId="newData.userId"
              v-model:articleGroupId="newData.articleGroupId"
            />
          </a-form-item>
          <a-form-item name="publishTime" label="发布时间">
            <a-date-picker v-model:value="newData.publishTime" />
            <a-form-item-rest>
              <a-time-picker v-model:value="newData.publishTime" style="margin-left: 10px" />
            </a-form-item-rest>
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
          <a-form-item name="featuredPic" label="头图">
            <pic-upload v-model="editData.featuredPic" />
          </a-form-item>
          <a-form-item name="title" label="标题">
            <a-input
              v-model:value="editData.title"
              placeholder="文章标题"
              show-count
              :maxlength="42"
            />
          </a-form-item>
          <a-form-item name="content" label="正文">
            <article-editor v-model="editData.content" />
          </a-form-item>

          <a-form-item name="userId" label="作者">
            <a-select
              v-model:value="editData.userId"
              style="width: 300px"
              :options="userOptions"
              disabled
            />
          </a-form-item>
          <a-form-item name="categoryId" label="类别">
            <category-select
              v-model:categoryId="editData.categoryId"
              v-model:categoryName="editData.category"
            />
          </a-form-item>
          <a-form-item name="tag" label="标签">
            <tag-input v-model="editData.tag" />
          </a-form-item>
          <a-form-item name="articleGroupId" label="分组">
            <article-group-select
              v-model:itemId="editData.id"
              v-model:userId="editData.userId"
              v-model:articleGroupId="editData.articleGroupId"
              v-model:articleGroupName="editData.articleGroupName"
            />
          </a-form-item>
          <a-form-item name="publishTime" label="发布时间">
            <template v-if="editData.status === 1">
              <a-date-picker v-model:value="editData.publishTime" disabled />
              <a-form-item-rest>
                <a-time-picker
                  v-model:value="editData.publishTime"
                  style="margin-left: 10px"
                  disabled
                />
              </a-form-item-rest>
            </template>
            <template v-else>
              <a-date-picker v-model:value="editData.publishTime" />
              <a-form-item-rest>
                <a-time-picker v-model:value="editData.publishTime" style="margin-left: 10px" />
              </a-form-item-rest>
            </template>
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
import { PicUpload } from '@/components'
import { add, update, remove, getArticleListByKeyword } from '@/api/article'
import { TagInput, ArticleEditor, CategorySelect, ArticleGroupSelect } from '@/components/'
import { authorSelect } from './components'
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
  title: [
    {
      max: 42,
      required: true,
      trigger: 'blur',
      message: '标题不能为空且大于42个字符'
    }
  ],
  content: [
    {
      required: true,
      trigger: 'blur',
      message: '正文不能为空'
    }
  ],
  userId: [
    {
      required: true,
      trigger: 'blur',
      message: '作者不能为空'
    }
  ],
  categoryId: [
    {
      required: true,
      trigger: 'blur',
      message: '类别不能为空'
    }
  ],
  publishTime: [
    {
      required: true,
      trigger: 'blur',
      message: '发布时间不能为空'
    }
  ]
}

// form
const form = ref()
const showAddModal = ref(false)
const showEditModal = ref(false)

const newData = reactive({
  title: '',
  featuredPic: '',
  summary: '',
  content: '',
  userId: '',
  categoryId: '',
  tag: '',
  articleGroupId: '',
  publishTime: ''
})

const userOptions = ref([])

const editData = reactive({
  id: null,
  likeCount: null,
  pvCount: null,
  collecionCount: null,
  createdTime: '',
  status: null,
  title: '',
  author: '',
  summary: '',
  content: '',
  category: '',
  publishTime: '',
  featuredPic: '',
  articleGroupName: '',
  articleGroupId: null,
  tag: null,
  userId: null,
  categoryId: null
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
  return await getArticleListByKeyword(params).then((res) => {
    const data = res.data
    dataSource.value = data.records
    console.log(data.records)
    pagination.total = data.total
    loading.value = false
  })
}

//add
const handleAddOk = () => {
  loading.value = true

  form.value
    .validate()
    .then(async () => {
      newData.summary = newData.content.replace(/<[^>]+>|\n/g, '').substring(0, 120)

      const formData = new FormData()
      Object.keys(newData).forEach((key) => {
        formData.append(key, newData[key])
      })

      formData.set(
        'publishTime',
        DateTime.fromJSDate(newData.publishTime.$d).toFormat("yyyy-MM-dd'T'HH:mm:ss")
      )

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
    .catch(() => {
      loading.value = false
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
  editData['publishTime'] = editData['publishTime']
    ? dayjs(editData['publishTime'], 'YYYY-MM-DD HH:mm:ss')
    : null
  userOptions.value = [{ value: editData.userId, label: editData.author }]

  showEditModal.value = true
}

const handleEditOk = () => {
  loading.value = true
  form.value
    .validate()
    .then(async () => {
      editData.summary = editData.content.replace(/<[^>]+>|\n/g, '').substring(0, 120)

      const formData = new FormData()
      Object.keys(editData).forEach((key) => {
        formData.append(key, editData[key])
      })

      formData.set(
        'createdTime',
        DateTime.fromJSDate(editData.createdTime.$d).toFormat("yyyy-MM-dd'T'HH:mm:ss")
      )

      if (editData['publishTime']) {
        formData.set(
          'publishTime',
          DateTime.fromJSDate(editData.publishTime.$d).toFormat("yyyy-MM-dd'T'HH:mm:ss")
        )

        formData.set('status', 1)
      }

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
    .catch(() => {
      loading.value = false
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
    title: '文章标题',
    dataIndex: 'title',
    sorter: (a, b) => (a.title.length - b.title.length > 0 ? 1 : -1),
    width: 250
  },
  {
    title: '头图',
    dataIndex: 'featuredPic',
    width: 110
  },
  {
    title: '摘要',
    dataIndex: 'summary',
    sorter: (a, b) => (a.summary.length - b.summary.length > 0 ? 1 : -1),
    width: 300
  },
  {
    title: '作者',
    dataIndex: 'author',
    sorter: (a, b) => (a.author.length - b.author.length > 0 ? 1 : -1),
    width: 100
  },
  {
    title: '类别',
    dataIndex: 'category',
    sorter: (a, b) => (a.category.length - b.category.length > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '标签',
    dataIndex: 'tag',
    sorter: (a, b) => (a.tag.length - b.tag.length > 0 ? 1 : -1),
    width: 200
  },
  {
    title: '分组',
    dataIndex: 'articleGroupName',
    sorter: (a, b) => (a.articleGroupName.length - b.articleGroupName.length > 0 ? 1 : -1),
    width: 160
  },
  {
    title: '浏览数',
    dataIndex: 'likeCount',
    sorter: (a, b) => (a.likeCount - b.likeCount > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '评论数',
    dataIndex: 'commentCount',
    sorter: (a, b) => (a.likeCount - b.likeCount > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '发布状态',
    dataIndex: 'status',
    sorter: (a, b) => (a.status.length - b.status.length > 0 ? 1 : -1),
    width: 110
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    sorter: (a, b) => (new Date(a.updateTime) > new Date(b.updateTime) > 0 ? 1 : -1),
    width: 160
  },
  {
    title: '发布时间',
    dataIndex: 'publishTime',
    sorter: (a, b) => (new Date(a.publishTime) > new Date(b.publishTime) > 0 ? 1 : -1),
    width: 160
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
