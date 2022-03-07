package com.chf.lightjob.model;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:13
 */
public class DataResult<T> {

    private boolean success;
    private int errorCode;
    private String errorMsg;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> DataResult<T> success(){
        return success(null);
    }

    public static <T> DataResult<T> success(T data){
        DataResult result = new DataResult();
        result.setErrorCode(0);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static <T> DataResult<T> failure(String errorMsg){
        return failure(500, errorMsg);
    }

    public static <T> DataResult<T> failure(int errorCode, String errorMsg){
        DataResult result = new DataResult();
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        result.setSuccess(false);
        return result;
    }
}
