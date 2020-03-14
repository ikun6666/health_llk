package top.llk.aopLog;

import com.alibaba.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import top.llk.interfaces.OperationLogService;
import top.llk.pojo.OperationLog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2020/3/14
 * @Content:
 */
@Aspect
@Component
public class AspectLog {
    @Reference
    private OperationLogService operationLogService;

    @Around("execution(* top.llk.controller.*.*(..))")
    public Object insertLogAround(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("*******************************记录日志[start]********************************");
        OperationLog operationLog = new OperationLog();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //指定操作开始时间
        operationLog.setOperateTime(sdf.format(new Date()));
        //指定操作用户名
        //获取当前登录用户的用户名
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        operationLog.setOperateUser(user.getUsername());
        operationLog.setOperateClass(pjp.getTarget().getClass().toString());
        //获取操作的方法名
        operationLog.setOperateMethod(pjp.getSignature().getName());

        //获取请求的参数
        Object[] args = pjp.getArgs();
        operationLog.setParamAndValue(Arrays.toString(args));
        long start = System.currentTimeMillis();

        //放行

        Object proceed = pjp.proceed();

        long end = System.currentTimeMillis();
        System.out.println("---------------------->用时:" + (end - start) + "ms");
        //记录用时
        operationLog.setCostTime((int) (end - start));


        if (proceed != null) {
            operationLog.setReturnClass(proceed.getClass().toString());
            operationLog.setReturnValue(proceed.toString());
        }else {
            operationLog.setReturnClass("java.lang.Object");
            operationLog.setReturnValue("void");
        }

        operationLogService.add(operationLog);
        System.out.println("*******************************记录日志[end]********************************");

        return proceed;
    }
}
