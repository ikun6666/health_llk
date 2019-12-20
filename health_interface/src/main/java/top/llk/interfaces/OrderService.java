package top.llk.interfaces;

import top.llk.entity.Result;

import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
public interface OrderService {
    /**
     * 预约体检
     *
     * @param map
     * @return
     */
    Result order(Map map) throws Exception;

    /**
     * 查询预约详情
     * @param orderId
     * @return
     */
    Result findById4Detail(Integer orderId) throws Exception;
}
