package com.sillylab.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 如果使用mybatis-plus, 使用@TableField注解，设置fill=FieldFill.INSERT或FieldFill.INSERT_UPDATE时，
 *  会触发该handler, 检查插入/更新时创建时间或更新时间字段是否设置，未设置则设为当前时间
 * @author zhengrun
 * @date 2022-03-28
 */
public class TimeMetaObjectHandler implements MetaObjectHandler {
    private static final String DEFAULT_CREATE_TIME_FIELD_NAME = "createTime";
    private static final String DEFAULT_UPDATE_TIME_FIELD_NAME = "updateTime";
    private String createTimeFieldName;
    private String updateTimeFieldName;
    private volatile boolean hasInit = false;
    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.getSetterNames() != null){
            checkInit();
            Date currentDate = new Date();
            //过滤创建和更新字段，未设置则设置当前时间为默认值
            Arrays.stream(metaObject.getSetterNames())
                    .filter(name -> Objects.equals(createTimeFieldName, name)
                            || Objects.equals(updateTimeFieldName, name))
                    .forEach(name -> {
                        if(metaObject.getSetterType(name).isAssignableFrom(Date.class)
                                && this.getFieldValByName(name, metaObject) == null){
                            this.setFieldValByName(name, currentDate, metaObject);
                        }
                    });
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.getSetterNames() != null){
            checkInit();
            Date currentDate = new Date();
            //过滤更新字段，未设置则设置当前时间为默认值
            Arrays.stream(metaObject.getSetterNames())
                    .filter(name -> Objects.equals(updateTimeFieldName, name))
                    .forEach(name -> {
                        if(metaObject.getSetterType(name).isAssignableFrom(Date.class)
                                && this.getFieldValByName(name, metaObject) == null){
                            this.setFieldValByName(name, currentDate, metaObject);
                        }
                    });
        }
    }


    private void checkInit() {
        if(!hasInit){
            init(DEFAULT_CREATE_TIME_FIELD_NAME, DEFAULT_UPDATE_TIME_FIELD_NAME);
        }
    }

    public void init(String createTimeFieldName, String updateTimeFieldName) {
        this.createTimeFieldName = createTimeFieldName;
        this.updateTimeFieldName = updateTimeFieldName;
        this.hasInit = true;
    }
}
