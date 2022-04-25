package com.sillylab.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
public class MyBatisExtendsConfig {
    @Value("${mybatis.field.createTime:createTime}")
    private String createTimeFieldName;

    @Value("${mybatis.field.updateTime:updateTime}")
    private String updateTimeFieldName;

    /**
     * 插入/更新的创建时间、更新时间拦截器
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    @ConditionalOnBean({SqlSessionFactory.class})
    public TimeInterceptor timeInterceptor(SqlSessionFactory sqlSessionFactory){
        TimeInterceptor timeInterceptor = new TimeInterceptor();
        Properties properties = new Properties();
        properties.setProperty(TimeInterceptor.CREATE_TIME_FIELD_NAME,createTimeFieldName);
        properties.setProperty(TimeInterceptor.UPDATE_TIME_FIELD_NAME,updateTimeFieldName);
        timeInterceptor.setProperties(properties);
        sqlSessionFactory.getConfiguration().addInterceptor(timeInterceptor);
        return timeInterceptor;
    }

    @Bean
    @ConditionalOnClass({MetaObjectHandler.class})
    public TimeMetaObjectHandler timeMetaObjectHandler(){
        TimeMetaObjectHandler handler = new TimeMetaObjectHandler();
        handler.init(createTimeFieldName, updateTimeFieldName);
        return handler;
    }
}
