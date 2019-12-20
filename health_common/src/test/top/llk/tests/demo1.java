package top.llk.tests;

import com.aliyuncs.exceptions.ClientException;
import top.llk.util.SMSUtils;

import java.util.Date;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/15
 * @Content:
 */
public class demo1 {
    public static void main(String[] args) throws ClientException {
//        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,"15606957027","1234");
        SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,"15606957027", "2015-12-10");
    }
}
