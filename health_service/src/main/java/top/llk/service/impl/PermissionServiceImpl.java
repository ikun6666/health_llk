package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.PermissionDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.PermissionService;
import top.llk.pojo.Permission;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 权限管理分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) throws Exception {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Permission> page = permissionDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 根据id删除权限
     *
     * @param id
     */
    @Override
    public void deleteById(Integer id) throws RuntimeException {
        //1.查询是否被角色使用
        int usedCount = permissionDao.queryBeUsedCountById(id);
        //2.未被使用直接删除,已被使用抛异常给controller
        if (usedCount > 0) {
            throw new RuntimeException("当前权限被使用，不能删除");
        } else {
            permissionDao.deleteCheckItemById(id);
        }
    }

    /**
     * 编辑权限
     *
     * @param permission
     * @return
     */
    @Override
    public void editPermission(Permission permission) {
        permissionDao.editPermission(permission);
    }

    /**
     * 根据主键查找permission
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }
}
