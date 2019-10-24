package com.ebupt.txcy.yellowpagelibbak.aop;

import java.util.Optional;

import com.ebupt.txcy.serviceapi.vo.Response;
import com.ebupt.txcy.serviceapi.vo.ResponseResult;
import com.ebupt.txcy.yellowpagelibbak.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class GlobalExceptionHandler {

    /**
     * 统一处理 Hibernate Validator 参数校验异常
     */
    @ExceptionHandler
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return Response.error(ResponseResult.PARAMETER_ERROR.getCode(),
                ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    public Response<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return Response.error(ResponseResult.PARAMETER_NULL.getMsg() + ex.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Void> handleHttpMessageNotReadableException() {
        return Response.error(ResponseResult.PARAMETER_ERROR);
    }
    @ExceptionHandler
    public Response<Void> handleServiceException(ServiceException ex) {
        return Response.error(Optional.ofNullable(ex.getMessage()).orElse(ResponseResult.SERVER_ERROR.getMsg()));
    }

    @ExceptionHandler
    public Response<Void> handleOtherException(Exception ex) {
        log.error("服务器内部错误", ex);
        return Response.error(ResponseResult.SERVER_ERROR);
    }

}
