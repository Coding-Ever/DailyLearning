package com.ever.common;

import lombok.Data;

@Data
public class RespBean {
    private String code;
    private String netState;
    private String msg;

    public static RespBean getInstance(){
        return new RespBean();
    }

    public static RespBean error(String msg){
        RespBean instance = getInstance();
        instance.setCode("400");
        instance.setNetState("网络出现问题！");
        instance.setMsg(msg);
        return instance;
    }

    public static RespBean success(String msg){
        RespBean instance = getInstance();
        instance.setCode("200");
        instance.setNetState("OK");
        instance.setMsg(msg);
        return instance;
    }
}
