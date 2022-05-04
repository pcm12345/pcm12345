package com.carrot.common.mapper.crm;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface ReumMapper {

  public List<Map<Object, Object>> selectAll(@Param("sql") String sql);

  public List<Map<Object, Object>> selectAll(@Param("sql") String sql, Map<String, Object> param);

  public Map<String, Object> selectOne(@Param("sql") String sql, @Param("param") Map<String, Object> param);

  public void update(@Param("sql") String sql);

  public void update(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  public void insert(@Param("sql") String sql);

  public void insert(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  public void delete(@Param("sql") String sql);

  public void delete(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  public Map<Object, Object> getData(@Param("table") String table,
      @Param("data") Map<String, Object> param);

  public List<Map<Object, Object>> getList(@Param("table") String table,
      @Param("data") Map<String, Object> param);

  /* PHP Reum_PDO와 동일 */
  public Map<Object, Object> queryOne(@Param("sql") String sql);

  public Map<Object, Object> queryOne(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  public List<Map<Object, Object>> queryAll(@Param("sql") String sql);

  public List<Map<Object, Object>> queryAll(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  public int queryInt(@Param("sql") String sql);

  public int queryInt(@Param("sql") String sql, @Param("param") Map<String, Object> param);

  public int delData(@Param("table") String table, @Param("param") Map<String, Object> param);

  public int insData(@Param("table") String table, @Param("data") Map<String, Object> data);

  public int upData(@Param("table") String table, @Param("data") Map<String, Object> data);

  public int upData(@Param("table") String table, @Param("data") Map<String, Object> data,
      @Param("where") String where);
}
