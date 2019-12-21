package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.llk.constant.MessageConstant;
import top.llk.entity.Result;
import top.llk.interfaces.MemberService;
import top.llk.interfaces.ReportService;
import top.llk.interfaces.SetmealService;
import top.llk.pojo.StartToEndTime;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */

@RestController
@RequestMapping("report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 默认获取会员数量表:12个月
     *
     * @return
     */
    @RequestMapping("getMemberReport")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//获得当前日期之前12个月的日期

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("months", list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }


    /**
     * 根据用户指定的时间段==>获取会员增加数量表
     *
     * @return
     */
    @RequestMapping("getMemberReportByTime")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public Result getMemberReportByTime(@RequestBody StartToEndTime startToEndTime) {
        Date start = null;
        Date end = null;
        try {
            //获取开始和结束月份
            start = startToEndTime.getDate_start();
            end = startToEndTime.getDate_end();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.MONTH_ERROR);
        }

        //计算月份差=后-前
        int countMonth = end.getMonth() - start.getMonth();
        if (countMonth < 0) {
            //如果前后月份相反,则差值为负数,回传错误信息
            return new Result(false, MessageConstant.MONTH_ERROR);
        }
        //以开始月份作为起始
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(start);


        List<String> list = new ArrayList<>();
        //以月份差值作为循环次数控制值
        calendar.add(Calendar.MONTH, -1);
        for (int i = 0; i <= countMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }

        //整理前端需要的数据
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 获取套餐预约数量统计
     *
     * @return
     */
    @RequestMapping("getSetmealReport")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public Result getSetmealReport() {
        try {
            HashMap<String, Object> map = setmealService.countOredersBySetmealId();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计
     *
     * @return
     */
    @RequestMapping("getBusinessReportData")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")

    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }
    }

    /**
     * 运营数据统计报告下载
     *
     * @return
     */
    @RequestMapping("exportBusinessReport")
    @PreAuthorize("hasAuthority('REPORT_VIEW')")

    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.1调用服务,获取报告需要的数据
            Map<String, Object> result = reportService.getBusinessReportData();

            //1.2取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2.读取模板文件
            //2.1获取模板的绝对路径
            ServletContext context = request.getServletContext();
            String realPath = context.getRealPath("template/report_template.xlsx");

            //2.2创建模板对象
            XSSFWorkbook workbook = new XSSFWorkbook(realPath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            //2.3写入值
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                //高精度
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }


            //3.以流的形式传输文件
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attcahment;filename=" + reportDate + "report.xlsx");
            workbook.write(os);

            //4.关闭流
            os.flush();
            os.close();
            workbook.close();

            return null;
            //会报错
            //            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

}
