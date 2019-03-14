package com.bonc.baiduapidemo.service;


/**
 * @Author: crc
 * @Date: 2018/9/17 11:16
 */
public interface MsgApiService {

    /**
     * 流失高危
     * @param phone
     * @return
     */
    Object getGaoWeiMsg(String userName,String authString);

    /**
     * 360视图
     * @param phone
     * @return
     */
    Object get360ViewMsg(String phone,String areaId);

    Object getApiMsg(String phone);

}
