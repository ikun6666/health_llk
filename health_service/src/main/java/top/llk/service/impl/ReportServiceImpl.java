package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import top.llk.dao.MemberDao;
import top.llk.dao.OrderDao;
import top.llk.interfaces.ReportService;
import top.llk.util.DateUtils;

import java.util.*;

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

    @Override
    public Map getmemberbyage() {
        //查询所有会员的年龄
        List<Integer> list = memberDao.findage();
        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        Integer d = 0;
        for (Integer integer : list) {
            if (integer >= 0 && integer <= 18) {
                a++;
            } else if (integer >= 19 && integer <= 35) {
                b++;
            } else if (integer >= 36 && integer <= 45) {
                c++;
            } else if (integer > 45) {
                d++;
            }
        }
        List<String> list1 = new ArrayList<>();
        list1.add("0-18");
        list1.add("19-35");
        list1.add("36-45");
        list1.add("45以上");

        Map map = new HashMap();
        map.put("name", "0-18");
        map.put("value", a);
        Map map1 = new HashMap();
        map1.put("name", "19-35");
        map1.put("value", b);
        Map map2 = new HashMap();
        map2.put("name", "36-45");
        map2.put("value", c);
        Map map3 = new HashMap();
        map3.put("name", "45以上");
        map3.put("value", d);
        List<Map> mapList = new ArrayList<>();
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);

        Map<String, Object> mapa = new HashMap<>();
        mapa.put("age", list1);
        mapa.put("ageCount", mapList);
        return mapa;
    }


    /**
     * 查询男女会员占比饼形图
     *
     * @return
     */
    @Override
    public Map getmemberbygenderReport() {

        List<Map> maplist = memberDao.memberbygenderCount();
        //2.定义List<String>存放男女名称
        List<String> genders = new ArrayList<>();

        //3.获取套餐名称集合
        if (maplist != null && maplist.size() > 0) {
            for (Map map : maplist) {

                String gender = Integer.parseInt(map.get("name").toString())==1?"男":"女";
                genders.add(gender);
                map.put("name",gender);
            }
        }

        //4.定义map最终返回页面需要的数据
        Map<String, Object> map2 = new HashMap<>();
        map2.put("genders", genders);
        map2.put("memberbygenderCount", maplist);
        return map2;
    }
}
