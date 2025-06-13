package com.flowers.shopping.handler;

import com.flowers.shopping.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理异常
    @ExceptionHandler
    public Result ex(Exception e){//方法形参中指定能够处理的异常类型
        e.printStackTrace();//打印堆栈中的异常信息
        //捕获到异常之后，响应一个标准的Result
        return Result.error("对不起,操作失败,请联系管理员");
    }

    @ExceptionHandler
    public Result ex(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        log.error("参数错误:{}", msg);
        return Result.error(msg);
    }

}
