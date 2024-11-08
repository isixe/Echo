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
          <template v-if="column.dataIndex === 'avatar'">
            <a-avatar class="user-avatar" :size="26" :src="text">
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
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
    <a-modal v-model:open="showAddModal" width="800px" title="新增用户">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="newData" :rules="rules">
          <a-form-item name="avatar" label="头像">
            <div>
              <a-avatar class="user-avatar" :size="110" :src="newUserIcon">
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
            </div>
            <div style="margin-top: 15px">
              <a-upload
                :before-upload="beforeUpload"
                :max-count="1"
                :showUploadList="false"
                listType="picture"
                :customRequest="avatarUploadAction"
              >
                <a-button>
                  <upload-outlined />
                  选择文件
                </a-button>
              </a-upload>
            </div>
          </a-form-item>
          <a-form-item name="name" label="用户名">
            <a-input v-model:value="newData.name" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="newData.password"
              placeholder="请输入密码"
              autocomplete="new-password"
            />
          </a-form-item>
          <a-form-item name="email" label="邮箱">
            <a-input v-model:value="newData.email" placeholder="请输入邮箱" />
          </a-form-item>
          <a-form-item name="description" label="简介">
            <a-textarea
              v-model:value="newData.description"
              placeholder="请输入简介"
              show-count
              :maxlength="60"
            />
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (showAddModal = !showAddModal)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="handleAddOk"
          >提交</a-button
        >
      </template>
    </a-modal>
  </template>
  <template>
    <a-modal v-model:open="showEditModal" width="800px" title="更新用户信息">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="editData" :rules="rules">
          <a-form-item name="avatar" label="头像">
            <div>
              <a-avatar class="user-avatar" :size="110" :src="editData.avatar">
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
            </div>
            <div style="margin-top: 15px">
              <a-upload
                :before-upload="beforeUpload"
                :max-count="1"
                :showUploadList="false"
                listType="picture"
                :customRequest="avatarUploadAction"
              >
                <a-button>
                  <upload-outlined />
                  选择文件
                </a-button>
              </a-upload>
            </div>
          </a-form-item>
          <a-form-item name="name" label="用户名">
            <a-input v-model:value="editData.name" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="editData.password"
              placeholder="请输入密码"
              autocomplete="new-password"
            />
          </a-form-item>
          <a-form-item name="email" label="邮箱">
            <a-input v-model:value="editData.email" placeholder="请输入邮箱" />
          </a-form-item>
          <a-form-item name="description" label="简介">
            <a-textarea
              v-model:value="editData.description"
              placeholder="请输入简介"
              show-count
              :maxlength="60"
            />
          </a-form-item>
          <a-form-item name="lastActiveTime" label="最后活跃时间" style="display: flex">
            <a-form-item>
              <a-date-picker v-model:value="disabledData.lastActiveTime" disabled />
              <a-time-picker
                style="margin-left: 10px"
                v-model:value="disabledData.lastActiveTime"
                disabled
              />
            </a-form-item>
          </a-form-item>
          <a-form-item name="createdTime" label="创建时间" style="display: flex">
            <a-form-item>
              <a-date-picker v-model:value="disabledData.createdTime" disabled />
              <a-time-picker
                style="margin-left: 10px"
                v-model:value="disabledData.createdTime"
                disabled
              />
            </a-form-item>
          </a-form-item>
        </a-form>
      </div>
      <template #footer>
        <a-button key="back" @click="() => (showEditModal = !showEditModal)">取消</a-button>
        <a-button key="submit" type="primary" :loading="loading" @click="handleEditOk"
          >提交</a-button
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
import { add, update, remove, getUserListByKeyword } from '@/api/user'
import { uploadAvatar } from '@/api/file'
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
  name: [
    {
      min: 4,
      max: 16,
      required: true,
      trigger: 'blur',
      message: '用户名长度不能小于4个字符，大于16个字符'
    }
  ],
  password: [{ min: 6, required: true, trigger: 'blur', message: '密码不能小于6位' }],
  email: [
    {
      pattern: '^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$',
      required: true,
      trigger: 'blur',
      message: '邮箱格式不正确'
    }
  ]
}

