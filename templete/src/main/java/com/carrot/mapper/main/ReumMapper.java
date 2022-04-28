package com.carrot.mapper.main;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReumMapper {

  List<Map<Object, Object>> selectAll(@Param("sql") String sql);

  List<Map<Object, Object>> selectAll(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  Map<String, Object> selectOne(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  void update(@Param("sql") String sql);

  void update(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  void insert(@Param("sql") String sql);

  void insert(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  void delete(@Param("sql") String sql);

  void delete(@Param("sql") String sql, @Param("param") Map<String, Object> data);

  Map<Object, Object> getData(
      @Param("table") String table,
      @Param("data") Map<String, Object> param);

  List<Map<Object, Object>> getList(@Param("table") String table,
      @Param("data") Map<String, Object> param);

  Map<Object, Object> queryOne(@Param("sql") String sql);

  Map<Object, Object> queryOne(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  List<Map<Object, Object>> queryAll(@Param("sql") String sql);

  List<Map<Object, Object>> queryAll(@Param("sql") String sql,
      @Param("param") Map<String, Object> param);

  int queryInt(@Param("sql") String sql);

  int queryInt(@Param("sql") String sql, @Param("param") Map<String, Object> param);

  int delData(@Param("table") String table, @Param("param") Map<String, Object> param);

  int insData(@Param("table") String table, @Param("data") Map<String, Object> data);

  int upData(@Param("table") String table, @Param("data") Map<String, Object> data);

  int upData(@Param("table") String table,
      @Param("data") Map<String, Object> data, @Param("where") String where);


}
