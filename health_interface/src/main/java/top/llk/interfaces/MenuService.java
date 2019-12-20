package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.Menu;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
public interface MenuService {
    /**
     * 菜单列表分页查询
     * @param queryPageBean
     * @return
     */
    PageResult selectByCondition(QueryPageBean queryPageBean);

    /**
     * 删除菜单项
     *
     * @param id
     * @return
     */
    void deleteById(Integer id) throws Exception;

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    void addMenu(Menu menu);

    /**
     * 新建菜单时动态加载所有父菜单
     *
     * @return
     */
    List<Menu> getParentMenuList();

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    void editMenu(Menu menu);
}
