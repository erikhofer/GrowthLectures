package com.xinra.growthlectures.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
  
  
  
  @Override
  public void initialize(ValidEmail constraintAnnotation) {}
  
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    System.out.println("test");
    return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
  }
}