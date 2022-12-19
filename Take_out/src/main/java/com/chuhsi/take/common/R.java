package com.chuhsi.take.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结构类
 *
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private Integer code;   //1成功，0失败

    private String msg;     //错误信息

    private T data;         //数据

    private Map map = new HashMap();  //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
