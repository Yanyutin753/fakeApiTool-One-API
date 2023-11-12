package com.yyandywt99.fakeapitool.mapper;

import com.yyandywt99.fakeapitool.pojo.addKeyPojo;
import com.yyandywt99.fakeapitool.pojo.token;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Yangyang
 * @create 2023-11-07 15:07
 */
@Mapper
public interface apiMapper {
    /**
     * @更新updateUrl
     * @更新oneAPi里的fakeApi调用地址
     */
    @Update("update channels set base_url = #{newDateUrl} where base_url like '%fakeopen.com%'")
    public void toUpdateUrl(String newDateUrl);

    /**
     * @更新updateApi
     * @更新oneAPi里的fakeApi的api
     */
    void updateApi(String api,Integer count);

    @Insert("INSERT INTO oneapi.channels (type, `key`, status, name," +
            " weight, created_time, test_time, response_time, base_url," +
            " other, balance, balance_updated_time, models, `group`, used_quota," +
            " model_mapping, priority) VALUES (8, #{key}, 1, #{name}, 0, UNIX_TIMESTAMP(NOW())," +
            "UNIX_TIMESTAMP(NOW()), 6, 'https://ai.fakeopen.com/', ''," +
            " 0, 0, 'gpt-3.5-turbo,gpt-3.5-turbo-0301,gpt-3.5-turbo-0613,gpt-3.5-turbo-16k,gpt-3.5-turbo-16k-0613'," +
            " 'default', 0, '', 1);")
    void addKeys(addKeyPojo addKeyPojo);

    @Select("select id from channels where name = #{name}")
    List<Integer> deleteKeys(String name);

    @Insert("insert into fakeapitool (name, value, userName, password, updateTime)" +
            " values (#{name},#{value},#{userName},#{password},now())")
    void addToken(token token);

    @Delete("delete from fakeapitool where name = #{name};")
    void deleteToken(String name);

    void requiredToken(token tem);

    /**
     * 通过构造CONCAT()来连接%值%
     */
    @Select("select name, value, userName, password, updateTime from fakeapitool where name like CONCAT('%', #{name}, '%')")
    List<token> selectToken(String name);

    void requiredUser(String userName,String password);

    @Select("select count(*) from fakeapitooluser" +
            " where name = #{userName} and password = #{password};")
    Integer login(String userName, String password);

    /**
     * 精确查找
     */
    @Select("select name, value, userName, password, updateTime from fakeapitool where name = #{name}")
    token selectAccuracyToken(String name);
}
