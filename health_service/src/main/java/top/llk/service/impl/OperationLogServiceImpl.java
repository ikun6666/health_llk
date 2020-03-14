package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import top.llk.dao.OperationLogDao;
import top.llk.interfaces.OperationLogService;
import top.llk.pojo.OperationLog;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2020/3/14
 * @Content:
 */
@Service(interfaceClass = OperationLogService.class)
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;
    /**
     * 添加日志
     *
     * @param operationLog
     */
    @Override
    public void add(OperationLog operationLog) {
        operationLogDao.insert(operationLog);
        System.out.println("------------------------------------------------插入成功------------------------------------------------");
    }
}
