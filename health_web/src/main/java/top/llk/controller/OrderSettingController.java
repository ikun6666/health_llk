package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.llk.constant.MessageConstant;
import top.llk.entity.Result;
import top.llk.interfaces.OrderSettingService;
import top.llk.pojo.OrderSetting;
import top.llk.util.POIUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置控制层
 *
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */
@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 接收上传的xml,读取并批量修改数据库中的预约时间,预约数量
     *
     * @param excelFile 批量设置预约数的表格
     * @return
     */
    @RequestMapping("upload")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result upload(MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            /**
             *  String[] strings of list
             String orderDate = strings[0]; 日期
             String number = strings[1];    预约总数
             */

            orderSettingService.setOrders(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);

        }
    }

    /**
     * 查询年.月的预约情况,回传给页面
     *
     * @param currentYear  页面显示的年
     * @param currentMonth 页面显示的月
     * @return
     */
    @RequestMapping("queryMonthOrders")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public List<Map<String, Object>> queryMonthOrders(Integer currentYear, Integer currentMonth) {
        return orderSettingService.queryMonthOrders(currentYear, currentMonth);
    }

    /**
     * 设置某一天的预约数
     *
     * @param number 可预约数量
     * @param day    指定日期
     * @return
     */
    @RequestMapping("setNumber")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result setNumber(Integer number, @RequestBody Date day) {
        try {
            orderSettingService.setNumber(day, number);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);

        }
    }
}
