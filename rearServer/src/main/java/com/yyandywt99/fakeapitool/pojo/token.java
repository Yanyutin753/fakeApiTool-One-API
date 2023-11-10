package com.yyandywt99.fakeapitool.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class token {
    private String name;
    private String value;
    private String userName;
    private String password;
    private String updateTime;

}