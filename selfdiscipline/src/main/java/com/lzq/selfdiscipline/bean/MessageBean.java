package com.lzq.selfdiscipline.bean;

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

    public static MessageBean<?> getInstance(String code, String message){
        MessageBean messageBean = new MessageBean();
        messageBean.setCode(code);
        messageBean.setMessage(message);
        return messageBean;
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
