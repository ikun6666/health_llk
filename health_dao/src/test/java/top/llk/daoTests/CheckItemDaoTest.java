package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.CheckItemDao;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.CheckItem;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/3
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class CheckItemDaoTest {

    @Autowired
    private CheckItemDao checkItemDao;


    @Test
    public void testCountItems() {
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setCurrentPage(1);
        queryPageBean.setIndex(0);
        queryPageBean.setPageSize(10);
        queryPageBean.setQueryString("");


        Long aLong = checkItemDao.countItems(queryPageBean);
        System.out.println("查到的页数...........:" + aLong);
    }

    @Test
    public void testQueryCheckItems() {
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setCurrentPage(1);
        queryPageBean.setIndex(0);
        queryPageBean.setPageSize(10);
        queryPageBean.setQueryString("尿糖");
//        queryPageBean.setQueryString("%29%");
        List<CheckItem> checkItems = checkItemDao.queryCheckItems(queryPageBean);
//        checkItems.forEach(System.out::println);
        System.out.println(checkItems.size());
    }

    @Test
    public void testAdd() {
        CheckItem checkItem = new CheckItem();
        checkItem.setCode("1099");
        checkItem.setName("弹跳高度");
        checkItem.setRemark("看看你能跳多高");

        checkItemDao.add(checkItem);
    }

    @Test
    public void testqueryBeUsedById() {
        int i = checkItemDao.queryBeUsedCountById("1008");
        System.out.println("这个检查项被" + i + "个检查组使用");
    }

    @Test
    public void testDeleteCheckItemById() {

        checkItemDao.deleteCheckItemById("99");
    }

    @Test
    public void testEditCheckItem() {
//        checkItemDao.editCheckItem(null);
        CheckItem checkItem = new CheckItem();
        checkItem.setRemark("测试修改remark");
        checkItem.setId(93);

        checkItemDao.editCheckItem(checkItem);
    }

    @Test
    public void testFindAll() {
        System.out.println(checkItemDao.countAllCheckItem());

    }

    @Test
    public void testFindAllCheckItem() {
        System.out.println(checkItemDao.findAllCheckItem());
    }
}
