package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.llk.constant.MessageConstant;
import top.llk.entity.Result;
import top.llk.interfaces.SetmealService;
import top.llk.pojo.Setmeal;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     *
     * @return
     */
    @RequestMapping("getSetmeal")
    public Result getSetmeal() {
        try {
            List<Setmeal> setmeals = setmealService.findAllSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);

        }
    }

    /**
     * 查询套餐详情
     *
     * @return
     */
    @RequestMapping("findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findSetmealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);

        }
    }
}
