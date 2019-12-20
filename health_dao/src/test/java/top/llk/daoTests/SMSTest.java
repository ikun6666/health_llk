package top.llk.daoTests;

import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;
import top.llk.util.SMSUtils;
import top.llk.util.ValidateCodeUtils;

import java.util.Date;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
public class SMSTest {
//
    @Test
    public void test() throws ClientException {
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, "15606957027", "1234");

    }
    @Test
        public void testCheckCode() {
        String checkCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
        System.out.println(ValidateCodeUtils.generateValidateCode4String(6));
        System.out.println(checkCode);
    }

    @Test
        public void testdate() {
        System.out.println(new Date().toString());
        System.out.println(new Date());
    }
}
