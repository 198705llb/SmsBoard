package com.warmme.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
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

    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    static {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                clearTimeOut();
            }
        },1000,5,TimeUnit.MINUTES);
    }

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
        smsInfo.setStorTime(new Date());
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

    /**清除超时短信
     *
     */
    private static void clearTimeOut(){
        Collection<BlockingDeque<SmsInfo>> values = map.values();
        if (CollectionUtils.isEmpty(values)){
            return;
        }
        for (BlockingDeque<SmsInfo> value : values) {
            if (CollectionUtils.isEmpty(value)){
                continue;
            }
            while(value.size()>0){
                SmsInfo last = value.getLast();
                if (last==null|| last.getStorTime()==null || last.getStorTime().getTime()<(System.currentTimeMillis()-1000*60*30)){
                 value.removeLast();
                }else {
                    break;
                }
            }

        }
    }

}
