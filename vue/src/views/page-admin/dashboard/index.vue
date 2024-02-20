<template>
  <div class="top-card">
    <div class="top-card-left">
      <div class="top-card-left-title">早安，管理员，请开始一天的工作吧</div>
      <div class="top-card-left-subtitle">当前暂无任务</div>
      <div class="top-card-left-content">
        <div>
          <font-awesome-icon :icon="['fas', 'arrow-right-arrow-left']" style="color: #74c0fc" />
          今日浏览（{{ 100 }}）
        </div>
        <div>
          <font-awesome-icon :icon="['fas', 'user']" style="color: #74c0fc" />
          总用户数（{{ 100 }}）
        </div>
        <div>
          <font-awesome-icon :icon="['fas', 'pen']" style="color: #74c0fc" />
          总发文数（{{ 100 }}）
        </div>
      </div>
    </div>
    <div class="top-card-right">
      <img src="../../../assets/png/dashboard.png" />
    </div>
  </div>

  <div class="function-card">
    <div class="function-card-box">
      <div class="quick-entrance">
        <div class="function-card-title">快捷入口</div>
        <div class="function-card-content">
          <div class="flex-card" @click="() => $router.push('/system/userList')">
            <div class="function-card-item">
              <div
                function-card
                class="quick-entrance-item-icon"
                style="background-color: rgba(255, 156, 110, 0.3)"
              >
                <SettingOutlined style="color: rgb(255, 156, 110); font-size: 24px" />
              </div>
              <p>系统管理</p>
            </div>
          </div>
          <div class="flex-card">
            <div class="function-card-item" @click="() => $router.push('/permission/roleList')">
              <div
                function-card
                class="quick-entrance-item-icon"
                style="background-color: rgba(105, 192, 255, 0.3)"
              >
                <KeyOutlined style="color: rgb(105, 192, 255); font-size: 24px" />
              </div>
              <p>权限管理</p>
            </div>
          </div>
          <div class="flex-card">
            <div class="function-card-item">
              <div
                function-card
                class="quick-entrance-item-icon"
                style="background-color: rgba(179, 127, 235, 0.3)"
              >
                <UserOutlined style="color: rgb(179, 127, 235); font-size: 24px" />
              </div>
              <p>信息管理</p>
            </div>
          </div>
          <div class="flex-card">
            <div class="function-card-item">
              <div
                function-card
                class="quick-entrance-item-icon"
                style="background-color: rgba(255, 214, 102, 0.3)"
              >
                <HeartOutlined style="color: rgb(255, 214, 102); font-size: 24px" />
              </div>
              <p>关于我们</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <div class="chart-card">
        <ChartLine></ChartLine>
    </div> -->
</template>

<script setup>
import ChartLine from './components/ChartLine'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUser, faPen, faArrowRightArrowLeft, faGear } from '@fortawesome/free-solid-svg-icons'
library.add(faUser, faPen, faArrowRightArrowLeft, faGear)

defineModel('search')
const useSearch = defineModel('useSearch')
useSearch.value = false
</script>

<style scoped>
.top-card {
  min-height: 230px;
  padding: 15px;
  background-color: #fff;
  box-sizing: border-box;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.top-card-left-title {
  font-size: 26px;
}

.top-card-left-subtitle {
  margin: 10px 0;
  font-size: 16px;
}

.top-card-left-content {
  margin: 20px 0;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

.top-card-right {
  position: relative;
}

.top-card-right img {
  height: 270px;
  top: -35px;
  right: 0;
  position: absolute;
}

.function-card,
.chart-card {
  background-color: #fff;
  margin: 15px 0;
  padding: 15px;
}

.function-card-box {
  margin: 0 30px;
}

.function-card-title {
  border-color: rgb(243 244 246);
  border-style: solid;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  padding-bottom: 1.15rem;
}

.function-card-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
}

.flex-card {
  display: flex;
  justify-content: center;
  flex-grow: 1;
}

.function-card-item {
  display: flex;
  flex-direction: column;
  padding: 20px 40px;
  border-radius: 10px;
}

.function-card-item:hover {
  box-shadow: 0px 5px 13px 3px #eeeeee;
}

.quick-entrance-item-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media screen and (max-width: 1000px) {
  .top-card {
    grid-template-columns: 2fr 3fr;
  }

  .top-card-left-content:nth-child(2) {
    padding: 10px 0;
    grid-template-columns: 1fr;
  }
}

@media screen and (max-width: 700px) {
  .top-card {
    grid-template-columns: 1fr;
  }

  .top-card-right {
    display: none;
  }

  .function-card-content {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media screen and (max-width: 600px) {
  .function-card-content {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
