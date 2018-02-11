package com.tiass.services.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.tiass.models.utils.MiscManager;
import com.tiass.services.data.served.ValidationError;

public class ValidationErrorBuilder {
	

    public static ValidationError fromBindingErrors(Errors errors, MessageSource messageSource) {
    	// obtain locale from LocaleContextHolder
        Locale currentLocale = LocaleContextHolder.getLocale();
        System.out.println("fromBindingErrors ");
        ValidationError error = new ValidationError(messageSource.getMessage("validation.process.failed", new Object[]{errors.getErrorCount() },currentLocale) );
        for (ObjectError objectError : errors.getAllErrors()) {
        	MiscManager.prettyprint(objectError);
            error.addValidationError(objectError.getCode());
        } 
        return error;
    }
}