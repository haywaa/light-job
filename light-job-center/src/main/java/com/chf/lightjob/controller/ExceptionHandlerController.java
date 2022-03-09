package com.chf.lightjob.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSON;
import com.chf.lightjob.constants.ErrorCode;
import com.chf.lightjob.exception.BizException;
import com.chf.lightjob.model.DataResult;

/**
 * @description
 * @author: wenbai
 * @create: 2019-07-30 19:45
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public DataResult handleBizException(BizException ex) {
        return DataResult.failure(ex.getErrorCode(), ex.getErrorInfo());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public DataResult handleException(Exception ex, HttpServletRequest request) {
        logger.error("controller execption => path: [" + request.getServletPath() + "], params: [" + JSON.toJSONString(request.getParameterMap()) + "]", ex);
        return DataResult.failure(ErrorCode.FAIL_CODE.getErrorNo(), ErrorCode.FAIL_CODE.getErrorMsg());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public DataResult handle(MethodArgumentNotValidException exception) {
        StringBuffer errorMsg = new StringBuffer();

        BindingResult result = exception.getBindingResult();
        for (ObjectError error : result.getAllErrors()) {
            errorMsg.append(error.getDefaultMessage());
            errorMsg.append("\n");
        }
        return DataResult.failure(errorMsg.toString());
    }
}
