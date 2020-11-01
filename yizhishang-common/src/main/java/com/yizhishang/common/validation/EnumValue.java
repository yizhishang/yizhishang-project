package com.yizhishang.common.validation;

import com.yizhishang.common.exception.ServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author yizhishang
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

    String message() default "枚举值不存在";

    int[] in() default {};

    Class<? extends Enum<?>> enumClass() default YesOrNoEnum.class;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类中必须实现的用以校验code值静态方法
     */
    String enumMethod() default "";

    @Slf4j
    class Validator implements ConstraintValidator<EnumValue, Integer> {

        private int[] in;

        private Class<? extends Enum<?>> enumClass;
        private String enumMethod;

        private Class<?>[] groups;

        @Override
        public void initialize(EnumValue field) {
            in = field.in();
            enumClass = field.enumClass();
            enumMethod = field.enumMethod();
            groups = field.groups();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            if (value == null) {
                return Boolean.FALSE;
            }

            if (in.length > 0) {
                for (int item : in) {
                    if (value.equals(item)) {
                        return false;
                    }
                }
                return Boolean.FALSE;
            }

            if (enumClass == null) {
                return Boolean.FALSE;
            }

            Class<?> valueClass = value.getClass();
            try {
                Method method = enumClass.getMethod(enumMethod, valueClass);
                if (!Boolean.TYPE.equals(method.getReturnType())) {
                    throw new ServiceException(String.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new ServiceException(String.format("%s method return is not static method in the %s class", enumMethod, enumClass));
                }

                Boolean result = (Boolean) method.invoke(null, value);
                if (result == null) {
                    return false;
                }
                return result;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("枚举类校验出错", e);
                return false;
            }
        }
    }

    @Getter
    enum YesOrNoEnum {
        /**
         * 1-是
         */
        YES(1, "是"),
        /**
         * 0-否
         */
        NO(0, "否");

        private int code;
        private String message;

        YesOrNoEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

    }
}
