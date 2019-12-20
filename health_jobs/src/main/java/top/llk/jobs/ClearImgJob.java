package top.llk.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import top.llk.constant.RedisConstant;
import top.llk.util.QiniuUtils;

import java.util.Date;
import java.util.Set;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
public class ClearImgJob {
    //导入连接池
    @Autowired
    private JedisPool jedisPool;

    //清理图片
    public void clearImg() {
        System.out.println("清理垃圾图片..........." + new Date());
        //计算redis差值
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set != null && set.size() > 0) {
            //遍历集合
            for (String pic : set) {
                QiniuUtils.deleteFileFromQiniu(pic);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
            }
        }

    }
}
