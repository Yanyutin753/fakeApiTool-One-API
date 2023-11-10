# FakeApiTool

## 简介

FakeApiTool 是一个基于 [One API](https://github.com/songquanpeng/one-api) 和 [Pandora](https://chat1.zhile.io/auth) 中的 fakeApi 的工具，旨在更加简便地使用[pandora](https://chat1.zhile.io/auth)资源，使得可以方便地白嫖 chatGPT，本工具是站在巨人的肩膀上，方便大家，麻烦给个不要钱的星星⭐⭐⭐！

## 功能特性

1. **保存账号信息：** 支持保存 OpenAI 账号密码和 token，方便快速访问。

2. **自动获取 API keys：** 工具可以自动获取 One-API 的 API keys，省去手动获取的步骤。

3. **自动添加渠道：** 工具能够自动在 One-API 中添加渠道，简化配置过程。

4. **每日自动更新渠道请求地址：** 工具会每日自动更新渠道的请求地址，确保始终使用最新的数据。

### 初始用户名：root 初始密码值:123456

### 图片展示
![Image](image/login.png)

# 管理Token,记录token更新时间，自动更新One-APi的渠道
![Image](image/home.png)

## 使用方法
- 1.请确保部署好了One-API,且One-api接入了Sql,点击[详情](https://github.com/songquanpeng/one-api)
- 2.下载[启动包](https://github.com/Yanyutin753/fakeApiTool-One-API/blob/main/simplyDeploy/fakeApiTool-0.0.1-SNAPSHOT.jar)
- 3.上传到安装好One-API的服务器上
- 4.到达安装好包的路径下
```
# 填写下面路径
cd （你的安装路径）
```
- 5.输入下面代码启动
```
# 修改下面代码，输入你的oneapi数据库密码
nohup java -jar fakeApiTool-0.0.1-SNAPSHOT.jar --server.port=8008 --spring.datasource.password=（你的oneapi数据库密码） --spring.datasource.username=oneapi> output.log 2>&1 &
# 等待一会 放行8008端口即可运行
```

### 想要二开项目的友友们，可以自行更改前后端项目，本人小白，项目写的不太好，还请谅解！

## 强调
本项目是站在巨人的肩膀上的，感谢[One API](https://github.com/songquanpeng/one-api)大佬，感谢[Pandora](https://chat1.zhile.io/auth)大佬!，欢迎各位来帮助修改本项目，使得本项目变得更方便，更简单！

### 请给我一个免费的⭐吧！！！
