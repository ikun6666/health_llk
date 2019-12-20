package top.llk.jobs;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
public class test {
    public static void main(String[] args) {
//配置文件未配置,无法郧西县
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");

    }
}
