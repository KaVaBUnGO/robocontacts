package com.robocontacts.domain.validator;


import com.robocontacts.domain.UserCreateForm;
import com.robocontacts.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ekaterina on 11.10.2016.
 */

@Component
public class UserCreateFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreateFormValidator.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz){
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors){
        LOGGER.debug("Validating {}", target);
        UserCreateForm form = (UserCreateForm)target;
        validatePassword(errors, form);
        validateLogin(errors, form);
    }

    private void validateLogin(Errors errors, UserCreateForm form) {
        if (userService.getUserByLogin(form.getLogin()) != null){
            errors.reject("User with this name already exist");
        }
    }

    private void validatePassword(Errors errors, UserCreateForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())){
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

}
