<template>
    <div class="form-container">
        <a-form ref="form" v-bind="formItemLayout" :model="data" :rules="rules">
            <a-form-item name="name" label="用户名">
                <a-input v-model:value="data.name" placeholder="请输入用户名" />
            </a-form-item>
            <a-form-item name="password" label="密码">
                <a-input-password v-model:value="data.password" placeholder="请输入密码" />
            </a-form-item>
            <a-form-item name="email" label="邮箱">
                <a-input v-model:value="data.email" placeholder="请输入邮箱" />
            </a-form-item>
            <a-form-item :wrapper-col="{ span: 14, offset: 6 }">
                <a-button type="primary" @click.prevent="handleSubmit">提交</a-button>
                <a-button style="margin-left: 10px" @click="handleCancel">取消</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>

<script setup>
import router from '@/router'
import { add } from '@/api/admin'

const rules = {
    name: [{ min: 4, max: 16, required: true, trigger: 'blur', message: '用户名长度不能小于4个字符，大于16个字符' }],
    password: [{ min: 6, required: true, trigger: 'blur', message: '密码不能小于6位' }],
    email: [{ pattern: '^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$', require: false, trigger: 'blur', message: '邮箱格式不正确' }]
}
const form = ref()
const data = reactive({
    name: '',
    password: '',
    email: '',
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

const handleCancel = () => {
    router.push('/permission/roleList')
}

const handleSubmit = () => {
    form.value
        .validate()
        .then(async () => {
            const formData = new FormData()
            Object.keys(data).forEach((key) => {
                formData.append(key, data[key]);
            });
            await add(formData).then(() => {
                router.push('/permission/roleList')
            })
        })
}
</script>

<style scoped>
.form-container {
    padding: 20px;
    background-color: #ffffff;
}

.ant-form {
    margin-top: 20px;
}
</style>