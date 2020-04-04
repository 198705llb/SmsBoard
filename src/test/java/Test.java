import com.alibaba.fastjson.JSON;
import com.warmme.controller.SmsInfo;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setMsg("msg");
        smsInfo.setReceiverPhone("18575582923");
        smsInfo.setReceiveTime(new Date());
        smsInfo.setSenderPhone("10068855");
        smsInfo.setStorTime(new Date());

        System.err.println(JSON.toJSON(smsInfo));
    }
}
