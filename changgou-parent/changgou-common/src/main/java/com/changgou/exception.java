package com.changgou;

import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/2 16:19
 */
@ControllerAdvice  // 开启spring的 公共异常处理; 主要作用在 RequestMapping 上 所有的异常都会被捕获
public class exception {

    // 处理特定异常   (空指针)
    @ExceptionHandler(value = NullPointerException.class)
    public Result error1(NullPointerException e){
        return new Result(true, StatusCode.ERROR ,"系统异常"+e.getMessage());
    }

    // 处理所有异常
    @ExceptionHandler(value = Exception.class)
    public Result error1(Exception e){
        return new Result(true,StatusCode.ERROR ,"系统异常"+e.getMessage());
    }

}
