package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import top.llk.dao.CheckItemDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.CheckItemService;
import top.llk.pojo.CheckItem;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/3
 * @Content:
 */

@Service
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加检查项
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        if (checkItem.getAge() == null) {
            checkItem.setAge("0-100");
        }
        if (checkItem.getSex() == null) {
            checkItem.setSex("0");
        }
        checkItemDao.add(checkItem);
    }

    /**
     * 搜索检查项
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Long total = checkItemDao.countItems(queryPageBean);
        //预处理 index.
        queryPageBean.setIndex((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        //预处理  queryString,
        if (queryString != null && !"".equals(queryString)) {
            queryString = "%" + queryString + "%";
            //如果queryString 不为空:有搜索内容,则index为0
            queryPageBean.setIndex(0);

        }
        queryPageBean.setQueryString(queryString);
        List<CheckItem> rows = checkItemDao.queryCheckItems(queryPageBean);
        PageResult pageResult = new PageResult(total, rows);

        return pageResult;
    }

    /**
     * 根据id删除checkitem,被其他检查组使用不允许删除
     *
     * @param
     * @return
     * @description
     */
    @Override
    public void deleteCheckItemById(String id) throws RuntimeException {
        //1.查询是否被检查组使用的数量
        int usedCount = checkItemDao.queryBeUsedCountById(id);
        //2.未被使用直接删除,已被使用抛异常给controller
        if (usedCount > 0) {
            throw new RuntimeException("当前检查项被引用，不能删除");
        } else {
            checkItemDao.deleteCheckItemById(id);
        }
    }

    /**
     * 编辑检查项
     *
     * @param checkItem
     */
    @Override
    public void editCheckItem(CheckItem checkItem) {
        checkItemDao.editCheckItem(checkItem);
    }

    @Override
    public PageResult findAll() {
        List<CheckItem> allCheckItem = checkItemDao.findAllCheckItem();
        Long total = checkItemDao.countAllCheckItem();
        return new PageResult(total,allCheckItem);

    }
}
