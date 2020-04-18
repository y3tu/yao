package com.y3tu.yao.upms.server.exception;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 * 通用权限服务异常
 *
 * @author y3tu
 */
public class UpmsException extends BaseException {
    public UpmsException() {
        super();
    }

    public UpmsException(String message) {
        super(message);
    }

    public UpmsException(Throwable e) {
        super(e);
    }

    public UpmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpmsException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public UpmsException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public UpmsException(IError error) {
        super(error);
    }

    public UpmsException(String message, IError error) {
        super(message, error);
    }

    public UpmsException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public UpmsException(Throwable cause, IError error) {
        super(cause, error);
    }
}
