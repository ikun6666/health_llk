package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;
import top.llk.constant.MessageConstant;
import top.llk.constant.RedisConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.SetmealService;
import top.llk.pojo.CheckGroup;
import top.llk.pojo.Setmeal;
import top.llk.util.QiniuUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */

@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询套餐
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }

    /**
     * 文件上传,调用另一个服务器接收存储
     */
    @RequestMapping("upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {

            //拼接uuid和文件名
            String uuid = UUID.randomUUID().toString();
            System.out.println(uuid);
            String fileName = imgFile.getOriginalFilename();
            //生成uuid拼接上文件名的后缀
            String uuidFileName = uuid + fileName.substring(fileName.lastIndexOf("."));

            //调用工具类
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), uuidFileName);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
            // 将上传成功后新的文件名返回前端
            //sadd: key=setmealPicResources value=文件名
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, uuidFileName);

            //回传文件名!!
            result.setData(uuidFileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     *
     * @param checkgroupIds
     * @param setmeal
     */
    @RequestMapping("addSetmeal")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    public Result addSetmeal(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.addSetmeal(checkgroupIds, setmeal);
            //添加到redis的图片集合中
            String uuidFileName = setmeal.getImg();
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, uuidFileName);

            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);

        }
    }

    /**
     * 根据id删除套餐
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteSetmealById")
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    public Result deleteSetmealById(Integer id) {
        try {
            //删除七牛云的图片
            String fileName = setmealService.findSetmealById(id).getImg();
            QiniuUtils.deleteFileFromQiniu(fileName);
            //删除数据库的信息
            setmealService.deleteSetmealById(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    /**
     * 接收编辑请求,根据id查询套餐和相关检查组的信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findSetmealById")
    public HashMap<String, Object> findSetmealById(Integer id) {
        Setmeal setmeal = setmealService.findSetmealById(id);
        Integer[] checkgroupIds = setmealService.findCheckGroupsBySetmealId(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("setmeal", setmeal);
        map.put("checkgroupIds", checkgroupIds);
        return map;
    }

    /**
     * 修改套餐
     *
     * @param checkgroupIds
     * @param setmeal
     * @return
     */
    @RequestMapping("editSetmeal")
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    public Result editSetmeal(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            try {
                //查找原套餐的img,如果原img与现img不一致,则删除原来img
                String fileNameBeforeUpdate = setmealService.findSetmealById(setmeal.getId()).getImg();
                if (fileNameBeforeUpdate != null && fileNameBeforeUpdate != setmeal.getImg()) {
                    //删除七牛云的图片
                    QiniuUtils.deleteFileFromQiniu(fileNameBeforeUpdate);
                    //清除缓存内的信息
                    jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, fileNameBeforeUpdate);
                    jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileNameBeforeUpdate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //修改套餐
            setmealService.editSetmeal(setmeal, checkgroupIds);


            //添加到redis的图片集合中
            String uuidFileName = setmeal.getImg();
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, uuidFileName);

            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }
}
