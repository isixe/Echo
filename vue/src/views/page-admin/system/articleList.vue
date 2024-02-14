<template>
  <div class="container">
    <a-button type="primary" class="editable-add-btn" @click="() => (showAddModal = !showAddModal)"
      >新增
    </a-button>
    <a-button type="primary" class="editable-add-btn" danger @click="handleMutiDelete"
      >批量删除
    </a-button>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
import { DateTime } from 'luxon'
import { Modal } from 'ant-design-vue'
import { createVNode } from 'vue'
import { message } from 'ant-design-vue'
import { add, update, remove, getUserList } from '@/api/article'
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

const rules = {}

// form
const form = ref()
const showAddModal = ref(false)
const showEditModal = ref(false)
const newUserIcon = ref('')

const originData = ref()
const newData = reactive({})

const editData = reactive({})

const disabledData = reactive({})

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
  return await getArticleList(params).then((res) => {
    const data = res.data
    console.log(data)
    // dataSource.value = data.records
    // pagination.total = data.total
    loading.value = false
  })
}

//add
const handleAddOk = () => {
  loading.value = true
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
const showEdit = (record) => {}

const handleEditOk = () => {
  loading.value = true
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
    width: 100
  },
  {
    title: '副标题',
    dataIndex: 'subTitle',
    sorter: (a, b) => (a.name.length - b.name.length > 0 ? 1 : -1),
    width: 150
  },
  {
    title: '作者',
    dataIndex: 'userId',
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
    sorter: (a, b) => (new Date(a) - new Date(b) ? 1 : -1),
    width: 160
  },
  {
    title: '创建时间',
    dataIndex: 'createdTime',
    sorter: (a, b) => (new Date(a) - new Date(b) ? 1 : -1),
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
