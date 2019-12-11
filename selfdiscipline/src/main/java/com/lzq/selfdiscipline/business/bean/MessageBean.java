package com.lzq.selfdiscipline.business.bean;

/**
 * 返回页面 数据实体类
 */
public class MessageBean<T> {
    // 请求返回状态码
    private String code;
    // 请求返回描述信息
    private String message;
    // 请求返回参数
    private T data;

    /**
     * 私有 无参构造方法
     */
    private MessageBean() {
    }

    public static <T> MessageBean<T> getInstance(String code, String message, Class<T> clazz){
        MessageBean<T> bean = new MessageBean<T>();
        bean.setCode(code);
        bean.setMessage(message);
        return bean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
