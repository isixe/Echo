<template>
  <div class="echo-login">
    <div class="container">
      <div class="wrapper">
        <div class="login-logo enter-x-l">
          <p>
            <img alt="Echo Admin" src="../../../assets/svg/logo.svg" />
            <span>Echo Admin</span>
          </p>
        </div>
        <a-row class="login-content">
          <a-col :span="12">
            <div class="login-info">
              <img class="enter-x-r" alt="Echo Admin" src="../../../assets/svg/login-box-bg.svg" />
              <h1 class="enter-x-r-5">Echo Admin后台管理系统</h1>
              <p class="enter-x-r-10"><span>欢迎使用</span></p>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="login-form">
              <h1 class="login-title enter-x-l">登录</h1>
              <a-form ref="form" :model="data" :rules="rules">
                <a-form-item name="name" class="enter-x-l-5">
                  <a-input v-model:value="data.name" size="large" placeholder="用户名" />
                </a-form-item>
                <a-form-item name="password" class="enter-x-l-10">
                  <a-input-password
                    v-model:value="data.password"
                    size="large"
                    placeholder="密码"
                    autocomplete="on"
                    @keyup.enter="handleLogin"
                  />
                </a-form-item>
                <a-form-item class="enter-x-l-15">
                  <a-checkbox v-model:checked="data.rememberMe">记住我</a-checkbox>
                </a-form-item>
                <a-form-item class="enter-x-l-20">
                  <a-button
                    :loading="loading"
                    class="btn-login"
                    size="large"
                    type="primary"
                    @click="handleLogin"
                    >登录</a-button
                  >
                </a-form-item>
              </a-form>
            </div>
          </a-col>
        </a-row>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAdminStore } from '@/stores/admin'

const router = useRouter()

//data check
const rules = {
  name: [{ min: 4, required: true, trigger: 'blur', message: '用户名长度不能小于4位' }],
  password: [{ min: 6, required: true, trigger: 'blur', message: '密码不能小于6位' }]
}

//form data
const form = ref()
const data = reactive({
  name: '',
  password: '',
  rememberMe: false
})

onMounted(() => {
  const store = useAdminStore()
  if (store.tokenValue) {
    router.push('/dashboard')
  }
})

//click function
const loading = ref(false)
const handleLogin = async () => {
  loading.value = true

  form.value
    .validate()
    .then(() => {
      const store = useAdminStore()
      store
        .loginAction(data)
        .then(() => {
          router.push('/dashboard')
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
</script>

<style scoped>
.echo-login {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-logo {
  position: absolute;
  left: 5px;
  top: 5px;
  display: none;
}

.login-logo p {
  color: #ffffff;
  display: flex;
  align-items: center;
  font-size: 18px;
  padding: 2px;
}

.login-logo img {
  padding: 0 2px;
  width: 35px;
  height: 35px;
}

.echo-login::before {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  margin-left: -48%;
  background-image: url('../../../assets/svg/login-bg.svg');
  background-position: 100%;
  background-repeat: no-repeat;
  background-size: auto 100%;
  content: '';
}

.login-info {
  color: #ffffff;
}

.login-info img {
  width: 270px;
}

.login-form {
  width: 400px;
}

.btn-login {
  width: 100%;
}

.ant-col {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media screen and (max-width: 1200px) {
  .login-logo {
    display: block;
  }

  .echo-login {
    background-color: #293146;
  }

  .echo-login::before,
  .ant-col:first-child {
    display: none;
  }

  .ant-col {
    max-width: 100%;
  }

  .login-form {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 10px;
  }

  .login-form h1 {
    text-align: center;
  }

  .login-content {
    display: flex;
    justify-content: center;
  }

  .enter-x-l {
    animation: enter-x-l-min-animation 0.4s ease-in-out 0.3s;
    animation-fill-mode: forwards;
  }

  .enter-x-l-5 {
    animation: enter-x-l-min-animation 0.45s ease-in-out 0.3s;
    animation-fill-mode: forwards;
  }

  .enter-x-l-10 {
    animation: enter-x-l-min-animation 0.5s ease-in-out 0.3s;
    animation-fill-mode: forwards;
  }

  .enter-x-l-15 {
    animation: enter-x-l-min-animation 0.55s ease-in-out 0.3s;
    animation-fill-mode: forwards;
  }

  .enter-x-l-20 {
    animation: enter-x-l-min-animation 0.6s ease-in-out 0.3s;
    animation-fill-mode: forwards;
  }
}

@media screen and (max-width: 750px) {
  .login-form {
    width: 320px;
  }
}

@keyframes enter-x-r-animation {
  from {
    opacity: 0;
    transform: translateX(-100px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes enter-x-l-animation {
  from {
    opacity: 0;
    transform: translateX(100px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes enter-x-l-min-animation {
  from {
    opacity: 0;
    transform: translateX(25px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.enter-x-l {
  animation: enter-x-l-animation 0.4s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-l-5 {
  animation: enter-x-l-animation 0.45s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-l-10 {
  animation: enter-x-l-animation 0.5s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-l-15 {
  animation: enter-x-l-animation 0.55s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-l-20 {
  animation: enter-x-l-animation 0.6s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-r {
  animation: enter-x-r-animation 0.45s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-r-5 {
  animation: enter-x-r-animation 0.5s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}

.enter-x-r-10 {
  animation: enter-x-r-animation 0.55s ease-in-out 0.3s;
  animation-fill-mode: forwards;
}
</style>
