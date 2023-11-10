package com.yyandywt99.fakeapitool.service.impl;

import com.yyandywt99.fakeapitool.mapper.apiMapper;
import com.yyandywt99.fakeapitool.pojo.token;
import com.yyandywt99.fakeapitool.service.apiService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 自动更新每日FakeOPenAI地址
     * @return 每日FakeOPenAI地址
     */
    @Override
    public String getUpdateUrl() throws Exception {
        try {
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

            if (responseCode == HttpURLConnection.HTTP_OK) {
                apiMapper.toUpdateUrl(newDateUrl);
                return "修改成功";
            }
            else {
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
        try {
            List<String> keys = getKeys(tem);
            if(keys != null){
                apiMapper.requiredToken(tem);
                deletekeys(tem.getName());
                addKeys(tem);
                return "修改成功！";
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
     * 添加token
     * 并添加对应keys
     * @return "添加成功！"or"添加失败,检查你的token是否正确！"
     */
    @Override
    public String addToken(token token) {
        try {
            if(addKeys(token)){
                apiMapper.addToken(token);
                return "添加成功！";
            }
            else {
                return "添加失败,检查你的token是否正确！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加失败");
            return "添加失败,检查你的token是否正确！";
        }

    }

    /**
     * 删除token
     * 并删除对应keys
     * @return "删除成功！"or"删除失败"
     */
    @Override
    public String deleteToken(String name){
        try {
            apiMapper.deleteToken(name);
            deletekeys(name);
            return "删除成功！";
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
        try {
            List<String> keys = getKeys(token);
            if(keys != null){
                try {
                    for (String key : keys){
                        apiMapper.addKeys(token.getName(),key);
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
    @Override
    public void deletekeys(String name) {
        try {
            apiMapper.deleteKeys(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public String login(String userName, String password) {
        try {
            Integer res = apiMapper.login(userName,password);
            log.info(res.toString());
            if(res == 1){
                return "登录成功！";
            }
            return "用户名账号错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "用户名账号错误";
        }
    }
}
