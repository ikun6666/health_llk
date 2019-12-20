package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import top.llk.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/17
 * @Content:
 */
public interface MenuDao {
    /**
     * 根据角色id查询menu
     *
     * @param roleId
     * @return
     */
    LinkedHashSet<Menu> findMenuListByRoleId(Integer roleId);

    /**
     * 根据一级菜单id遍历查询menu的二级菜单
     *
     * @param parentMenuId 父菜单id
     * @return
     */
    List<Menu> findMenuListByparentMenuId(Integer parentMenuId);


    /**
     * 菜单列表分页查询
     *
     * @param queryString 搜索内容
     * @return
     */
    Page<Menu> selectByCondition(String queryString);

    /**
     * 根据菜单主键查询是否有父菜单id
     *
     * @param id 菜单id
     * @return
     */
    @Select("SELECT parentMenuId FROM t_menu WHERE id =#{id}")
    Integer selectParentMenuIdByPrimaryKey(Integer id);

    /**
     * 删除菜单项
     *
     * @param id 菜单id
     * @return
     */
    @Delete("delete  from t_menu where id = #{id}")
    void deleteById(Integer id);

    /**
     * 查询中间表,统计menu被role使用的数量
     *
     * @param id 菜单id
     * @return
     */
    @Select("select count(*) from t_role_menu where menu_id = #{id}")
    int countUsedByRole(Integer id);

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    void addMenu(Menu menu);

    /**
     * 新建菜单时动态加载所有父菜单
     *
     * @return
     */
    @Select("select * from t_menu where level = 1")
    List<Menu> selectParentMenuList();

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    void updateMenu(Menu menu);
}
