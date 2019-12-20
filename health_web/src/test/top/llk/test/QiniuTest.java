package top.llk.test;

import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/7
 * @Content:
 */
public class QiniuTest {
    @Test
    public void test7niu() {
        String accessKey = "u4j1NWxD7-QOKvcoWaXgx6Rk9qBzAMHmmJKFnvND";
        String secretKey = "XOt2o-PSfPLPvO7hTHEAAUMh6M7BwcR2ecsLLsGs";
        String bucket = "7niumemory";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
    }
}
