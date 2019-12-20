package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.MenuDao;
import top.llk.dao.RoleDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.MenuService;
import top.llk.pojo.Menu;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 菜单列表分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
//        PageHelper.orderBy("path");
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Menu> menus = menuDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(menus.getTotal(), menus.getResult());
    }

    /**
     * 删除菜单项
     *
     * @param id
     * @return
     */
    @Override
    public void deleteById(Integer id) throws Exception {
        //1.菜单必须没有被使用才能删除
        int count = menuDao.countUsedByRole(id);
        if (count > 0) {
            throw new RuntimeException("菜单使用中,无法直接删除");
        }

        //2.把菜单id当做父菜单id,查询子菜单数量
        List<Menu> childMenuList = menuDao.findMenuListByparentMenuId(id);
        if (childMenuList != null && childMenuList.size() > 0) {
            //子菜单集合元素个数>0,说明是非空的一级菜单
            throw new RuntimeException("不能直接删除非空的一级菜单");
        }
        menuDao.deleteById(id);
    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @Override
    public void addMenu(Menu menu) {
        menuDao.addMenu(menu);
    }

    /**
     * 新建菜单时动态加载所有父菜单
     *
     * @return
     */
    @Override
    public List<Menu> getParentMenuList() {
        List<Menu> menus = menuDao.selectParentMenuList();
        //添加一个没有父菜单的选项
        Menu menu = new Menu();
        menu.setName("无父菜单");
        menus.add(menu);
        return menus;
    }

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    @Override
    public void editMenu(Menu menu) {
        menuDao.updateMenu(menu);
    }
}
