package com.yizhishang.common.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author yizhishang
 */
public class ValidateUtil<T> {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();

    /**
     * 验证请求参数(单个错误)
     *
     * @return null 则说明验证成功，如果非null 则说明验证失败
     */
    public String validateOne(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if (null == violations || violations.isEmpty()) {
            return null;
        }
        for (ConstraintViolation<T> violation : violations) {
            return violation.getMessage();
        }
        return null;
    }
}
