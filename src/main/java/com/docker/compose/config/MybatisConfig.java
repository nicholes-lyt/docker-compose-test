package com.docker.compose.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MybatisConfig {

  @Primary
  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(){
    try {
      SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
      sqlSessionFactoryBean.setDataSource(dataSource());
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      sqlSessionFactoryBean
          .setMapperLocations(resolver.getResources("classpath:/com/docker/compose/*.xml"));
      return sqlSessionFactoryBean.getObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Bean
  public PlatformTransactionManager transactionManager(){
    return new DataSourceTransactionManager(dataSource());
  }

}
