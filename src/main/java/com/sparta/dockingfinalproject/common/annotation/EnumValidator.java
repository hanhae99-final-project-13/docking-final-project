package com.sparta.dockingfinalproject.common.annotation;

import static org.springframework.util.StringUtils.hasText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumTypeValid, String> {

  private EnumTypeValid annotation;

  @Override
  public void initialize(EnumTypeValid constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean result = false;
    Object[] enumValues = this.annotation.enumClass().getEnumConstants();

    if (hasText(value) && enumValues != null) {
      for (Object enumValue : enumValues) {

        if (value.equals(enumValue.toString())
            || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
          result = true;
          break;
        }
      }
    }
    return result;
  }

}