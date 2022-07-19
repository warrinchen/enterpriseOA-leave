package com.exp.oa.service.exception;

/**
 * 业务逻辑异常
 */
public class BusinessException extends RuntimeException{
    private String code; //异常编码
    private String message; //异常的具体文本信息

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessException(String code, String message) {
        super(code + ":" + message);
        this.code = code;
        this.message = message;


    }
}
