package com.yizhishang.common.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Money.Validator.class)
public @interface Money {

    String message() default "不是金额格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Money, Double> {

        /**
         * 表示金额的正则表达式
         */
        private String moneyReg = "^(-)?\\d+(\\.\\d{1,2})?$";
        private Pattern moneyPattern = Pattern.compile(moneyReg);

        @Override
        public boolean isValid(Double value, ConstraintValidatorContext arg1) {
            if (value == null) {
                //金额是空的，返回true，是因为如果null，则会有@NotNull进行提示
                //如果这里false的话，那金额是null，@Money中的message也会进行提示
                //自己可以尝试
                return true;
            }
            return moneyPattern.matcher(value.toString()).matches();
        }
    }
}