package com.yizhishang.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author yizhishang
 */
public class ValidateUtil<T> {

    private static volatile Validator validator;

    private static void initialize() {
        if (validator == null) {
            synchronized (ValidateUtil.class) {
                if (validator == null) {
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    validator = validatorFactory.getValidator();
                }
            }
        }
    }

    /**
     * 验证请求参数(单个错误)
     *
     * @return null 则说明验证成功，如果非null 则说明验证失败
     */
    public String validateOne(T model) {
        initialize();
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if (null == violations || violations.isEmpty()) {
            return null;
        }
        for (ConstraintViolation<T> violation : violations) {
            if (StringUtils.isNotEmpty(violation.getMessage())) {
                return violation.getMessage();
            }
        }
        return null;
    }

    /**
     * 对象是否不为空(新增)
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return "".equals(o.toString().trim());
        }
        if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        }
        if (o instanceof Map) {
            return ((Map) o).size() == 0;
        }
        if (o instanceof Object[]) {
            return ((Object[]) o).length == 0;
        }
        if (o instanceof int[]) {
            return ((int[]) o).length == 0;
        }
        if (o instanceof long[]) {
            return ((long[]) o).length == 0;
        }
        return false;
    }

    /**
     * 对象组中是否存在空对象
     */
    public static boolean isOneEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全是空对象
     */
    public static boolean isAllEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

}
