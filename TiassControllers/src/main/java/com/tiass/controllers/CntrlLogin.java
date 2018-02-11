package com.tiass.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.tiass.models.utils.MiscManager;
import com.tiass.services.ServiceLogin;
import com.tiass.services.data.LoginData;
import com.tiass.services.data.MobileData;
import com.tiass.services.data.SignInData;
import com.tiass.services.data.served.LoginDataClient;
import com.tiass.services.data.served.TiassResponse;
import com.tiass.services.utils.ValidationErrorBuilder;
import com.tiass.services.validators.*;
 
@RestController()
@RequestMapping("/auth")
public class CntrlLogin {
	@Autowired
	ValidatorLogin loginValidator;
	@Autowired
	ValidatorSignin signinValidator;
	@Autowired
	ServiceLogin serviceLogin;

	@Autowired
	private MessageSource messageSource;

	public CntrlLogin() {

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, /*consume = "application/json",*/ produces = "application/json")
	public ResponseEntity<TiassResponse<LoginDataClient>> login(@Valid @RequestBody LoginData data, BindingResult result, HttpServletRequest request) {  
		data.setIpVisitor(request.getRemoteAddr() );
		 
/*
		loginValidator.validate(data, result);
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(result, messageSource));
		}
		LoginDataClient LCD = serviceLogin.login(data);
		return ResponseEntity.ok(LCD);
		
*/
		TiassResponse<LoginDataClient> TR = new TiassResponse<LoginDataClient>();
		loginValidator.validate(data, result);
		if (result.hasErrors())
			TR.setErrors(ValidationErrorBuilder.fromBindingErrors(result, messageSource));
		else {
			LoginDataClient LCD = serviceLogin.login(data); 
			TR.setTiassResults( LCD);
		}
		 
		return TR.constructResponse();
	
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<TiassResponse<LoginDataClient>> signin(@Valid @RequestBody SignInData data,
			BindingResult result, HttpServletRequest request) {  
		data.setIpVisitor(request.getRemoteAddr() );
		TiassResponse<LoginDataClient> TR = new TiassResponse<LoginDataClient>();
		signinValidator.validate(data, result);
		if (result.hasErrors())
			TR.setErrors(ValidationErrorBuilder.fromBindingErrors(result, messageSource));
		else {
			LoginDataClient LCD = serviceLogin.signin(data);
			System.out.println("LoginDataClient ");
			MiscManager.prettyprint(LCD);
			TR.setTiassResults(LCD);
		}
		/*
		 * if (TR.isResponseSet()) return ResponseEntity.ok(TR); else return
		 * ResponseEntity.badRequest().body(null);
		 */
		return TR.constructResponse();
	}

	@RequestMapping(value = "/mukin", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<TiassResponse<LoginDataClient>> mukin(@RequestBody MobileData data, HttpServletRequest request) {  
		data.setIpVisitor(request.getRemoteAddr() );
		TiassResponse<LoginDataClient> TR = new TiassResponse<LoginDataClient>(); 
		LoginDataClient LCD = serviceLogin.mukin(data);
		System.out.println("mukin LoginDataClient ");
		MiscManager.prettyprint(LCD);
		if (LCD != null) 
			TR.setTiassResults(LCD);
		
		return TR.constructResponse();
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private Logger logger = Logger.getLogger(CntrlLogin.class);
}
