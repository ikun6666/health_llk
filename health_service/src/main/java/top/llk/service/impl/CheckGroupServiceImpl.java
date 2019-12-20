package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.CheckGroupDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.CheckGroupService;
import top.llk.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/5
 * @Content:
 */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 分页查询检查组
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加检查组
     * 操作两个表,需开启事务
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) throws Exception {
        //1.操作checkGroup表
        checkGroupDao.addCheckGroup(checkGroup);

        //测试事务回滚
//        int n = 1 / 0;

        //2.操作中间表
        setCheckGroupAndCheckItem(checkGroup, checkitemIds);

    }

    /**
     * 操作检查和检查项的中间表(修改和新增检查组都用到)
     *
     * @param checkGroup
     * @param checkitemIds
     */
    private void setCheckGroupAndCheckItem(CheckGroup checkGroup, Integer[] checkitemIds) {

        HashMap<String, Object> map = new HashMap<>();
        for (Integer checkitemId : checkitemIds) {
            //会覆盖原有的key-value
            map.put("checkGroupId", checkGroup.getId());
            map.put("checkitemId", checkitemId);
            checkGroupDao.addCheckGroupAndCheckitem(map);
        }
    }

    /**
     * 根据id删除检查组
     * 分两步 操作两个表
     *
     * @param id
     * @return
     */
    @Override
    public void deleteCheckGroupById(Integer id) {
        checkGroupDao.deleteCheckGroupAndCheckItemById(id);
        checkGroupDao.deleteCheckGroupById(id);
    }

    /**
     * 接收编辑请求,根据id查询检查组,回传检查组信息
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findCheckGroupById(Integer id) {
        return checkGroupDao.findCheckGroupById(id);
    }

    /**
     * 根据id查询检查项ids
     *
     * @param id
     * @return
     */
    @Override
    public Integer[] findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组
     * 1.操作单表;2.1删除中间表关联数据;2.2操作多表
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @Override
    public void editCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //编辑检查组单表
        checkGroupDao.editCheckGroup(checkGroup);
        //删除中间表的关联数据
        checkGroupDao.deleteCheckGroupAndCheckItemById(checkGroup.getId());
        //测试回滚功能
        //int n=1/0;
        //重新添加关联数据
        setCheckGroupAndCheckItem(checkGroup, checkitemIds);

    }

    /**
     * 查找所有检查组
     *
     * @return
     */
    @Override
    public PageResult findAll() {
        List<CheckGroup> checkGroup = checkGroupDao.findALL();
        PageResult pageResult = new PageResult(null, checkGroup);
        return pageResult;
    }
}
