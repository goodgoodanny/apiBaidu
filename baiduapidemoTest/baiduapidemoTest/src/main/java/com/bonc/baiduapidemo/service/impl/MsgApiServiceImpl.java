package com.bonc.baiduapidemo.service.impl;

import com.bonc.baiduapidemo.service.MsgApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: crc
 * @Date: 2018/9/17 11:20
 */
@Service
public class MsgApiServiceImpl implements MsgApiService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object getGaoWeiMsg(final String userName,final String authString) {
        Object resultList = jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                // 调用的sql
                String storedProc = "{call P_API_GAOWEI_TASK_INFO_SMS(?,?,?)}";
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, userName);
                cs.setString(2, authString);
                cs.registerOutParameter(3, Types.CLOB);
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(3));
                return result;
            }
        });
        String resultListStr = resultList.toString().substring(1,resultList.toString().length()-1);
        System.out.println("resultListStr------------------"+resultListStr);
        return resultListStr;
    }

    @Override
    public Object get360ViewMsg(final String phone,final String areaId) {
        System.out.println("areaId------------------"+areaId);
        Object resultList = jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                // 调用的sql
                String storedProc = "{call P_API_360VIEW_TASK_INFO_SMS(?,?,?)}";
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, phone);
                cs.setString(2, areaId);
                cs.registerOutParameter(3, Types.CLOB);
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(3));
                return result;
            }
        });
        String resultListStr = resultList.toString().substring(1,resultList.toString().length()-1);
        System.out.println("resultListStr------------------"+resultListStr);
        return resultListStr;
    }

    @Override
    public Object getApiMsg(final String phone) {
        Object resultList = jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                // 调用的sql
                String storedProc = "{call P_API_TASK_INFO_SMS(?,?)}";
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, phone);
                cs.registerOutParameter(2, Types.CLOB);
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(2));
                return result;
            }
        });
        System.out.println("resultList.toString()==="+resultList.toString().substring(1,resultList.toString().length()-1));
        String resultListStr = resultList.toString().substring(1,resultList.toString().length()-1);
        return resultListStr;
    }
}
