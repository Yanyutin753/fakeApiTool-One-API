package com.yyandywt99.fakeapitool.service.impl;

import com.yyandywt99.fakeapitool.mapper.apiMapper;
import com.yyandywt99.fakeapitool.pojo.addKeyPojo;
import com.yyandywt99.fakeapitool.pojo.token;
import com.yyandywt99.fakeapitool.service.apiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yangyang
 * @create 2023-11-07 14:54
 */
@Slf4j
@Service
public class apiServiceImpl implements apiService {
    /**
     * 拿到apiMapper,为调用做准备
     */
    @Autowired
    private apiMapper apiMapper;

    @Value("${baseUrlWithoutPath}")
    private String baseUrlWithoutPath;

    @Value("${baseUrlAutoToken}")
    private String baseUrlAutoToken;
    private String session;

    private String getSession(){
        return this.session;
    };

    private void setSession(String session){
        this.session = session;
    }


    public boolean existSession(){
        if(getSession() == null || getSession().isEmpty()){
            return false;
        }
        return true;
    }
    /**
     *
     * @param temTaken
     * @return 以fk-开头的fakeApiKey
     * @throws Exception
     * 通过https://ai.fakeopen.com/token/register
     * unique_name（apiKey名字）、access_token（token）、
     * expires_i（有效期默认为0）、show_conversations（是否不隔绝对话，默认是）、
     *
     */
    public List<String> getKeys(token temTaken) throws Exception {
        List<String> res = new ArrayList<>();
        int temToken = 1;
        while (temToken <= 3) {
            // 替换为你的unique_name
            String unique_name = temTaken.getName() + temToken;
            // 请确保在Java中有token_info这个Map
            String access_token = temTaken.getValue();
            // 假设expires_in为0
            int expires_in = 0;
            boolean show_conversations = true;

            String url = "https://ai.fakeopen.com/token/register";
            String data = "unique_name=" + unique_name + "&access_token=" + access_token + "&expires_in=" + expires_in + "&show_conversations=" + show_conversations;
            String tokenKey = "";
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // 设置请求方法为POST
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                // 发送POST数据
                OutputStream os = con.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                // 获取响应
                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String responseJson = response.toString();
                    tokenKey = new JSONObject(responseJson).getString("token_key");
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String errStr = response.toString().replace("\n", "").replace("\r", "").trim();
                    System.out.println("share token failed: " + errStr);
                    return null;
                }

                // 使用正则表达式匹配字符串
                String shareToken = tokenKey;
                if (shareToken.matches("^(fk-|pk-).*")) {
                    log.info("open_ai_api_key has been updated to: " + shareToken);
                    res.add(shareToken);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            temToken ++;
        }
        return res;
    }

