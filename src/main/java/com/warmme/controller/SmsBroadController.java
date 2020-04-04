package com.warmme.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class SmsBroadController {

    private static Logger logger = LoggerFactory.getLogger(SmsBroadController.class);

    @GetMapping("/smsList")
    @ResponseBody
    public List<SmsInfo> smsList(@RequestParam("phone") String phone) {
        List<SmsInfo> smsInfoList = SmsQuene.getByReceiverPhone(phone);
        String json = JSON.toJSONString(smsInfoList);
        logger.info("get values  ==> {}", json);
        return smsInfoList;
    }

    @PostMapping("/sms")
    public String addSmsInfo(@RequestBody String smsInfoJSON) {
        logger.info("add smsInfo ==> {}", JSON.toJSONString(smsInfoJSON));
        if (smsInfoJSON.contains("{")){
            smsInfoJSON = smsInfoJSON.substring(smsInfoJSON.indexOf("{"),smsInfoJSON.length());
        }
        JSONObject smsInfoJSONObject = JSON.parseObject(smsInfoJSON);
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setSenderPhone(smsInfoJSONObject.getString("senderPhone"));
        smsInfo.setReceiverPhone(smsInfoJSONObject.getString("receiverPhone"));
        smsInfo.setMsg(smsInfoJSONObject.getString("msg"));
        smsInfo.setStorTime(new Date());
        try {
            smsInfo.setReceiveTime(new Date(smsInfoJSONObject.getLong("receiveTime")));
        } catch (Exception e) {
            logger.error("",e);
            return e.getMessage();
        }
        try {
            SmsQuene.add(smsInfo);
        } catch (Exception e) {
            logger.error("",e);
            return e.getMessage();
        }
        return "succeed";
    }
}
