package com.bci.reactive.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertyResolver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PasswordRegexValidValidator implements
        ConstraintValidator<PasswordRegexValid, String> {

    private Pattern pattern = null;
    @Override
    public void initialize(PasswordRegexValid passwordRegexValid) {
    }
    @Autowired
    public PasswordRegexValidValidator(PropertyResolver propertyResolver) {
        log.info("PasswordRegexValidValidator {} ", propertyResolver.getRequiredProperty("config.valid.password"));
        this.pattern = Pattern.compile(propertyResolver.getRequiredProperty("config.valid.password"));
    }
    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        log.debug("isValid {} {}", contactField, this.pattern );
        Matcher matcher = this.pattern.matcher(contactField);
        if (matcher.find()){
            return true;
        }
        return false;
    }

}