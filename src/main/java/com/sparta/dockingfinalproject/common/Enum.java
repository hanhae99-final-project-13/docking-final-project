package com.sparta.dockingfinalproject.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {

  String message() default "유효한 값을 입력해주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  Class<? extends java.lang.Enum<?>> enumClass();

  boolean ignoreCase() default false;

}