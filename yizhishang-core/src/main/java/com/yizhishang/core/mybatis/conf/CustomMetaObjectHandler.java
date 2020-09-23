package com.yizhishang.core.mybatis.conf;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.util.Date;

/**
 * @author yizhishang
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String VERSION = "version";

    private static final String ERROR_LOG = "没有{}此字段，则不处理";
    
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Object createTime = getFieldValByName(CREATE_TIME, metaObject);
            if (createTime == null) {
                this.setInsertFieldValByName(CREATE_TIME, new Date(), metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
            log.warn(ERROR_LOG, CREATE_TIME, e);
        }

        try {
            Object modifyTime = getFieldValByName(UPDATE_TIME, metaObject);
            if (modifyTime == null) {
                this.setInsertFieldValByName(UPDATE_TIME, new Date(), metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
            log.warn(ERROR_LOG, UPDATE_TIME, e);
        }

        try {
            Object version = this.getFieldValByName(VERSION, metaObject);
            if (version == null) {
                this.setInsertFieldValByName(VERSION, 1, metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
            log.warn(ERROR_LOG, VERSION, e);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        } catch (ReflectionException e) {
            //没有此字段，则不处理
            log.warn(ERROR_LOG, VERSION, e);
        }

        Object version = this.getFieldValByName(VERSION, metaObject);
        if (version == null) {
            throw new MybatisPlusException("更新时没有找到version");
        }
    }

}
