package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.CheckItem;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/3
 * @Content:
 */
public interface CheckItemService {
    public void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteCheckItemById(String id);

    void editCheckItem(CheckItem checkItem);

    PageResult findAll();
}
