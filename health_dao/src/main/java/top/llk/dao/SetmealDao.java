package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import top.llk.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */
public interface SetmealDao {
    /**
     * 查询套餐详情
     *
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);

    /**
     * 添加新套餐
     *
     * @param setmeal
     */
    void insertSetmeal(Setmeal setmeal);

    /**
     * 操作中间表,添加新套餐的检查组
     *
     * @param map
     */
    void insertSetmealAndCheckGroup(HashMap<String, Object> map);


    /**
     * 删除中间表数据
     *
     * @param id
     */
    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{id}")
    void deleteSetmealAndCheckGroupById(Integer id);

    /**
     * 删除套餐单表数据
     *
     * @param id
     */
    @Delete("delete from t_setmeal where id = #{id}")
    void deleteSetmealById(Integer id);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    Setmeal findSetmealById(Integer id);

    /**
     * 根据id查询套餐相关检查组
     *
     * @param id
     * @return
     */
    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{id}")
    Integer[] findCheckGroupsBySetmealId(Integer id);

    /**
     * 修改套餐单表
     *
     * @param setmeal
     */
    void editSetmeal(Setmeal setmeal);

    /**
     * 查询所有套餐
     *
     * @return
     */
    @Select("select * from t_setmeal")
    List<Setmeal> findAllSetmeal();

    /**
     * 获取每个套餐的预约数量统计
     *
     * @return
     */
    List<Map<String,Object>> countOredersGroupBySetmeal();
}
