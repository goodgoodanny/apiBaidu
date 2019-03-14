package com.bonc.baiduapidemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.baiduapidemo.dao.CityDao;
import com.bonc.baiduapidemo.domain.City;
import com.bonc.baiduapidemo.service.CityService;
import com.bonc.baiduapidemo.util.HttpClientTest;
import com.bonc.baiduapidemo.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 23/08/2018.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Autowired
    RestTemplate restTemplate;

//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
////        sessionFactory.setDataSource(dataSource);
//        return sessionFactory.getObject();
//    }
    //http://api.map.baidu.com/geocoder/v2/?address=%E7%A7%91%E6%8A%80%E4%BA%8C%E8%B7%AF&output=json&ak=ZGEa8tUqEFVD05QmkN9GAUwZlyRMKO2H
    public Object  SpringRestTemplate(String id,String cityNo){
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=ZGEa8tUqEFVD05QmkN9GAUwZlyRMKO2H&address="+id;

//        JSONObject json = restTemplate.getForObject(url, JSONObject.class);
//        System.out.println(json.toJSONString());
        String str =  doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(str);
        String result = jsonObject.getString("result");
        JSONObject resultdata= JSON.parseObject(result);
        String location = resultdata.getString("location");
        String[] lnglat = location.split(",");
        String lng = lnglat[0];
        String lat = lnglat[1];
//        System.out.println("lng="+lng.substring(7,lng.length()));
//        System.out.println("lat="+lat.substring(6,lat.length()-1));
        List<String> resultList = doProcedure(lng.substring(7,lng.length()),lat.substring(6,lat.length()-1),cityNo);
        String resultStr = StringUtils.strip(resultList.toString(),"[]");
        System.out.println("resultStr="+ StringUtils.strip(resultStr.toString(),"[]"));
        JSONObject json = new JSONObject();
        json.put("result", resultStr);
        return json.toString();
    }
    // 调用函数
    @Autowired //注入数据库操作
    private JdbcTemplate jdbcTemplate;

    public List<String> doProcedure(final String lng, final String lat,final String cityNo) {
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call dmcode.p_dmcode_manager_position(?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, "841");
                cs.setString(2, lng);
                cs.setString(3, lat);
                cs.registerOutParameter(4,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(4));
                return result;
            }
        });

//        System.out.println("resultList="+resultList);
        return resultList;

    }
    public static String doGet(String url) {
        // get请求返回结果
        String result = "";
        try {
            HttpClient httpClient = HttpUtils.getHttpClient();
            // 发送get请求
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            /** 状态码为：200，请求发送成功，并得到响应 **/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));

        return restTemplate;
    }
    public List<City> findAllCity(){
        return cityDao.findAllCity();
    }

    public City findCityById(Long id) {
        return cityDao.findById(id);
    }

    @Override
    public Long saveCity(City city) {
        return cityDao.saveCity(city);
    }

    @Override
    public Long updateCity(City city) {
        return cityDao.updateCity(city);
    }

    @Override
    public Long deleteCity(Long id) {
        return cityDao.deleteCity(id);
    }

}
