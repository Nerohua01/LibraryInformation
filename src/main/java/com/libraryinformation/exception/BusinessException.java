package com.libraryinformation.exception;

/**
 * @author Nerohua
 */
public class BusinessException extends RuntimeException {
    private Integer zh3317_code;

    public Integer getZh3317_code() {
        return zh3317_code;
    }

    public void setZh3317_code(Integer zh3317_code) {
        this.zh3317_code = zh3317_code;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.zh3317_code = code;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.zh3317_code = code;
    }

}
