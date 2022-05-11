package com.Parsing.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  //标识当前类是一个全局异常处理类
public class GlobalException {
    private final Logger logger = LogManager.getLogger(GlobalException.class);
    // 处理算术异常
    @ExceptionHandler(value = {java.lang.ArithmeticException.class})
    public String  handleArithmeticException(Exception e){
        logger.error(e.getMessage());
        return e.getMessage();
    }
    //处理空指针异常
    @ExceptionHandler(value = {java.lang.NullPointerException.class})
    public String handleNullPointerException(Exception e){
        logger.error(e.getMessage());
        return e.getMessage();
    }
    //处理数组下标越界异常
    @ExceptionHandler(value = {java.lang.ArrayIndexOutOfBoundsException.class})
    public String handleArrayIndexOutOfBoundsException(Exception e){
        logger.error(e.getMessage());
        return e.getMessage();
    }

    // ZipException
    @ExceptionHandler(value = {net.lingala.zip4j.exception.ZipException.class})
    public String ZipException(Exception e){
        logger.error(e.getMessage());
        return "压缩文件不合法,可能被损坏";
    }
}
