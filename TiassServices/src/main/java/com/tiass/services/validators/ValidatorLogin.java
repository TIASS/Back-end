package com.tiass.services.validators;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tiass.models.entities.UserCreds;
import com.tiass.services.ServiceUsers;
import com.tiass.services.data.LoginData;

@Service
public class ValidatorLogin implements Validator {
	@Autowired
	ServiceUsers serviceUsers;
	@Autowired
    private MessageSource messageSource;
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return LoginData.class.isAssignableFrom(clazz);
	}
	 
	@Override
	public void validate(Object target, Errors errors) {
		LoginData data = (LoginData) target;

        // obtain locale from LocaleContextHolder
        Locale currentLocale = LocaleContextHolder.getLocale();
        
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage("login.missing.email", new Object[]{ },currentLocale));
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pswrd", messageSource.getMessage("login.missing.pswrd", new Object[]{ },currentLocale));
 
		 
		UserCreds creds = serviceUsers.getUserCreds(data.getEmail(), data.getPswrd());
		if (creds == null || !serviceUsers.isUserActif(creds.getUser()))
			errors.rejectValue("email", messageSource.getMessage("login.failed", new Object[]{ },currentLocale) ); 
	}
}
