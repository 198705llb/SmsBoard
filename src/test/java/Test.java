import com.alibaba.fastjson.JSON;
import com.warmme.controller.SmsInfo;

public class Test {
    public static void main(String[] args) {
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setMsg("msg");
        smsInfo.setReceiverPhone("18575582923");
        smsInfo.setReceiveTime(System.currentTimeMillis());
        smsInfo.setSenderPhone("10068855");
        smsInfo.setStorTime(System.currentTimeMillis());

        System.err.println(JSON.toJSON(smsInfo));
    }
}
