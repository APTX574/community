package com.community.annotation;

import java.lang.annotation.*;

/**
 * @author aptx
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
