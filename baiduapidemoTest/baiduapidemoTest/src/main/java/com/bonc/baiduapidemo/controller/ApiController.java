package com.bonc.baiduapidemo.controller;

import com.bonc.baiduapidemo.service.MsgApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: crc
 * @Date: 2018/9/17 11:11
 */
@EnableAutoConfiguration
@RestController
public class ApiController {
    @Autowired
    private MsgApiService apiService;

    @RequestMapping(value = "/api/getToken", method = RequestMethod.POST)
    public Object showGaoWeiMsg(@RequestBody Map<String,Object> param) {
        String userName =param.get("userName").toString();
        String authString =param.get("authString").toString();
        return apiService.getGaoWeiMsg(userName, authString);
    }
//    @RequestMapping(value = "/api/getToken", method = RequestMethod.GET)
//    public Object showGaoWeiMsg(@RequestParam String userName,String authString) {
//        return apiService.getGaoWeiMsg(userName, authString);
//    }
    @RequestMapping(value = "/api/360view", method = RequestMethod.POST)
    @ResponseBody
    public Object show360ViewMsg(@RequestBody Map<String,Object> param) {
        String requestId =param.get("requestId").toString();
        String phoneList =param.get("phoneList").toString();
        String attrList =param.get("attrList").toString();
        String areaId =param.get("areaId").toString();
        return apiService.get360ViewMsg(phoneList,areaId);
    }
//    @RequestMapping(value = "/api/360view", method = RequestMethod.POST)
//    public Object show360ViewMsg(String requestId,String phoneList,String months) {
//        return apiService.get360ViewMsg(phoneList,phoneList);
//    }
    @RequestMapping(value = "/api/msg", method = RequestMethod.POST)
    @ResponseBody
    public Object showMsg(@RequestBody Map<String,Object> param){
        String phoneNum =param.get("phoneNum").toString();
        String months =param.get("months").toString();
        return apiService.getApiMsg(phoneNum);
    }
//    @RequestMapping(value = "/api/msg", method = RequestMethod.POST)
//    public Object showMsg(String phoneNum,String months) {
//        return apiService.getApiMsg(phoneNum);
//    }
}
