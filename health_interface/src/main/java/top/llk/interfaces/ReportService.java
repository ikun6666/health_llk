package top.llk.interfaces;

import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/14
 * @Content:
 */
public interface ReportService {


    /**
     * 获取套餐预约数量统计
     * @return
     */
    Map<String,Object> getBusinessReportData() throws Exception;
}
