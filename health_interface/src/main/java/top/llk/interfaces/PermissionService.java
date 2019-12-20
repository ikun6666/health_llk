package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.Permission;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
public interface PermissionService {
    /**
     * 权限管理分页查询
     *
     * @param queryPageBean
     * @return
     */
    PageResult selectByCondition(QueryPageBean queryPageBean) throws Exception;
    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    void add(Permission permission);

    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(Integer id) throws RuntimeException;

    /**
     * 编辑权限
     *
     * @param permission
     * @return
     */
    void editPermission(Permission permission);

    /**
     * 根据主键查找permission
     * @param id
     * @return
     */
    Permission findById(Integer id);
}
