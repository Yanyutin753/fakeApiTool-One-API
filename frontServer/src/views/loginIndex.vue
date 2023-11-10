<template>
  <div class="content">
    <div class="login-container">
      <img :src="image" alt="Your Image" />
      <h1>fakeApiTool用户登录</h1>
    </div>
    <div class="container">
      <div style="display: block; transform: translate(0vw, 2vh)">
        <van-field
          v-model="username"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          class="userName"
        />
      </div>
      <div
        style="display: block; transform: translate(0vw, 2vh); margin-top: 5vh"
      >
        <van-field
          v-model="password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          class="userName"
        />
      </div>
      <div
        style="
          display: block;
          transform: translate(0.5vw, 2.5vh);
          margin-top: 5vh;
        "
      >
        <van-checkbox
          class="remember"
          v-model="checked"
          checked-color="#0ea27e"
          icon-size="1vw"
        >
          <h9 style="font-size: 14px; transform: translateX(7px)"
            >记住密码</h9
          ></van-checkbox
        >
      </div>
      <div
        style="
          display: block;
          transform: translate(0vw, 0.5vh);
          margin-top: 5vh;
        "
      >
        <input type="submit" @click="submit" value="登录" class="userName" />
      </div>
    </div>
    <!-- 页面结尾文字 -->
    <div class="bottom">
      <div style="text-align: center; justify-content: flex-end">
        <h2>
          欢迎大家来扩展
          <a href="https://github.com/Yanyutin753/fakeApiTool-One-API"
            >fakeApiToolv0.1.0-oneApi
          </a>
          获取token
          <a href="https://chat.openai.com/api/auth/session">官网地址 </a>
          <a href="https://ai.fakeopen.com/auth">Pandora地址 </a>
          安装One ApI<a href="https://github.com/songquanpeng/one-api"> 地址</a>
        </h2>
      </div>
    </div>
  </div>
</template>
  <script>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import png from "../asserts/chatGpt.jpg";
import { ElMessage } from "element-plus";
export default {
  setup() {
    const router = useRouter();
    const username = ref("");
    const password = ref("");
    const checked = ref("");
    const image = png;

    onMounted(() => {
      const savedUsername = localStorage.getItem("savedUsername");
      const savedPassword = localStorage.getItem("savedPassword");
      const savedRemember = localStorage.getItem("savedRemember");

      if (savedRemember === "true") {
        username.value = savedUsername || "";
        password.value = savedPassword || "";
        checked.value = true;
      }
    });

    const submit = () => {
      // 处理登录逻辑
      if (checked.value) {
        // 如果记住密码被选中，将用户名和密码保存到本地存储
        localStorage.setItem("savedUsername", username.value);
        localStorage.setItem("savedPassword", password.value);
        localStorage.setItem("savedRemember", "true");
      } else {
        // 如果不记住密码，清除本地存储中的信息
        localStorage.removeItem("savedUsername");
        localStorage.removeItem("savedPassword");
        localStorage.removeItem("savedRemember");
      }
      axios
        .post(
          `/api/login?userName=${username.value}&password=${password.value}`
        )
        .then((response) => {
          if (response.data.code == 1) {
            console.log("登录成功", response.data.data);
            const token = response.data.data;
            localStorage.setItem("jwtToken", token);
            ElMessage("登录成功！");
            setTimeout(() => {
              router.replace("/");
            }, 1000);
          } else {
            console.error("登录失败");
            ElMessage("账号或密码错误！");
          }
        })
        .catch((error) => {
          console.error("登录时出现错误:", error);
          ElMessage("账号或密码错误！");
        });
    };

    return {
      username,
      password,
      image,
      checked,
      submit,
    };
  },
};
</script>
  
<style scoped>
.content {
  background: rgb(184 255 225 / 22%);
  zoom: 1;
  /* 禁止页面内容缩放 */
  width: 100vw;
  /* 设置容器宽度 */
  height: 100vh;
  /* 设置容器高度，使其占满整个视口 */
  overflow-y: hidden;
  /* 显示垂直滚动条 */
  overflow-x: hidden;
  /* 隐藏水平滚动条 */
}
body {
  font-family: Arial, sans-serif;
  background-color: #f2f2f2;
}
.van-cell {
  background: #fbfbfb;
}
.userName {
  font-size: 1vw;
}

.container {
  max-width: 30vw;
  height: auto;
  margin: 0 auto;
  background-color: #fff;
  padding: 28px;
  transform: translateY(10vh);
  box-shadow: 0px 0px 3.5px rgba(0, 0, 0, 0.2);
  border-radius: 14px;
}

input[type="submit"] {
  width: 100%;
  padding: 14px;
  background-color: #4caf50;
  color: #fff;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  font-weight: bold;
}

input[type="submit"]:hover {
  background-color: #0ea27e;
}

#background {
  width: 100%;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background-size: cover;
  background-position: 0px -560px;
  z-index: -1;
  opacity: 0.99;
}
.login-container {
  color: #fff;
  margin-top: 3vh;
  margin-bottom: 5vh;
  background-color: #0ea27e;
  padding: 17.5px;
  border-radius: 21px;
  backdrop-filter: blur(7px);
  height: 5vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row; /* 指定Flex容器的主轴方向，这里设置为水平方向 */
}
.login-container h1 {
  margin-right: 7px; /* 在文本和图像之间添加一些间距 */
}

.login-container img {
  width: 35px;
  height: 35px;
}

/* 字体 a 超链接 h2 字体大小 */
a {
  color: #0ea27e;
}

h2 {
  font-size: 17.5px;
  color: #606266;
}

.bottom {
  /* 占据剩余空间 */
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 让底部组件紧贴容器底部 */
  justify-content: flex-end
}
</style>