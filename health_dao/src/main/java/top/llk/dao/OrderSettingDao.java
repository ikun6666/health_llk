package top.llk.dao;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.llk.pojo.OrderSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */
public interface OrderSettingDao {
    /**
     * 根据所选日期查询当日的可预约情况
     *
     * @param date
     * @return
     */
    @Select("select * from t_ordersetting where orderDate = #{value}")
    OrderSetting queryOrderSettingByDate(String date);

    /**
     * 修改日期和预约数
     *
     * @param
     * @return
     */
    int updateOrders(OrderSetting orderSetting);

    /**
     * 插入日期和预约数
     *
     * @param
     */
    @Insert("INSERT INTO t_ordersetting (orderDate,number) VALUES (#{orderDate},#{number})")
    void insertOrder(OrderSetting orderSetting);

    /**
     * 模糊查找月份的所有数据
     * SELECT * FROM t_ordersetting WHERE orderDate LIKE '2019-05%'
     *
     * @param date
     * @return
     */
    @Select("SELECT * FROM t_ordersetting WHERE orderDate LIKE #{value}")
    List<OrderSetting> queryOrderSettings(String date);

}
