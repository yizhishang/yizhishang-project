package com.yizhishang.common.pdf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 袁永君
 * @description 自定义PDF注解
 * @since 2018/5/10 11:56
 **/
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PDFField {

    /**
     * 列名
     */
    String title() default "";

    /**
     * 宽度
     */
    int width() default 20;

    /**
     * 排序值: 升序排列
     */
    int order() default 0;

    Class<? extends PdfAnnotationEnum> enumClass() default PdfAnnotationEnum.class;

    /**
     * 字体大小
     */
    int fontSize() default 14;
}