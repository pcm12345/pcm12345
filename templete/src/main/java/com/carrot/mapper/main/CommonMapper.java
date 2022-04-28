package com.carrot.mapper.main;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface CommonMapper {

    public Map<Object, Object> selectOne(@Param("sql") String sql);
    public Map<Object, Object> selectOne(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public List<Map<Object, Object>> selectAll(@Param("sql") String sql);
    public List<Map<Object, Object>> selectAll(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public int selectInt(@Param("sql") String sql);
    public int selectInt(@Param("sql") String sql, @Param("param") Map<String, Object> param);

    public void delete(@Param("sql") String sql);
    public int delete(@Param("table") String table, @Param("param") Map<String, Object> param);
    
    public void insert(@Param("sql") String sql);
    public int insert(@Param("table") String table, @Param("data") Map<String, Object> data);

    public int update(@Param("table") String table, @Param("data") Map<String, Object> data, @Param("where") String where);
    public int update(@Param("table") String table, @Param("data") Map<String, Object> data, @Param("where") String where, @Param("param") Map<String, Object> param);


}
