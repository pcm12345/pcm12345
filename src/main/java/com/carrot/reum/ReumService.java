package com.carrot.reum;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.carrot.common.mapper.crm.ReumMapper;

@Service
public class ReumService {

  private final ReumMapper reumMapper;

  public ReumService(ReumMapper reumMapper) {
    this.reumMapper = reumMapper;
  }

  public Map<String, Object> insData(String tableName, Map<String, Object> param) {
    int idx = reumMapper.insData(tableName, param);

    Map<String, Object> response = new HashMap<>();
    response.put("idx", idx);
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> upData(String tableName, Map<String, Object> params, int idx) {
    reumMapper.upData(tableName, params, "idx = " + idx);
    Map<String, Object> response = new HashMap<>();
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> info(String tableName, int idx) {
    String query = String.format("select * from %s where idx = #{param.idx} limit 1", tableName, idx + "");
    Map<String, Object> param = Map.of("idx", idx);
    Map<String, Object> info = reumMapper.selectOne(query, param);
    Map<String, Object> response = new HashMap<>();
    response.put("info", info);
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> info(String tableName, String value, String field) {
    String query = String.format("select * from %s where %s = #{param.value}  limit 1", tableName, field);
    Map<String, Object> param = Map.of("value", value);
    return reumMapper.selectOne(query, param);
  }

  public Map<String, Object> list(String tableName, Map<String, Object> param, int page, int pageSize){
    Map<String, Object> response = new HashMap<>();
    Set<String> whereKeySet = param.keySet();
    Map<String, Object> param_add = new HashMap<>();
    String val = "";
    String pkey = "";
    String where = "";
    if(whereKeySet.size() > 0){
      ///where절
      List<String> trash = new ArrayList<>();

      int t = 0;
      where = "where 1 ";
      for(String key : whereKeySet){
        t++;
        String substring = key.substring(key.length()-2);

        if(substring.equals(">>")){
          pkey = key.substring(0, key.length()-2);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s > #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        if(substring.equals("<<")){
          pkey = key.substring(0, key.length()-2);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s < #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }

        substring = key.substring(key.length()-1);
        if(substring.equals(">")){
          pkey = key.substring(0, key.length()-1);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s >= #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        if(substring.equals("<")){
          pkey = key.substring(0, key.length()-1);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          where += String.format(" and %s <= #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        where += String.format(" and %s = #{param.%s}", key, param.get(key));
        param_add.put(key, param.get(key));

      }
    }

    /// 페이지네이션
    String limit = " limit " + (page-1) * pageSize + ", " + pageSize;
    String query = String.format("select count(*) from %s %s", tableName, where);
    int size = reumMapper
        .queryInt(query, param_add);

    List<Map<Object, Object>> list = reumMapper
        .queryAll(String.format("select * from %s %s", tableName, where + limit), param_add);

    response.put("list", list);
    response.put("size", size);
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> selectAll(String query, Map<String, Object> param) {
    List<Map<Object, Object>> list = reumMapper.selectAll(query, param);
    Map<String, Object> response = new HashMap<>();
    response.put("list", list);
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> delData(String tableName, int idx) {
    Map<String, Object> params = Map.of("idx", idx);
    reumMapper.delData(tableName, params);
    Map<String, Object> response = new HashMap<>();
    response.put("err", 0);
    return response;
  }
}
