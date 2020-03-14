package aopLog;

import java.lang.annotation.*;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2020/3/14
 * @Content:
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
}
