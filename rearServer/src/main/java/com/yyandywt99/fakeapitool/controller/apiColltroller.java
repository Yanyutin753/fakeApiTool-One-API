package com.yyandywt99.fakeapitool.controller;

import com.yyandywt99.fakeapitool.pojo.Result;
import com.yyandywt99.fakeapitool.pojo.token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yangyang
 * @create 2023-11-07 14:55
 */
@Slf4j
@RestController()
@RequestMapping("/api")
public class apiColltroller {
    @Autowired
    private com.yyandywt99.fakeapitool.service.apiService apiService;

    /**
     * 每天8点触发
     * @return
     * 更新fakeapiUrl
     */
    @Scheduled(cron = "0 03 10 * * ?")
    public void autoUpdateUrl() throws Exception{
        try {
            String res = apiService.getUpdateUrl();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新失败");
        }
    }

    @GetMapping("updateUrl")
    public Result toUpdateUrl() throws Exception{
        try {
            String res = apiService.getUpdateUrl();
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新失败");
        }
    }

    /**
     * @param token
     * @return 添加token并生成三个fk-的fakeApiKey
     */
    @PostMapping("addToken")
    public Result addToken(@RequestBody token token){
        try {
            String res = apiService.addToken(token);
            if(res.equals("添加成功！")){
                return Result.success(res);
            }
            else{
                return Result.error(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    /**
     * @param name
     * @return 通过token用户名，删除fakeapitool表里的token
     * 并删除对应的三个fk-的fakeApiKey
     */
    @PutMapping("deleteToken")
    public Result deleteToken(@RequestParam String name) throws Exception{
        try {
            String res = apiService.deleteToken(name);
            log.info(res);
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }

    /**
     * @param token
     * @return 通过传入token，修改fakeapitool表里的token
     * 并修改对应的三个fk-的fakeApiKey
     */
    @PostMapping("requiredToken")
    public Result requiredToken(@RequestBody token token){
        try {
            String res = apiService.requiredToken(token);
            if(res.equals("添加成功！")){
                return Result.success(res);
            }
            else{
                return Result.error(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改token失败");
        }
    }

    /**
     * @return 获取到全部token类
     */
    @GetMapping("seleteToken")
    public Result seleteToken(@RequestParam("name") String name){
        try {
            List<token> res = apiService.seleteToken(name);
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取失败");
        }
    }

    /**
     * 修改用户信息
     * @return "修改用户成功！"or"修改用户失败"
     */
    @PostMapping("requiredUser")
    public Result requiredUser(@RequestParam("userName") String userName,
                               @RequestParam("password") String password){
        try {
            String res = apiService.requiredUser(userName,password);
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改用户失败");
        }
    }

    /**
     * 登录用户接口
     * @return "jwt令牌！"or"NOT_LOGIN"
     */
    @PostMapping("/login")
    public Result login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password) {
        String res = apiService.login(userName,password);
        if(res.contains("登录成功")){
            log.info("登录成功");
            Map<String,Object> chaims = new HashMap<String,Object>();
            chaims.put("id",1);
            String s = com.yyandywt99.fakeapitool.util.JwtUtils.generateJwt(chaims);
            return Result.success(s);
        }
        return Result.error("登陆失败");
    }

    /**
     * 验证是否登录成功
     * @return 没登陆成功否则返回"NOT_LOGIN"
     */
    @PostMapping("/loginToken")
    public Result loginToken(@RequestParam("token") String token){
        log.info(token);
        if(!StringUtils.hasLength(token)){
            log.info("请求头token为空,返回未登录的信息");
            return Result.error("NOT_LOGIN");
        }
        try {
            com.yyandywt99.fakeapitool.util.JwtUtils.parseJWT(token);
            log.info("令牌合法，可以正常登录");
            return Result.success("YES_LOGIN");
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("解析令牌失败, 返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
            return error;
        }
    }
}
