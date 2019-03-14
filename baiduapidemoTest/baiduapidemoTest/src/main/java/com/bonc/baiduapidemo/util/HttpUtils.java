package com.bonc.baiduapidemo.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Http（get/post请求）工具类
 *
 * @author leiyaozu
 */
public class HttpUtils {

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

    /**
     *  httpclint 请求返回结果转 JSON
     *
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPostToJsonObject(String url, JSONObject json){
        String str =  doPost(url, json);
        if(org.apache.commons.lang.StringUtils.isBlank(str)){
            return null;
        }else{
            return JSONObject.parseObject(str);
        }
    }
    public static String doPost(String url, JSONObject json) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        try {
            httpClient = HttpUtils.getHttpClient();
            httpPost = new HttpPost(url+"?sessionLoginId=admin");
            //把json对象转换为json字符串
            String jsonStr = json.toString();
            //请求实体
            StringEntity entity = new StringEntity(jsonStr, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            /** 请求发送成功，并得到响应 **/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    public static CloseableHttpClient getHttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory)
                    .build();
            return closeableHttpClient;
        } catch (Exception e) {
            System.out.println("create closeable http client failed!");
            return HttpClientBuilder.create().build();
        }
    }

}
