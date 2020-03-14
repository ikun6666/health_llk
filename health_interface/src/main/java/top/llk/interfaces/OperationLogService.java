package top.llk.interfaces;

import top.llk.pojo.OperationLog;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2020/3/14
 * @Content:
 */
public interface OperationLogService {
    /**
     * 添加日志
     * @param operationLog
     */
    void add(OperationLog operationLog);
}
