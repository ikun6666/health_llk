package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.SetmealDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.SetmealService;
import top.llk.pojo.Setmeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    /**
     * 查询套餐
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加套餐
     * 1.操作setmeal单表
     * 2.操作中间表
     *
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void addSetmeal(Integer[] checkgroupIds, Setmeal setmeal) {
        //操作setmeal单表
        setmealDao.insertSetmeal(setmeal);
        //操作中间表,插入关联数据
        insertSetmealAndCheckGroup(checkgroupIds, setmeal);
    }

    /**
     * 操作中间表,插入关联数据
     *
     * @param checkgroupIds
     * @param setmeal
     */
    private void insertSetmealAndCheckGroup(Integer[] checkgroupIds, Setmeal setmeal) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            HashMap<String, Object> map = new HashMap<>();
            for (Integer checkgroupId : checkgroupIds) {
                map.put("checkgroupId", checkgroupId);
                map.put("setmealId", setmeal.getId());
                setmealDao.insertSetmealAndCheckGroup(map);
            }
        }
    }

    /**
     * 根据id删除套餐
     *
     * @param id
     * @return
     */
    @Override
    public void deleteSetmealById(Integer id) {
        //删除中间表数据
        setmealDao.deleteSetmealAndCheckGroupById(id);

        //删除套餐单表数据
        setmealDao.deleteSetmealById(id);
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findSetmealById(Integer id) {
        return setmealDao.findSetmealById(id);
    }

    /**
     * 根据id查询套餐的相关检查组
     *
     * @param id
     * @return
     */
    @Override
    public Integer[] findCheckGroupsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupsBySetmealId(id);
    }

    /**
     * 修改套餐
     * 1.删除中间表关联数据, 插入新数据
     * 2.修改套餐单表
     *
     * @param checkgroupIds
     * @param setmeal
     * @return
     */
    @Override
    public void editSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        //删除中间表关联数据
        setmealDao.deleteSetmealAndCheckGroupById(setmeal.getId());
        //插入中间表关联数据
        insertSetmealAndCheckGroup(checkgroupIds, setmeal);
        //修改套餐单表
        setmealDao.editSetmeal(setmeal);

    }

    /**
     * 查询所有套餐
     *
     * @return
     */
    @Override
    public List<Setmeal> findAllSetmeal() {
        return setmealDao.findAllSetmeal();
    }

    /**
     * 获取每个套餐的预约数量统计
     *
     * @return
     */
    @Override
    public HashMap<String, Object> countOredersBySetmealId() {
        HashMap<String, Object> map = new HashMap<>();
        //[{value:335, name:'直接访问'},{value:335, name:'邮件营销'}...]
        List<Map<String, Object>> setmealCount = setmealDao.countOredersGroupBySetmeal();
        ArrayList<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> setmealAndCountMap : setmealCount) {
            //取出每个套餐的名称,添加到集合中
            String sname = (String) setmealAndCountMap.get("name");
            setmealNames.add(sname);
        }
        //前端需要的数据:
        //['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
        map.put("setmealNames", setmealNames);
        //[{value:335, name:'直接访问'},{value:335, name:'邮件营销'}...]
        map.put("setmealCount", setmealCount);

        return map;
    }
}
