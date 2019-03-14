package com.bonc.baiduapidemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.baiduapidemo.domain.City;
import com.bonc.baiduapidemo.service.CityService;
import com.bonc.baiduapidemo.service.MsgApiService;
import com.bonc.baiduapidemo.service.impl.CityServiceImpl;
import com.bonc.baiduapidemo.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 城市 Controller 实现 Restful HTTP 服务
 *
 * Created by bysocket on 23/08/2018.
 */
@EnableAutoConfiguration
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;

    private final String DES_KEY = "key18611";
    /*@RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
    public City findOneCity(@PathVariable("id") Long id) {
        return cityService.findCityById(id);
    }*/

    @RequestMapping(value = "/api/city/", method = RequestMethod.GET)
    public Object baiduApi(@RequestParam(value = "id") String id,@RequestParam(value = "cityNo") String cityNo, @RequestParam(value = "token") String token) {
        DESUtil des = new DESUtil(DES_KEY);
        JSONObject json = new JSONObject();
        String msg = "";
        try {
            msg = des.decryptStr(token);
        } catch (Exception e) {
            json.put("result", "您暂无无权限查看！！！");
            return json.toString();
        }
        if (!msg.equals(id + cityNo)) {
            json.put("result", "您暂无无权限查看！！！");
            return json.toString();
        } else {
            return cityService.SpringRestTemplate(id,cityNo);
        }
    }

   /* @RequestMapping(value = "/api/city", method = RequestMethod.GET)
    public List<City> findAllCity() {
        return cityService.findAllCity();
    }*/

    @RequestMapping(value = "/api/city", method = RequestMethod.POST)
    public void createCity(@RequestBody City city) {
        cityService.saveCity(city);
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.PUT)
    public void modifyCity(@RequestBody City city) {
        cityService.updateCity(city);
    }

    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.DELETE)
    public void modifyCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
    }

}
