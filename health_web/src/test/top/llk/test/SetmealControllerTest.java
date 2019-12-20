package top.llk.test;

import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.controller.SetmealController;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:springmvc.xml")
public class SetmealControllerTest {

    @Autowired
    private SetmealController setmealController;

    @Test
    public void testFindPage() {
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setQueryString("入职无忧体检套餐（男女通用）");
        queryPageBean.setPageSize(10);
        queryPageBean.setCurrentPage(1);

        PageResult page = setmealController.findPage(queryPageBean);
        System.out.println(page);
    }


    @Test
    public void test7niu() {
        String accessKey = "u4j1NWxD7-QOKvcoWaXgx6Rk9qBzAMHmmJKFnvND";
        String secretKey = "XOt2o-PSfPLPvO7hTHEAAUMh6M7BwcR2ecsLLsGs";
        String bucket = "7niumemory";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
    }
}
