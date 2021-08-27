package com.yizhishang.common.exception;

import com.yizhishang.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author yizhishang
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseData handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return ResponseData.error("数据库中已存在该记录");
    }

    @ExceptionHandler(BindException.class)
    public ResponseData handBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        logger.error("{}", bindingResult);
        ObjectError error = bindingResult.getAllErrors().get(0);
        String message = error.getDefaultMessage();
        String objectName = error.getObjectName();
        return ResponseData.error(objectName + ": " + message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseData handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseData.error(e.getMessage());
    }
}
