package com.warmme.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmsBroadController {

    private static Logger logger = LoggerFactory.getLogger(SmsBroadController.class);

    @GetMapping("/smsList")
    public String smsList(@RequestParam("phone") String phone) {

        String json = JSON.toJSONString(SmsQuene.getByReceiverPhone(phone));
        logger.info("get values  ==> {}", json);
        return json;
    }

    @PostMapping("/sms")
    public String addSmsInfo(@RequestBody String smsInfoJSON) {
        logger.info("add smsInfo ==> {}", JSON.toJSONString(smsInfoJSON));
        if (smsInfoJSON.contains("{")){
            smsInfoJSON = smsInfoJSON.substring(smsInfoJSON.indexOf("{"),smsInfoJSON.length());
        }
        SmsInfo smsInfo = JSON.parseObject(smsInfoJSON, SmsInfo.class);
        try {
            SmsQuene.add(smsInfo);
        } catch (Exception e) {
            logger.error("",e);
            return e.getMessage();
        }
        return "succeed";
    }
}
