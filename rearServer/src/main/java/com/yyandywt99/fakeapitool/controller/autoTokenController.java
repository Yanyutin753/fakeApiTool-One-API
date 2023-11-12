package com.yyandywt99.fakeapitool.controller;

import com.yyandywt99.fakeapitool.pojo.Result;
import com.yyandywt99.fakeapitool.pojo.token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yangyang
 * @create 2023-11-11 18:19
 */
@RestController
@RequestMapping("/api")
public class autoTokenController {
    @Autowired
    private com.yyandywt99.fakeapitool.service.apiService apiService;

    /**
     * 自动更新Token
     * 更换fakeApiTool里存储的Token
     * 更换One-API相应的FakeAPI
     * @return "更新成功" or "更新失败"
     * @throws Exception
     */
    @Scheduled(cron = "0 3 0 */5 * ?")
    public Result toUpdateToken() throws Exception{
        try {
            String res = apiService.autoUpdateToken("");
            if(res.contains("自动修改Token成功")){
                return Result.success(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("更新失败");
    }

    @GetMapping("updateToken")
    public Result toUpdateToken(@RequestParam("name") String name) throws Exception{
        try {
            boolean res = apiService.autoUpdateSimpleToken(name);
            token tem = apiService.selectAccuracyToken(name);
            if(tem != null){
                String resToken = tem.getValue();
                if(resToken.length() > 0 && res ){
                    return Result.success(tem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("刷新Token失败");
    }
}