    /**
     * 添加Key值
     * 会通过Post方法访问One-Api接口/api/channel/,添加新keys
     * @return "true"or"false"
     */
    public boolean addKey(addKeyPojo addKeyPojo) throws Exception {
        if(!existSession()){
            return false;
        }
        String url = baseUrlWithoutPath+"/api/channel/";
        log.info(url);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 8);
        jsonObject.put("key", addKeyPojo.getKey());
        jsonObject.put("name", addKeyPojo.getName());
        jsonObject.put("base_url", "https://ai.fakeopen.com");
        jsonObject.put("other", "");
        jsonObject.put("models", "gpt-3.5-turbo,gpt-3.5-turbo-0301,gpt-3.5-turbo-0613,gpt-3.5-turbo-16k,gpt-3.5-turbo-16k-0613,gpt-3.5-turbo-instruct");
        jsonObject.put("group", "default");
        jsonObject.put("model_mapping", "");
        jsonObject.put("groups", new JSONArray().put("default"));
        // 将JSON对象转换为字符串
        String json = jsonObject.toString();
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost addPutKey = new HttpPost(url);
            addPutKey.addHeader("Cookie", getSession());
            addPutKey.setEntity(new StringEntity(json, "UTF-8"));
            // 发送请求
            HttpResponse response = httpClient.execute(addPutKey);
            log.info(response.toString());
            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            // 获得响应消息
            String responseContent = EntityUtils.toString(response.getEntity());
            // 处理响应数据
            JSONObject jsonResponse = new JSONObject(responseContent);
            // 提取返回的数据
            log.info(jsonResponse.toString());
            boolean success = jsonResponse.getBoolean("success");
            log.info(success+"");
            if (statusCode == 200 && success) {
                System.out.println("Request was successful");
                return true;
            } else {
                // 请求失败
                System.out.println("Request failed with status code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 自动更新每日FakeOPenAI地址
     * @return 每日FakeOPenAI地址
     */
    @Override
    public String getUpdateUrl() throws Exception {
        try {
            log.info("每天早上八点定时任务启动，定期更改URL");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 获取当前时间
            Date currentDate = new Date();
            // 使用SimpleDateFormat格式化日期
            String newDate = sdf.format(currentDate);
            log.info(newDate);
            String newDateUrl = "https://ai-"+newDate.toString()+".fakeopen.com/";
            URL url = new URL(newDateUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            //访问Url，防止出现访问错误却修改的情况
            if (responseCode == HttpURLConnection.HTTP_OK) {
                apiMapper.toUpdateUrl(newDateUrl);
                log.info("修改成功");
                return "修改成功";
            }
            else {
                log.info("修改失败");
                return "修改失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败";
        }
    }


    /**
     * 修改token值或者其他
     * 会通过删除相应的keys,并添加新keys(会检验是否Token合格)
     * @return "修改成功！"or"修改失败"or修改失败,检查你的token是否正确！
     */
    @Override
    public String requiredToken(token tem){
        if(!existSession()){
            return "修改失败,session已过期，请重新登录";
        }
        try {
            boolean res = verifyToken(tem);
            if(res){
                apiMapper.requiredToken(tem);
                //先删除
                boolean resDelete = deleteKeys(tem.getName());
                //后添加
                boolean resAdd = addKeys(tem);
                if(resAdd && resDelete){
                    return "修改成功！";
                }
                else {
                    return "修改失败,检查你的token是否正确！";
                }
            }
            else {
                log.info("修改失败");
                return "修改失败,检查你的token是否正确！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("修改失败");
            return "修改失败";
        }
    }

    /**
     * 测试Token
     * @return "true(可用)"or"false(不可用)"
     */
    private boolean verifyToken(token temTaken) {
        // 替换为你的unique_name
        String unique_name = temTaken.getName() + "Test";
        // 请确保在Java中有token_info这个Map
        String access_token = temTaken.getValue();
        // 假设expires_in为0
        int expires_in = 0;
        boolean show_conversations = true;

        String url = "https://ai.fakeopen.com/token/register";
        String data = "unique_name=" + unique_name + "&access_token=" + access_token + "&expires_in=" + expires_in + "&show_conversations=" + show_conversations;
        String tokenKey = "";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 设置请求方法为POST
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // 发送POST数据
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            // 获取响应
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String responseJson = response.toString();
                tokenKey = new JSONObject(responseJson).getString("token_key");
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String errStr = response.toString().replace("\n", "").replace("\r", "").trim();
                log.info("share token failed: " + errStr);
            }
            // 使用正则表达式匹配字符串
            String shareToken = tokenKey;
            if (shareToken.matches("^(fk-|pk-).*")) {
                log.info("测试token可用");
                return true;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 添加token
     * 并添加对应keys
     * @return "添加成功！"or"添加失败,检查你的token是否正确或登录是否过期！"
     */
    @Override
    public String addToken(token token) {
        if(!existSession()){
            return "添加失败,session已过期，请重新登录";
        }
        if(token.getValue() == null || token.getValue().length() == 0){
            String res = updateToken(token);
            if(res != null){
                token.setValue(res);
            }
            else {
                return "添加失败,检查你的账号密码是否正确或FakeOpen服务异常";
            }
        }
        try {
            if(addKeys(token)){
                apiMapper.addToken(token);
                return "添加成功！";
            }
            else {
                return "添加失败,检查你的token是否正确或登录是否过期！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加失败");
            return "添加失败,检查你的token是否正确或登录是否过期！";
        }

    }

    /**
     * 删除token
     * 并删除对应keys
     * @return "删除成功！"or"删除失败"
     */
    @Override
    public String deleteToken(String name){
        if(!existSession()){
            return "删除失败,session已过期，请重新登录";
        }
        try {
            boolean resDelete = false;
            try {
                resDelete = deleteKeys(name);
                apiMapper.deleteToken(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (resDelete){
                return "删除成功！";
            }
            else {
                return "删除失败！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }
    }

    /**
     * one-api里面channels里面添加对应keys
     * @return
     */
    @Override
    public boolean addKeys(token token){
        if(!existSession()){
            return false;
        }
        try {
            List<String> keys = getKeys(token);
            if(keys != null){
                try {
                    for (String key : keys){
                        addKeyPojo keyPojo = new addKeyPojo();
                        keyPojo.setKey(key);
                        keyPojo.setName(token.getName());
                        //通过One-API接口添加渠道
                        addKey(keyPojo);
                    }
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                throw new Exception("Invalid token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * one-api里面channels里面删除对应keys
     * @return
     */
    public boolean deleteKeys(String name) {
        if(!existSession()){
            return false;
        }
        try {
            List<Integer> resId = apiMapper.deleteKeys(name);
            log.info(resId.toString());
            for (Integer i : resId){
                String url = baseUrlWithoutPath+"/api/channel/"+i;
                log.info(url);
                try {
                    HttpClient httpClient = HttpClients.createDefault();
                    HttpDelete deleteKey = new HttpDelete(url);
                    deleteKey.addHeader("Cookie", getSession());
                    // 发送请求
                    HttpResponse response = httpClient.execute(deleteKey);
                    log.info(response.toString());
                    // 处理响应
                    int statusCode = response.getStatusLine().getStatusCode();
                    // 获得响应数据
                    String responseContent = EntityUtils.toString(response.getEntity());
                    // 处理响应数据
                    JSONObject jsonResponse = new JSONObject(responseContent);
                    // 提取返回的数据
                    log.info(jsonResponse.toString());
                    boolean success = jsonResponse.getBoolean("success");
                    log.info(success+"");
                    if (statusCode == 200 && success) {
                        log.info("Request was successful");
                    }
                    else {
                        log.info("Request failed with status code: " + statusCode);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 打印token全部
     * @return res（List<token> ）
     */
    @Override
    public List<token> seleteToken(String name) {
        try {
            List<token> res = apiMapper.selectToken(name);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public token selectAccuracyToken(String name) {
        try {
            token res = apiMapper.selectAccuracyToken(name);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改用户信息
     * @return "修改用户成功！"or"修改用户失败"
     */
    @Override
    public String requiredUser(String userName,String password) {
        try {
            apiMapper.requiredUser(userName,password);
            return "修改用户成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "修改用户失败";
        }
    }

    public String loginOneApi(String userName, String password) throws JSONException {
        String url = baseUrlWithoutPath+"/api/user/login";
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Cookie存储
        CookieStore cookieStore = new BasicCookieStore();
        // 创建HttpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头部为 "application/json"
        httpPost.setHeader("Content-Type", "application/json");
        // 设置JSON数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("password", password);
        // 将JSON对象转换为字符串
        String json = jsonObject.toString();
        try {
            String value = "";
            String sessionValue = "";
            httpPost.setEntity(new StringEntity(json, "UTF-8"));
            // 执行HTTP请求
            HttpResponse response = httpClient.execute(httpPost);
            log.info(response.toString());
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            log.info(statusCode+"");
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                String name = header.getName();
                value = header.getValue();
                String sessionKey = "session=";
                int startIndex = value.indexOf(sessionKey) + sessionKey.length();
                int endIndex = value.indexOf(";", startIndex);
                //获取到session保存到类
                sessionValue = sessionKey + value.substring(startIndex, endIndex);
            }
            // 关闭HttpClient
            httpClient.close();
            return sessionValue;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 新增保存登录信息
     * 通过登录One-API获取相应的数据
     * 请确保相应的UseName和password和One-API保持一致
     * @return "登录成功！"
     * "登录成功！但是账号密码和One-API不一致，请修改账号密码！"
     * "用户名账号错误"
     */
    @Override
    public String login(String userName, String password) {
        try {
            Integer res = apiMapper.login(userName,password);
            String urlRes = loginOneApi(userName, password);
            setSession(urlRes);
            if(res == 1 && urlRes != null && urlRes.length() > 0){
                return "登录成功！";
            }
            else if(res == 1){
                return "登录成功！但是账号密码和One-API不一致，请修改账号密码！";
            }
            else {
                return "用户名账号错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "用户名账号错误";
        }
    }

//    /**
//     * 自动更新Token方法
//     * 通过 自己用python搭建的网站baseUrlAutoToken+"/get-token 拿到token
//     * 更换fakeApiTool里存储的Token
//     */
//    public String updateToken(token token){
//        String url = baseUrlAutoToken+"/get-token";
//        try {
//            // 创建HttpClient实例
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            // 创建HttpPost请求
//            HttpPost httpPost = new HttpPost(url);
//
//            // 设置请求头部为 "application/json"
//            httpPost.setHeader("Content-Type", "application/json");
//            // 设置JSON数据
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("username", token.getUserName());
//            jsonObject.put("password", token.getPassword());
//            // 将JSON对象转换为字符串
//            String json = jsonObject.toString();
//            try {
//                String value = "";
//                httpPost.setEntity(new StringEntity(json, "UTF-8"));
//                //设置用户代理
//                String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
//                httpPost.setHeader("User-Agent", userAgent);
//                // 执行HTTP请求
//                HttpResponse response = httpClient.execute(httpPost);
//                log.info(response.toString());
//                int statusCode = response.getStatusLine().getStatusCode();
//                // 获得响应数据
//                String responseContent = EntityUtils.toString(response.getEntity());
//                // 处理响应数据
//                String access_token = null;
//                try {
//                    JSONObject jsonResponse = new JSONObject(responseContent);
//                    // 提取返回的数据
//                    log.info(jsonResponse.toString());
//                    access_token = jsonResponse.getString("token");
//                    httpClient.close();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    httpClient.close();
//                }
//                //关闭进程
//                if (statusCode == 200 && access_token.length() > 400) {
//                    log.info("Request was successful");
//                    //用来防止请求的token出现问题，回退token值
//                    return access_token;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

    /**
     * 自动更新Token方法
     * 通过https://ai.fakeopen.com/auth/login拿到token
     * 更换fakeApiTool里存储的Token
     */
    public String updateToken(token token){
        String url = "https://ai.fakeopen.com/auth/login";
        try {
            // 创建HttpClient实例
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 创建HttpPost请求
            HttpPost httpPost = new HttpPost(url);

            // 使用MultipartEntityBuilder构建表单数据
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("username", token.getUserName(), ContentType.TEXT_PLAIN);
            builder.addTextBody("password", token.getPassword(), ContentType.TEXT_PLAIN);

            // 设置请求实体
            httpPost.setEntity(builder.build());

            //设置用户代理
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
            httpPost.setHeader("User-Agent", userAgent);

            // 执行HTTP请求
            HttpResponse response = httpClient.execute(httpPost);
            log.info(response.toString());
            int statusCode = response.getStatusLine().getStatusCode();
            // 获得响应数据
            String responseContent = EntityUtils.toString(response.getEntity());
            // 处理响应数据
            String access_token = null;
            try {
                JSONObject jsonResponse = new JSONObject(responseContent);
                // 提取返回的数据
                log.info(jsonResponse.toString());
                access_token = jsonResponse.getString("access_token");
                httpClient.close();
            } catch (JSONException e) {
                e.printStackTrace();
                httpClient.close();
            }
            //关闭进程
            if (statusCode == 200 && access_token.length() > 400) {
                log.info("Request was successful");
                //用来防止请求的token出现问题，回退token值
                return access_token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 自动更新Token
     * 更换fakeApiTool里存储的Token
     * 更换One-API相应的FakeAPI
     * @return "更新成功" or "更新失败"
     */
    @Override
    public String autoUpdateToken(String name) {
        if(!existSession()){
            return "添加失败,session已过期，请重新登录";
        }
        List<token> resTokens = apiMapper.selectToken(name);
        int newToken = 0;
        for (token token : resTokens) {
            String temRes = updateToken(token);
            if (temRes != null) {
                String temToken = token.getValue();
                token.setValue(temRes);
                //执行修改token操作
                if (requiredToken(token).equals("修改成功！")) {
                    newToken++;
                } else {
                    token.setValue(temToken);
                }
            }
        }
        if (newToken == 0) {
            return "自动修改Token失败！";
        } else {
            return "自动修改Token成功：" + newToken + "失败：" + (resTokens.size() - newToken);
        }
    }

    /**
     * 刷新Token
     * 更换fakeApiTool里存储的Token
     * 更换One-API相应的FakeAPI
     * @return "更新成功" or "更新失败"
     */
    @Override
    public boolean autoUpdateSimpleToken(String name) {
        if(!existSession()){
            return false;
        }
        token token = apiMapper.selectAccuracyToken(name);
        if(token == null){
            log.info("未查询到相关数据");
            return false;
        }
        String temRes = updateToken(token);
        if (temRes != null) {
            String temToken = token.getValue();
            token.setValue(temRes);
            //执行修改token操作
            if (requiredToken(token).equals("修改成功！")) {
                return true;
            } else {
            //否则回退
                token.setValue(temToken);
            }
        }
        return false;
    }
}