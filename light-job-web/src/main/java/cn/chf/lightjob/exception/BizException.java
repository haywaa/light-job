package cn.chf.lightjob.exception;

/**
 * @Author ChenHaifeng
 * @Date 2018/9/23 下午2:37
 */
public class BizException extends RuntimeException {

    private int errorCode = -1;
    private String errorInfo;

    public BizException(String errorInfo){
        this(-1, errorInfo);
    }

    public BizException(int errorCode, String errorInfo){
        super(errorInfo);
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }
}