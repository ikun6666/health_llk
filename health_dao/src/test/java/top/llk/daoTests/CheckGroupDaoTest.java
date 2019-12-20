package top.llk.daoTests;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.CheckGroupDao;
import top.llk.dao.SetmealDao;
import top.llk.pojo.CheckGroup;
import top.llk.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/5
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class CheckGroupDaoTest {

    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private SetmealDao setmealDao;

    @Test
    public void testSelectByCondition() {
        Page<CheckGroup> page = checkGroupDao.selectByCondition("一般检查");
        System.out.println(page);
    }

    @Test
    public void testAddCheckGroup() {
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setCode("188");
        checkGroup.setName("测试10088");


        checkGroupDao.addCheckGroup(checkGroup);
        System.out.println(checkGroup.getId());

    }

    @Test
    public void testAddGroupandItem() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("checkGroupId", 13);
        map.put("checkitemId", 92);
        checkGroupDao.addCheckGroupAndCheckitem(map);
    }

    @Test
    public void testFindAllCheckItem() {
        CheckGroup cg = checkGroupDao.findCheckGroupById(5);

        System.out.println(cg);
    }

    @Test
    public void testFindCheckItemIdsByCheckGroupId() {
        Integer[] ids = checkGroupDao.findCheckItemIdsByCheckGroupId(5);
        System.out.println(ids.length);
    }

    @Test
    public void testEditCheckGroup() {
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setId(28);
        checkGroup.setCode("188");
        checkGroup.setName("测试修改2");
        checkGroup.setAttention("测试修改2");
        checkGroup.setRemark("测试修改2");

        checkGroupDao.editCheckGroup(checkGroup);
    }

    @Test
    public void testfindSetmealById() {
        Setmeal setmeal = setmealDao.findSetmealById(12);
        System.out.println(setmeal);
    }


    @Test
    public void testFindCheckGroupById() {
        CheckGroup checkGroupById = checkGroupDao.findCheckGroupById(5);
        System.out.println(checkGroupById);
    }


    @Test
    public void testFindSetmealById() {
        System.out.println("=============================================================");

        Setmeal setmealById = setmealDao.findSetmealById(12);
        System.out.println(setmealById);
        System.out.println("=============================================================");
        List<CheckGroup> checkGroups = setmealById.getCheckGroups();
        checkGroups.forEach(System.out::println);
        System.out.println("=============================================================");

    }
}
