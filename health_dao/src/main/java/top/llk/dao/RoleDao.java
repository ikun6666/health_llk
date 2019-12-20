package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import top.llk.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */
public interface RoleDao {
    /**
     * 根据用户id查询角色
     *
     * @param id
     * @return
     */
    Set<Role> findRolesByUserId(Integer id);

    /**
     * 角色分页查询
     *
     * @param queryString
     * @return
     */
    Page<Role> selectByCondition(String queryString);

    /**
     * 添加角色,返回自增id
     *
     * @param role
     */
    void addRole(Role role);

    /**
     * 操作中间表,绑定role和menu
     *
     * @param map:key1 roleId key2 menuId
     */
    @Insert("INSERT INTO t_role_menu (role_id,menu_id) VALUES (#{roleId},#{menuId})")
    void addRoleAndMenu(Map<String, Object> map);

    /**
     * 查询中间表是否已经绑定过父菜单
     *
     * @param
     * @param
     * @return
     */
    @Select("SELECT * FROM t_role_menu WHERE role_id= #{roleId} AND menu_id = #{parentMenuId}")
    Integer haveSelected(Map<String, Object> map);

    /**
     * 操作中间表,绑定role和permission
     *
     * @param map:key1 roleId key2 permissionId
     */
    @Insert("INSERT INTO t_role_permission (role_id,permission_id) VALUES (#{roleId},#{permissionId})")
    void addRoleAndPermission(HashMap<String, Object> map);

    /**
     * 操作中间表,删除role和menu
     *
     * @param id roleId
     */
    @Delete("DELETE FROM t_role_menu WHERE role_id = #{id} ")
    void deleteRoleAndMenuByRoleId(Integer id);

    /**
     * 操作中间表,删除role和permission
     *
     * @param id roleId
     */
    @Delete("DELETE FROM t_role_permission WHERE role_id = #{id} ")
    void deleteRoleAndPermissionByRoleId(Integer id);

    /**
     * 删除role
     *
     * @param id
     */
    @Delete("DELETE FROM t_role WHERE id = #{id} ")
    void deleteRoleByRoleId(Integer id);

    /**
     * 根据 role id查询中间表,看role是否被使用
     *
     * @param id 角色id
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_user_role WHERE role_id = #{id}")
    int countBeUsed(Integer id);

    /**
     * 根据role id查询拥有的权限id
     *
     * @param id 角色id
     * @return
     */
    @Select("select permission_id from t_role_permission where role_id = #{id}")
    List<Integer> findPermissionIdsByRoleId(Integer id);

    /**
     * 根据role id查询拥有的菜单id
     *
     * @param id 角色id
     * @return
     */
    @Select("select menu_id from t_role_menu where role_id = #{id}")
    List<Integer> findMenuIdsByRoleId(Integer id);

    /**
     * RoleAndMenu中间表插入数据
     * @param map
     */
//    @Insert("insert into t_role_menu set role_id =#{roleId},menu_id= #{menuId}")
//    void insertRoleAndMenu(HashMap<String, Object> map);

    /**
     * RoleAndPermission中间表插入数据
     * @param map
     */
//    @Insert("insert into t_role_permission set role_id =#{roleId},permission_id= #{permissionId}")
//    void insertRoleAndPermission(HashMap<String, Object> map);

    /**
     * 更新role信息
     *
     * @param role
     */
    void update(Role role);

}
