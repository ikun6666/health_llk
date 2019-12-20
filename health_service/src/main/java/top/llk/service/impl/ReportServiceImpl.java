package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import top.llk.dao.MemberDao;
import top.llk.dao.OrderDao;
import top.llk.interfaces.ReportService;
import top.llk.util.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/14
 * @Content:
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        /*前端需求数据:map{
                            reportDate 今日日期 ok
                            todayNewMember: 今日新增,
                            totalMember: 0,总会员数 ok
                            thisWeekNewMember: 0,本周新增会员数 ok
                            thisMonthNewMember: 0,本月新增会员数 ok
                            todayOrderNumber: 0,今日预约数  ok
                            todayVisitsNumber: 0, 今日到诊数 ok
                            thisWeekOrderNumber: 0, 本周预约数 ok
                            thisWeekVisitsNumber: 0, 本周到诊数 ok
                            thisMonthOrderNumber: 0,本月预约数 ok
                            thisMonthVisitsNumber: 0,本月到诊数 ok
         hotSetmeal: [
           {name: '阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐', setmeal_count: 200, proportion: 0.222},
           {name: '阳光爸妈升级肿瘤12项筛查体检套餐', setmeal_count: 200, proportion: 0.222}
                ]
                        }
                */
        HashMap<String, Object> reportData = new HashMap<>();
//========================设置指定日期=================
        //报告日期:今日日期 "2019-12-14"
        String todayDate = DateUtils.parseDate2String(new Date());
        reportData.put("reportDate", todayDate);

        //本周的第一天
        String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(new Date()));

        //本月第一天
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());


//============================会员数据统计====================
        //今天之前的会员数
        Integer memberCountBeforeToday = memberDao.findMemberCountBeforeDate(todayDate);

        //本周第一天之前的会员数
        Integer memberCountBeforeWeek = memberDao.findMemberCountBeforeDate(firstDayOfWeek);

        //本月之前的会员数
        Integer memberCountBeforeMonth = memberDao.findMemberCountBeforeDate(firstDayOfMonth);

        //本日会员总数 2019-12-14
        Integer totalMember = memberDao.findMemberTotalCount();
        reportData.put("totalMember", totalMember);

        //今天新增的会员数 (今日-昨天)
        reportData.put("todayNewMember", totalMember - memberCountBeforeToday);

        //本周新增会员数(今日-本周第一天)
        reportData.put("thisWeekNewMember", totalMember - memberCountBeforeWeek);

        //本月新增会员数 2019-12
        reportData.put("thisMonthNewMember", totalMember - memberCountBeforeMonth);

//=========================预约到诊数据统计=================================
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(todayDate);
        reportData.put("todayOrderNumber", todayOrderNumber);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(todayDate);
        reportData.put("todayVisitsNumber", todayVisitsNumber);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(firstDayOfWeek);
        reportData.put("thisWeekOrderNumber", thisWeekOrderNumber);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfWeek);
        reportData.put("thisWeekVisitsNumber", thisWeekVisitsNumber);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDayOfMonth);
        reportData.put("thisMonthOrderNumber", thisMonthOrderNumber);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfMonth);
        reportData.put("thisMonthVisitsNumber", thisMonthVisitsNumber);

        //热门套餐
        List<Map<String, Object>> hotSetmeal = orderDao.findHotSetmeal();
        reportData.put("hotSetmeal", hotSetmeal);

        return reportData;
    }


}
