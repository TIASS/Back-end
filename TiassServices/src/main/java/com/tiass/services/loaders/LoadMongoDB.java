package com.tiass.services.loaders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.tiass.services.ManagerDB;
import com.tiass.services.utils.PropertiesHolder;

public class LoadMongoDB implements ServletContextListener {
	public static Logger logger = Logger.getLogger(LoadMongoDB.class);
	private ScheduledThreadPoolExecutor execInit = new ScheduledThreadPoolExecutor(1);

	/**
	 * pulls the system parameters form ngprops.properties, sets them into the
	 * ServletContext, and create the TreeSessionMap and set it in
	 * ApplicationScope
	 */
	public void contextInitialized(ServletContextEvent cse) {
		System.out.println("contextInitialized");
		try {
			execInit.schedule(() -> this.contextInitialize(cse), 1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void contextDestroyed(ServletContextEvent cse) {

	}

	private void contextInitialize(ServletContextEvent cse) {

		try {
			System.out.println("contextInitialize");
			Properties ppNGProps = new Properties();
			ppNGProps.load(this.getClass().getResourceAsStream("/ngprops.properties"));
			ManagerDB.setMongoDatabase(StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_host)),
					Integer.valueOf(StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_port))),
					StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_username)),
					StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_pass)),
					StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_name)), Arrays.asList(
							StringUtils.trim(ppNGProps.getProperty(PropertiesHolder.prop_db_packages)).split(",")));

			Properties ppLog4j = new Properties();
			ppLog4j.load(this.getClass().getResourceAsStream("/log4j.properties"));
			PropertyConfigurator.configure(ppLog4j);

		}

		catch (MalformedURLException e) {
			e.printStackTrace(System.out);
			logger.error(e.getStackTrace(), e);
		}

		catch (IOException e) {
			contextInitialize(cse);
			e.printStackTrace(System.out);
			logger.error(e.getStackTrace(), e);
		}

		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.error(e.getStackTrace(), e);
		} finally {

		}

	}

}
