package aopLog;

import org.aspectj.lang.annotation.Around;
import top.llk.pojo.OperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2020/3/14
 * @Content:
 */
@Aspect
@Component
public class OperateAdvice {

    @Around("execution(* top.llk.controller.*.*(..))")
    public Object insertLogAround(ProceedingJoinPoint pjp, OperateLog operateLog) throws Throwable {
        System.out.println("*************************************************记录日志[start" +
                "]*************************************************");
        long start = System.currentTimeMillis();
        OperationLog operationLog = new OperationLog();
        Object object = pjp.proceed();
        long end = System.currentTimeMillis();
        System.out.println("------------->执行时间为:"+(end-start)+"ms");
        return object;
    }
}
