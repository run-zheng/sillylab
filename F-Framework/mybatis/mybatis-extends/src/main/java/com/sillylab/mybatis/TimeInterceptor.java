package com.sillylab.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 *  插入/更新时，如果对象中有创建时间/更新时间字段，未设置值，将会被设置成当前时间
 *      默认时间字段名称为createTime, updateTime
 *  TODO: 增加注解版本 
 */
@Intercepts(
        @Signature(
                type = org.apache.ibatis.executor.Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
)
public class TimeInterceptor implements Interceptor {

    public static final String CREATE_TIME_FIELD_NAME = "__createTime__";
    public static final String UPDATE_TIME_FIELD_NAME = "__updateTime__";

    public static final String SERIAL_VERSION_UID = "serialVersionUID";
    public static final String MYBATIS_PLUS_PARAM = "param1";

    private static final String DEFAULT_CREATE_TIME_FIELD_NAME = "createTime";
    private static final String DEFAULT_UPDATE_TIME_FIELD_NAME = "updateTime";
    /**
     * 创建时间字段名
     */
    private String createTimeFieldName;
    /**
     * 更新时间字段名
     */
    private String updateTimeFieldName;
    /**
     * 外部初始化配置
     */
    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //获取命令类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //获取参数
        Object parameter = invocation.getArgs()[1];
        //插入/更新时，检查并设置创建时间/更新时间
        if(parameter != null && (SqlCommandType.INSERT.equals(sqlCommandType)
                || SqlCommandType.UPDATE.equals(sqlCommandType))){
            //是否可能是mybatis-plus的通用更新接口的参数形式，兼容通用mapper.update
            boolean isPlusUpdate = parameter.getClass().getDeclaredFields().length == 1
                    && parameter.getClass().getDeclaredFields()[0].getName().equals(SERIAL_VERSION_UID)
                    && parameter.getClass().isAssignableFrom(Map.class);
            //获取所有定义的属性
            Field[] declareFields = getAllDeclareFields(parameter, isPlusUpdate);
            Date currentDate = new Date();
            Arrays.stream(declareFields)
                    //过滤创建时间/更新时间字段
                    .filter(f -> f.getType().isAssignableFrom(Date.class)
                            && (Objects.equals(this.createTimeFieldName, f.getName())
                                ||Objects.equals(this.updateTimeFieldName, f.getName())))
                    .forEach(f -> {
                        try{
                            //插入时，需要设置创建时间
                            if(SqlCommandType.INSERT.equals(sqlCommandType)){
                                if(Objects.equals(this.createTimeFieldName, f.getName())){
                                    brokenAccessSetField(parameter, currentDate, f);
                                }
                            }
                            //更新/插入时，需要设置更新时间
                            if(Objects.equals(this.updateTimeFieldName, f.getName())){
                                if(isPlusUpdate){
                                    Map<String, Object> updateParams = (Map<String, Object>) parameter;
                                    if(updateParams.containsKey(MYBATIS_PLUS_PARAM)){
                                        brokenAccessSetField(updateParams.get(MYBATIS_PLUS_PARAM), currentDate, f);
                                    }
                                }else {
                                    brokenAccessSetField(parameter, currentDate, f);
                                }
                            }
                        }catch (Exception ex){
                            //ignore
                        }
                    });
        }
        return invocation.proceed();
    }

    private void brokenAccessSetField(Object parameter, Date currentDate, Field field)
            throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        //未设置
        Object o = field.get(parameter);
        if(o == null){
            field.set(parameter, currentDate);
        }
        field.setAccessible(isAccessible);
    }

    private Field[] getAllDeclareFields(Object parameter, boolean isPlusUpdate) {
        Field[] declareFields = null ;
        //mybatis-plus的特殊处理
        if(isPlusUpdate && parameter instanceof Map){
            Map<String, Object> updateParams = (Map<String, Object>)parameter;
            if(updateParams.containsKey(MYBATIS_PLUS_PARAM)){
                Class<?> updateParamType = updateParams.get(MYBATIS_PLUS_PARAM).getClass();
                declareFields = updateParamType.getDeclaredFields();
                if(updateParamType.getSuperclass() != null){
                    declareFields = getSuperFields(updateParamType, declareFields);
                }
            }
        }else {
            //获取私有变量
            declareFields = parameter.getClass().getDeclaredFields();
            if(parameter.getClass().getSuperclass() != null){
                declareFields = getSuperFields(parameter.getClass(), declareFields);
            }
        }
        return declareFields;
    }

    private Field[] getSuperFields(Class<?> aClass, Field[] declareFields) {
        return new Field[0];
    }

    @Override
    public Object plugin(Object target) {
        if(target instanceof org.apache.ibatis.executor.Executor){
            initFieldName();
            return Plugin.wrap(target, this); 
        }
        return target;
    }

    private void initFieldName() {
        if(properties != null){
            this.createTimeFieldName = properties.getProperty(CREATE_TIME_FIELD_NAME);
            this.updateTimeFieldName = properties.getProperty(UPDATE_TIME_FIELD_NAME);
        }
        if(!StringUtils.hasText(this.createTimeFieldName)){
            this.createTimeFieldName = DEFAULT_CREATE_TIME_FIELD_NAME;
        }
        if(!StringUtils.hasText(this.updateTimeFieldName)){
            this.updateTimeFieldName = DEFAULT_UPDATE_TIME_FIELD_NAME;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
