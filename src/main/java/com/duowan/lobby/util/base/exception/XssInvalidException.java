/**
 * Copyright 2010-2020 duowan
 */
package com.duowan.lobby.util.base.exception;

/**
 * @author hejiale
 *
 * create at 2014-6-20 下午8:08:23
 * 
 * XSS检测异常
 * 
 */
public class XssInvalidException extends SecurityException{

    private static final long serialVersionUID = 7016967341270813085L;

    public XssInvalidException() {
        super();
    }

    public XssInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public XssInvalidException(String s) {
        super(s);
    }

    public XssInvalidException(Throwable cause) {
        super(cause);
    }
}
