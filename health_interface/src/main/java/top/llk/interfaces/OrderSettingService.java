package top.llk.interfaces;

import top.llk.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */
public interface OrderSettingService {
    /**
     * 批量修改数据库中的预约时间,预约数量
     * string[] of list
     *
     * @param list
     * @return
     */
    void setOrders(List<String[]> list) throws Exception;

    /**
     * 查询年.月的预约情况
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryMonthOrders(Integer currentYear, Integer currentMonth);
    /**
     * 设置某一天的约束数
     *
     * @param number
     * @param day
     * @return
     */
    void setNumber(Date day, Integer number) throws Exception;
}
