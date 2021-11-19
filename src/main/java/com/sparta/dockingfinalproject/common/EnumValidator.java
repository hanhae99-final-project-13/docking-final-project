package com.sparta.dockingfinalproject.common;

import static org.springframework.util.StringUtils.hasText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {

  private Enum annotation;

  @Override
  public void initialize(Enum constraintAnnotation) {
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