package com.warmme.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * 短信队列后入先出
 */
public class SmsQuene {
    private static Logger logger = LoggerFactory.getLogger(SmsQuene.class);

    /**
     * 每个队列最大长度
     ***/
    private static int MAX_QUESIZE = 5;
    private static Map<String, BlockingDeque<SmsInfo>> map = new ConcurrentHashMap<>();

    /**
     * 添加信息
     *
     * @param smsInfo
     */
    public static void add(SmsInfo smsInfo) {
        if (smsInfo == null) {
            logger.error("smsInfo can not be null !");
            return;
        }
        logger.info("smsInfo==>{}", JSON.toJSONString(smsInfo));
        String receiverPhone = StringUtils.isEmpty(smsInfo.getReceiverPhone()) ? "unkown-phone" : smsInfo.getReceiverPhone();
        BlockingDeque<SmsInfo> smsInfoQue = map.get(receiverPhone);
        if (smsInfoQue == null) {
            smsInfoQue = new LinkedBlockingDeque<>(MAX_QUESIZE);
            map.put(receiverPhone, smsInfoQue);
        }
        if (smsInfoQue.size() >= MAX_QUESIZE) {
            smsInfoQue.removeLast();
        }
        smsInfo.setStorTime(System.currentTimeMillis());
        smsInfoQue.addFirst(smsInfo);
    }

    /**
     * 返回所有的短信信息
     *
     * @return
     */
    public static Map<String, List<SmsInfo>> getAll() {
        Map<String, List<SmsInfo>> mapResult = new ConcurrentHashMap<>();
        for (Map.Entry<String, BlockingDeque<SmsInfo>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<SmsInfo> smsInfoList = entry.getValue() == null ? new ArrayList<>() : entry.getValue().stream().collect(Collectors.toList());
            mapResult.put(key, smsInfoList);
        }
        return mapResult;
    }

    /**
     * 根据接收者手机号获取信息列表
     * @param receiverPhone
     * @return
     */
    public static List<SmsInfo> getByReceiverPhone(String receiverPhone){
        BlockingDeque<SmsInfo> smsInfos = map.get(receiverPhone);
        if (smsInfos==null){
            return null;
        }
        return smsInfos.stream().collect(Collectors.toList());
    }

}