// form
const form = ref()
const showAddModal = ref(false)
const showEditModal = ref(false)
const newUserIcon = ref('')

const originData = ref()

const newData = reactive({
  name: '',
  password: '',
  email: '',
  description: '',
  avatar: ''
})

const editData = reactive({
  id: '',
  name: '',
  password: '',
  email: '',
  description: '',
  avatar: '',
  lastActiveTime: '',
  createdTime: ''
})

const disabledData = reactive({
  lastActiveTime: '',
  createdTime: ''
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
  return await getUserListByKeyword(params).then((res) => {
    const data = res.data
    dataSource.value = data.records
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
      const formData = new FormData()
      Object.keys(newData).forEach((key) => {
        formData.append(key, newData[key])
      })
      await add(formData)
        .then(() => {
          message.success('添加成功')
          showAddModal.value = false
          Object.keys(newData).forEach((key) => {
            newData[key] = ''
          })
          newUserIcon.value = ''
          loading.value = false
          showAddModal.value = false

          if (pagination.total % pagination.pageSize == 0) {
            params.pageNum += 1
          }

          queryData(params)
        })
        .catch(() => {
          loading.value = false
          showAddModal.value = false
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

    if ((pagination.total - 1) % pagination.pageSize == 0) {
      params.pageNum -= 1
    }

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
const showEdit = (record) => {
  Object.keys(editData).forEach((key) => {
    editData[key] = record[key]
  })
  originData.value = JSON.stringify(editData)

  disabledData['lastActiveTime'] = dayjs(editData['lastActiveTime'], 'YYYY-MM-DD HH:mm:ss')
  disabledData['createdTime'] = dayjs(editData['createdTime'], 'YYYY-MM-DD HH:mm:ss')

  showEditModal.value = true
}

const handleEditOk = () => {
  loading.value = true
  form.value
    .validate()
    .then(async () => {
      if (originData.value === JSON.stringify(editData)) {
        loading.value = false
        return (showEditModal.value = false)
      }

      editData.lastActiveTime = DateTime.fromSQL(editData.lastActiveTime).toFormat(
        "yyyy-MM-dd'T'HH:mm:ss"
      )
      editData.createdTime = DateTime.fromSQL(editData.createdTime).toFormat(
        "yyyy-MM-dd'T'HH:mm:ss"
      )

      const formData = new FormData()
      Object.keys(editData).forEach((key) => {
        formData.append(key, editData[key])
      })
      update(formData)
        .then(() => {
          message.success('修改成功')
          queryData(params)
          loading.value = false
          showEditModal.value = false
        })
        .catch(() => {
          loading.value = false
          showEditModal.value = false
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

//upload
const beforeUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('仅支持JPG和PNG格式!')
  }

  return isJpgOrPng
}

const avatarUploadAction = (info) => {
  const formData = new FormData()
  formData.append('file', info.file)
  uploadAvatar(formData).then((res) => {
    message.success('上传成功')

    if (showAddModal.value) {
      newData.avatar = res.data
    }

    if (showEditModal.value) {
      editData.avatar = res.data
    }

    const reader = new FileReader()
    reader.readAsDataURL(info.file)
    reader.onload = () => (newUserIcon.value = reader.result)
  })
}

//set table head
const columns = [
  {
    title: '头像',
    dataIndex: 'avatar',
    width: 100
  },
  {
    title: '用户名',
    dataIndex: 'name',
    sorter: (a, b) => (a.name.length - b.name.length > 0 ? 1 : -1),
    width: 150
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    sorter: (a, b) => (a.email.length - b.email.length > 0 ? 1 : -1),
    width: 200
  },
  {
    title: '简介',
    dataIndex: 'description',
    sorter: (a, b) => (a.description.length - b.description.length > 0 ? 1 : -1),
    width: 200
  },
  {
    title: '最后活跃时间',
    dataIndex: 'lastActiveTime',
    sorter: (a, b) => (new Date(a.lastActiveTime) > new Date(b.lastActiveTime) ? 1 : -1),
    width: 160
  },
  {
    title: '创建时间',
    dataIndex: 'createdTime',
    sorter: (a, b) => (new Date(a.createdTime) > new Date(b.createdTime) ? 1 : -1),
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
</style>
