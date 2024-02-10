<template>
  <div class="container">
    <div class="container-left">
      <div class="register">
        <h2 class="register-title enter-y-t-30">注册</h2>
        <p class="register-sub-title enter-y-t-30">注册您的帐户</p>
        <a-form
          class="enter-y-t-30"
          ref="form"
          :model="data"
          :rules="rules"
          :layout="formState.layout"
        >
          <a-form-item name="name" label="用户名">
            <a-input v-model:value="data.name" size="large" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item name="email" label="邮箱">
            <a-input v-model:value="data.email" size="large" placeholder="请输入邮箱" />
          </a-form-item>
          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="data.password"
              size="large"
              placeholder="请输入密码"
              autocomplete="new-password"
            />
          </a-form-item>
          <a-form-item name="checkPassword" label="重复密码">
            <a-input-password
              v-model:value="data.checkPassword"
              size="large"
              placeholder="请输入密码"
              @keyup.enter="handleRegister"
            />
          </a-form-item>
          <a-form-item class=".enter-y-t-5">
            <a-button
              :loading="loading"
              class="register-submit"
              size="large"
              type="primary"
              @click="handleRegister"
              >注册</a-button
            >
          </a-form-item>
          <p class="register-bottom-text .enter-y-t-5">已有账号?<a href="/login">去登录</a></p>
        </a-form>
      </div>
    </div>
    <div class="container-right">
      <img src="../../../assets/png/right.png" />
      <h1 class="enter-y-t">Greet!</h1>
      <p class="enter-y-t">
        发现新事物和作出新发现，是很大的满足。我们活着只为的是去发现美。其他一切都是等待的种种形式。
      </p>
    </div>
  </div>
</template>

<script setup>
import { add } from '@/api/user'
import { message } from 'ant-design-vue'

const router = useRouter()

//form data
const form = ref()
const formState = reactive({
  layout: 'vertical'
})
const data = reactive({
  name: '',
  password: '',
  checkPassword: '',
  rememberMe: false
})

//data check
const validatePass = async (_rule, value) => {
  if (value !== data.password) {
    return Promise.reject('两次密码输入不同')
  }

  return Promise.resolve()
}

const rules = {
  name: [{ min: 4, required: false, trigger: 'blur', message: '用户名长度不能小于4位' }],
  password: [{ min: 6, required: false, trigger: 'blur', message: '密码不能小于6位' }],
  checkPassword: [{ validator: validatePass, required: false, trigger: 'blur' }],
  email: [
    {
      pattern: '^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$',
      required: false,
      trigger: 'blur',
      message: '邮箱格式不正确'
    }
  ]
}

//click function
const loading = ref(false)
const handleRegister = () => {
  loading.value = true
  form.value
    .validate()
    .then(() => {
      const formData = new FormData()
      formData.append('name', data.name)
      formData.append('email', data.email)
      formData.append('password', data.password)
      add(formData)
        .then(() => {
          loading.value = false
          message.success('注册成功')
          router.push('/login')
        })
        .catch(() => {
          loading.value = false
        })
    })
    .catch(() => {
      loading.value = false
    })
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  padding: 60px;
  display: grid;
  grid-template-columns: 50% 50%;
  background-image: url(../../../assets/png/bg.png);
  background-repeat: no-repeat;
  background-size: cover;
}

.container-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #d6ebff;
  padding: 0 40px;
  color: #011051;
}

.container-right h1 {
  text-align: right;
}

.container-right p {
  word-break: break-all;
}

.container-left {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #ffffff;
}

.register {
  padding: 10px 110px;
  border-radius: 10px;
}

.register-title,
.register-sub-title,
.register-bottom-text {
  text-align: center;
  padding-bottom: 10px;
}

.register-sub-title {
  margin-bottom: 20px;
}

.register-checkbox {
  display: flex;
  justify-content: space-between;
}

.register-submit {
  width: 100%;
  margin-top: 5px;
}

.enter-y-t {
  animation: enter-x-t-animation 0.65s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-y-t-5 {
  animation: enter-x-t-min-animation 0.45s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-y-t-10 {
  animation: enter-x-t-animation 0.55s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-y-t-20 {
  animation: enter-x-t-animation 0.65s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-y-t-30 {
  animation: enter-x-t-animation 0.75s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-y-t-40 {
  animation: enter-x-t-animation 0.85s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

@media screen and (max-width: 1200px) {
  .container {
    grid-template-columns: 60% 40%;
  }
}

@media screen and (max-width: 1000px) {
  .container {
    grid-template-columns: 100%;
  }

  .container-right {
    display: none;
  }
}

@media screen and (max-width: 700px) {
  .container {
    padding: 40px;
    grid-template-columns: 100%;
  }

  .container-right {
    display: none;
  }

  .container-left {
    padding: 0;
  }

  .register {
    padding: 65px 50px 45px;
  }
}

@keyframes enter-x-t-animation {
  from {
    opacity: 0;
    transform: translateY(50px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes enter-x-t-min-animation {
  from {
    opacity: 0;
    transform: translateY(25px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
