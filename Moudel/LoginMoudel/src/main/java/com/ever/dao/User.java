package com.ever.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    private Integer state;  // 0代表离线状态，1代表在线状态

    @JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss",timezone = "Asia/Shanghai")
    private Date expireTime;

}

