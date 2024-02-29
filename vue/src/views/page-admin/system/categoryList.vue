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
        <template v-if="['categoryName'].includes(column.dataIndex)">
          <div>
            <a-input
              v-if="editableData[record.id]"
              v-model:value="editableData[record.id][column.dataIndex]"
              style="margin: -5px 0"
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <span v-if="editableData[record.id]">
            <a-typography-link @click="save(record.id)">保存</a-typography-link>
            <a-divider type="vertical" />
            <a-popconfirm title="确定取消?" @confirm="cancel(record.id)">
              <a>取消</a>
            </a-popconfirm>
          </span>
          <span v-else>
            <span>
              <a @click="edit(record.id)">编辑</a>
            </span>
          </span>
          <span>
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
    <a-modal v-model:open="showAddModal" width="800px" title="新增类别">
      <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="newData" :rules="rules">
          <a-form-item name="categoryName" label="分类名称">
            <a-input v-model:value="newData.categoryName" />
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
</template>

<script setup>
import { Modal } from 'ant-design-vue'
import { createVNode } from 'vue'
import { message } from 'ant-design-vue'
import { add, update, remove, getCategoryListByKeyword } from '@/api/category'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { cloneDeep } from 'lodash-es'

const searchText = defineModel('search')
const useSearch = defineModel('useSearch')
useSearch.value = true

//table
const loading = ref(true)
const dataSource = ref([])
const selectedKeys = ref([])
const editableData = reactive({})

const params = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: null
})

const rules = {
  categoryName: [
    {
      required: true,
      trigger: 'blur',
      message: '分类名称不能为空'
    }
  ]
}

const newData = reactive({
  categoryName: ''
})

// form
const form = ref()
const showAddModal = ref(false)

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
  return await getCategoryListByKeyword(params).then((res) => {
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
    formData.append('categoryName', newData.categoryName)

    add(formData)
      .then(() => {
        message.success('发布成功')
        queryData(params)

        newData.categoryName = ''
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
const edit = (id) => {
  editableData[id] = cloneDeep(dataSource.value.filter((item) => id === item.id)[0])
}

const save = (id) => {
  const formData = new FormData()
  formData.append('id', id)
  formData.append('categoryName', editableData[id].categoryName)
  update(formData).then(() => {
    message.success('更新成功')
    queryData(params)
  })
  delete editableData[id]
}

const cancel = (id) => {
  delete editableData[id]
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
    title: '分类名',
    dataIndex: 'categoryName',
    sorter: (a, b) => (a.categoryName.length - b.categoryName.length > 0 ? 1 : -1)
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 150
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
</style>
