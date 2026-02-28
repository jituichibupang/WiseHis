package com.cclinux.framework.exception;

import com.cclinux.framework.constants.AppCode;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public AppException() {
        this.code = AppCode.ERROR_SVR;
    }

    public AppException(String msg) {
        super(msg);
        this.code = AppCode.ERROR_LOGIC;
        this.msg = msg;
    }

    public AppException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public AppException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public AppException( int code) {
        super("");
        this.msg = "";
        this.code = code;
    }

    public AppException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
