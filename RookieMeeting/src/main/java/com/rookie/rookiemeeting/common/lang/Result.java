package com.rookie.rookiemeeting.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;  //200是正常，非200表示异常
    private String msg;
    private Object data;

    //成功消息
    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    //失败消息
    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    //失败消息
    public static Result fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result succ(int code, String msg, Object data) {
        Result rs = new Result();
        rs.setCode(code);
        rs.setMsg(msg);
        rs.setData(data);
        return rs;
    }

    public static Result fail(int code, String msg, Object data) {
        Result rs = new Result();
        rs.setCode(code);
        rs.setMsg(msg);
        rs.setData(data);
        return rs;
    }
}
