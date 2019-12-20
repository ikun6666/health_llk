package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.llk.pojo.Permission;

import java.util.Set;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */
public interface PermissionDao {
    /**
     * 根据roleid 查询所有permission
     *
     * @param id
     * @return
     */
    Set<Permission> findPermissionsByRoleId(Integer id);

    /**
     * 权限管理分页查询
     *
     * @param queryString
     * @return
     */

    Page<Permission> selectByCondition(String queryString);

    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    void add(Permission permission);

    /**
     * 查询权限是否被角色使用
     *
     * @param id
     * @return
     */
    @Select("select count(*) from t_role_permission where permission_id = #{id}")
    int queryBeUsedCountById(Integer id);

    /**
     * 删除权限
     *
     * @param id
     */
    @Update("delete from t_permission where id =#{id}")
    void deleteCheckItemById(Integer id);

    /**
     * 编辑权限
     *
     * @param permission
     */
    void editPermission(Permission permission);

    /**
     * 根据主键查找permission
     * @param id
     * @return
     */
    @Select("select * from t_permission where id =#{id}")
    Permission findById(Integer id);
}
