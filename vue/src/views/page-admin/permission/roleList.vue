<template>
    <a-button type="primary" class="editable-add-btn" @click="handleAdd">新增</a-button>
    <a-button type="primary" class="editable-add-btn" style="background-color: #52b35e;"
        @click="handleMutiEdit">批量编辑</a-button>
    <a-button type="primary" class="editable-add-btn" danger @click="handleMutiDelete">批量删除</a-button>
    <a-table :row-selection="rowSelection" :loading="loading" :columns="columns" rowKey="id" :data-source="dataSource"
        :pagination="pagination" :scroll="{ x: 1000 }">

        <template #bodyCell="{ column, text, record }">

            <template v-if="['name', 'password', 'email'].includes(column.dataIndex)">
                <div>
                    <a-input v-if="editableData[record.id]" v-model:value="editableData[record.id][column.dataIndex]"
                        style="margin: -5px 0" />
                    <template v-else>
                        {{ text }}
                    </template>
                </div>
            </template>

            <template v-if="column.dataIndex === 'action'">
                <span>
                    <span v-if="editableData[record.id]">
                        <a-typography-link @click="handleEditSave(record)">保存</a-typography-link>
                        <a-divider type="vertical" />
                        <a-popconfirm title="确认取消?" @confirm="cancel(record.id)">
                            <a>取消</a>
                        </a-popconfirm>
                    </span>
                    <span v-else>
                        <a @click="handleEdit(record.id)">编辑</a>
                    </span>
                    <a-divider type="vertical" />
                    <a-popconfirm v-if="selectedKeys.length > 1 && selectedKeys.includes(record.id)" title="确认批量删除?"
                        @confirm="handleDelete(record.id)">
                        <a>删除</a>
                    </a-popconfirm>
                    <a-popconfirm v-else title="确认删除?" @confirm="handleDelete(record.id)">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </template>
        </template>
    </a-table>
</template>
<script setup>
import router from '@/router'
import { cloneDeep } from 'lodash-es';
import { Modal } from 'ant-design-vue';
import { toRaw, createVNode } from 'vue';
import { message } from 'ant-design-vue'
import { update, remove, getRoleList } from '@/api/admin'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

const selectedKeys = ref([]);
const editableData = reactive({});
const props = defineProps(['searchText'])
const { searchText } = toRefs(props)

const loading = ref(true)
const dataSource = ref([])
const params = reactive({
    pageNum: 1,
    pageSize: 10,
    keyword: null
})

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
        return pagination.pageSize = pageSize
    },
    onChange: (page) => {
        loading.value = true
        params.pageNum = page
        queryData(params)
        return pagination.current = page
    }
})

onMounted(() => {
    queryData(params)
})

//get response
const queryData = async (params) => {
    return await getRoleList(params)
        .then((res) => {
            const data = res.data
            dataSource.value = data.records
            pagination.total = data.total
            loading.value = false
        });
}

//select items
const rowSelection = {
    onChange: (selectedRowKeys) => {
        selectedKeys.value = selectedRowKeys
    }
};

//add
const handleAdd = () => {
    router.push('/permission/roleList/add')
}

//delete
const handleDelete = (id) => {
    const formData = new FormData();
    formData.append('id', id);
    remove(formData).then(() => {
        message.success('删除成功')
        queryData(params)
    })
}

const handleMutiDelete = () => {
    if (selectedKeys.value.length > 0) {
        showDeleteConfirm()
    }
}

const showDeleteConfirm = () => {
    Modal.confirm({
        title: '确定要批量删除数据?',
        icon: createVNode(ExclamationCircleOutlined),
        content: '数据删除后，不能恢复',
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
            selectedKeys.value.forEach(key => {
                handleDelete(key)
            })
        },
        onCancel() {
            return
        },
    });
};

//edit
const handleEdit = (id) => {
    editableData[id] = cloneDeep(dataSource.value.filter(item => id === item.id)[0]);
};

const handleMutiEdit = () => {
    selectedKeys.value.forEach(key => {
        editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.id)[0]);
    })
}

const startSave = (id) => {
    const row = toRaw(editableData[id])
    const formData = new FormData()
    Object.keys(row).forEach((key) => {
        formData.append(key, row[key]);
    });
    update(formData).then(() => {
        message.success('修改成功')
        queryData(params)
        cancel(id)
    })
}

const handleEditSave = (record) => {
    const id = record.id
    if (JSON.stringify(record) === JSON.stringify(editableData[id])) {
        cancel(id)

    }
    startSave(id)
}

const cancel = id => {
    if (selectedKeys.value.includes(id)) {
        selectedKeys.value.forEach(key => {
            delete editableData[key]
        })
    }
    delete editableData[id]
}

//search
watch(searchText, (newValue) => {
    params.keyword = newValue
    queryData(params)
});

//set table head
const columns = [
    {
        title: '用户名',
        dataIndex: 'name',
        sorter: (a, b) => a.name.length - b.name.length > 0 ? 1 : -1,
        width: 150,
    },
    {
        title: '邮箱',
        dataIndex: 'email',
        sorter: (a, b) => a.email.length - b.email.length > 0 ? 1 : -1,
        width: 200,
    },
    {
        title: '最后活跃时间',
        dataIndex: 'lastActiveTime',
        customRender: (row) => row.text.replace('T', ' '),
        sorter: (a, b) => new Date(a) - new Date(b) ? 1 : -1,
        width: 160,
    },
    {
        title: '创建时间',
        dataIndex: 'createdTime',
        customRender: (row) => row.text.replace('T', ' '),
        sorter: (a, b) => new Date(a) - new Date(b) ? 1 : -1,
        width: 160,
    },
    {
        title: '操作',
        dataIndex: 'action',
        fixed: 'right',
        width: 150,
    }
];
</script>

<style scoped>
.editable-add-btn {
    margin-bottom: 8px;
    margin-right: 5px;
}
</style>