<template>
    <div class="container">
        <a-button type="primary" class="editable-add-btn" @click="() => open = !open">新增</a-button>
        <a-button type="primary" class="editable-add-btn" style="background-color: #52b35e;"
            @click="handleMutiEdit">批量编辑</a-button>
        <a-button type="primary" class="editable-add-btn" danger @click="handleMutiDelete">批量删除</a-button>
        <a-table :row-selection="rowSelection" :loading="loading" :columns="columns" rowKey="id" :data-source="dataSource"
            :pagination="pagination" :scroll="{ x: 1000 }">

            <template #bodyCell="{ column, text, record }">

                <template v-if="['name', 'password', 'email', 'avatar'].includes(column.dataIndex)">
                    <div>
                        <a-input v-if="editableData[record.id]" v-model:value="editableData[record.id][column.dataIndex]"
                            style="margin: -5px 0" />
                        <template v-else>
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
    </div>
    <template>
        <a-modal v-model:open="open" width="800px" title="新增管理员" @ok="handleOk">
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
                        <div style="margin-top: 15px;">
                            <a-upload :before-upload="beforeUpload" :max-count="1" :showUploadList="false"
                                listType="picture" :customRequest="avatarUploadAction">
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
                        <a-input-password v-model:value="newData.password" placeholder="请输入密码" />
                    </a-form-item>
                    <a-form-item name="email" label="邮箱">
                        <a-input v-model:value="newData.email" placeholder="请输入邮箱" />
                    </a-form-item>
                </a-form>
            </div>
        </a-modal>
    </template>
</template>
<script setup>
import { cloneDeep } from 'lodash-es';
import { Modal } from 'ant-design-vue';
import { toRaw, createVNode } from 'vue';
import { message } from 'ant-design-vue'
import { add, update, remove, getRoleList } from '@/api/admin'
import { uploadAvatar } from '@/api/file'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

defineEmits(['noSearch'])
const props = defineProps(['searchText'])
const { searchText } = toRefs(props)

//table
const loading = ref(true)
const dataSource = ref([])
const selectedKeys = ref([]);
const editableData = reactive({});

const params = reactive({
    pageNum: 1,
    pageSize: 10,
    keyword: null
})

//form
const form = ref()
const open = ref(false);
const newUserIcon = ref('')

const newData = reactive({
    name: '',
    password: '',
    email: '',
    avatar: ''
});

const formItemLayout = {
    labelCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 6,
        },
    },
    wrapperCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 14,
        },
    },
};

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
const rules = {
    name: [{ min: 4, max: 16, required: true, trigger: 'blur', message: '用户名长度不能小于4个字符，大于16个字符' }],
    password: [{ min: 6, required: true, trigger: 'blur', message: '密码不能小于6位' }],
    email: [{ pattern: '^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$', require: false, trigger: 'blur', message: '邮箱格式不正确' }]
}

const avatarUploadAction = (info) => {
    const formData = new FormData();
    formData.append('file', info.file);
    uploadAvatar(formData).then((res) => {
        newData.avatar = res.data
    })
}

const handleOk = () => {
    form.value
        .validate()
        .then(async () => {
            const formData = new FormData()
            Object.keys(newData).forEach((key) => {
                formData.append(key, newData[key])
            })
            await add(formData).then(() => {
                message.success('添加成功')
                open.value = false
                queryData(params)
                Object.keys(newData).forEach(key => {
                    newData[key] = '';
                });
                newUserIcon.value = ''
            }).catch(() => {
                open.value = false
            })
        })
};

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

const cancel = (id) => {
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

//upload
const beforeUpload = file => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
        message.error('仅支持JPG和PNG格式!');
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => newUserIcon.value = reader.result

    return isJpgOrPng;
};

//set table head
const columns = [
    {
        title: '头像',
        dataIndex: 'avatar',
        width: 100,
    },
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
}

.ant-form {
    margin-top: 20px;
}
</style>