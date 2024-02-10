<template>
  <div class="container">
    <div class="container-left">
      <img src="../../../assets/png/left.png" />
      <h1 class="enter-y-t">Welcome!</h1>
      <p class="enter-y-t">
        好奇是知识的萌芽，问题不在于学到的是什么样的知识，而在于所学的知识有何用处
      </p>
    </div>
    <div class="container-right">
      <div class="login">
        <h2 class="login-title enter-y-t-30">登录</h2>
        <p class="login-sub-title enter-y-t-30">登录到您的帐户</p>
        <a-form
          class="enter-y-t-30"
          ref="form"
          :model="data"
          :rules="rules"
          :layout="formState.layout"
        >
          <a-form-item name="name" label="用户名">
            <a-input v-model:value="data.name" size="large" placeholder="用户名或邮箱" />
          </a-form-item>
          <a-form-item name="password" label="密码">
            <a-input-password
              v-model:value="data.password"
              size="large"
              placeholder="密码"
              autocomplete="on"
              @keyup.enter="handleLogin"
            />
          </a-form-item>
          <a-form-item class="enter-y-t-20">
            <a-form-item style="height: 10px">
              <div class="login-checkbox">
                <a-checkbox v-model:checked="data.rememberMe">记住我</a-checkbox>
                <a href="/forget">忘记密码?</a>
              </div>
            </a-form-item>
          </a-form-item>
          <a-form-item class=".enter-y-t-5">
            <a-button
              :loading="loading"
              class="login-submit"
              size="large"
              type="primary"
              @click="handleLogin"
              >登录</a-button
            >
          </a-form-item>
          <p class="login-bottom-text .enter-y-t-5">还没有账号?<a href="/register">去注册</a></p>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'

const router = useRouter()

//data check
const rules = {
  name: [{ min: 4, required: false, trigger: 'blur', message: '用户名长度不能小于4位' }],
  password: [{ min: 6, required: false, trigger: 'blur', message: '密码不能小于6位' }]
}

//form data
const form = ref()
const formState = reactive({
  layout: 'vertical'
})
const data = reactive({
  name: '',
  password: '',
  rememberMe: false
})

onMounted(() => {
  const store = useUserStore()
  if (store.tokenValue) {
    router.push('/')
  }
})

//click function
const loading = ref(false)
const handleLogin = () => {
  loading.value = true
  form.value
    .validate()
    .then(() => {
      const store = useUserStore()
      store
        .loginAction(data)
        .then(() => {
          loading.value = false
          router.push('/')
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

.container-left {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #d6ebff;
  padding: 0 40px;
  color: #011051;
}

.container-left p {
  word-break: break-all;
}

.container-right {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #ffffff;
}

.login {
  padding: 65px 110px 45px;
  border-radius: 10px;
}

.login-title,
.login-sub-title,
.login-bottom-text {
  text-align: center;
  padding-bottom: 10px;
}

.login-sub-title {
  margin-bottom: 20px;
}

.login-checkbox {
  display: flex;
  justify-content: space-between;
}

.login-submit {
  width: 100%;
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
    grid-template-columns: 40% 60%;
  }
}

@media screen and (max-width: 1000px) {
  .container {
    grid-template-columns: 100%;
  }

  .container-left {
    display: none;
  }
}

@media screen and (max-width: 700px) {
  .container {
    padding: 40px;
    grid-template-columns: 100%;
  }

  .container-left {
    display: none;
  }

  .container-right {
    padding: 0;
  }

  .login {
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
