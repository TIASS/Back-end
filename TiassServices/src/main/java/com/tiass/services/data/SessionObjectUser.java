package com.tiass.services.data;

import java.util.Calendar;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;

/**
 * 
 * @author Jimmy Bahole
 *
 */
public class SessionObjectUser implements HttpSessionBindingListener, HttpSessionActivationListener {
	private String	ipVisitor;
	/**
	 * the user identification : it could be null or anything
	 */
	private String	userId;
	private String	userAgent;
	
	
	private Logger	logger	= Logger.getLogger(SessionObjectUser.class);

	

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		if (event.getValue() instanceof SessionObjectUser) {
			this.boundProcess(event, "SESSION BOUNDED");
		} 
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		if (event.getValue() instanceof SessionObjectUser) {
			this.boundProcess(event, "SESSION UN-BOUNDED");
		}
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent arg0) {
 		//System.out.println("session Did Activate******************************************");
	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent arg0) {
 		//System.out.println("session Will Passivate******************************************");
	}
	protected  void boundProcess(HttpSessionBindingEvent event, String prefix) {
		if(event !=null && event.getSession() !=null ){ 
			try{ 
				long creation = event.getSession().getCreationTime();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(creation);
				String result = String.format("##" + prefix + "## | IP : %1$-30s | DATE : %2$tY %2$tm %2$td %2$tH %2$tM %2$tS %2$tp | USER-ID : %3$s | USER-AGENT : %4$s | SESSION-ID : %5$s",
		 				((this.getIpVisitor() != null) ? this.getIpVisitor() : "UNDEFINED"),
						cal,
						((this.getUserId() != null) ? this.getUserId() : "UNDEFINED"),
						((this.getUserAgent()  != null) ? this.getUserAgent() : "UNDEFINED"),
						((event != null && event.getSession() != null && event.getSession().getId() != null) ? event.getSession().getId() : "UNDEFINED"));
				//System.out.println(result); 
				this.logger.info(result);
			}catch( java.lang.IllegalStateException e){
				e.printStackTrace(System.out);
			}catch( Exception e){
				e.printStackTrace(System.out);
			}
		}
		
	}
	public String getIpVisitor() {
		return ipVisitor;
	}

	public void setIpVisitor(String ipVisitor) {
		this.ipVisitor = ipVisitor;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
