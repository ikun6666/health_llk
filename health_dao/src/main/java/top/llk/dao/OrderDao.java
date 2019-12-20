package top.llk.dao;

import org.apache.ibatis.annotations.Select;
import top.llk.pojo.Order;
import top.llk.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 执行预约操作
 *
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/11
 * @Content:
 */

public interface OrderDao {

    /**
     * 添加预约
     *
     * @param order
     */
    void add(Order order);

    /**
     * 根据预约信息查找预约情况
     *
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 查询预约详情
     *
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);

    /**
     * 查询指定日期的预约数
     *
     * @param date
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_order WHERE orderDate = #{value}")
    Integer findOrderCountByDate(String date);

    /**
     * 查询指定日期之后的预约数
     *
     * @param date
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_order WHERE orderDate >= #{value}")
    Integer findOrderCountAfterDate(String date);

    /**
     * 查询当日已到诊人数
     *
     * @param date
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_order WHERE orderStatus = '已到诊' AND orderDate = #{value}")
    Integer findVisitsCountByDate(String date);


    /**
     * 查询指定日期之后的已到诊人数
     *
     * @param date
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_order WHERE orderStatus = '已到诊' AND orderDate >= #{value}")
    Integer findVisitsCountAfterDate(String date);

    /**
     * 查询热门前两名
     *
     * @return
     */
    List<Map<String, Object>> findHotSetmeal();


}
