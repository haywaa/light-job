package com.chf.lightjob.constants;

import com.chf.lightjob.exception.BizException;
import com.chf.lightjob.model.DataResult;

/**
 * @description
 * @author: davy
 * @create: 2022-03-07 15:45
 */
public enum ErrorCode {

    FAIL_CODE(500, "服务异常"),

    AUTH_UNAUTHORIZED(401, "未登陆"),

    INVALID_PARAM(10101, "无效的参数"),
    INVALID_USER_OR_PASSWORD(10103, "无效的账号或密码");

    private final Integer errorNo;
    private final String errorMsg;

    ErrorCode(Integer errorNo, String errorMsg) {
        this.errorNo = errorNo;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorNo() {
        return errorNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public BizException toBizException() {
        return new BizException(getErrorNo(), getErrorMsg());
    }

    public BizException toBizException(String message) {
        return new BizException(getErrorNo(), message);
    }

    public <T> DataResult<T> toDataResult() {
        return DataResult.failure(getErrorNo(), getErrorMsg());
    }

    public <T> DataResult<T> toDataResult(String message) {
        return DataResult.failure(getErrorNo(), message);
    }
}
