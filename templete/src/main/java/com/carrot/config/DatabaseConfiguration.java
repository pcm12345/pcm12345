package com.carrot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DatabaseConfiguration
 * 데이터베이스 접속, 트랜잭션 관리, DAO, Service 를 정의
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.carrot.mapper.main", sqlSessionTemplateRef = "mainTemplate") // Mapper(DAO) 인터페이스 스캔하도록 설정
public class DatabaseConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean(name="main")
	@ConfigurationProperties(prefix="spring.datasource.hikari.local")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	@Bean(name="mainDataSource")
	@Primary
	@Qualifier("mainDataSource")
	public DataSource dataSource() throws Exception {
		hikariConfig().setAutoCommit(false);
		DataSource dataSource = new HikariDataSource(hikariConfig());
		dataSource.getConnection().setAutoCommit(false);
		//return dataSource;
		return new LazyConnectionDataSourceProxy(dataSource);
	}
	
	@Bean(name="mainTransactionManager")
	@Primary
	public PlatformTransactionManager transactionManager() throws Exception {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;		
		//return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean(name="mainFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("mainDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:/mapper/xml/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	 
	@Bean(name="mainTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mainFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
