<template>
  <div class="error-container">
    <div class="error-content">
      <a-row :gutter="20">
        <a-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
          <div class="pic-error">
            <img class="pic-error-parent" src="@/assets/error_images/403.png" />
            <img class="pic-error-child left" src="@/assets/error_images/cloud.png" />
          </div>
        </a-col>

        <a-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
          <div class="bullshit">
            <div class="bullshit-oops"> 抱歉!</div>
            <div class="bullshit-headline">您的角色无法操作...</div>
            <div class="bullshit-info">当前帐号没有操作角色, 请联系管理员。</div>
            <a class="bullshit-return-home" href="/">
              返回首页
            </a>
          </div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>


<style scoped>
.error-container {
  position: relative;
  height: 100vh;
}

.error-container .error-content {
  position: absolute;
  top: 55%;
  left: 50%;
  width: 40vw;
  height: 400px;
  transform: translate(-50%, -50%);
}

.error-container .error-content .pic-error {
  position: relative;
  float: left;
  width: 100%;
  overflow: hidden;
}

.error-container .error-content .pic-error-parent {
  width: 100%;
}

.error-container .error-content .pic-error-child {
  position: absolute;
}

.error-container .error-content .pic-error-child.left {
  top: 17px;
  left: 220px;
  width: 60px;
  opacity: 0;
  animation-name: cloudLeft;
  animation-duration: 2s;
  animation-timing-function: linear;
  animation-delay: 1s;
  animation-fill-mode: forwards;
}

@keyframes cloudLeft {
  0% {
    top: 17px;
    left: 220px;
    opacity: 0;
  }

  20% {
    top: 33px;
    left: 188px;
    opacity: 1;
  }

  80% {
    top: 81px;
    left: 92px;
    opacity: 1;
  }

  100% {
    top: 97px;
    left: 60px;
    opacity: 0;
  }
}

.error-container .error-content .bullshit {
  position: relative;
  float: left;
  width: 300px;
  padding: 30px 0;
  overflow: hidden;
}

.error-container .error-content .bullshit-oops {
  margin-bottom: 20px;
  font-size: 32px;
  font-weight: bold;
  line-height: 40px;
  color: #5f74f2;
  opacity: 0;
  animation-name: slideUp;
  animation-duration: 0.5s;
  animation-fill-mode: forwards;
}

.error-container .error-content .bullshit-headline {
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
  line-height: 24px;
  color: #222;
  opacity: 0;
  animation-name: slideUp;
  animation-duration: 0.5s;
  animation-delay: 0.1s;
  animation-fill-mode: forwards;
}

.error-container .error-content .bullshit-info {
  margin-bottom: 30px;
  font-size: 13px;
  line-height: 21px;
  opacity: 0;
  animation-name: slideUp;
  animation-duration: 0.5s;
  animation-delay: 0.2s;
  animation-fill-mode: forwards;
}

.error-container .error-content .bullshit-return-home {
  display: block;
  float: left;
  width: 110px;
  font-size: 14px;
  line-height: 36px;
  color: #fff;
  text-align: center;
  cursor: pointer;
  background: #5f74f2;
  border-radius: 100px;
  opacity: 0;
  animation-name: slideUp;
  animation-duration: 0.5s;
  animation-delay: 0.3s;
  animation-fill-mode: forwards;
}

@keyframes slideUp {
  0% {
    opacity: 0;
    transform: translateY(60px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
