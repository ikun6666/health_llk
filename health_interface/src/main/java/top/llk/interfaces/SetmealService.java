package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */
public interface SetmealService {


    
    /**
     * 查询套餐
     *
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    void addSetmeal(Integer[] checkgroupIds, Setmeal setmeal);

    /**
     * 根据id删除套餐
     * @param id
     * @return
     */
    void deleteSetmealById(Integer id);

    /**
     * 根据id查找套餐
     * @param id
     * @return
     */
    Setmeal findSetmealById(Integer id);

    /**
     * 根据id查找套餐的相关检查组
     * @param id
     * @return
     */
    Integer[] findCheckGroupsBySetmealId(Integer id);

    /**
     * 修改套餐
     * @param checkgroupIds
     * @param setmeal
     * @return
     */
    void editSetmeal(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询所有套餐
     * @return
     */
    List<Setmeal> findAllSetmeal();

    /**
     * 获取每个套餐的预约数量统计
     * @return
     */
    HashMap<String, Object> countOredersBySetmealId();


}
