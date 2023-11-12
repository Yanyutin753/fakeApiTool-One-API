package com.yyandywt99.fakeapitool.service;

import com.yyandywt99.fakeapitool.pojo.token;

import java.util.List;

/**
 * @author Yangyang
 * @create 2023-11-07 14:53
 */
public interface apiService {
    String getUpdateUrl() throws Exception;

    String requiredToken(token token);

    String addToken(token token) ;

    String deleteToken(String name);

    boolean addKeys(token token);

    List<token> seleteToken(String name);

    token selectAccuracyToken(String name);

    String requiredUser(String userName,String password);

    String login(String userName, String password);

    String autoUpdateToken(String name) throws Exception;

    boolean autoUpdateSimpleToken(String name) throws Exception;
}
