package com.tiass.services.validators;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
 
import com.tiass.models.entities.Users;
import com.tiass.models.utils.MiscManager;
import com.tiass.services.ServiceUsers; 
import com.tiass.services.data.SignInData;

@Service
public class ValidatorSignin implements Validator {
	@Autowired
	ServiceUsers serviceUsers;
	@Autowired
    private MessageSource messageSource;
	@Override
	public boolean supports(Class<?> clazz) {
		return SignInData.class.isAssignableFrom(clazz);
	}
	 

  
	@Override
	public void validate(Object target, Errors errors) {
		SignInData data = (SignInData) target;
		 
        // obtain locale from LocaleContextHolder
        Locale currentLocale = LocaleContextHolder.getLocale();
       

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage("signin.missing.email", new Object[]{ },currentLocale));
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", messageSource.getMessage("signin.missing.name", new Object[]{ },currentLocale)); 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pswrd", messageSource.getMessage("signin.missing.pswrd", new Object[]{ },currentLocale));
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pswrdconf", messageSource.getMessage("signin.missing.pswrd", new Object[]{ },currentLocale));
		
		if (StringUtils.isNotBlank(data.getEmail()) && !MiscManager.stringValidation(data.getEmail(), Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE)))
			errors.rejectValue("email", messageSource.getMessage("signin.invalid.email", new Object[]{ },currentLocale)); 
		
		if (StringUtils.isNotBlank(data.getName()) &&  !MiscManager.stringValidation(data.getName(), Pattern.compile("^[a-zA-z ]*$", Pattern.CASE_INSENSITIVE)))
			errors.rejectValue("name", messageSource.getMessage("signin.invalid.name", new Object[]{ },currentLocale));
		/*
		if (StringUtils.isNotBlank(data.getMiddlename()) && !MiscManager.stringValidation(data.getName(), Pattern.compile("/^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,10}/")))
			errors.rejectValue("middlename", messageSource.getMessage("signin.invalid.middlename", new Object[]{ },currentLocale));
		*/
		 
		 
		// (?=.*\d)		#   must contains one digit from 0-9
		 // (?=.*[a-z])		#   must contains one lowercase characters
		 
		  
		//              .		#     match anything with previous condition checking
		//                {6,20}	#        length at least 6 characters and maximum of 20
		if (StringUtils.isNotBlank(data.getPswrd()) && !MiscManager.stringValidation(data.getPswrd(), Pattern.compile("^((?=.*\\d)(?=.*[a-z]).{6,20})" )))
			errors.rejectValue("pswrd", messageSource.getMessage("signin.invalid.pswrd", new Object[]{ },currentLocale));
		/*
		if(StringUtils.isNotBlank(data.getPswrd()) && !data.getPswrd().equals(data.getPswrdconf()))
			errors.rejectValue("pswrdconf", messageSource.getMessage("signin.notequal.pswrd", new Object[]{ },currentLocale));
		 */
		if (serviceUsers.isEmailExist(data.getEmail()))
			errors.rejectValue("email", messageSource.getMessage("signin.cannot.use.email", new Object[]{ },currentLocale));

		if (!Arrays.asList(Users.FEMALE, Users.MALE, Users.UNDEFINED).contains(data.getGender()))
			errors.rejectValue("gender", messageSource.getMessage("signin.missing.gender", new Object[]{ },currentLocale));
	}
}
