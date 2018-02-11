package com.tiass.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiass.models.entities.UserCreds;
import com.tiass.models.entities.UserCredsDAO;
import com.tiass.models.entities.UserCredsEmail;
import com.tiass.models.entities.UserMuks;
import com.tiass.models.entities.UserMuksDAO;
import com.tiass.models.entities.Users;
import com.tiass.models.entities.UsersDAO;
import com.tiass.models.utils.DateUtilities;
import com.tiass.models.utils.MiscManager;
import com.tiass.models.utils.StringUtilities;
import com.tiass.services.ServiceUsers.UserEntities;
import com.tiass.services.data.LoginData;
import com.tiass.services.data.MobileData;
import com.tiass.services.data.SignInData;
import com.tiass.services.data.served.LoginDataClient; 

@Service("serviceLogin")
public class ServiceLoginImpl implements ServiceLogin {
	@Autowired
	ServiceUsers serviceUsers;

	@Override
	public LoginDataClient login(LoginData data) {

		try {
			LoginDataClient LCD = null;
			UserMuks muk = null;
			UserCreds creds = serviceUsers.getUserCreds(data.getEmail(), data.getPswrd());
			if (creds == null)
				return LCD;

			if (serviceUsers.isUserActif(creds.getUser())) {
				Calendar addedCal = DateUtilities.calendarToGMT(Calendar.getInstance());
				muk = new UserMuks();
				muk.setUserIp(data.getIpVisitor());
				muk.setUserAgent(data.getUserAgent());
				muk = this.loginMukUser(creds.getUser(), muk, addedCal);
			}
			if (muk != null) {
				MobileData md = new MobileData();
				md.setId(MiscManager.serializeObjectId(creds.getUser().getId()));

				md.setMuk(muk.getMuk());
				md.setLocaleId(data.getLocaleId());
				md.setUserAgent(muk.getUserAgent());
				md.setLang(data.getLang());
				md.setTzoffset(data.getTzoffset());
				md.setIpVisitor(data.getIpVisitor());

				LCD = new LoginDataClient();
				LCD.setUser(creds.getUser());
				LCD.setEmail(data.getEmail());
				if (creds instanceof UserCredsEmail)
					LCD.setEmail(((UserCredsEmail) creds).getEmail());

				LCD.setMobileData(md);

				return LCD;
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return null;
	}


	@Override
	public LoginDataClient mukin(MobileData data) {
		Query<UserMuks> query = ManagerDB.constructQuery(UserMuks.class);
		query.field("muk").equal(data.getMuk());
		query.field("userAgent").equal(data.getUserAgent());
		UserMuksDAO dao = new UserMuksDAO(UserMuks.class, ManagerDB.getDatastore());
		List<UserMuks> list = dao.executeQuery(query, -1, -1);
		UserMuks muk = null;
		if (list != null && list.size() > 0) {
			Calendar addedCal = DateUtilities.calendarToGMT(Calendar.getInstance());
			String nMuk = StringUtilities.generateFormToken(9) + addedCal.getTimeInMillis();
			muk = list.get(0);
			muk.setMuk(nMuk);
			muk.setUserIp(data.getIpVisitor());
			muk.setTimes(muk.getTimes() + 1);
			muk.setModifyDate(addedCal.getTime()); 
			muk = dao.update(muk);
		} 
		if (muk != null) {
			MobileData md = new MobileData();
			md.setId(MiscManager.serializeObjectId(muk.getUser().getId()));
			UserCreds creds = serviceUsers.getUserCreds(muk.getUser());
			md.setMuk(muk.getMuk());
			md.setLocaleId(data.getLocaleId());
			md.setUserAgent(muk.getUserAgent());
			md.setLang(data.getLang());
			md.setTzoffset(data.getTzoffset());
			md.setIpVisitor(data.getIpVisitor());

			LoginDataClient LCD = new LoginDataClient();
			LCD.setUser(muk.getUser()); 
			if (creds instanceof UserCredsEmail)
				LCD.setEmail(((UserCredsEmail) creds).getEmail());

			LCD.setMobileData(md);

			return LCD;
		}
		return null;
	}
	
	
	@Override
	public LoginDataClient signin(SignInData data) {
		 

		try {
			UserEntities userEntities = null;

			boolean emailExist = serviceUsers.isEmailExist(data.getEmail());
			if (emailExist)
				return null;

			Calendar addedCal = DateUtilities.calendarToGMT(Calendar.getInstance());

			userEntities = this.signinUSer(data, addedCal);

			if (userEntities != null) {
				MobileData md = new MobileData();
				md.setId(MiscManager.serializeObjectId(userEntities.getUser().getId()));

				md.setMuk(userEntities.getMuk().getMuk());
				md.setLocaleId(data.getLocaleId());
				md.setUserAgent(userEntities.getMuk().getUserAgent());
				md.setLang(data.getLang());
				md.setTzoffset(data.getTzoffset());
				md.setIpVisitor(userEntities.getMuk().getUserIp());

				LoginDataClient LCD =new LoginDataClient();
				LCD.setUser(userEntities.getUser());

				if (userEntities.getCreds() instanceof UserCredsEmail)
					LCD.setEmail(((UserCredsEmail) userEntities.getCreds()).getEmail());
				
				LCD.setMobileData(md);

				return LCD;
			}

		} catch (Exception e) {
			 e.printStackTrace(System.out);
		}

		return null;
	}

	/**
	 * <b>Used to log a user.</b>
	 * <p>
	 * <ul>
	 * <li>Checks if the user has a @UserMuks record with
	 * his @UserMuks.getUserAgent() in an interval of one day (24 hours).</li>
	 * <li>If so, get the record by incrementing the @UserMuks.getTimes()
	 * value.</li>
	 * <li>Otherwise create a new one.</li>
	 * </ul>
	 * </p>
	 * <b>This Method generate a new Muk-Token in each case.</b>
	 * 
	 * @param user
	 * @param muk
	 * @param addedCal
	 * @return @UserMuks
	 * @author Jimmy Bahole
	 * @version 1.0
	 * @since Monday 5 Feb 2018
	 */
	protected UserMuks loginMukUser(Users user, UserMuks muk, Calendar addedCal) {

		if (user == null || muk == null || StringUtils.isBlank(muk.getUserAgent()))
			return null;
		String ip = muk.getUserIp(), ua = muk.getUserAgent();
		String nMuk = StringUtilities.generateFormToken(9) + addedCal.getTimeInMillis();
		Calendar startDateCal = DateUtilities.getEndOfDay(addedCal);
		Calendar endDateCal = DateUtilities.getStartOfDay(addedCal);
		Date startDate = startDateCal != null ? startDateCal.getTime() : Calendar.getInstance().getTime();
		Date endDate = endDateCal != null ? endDateCal.getTime() : Calendar.getInstance().getTime();
		Query<UserMuks> query = ManagerDB.constructQuery(UserMuks.class);
		query.field("user").equal(user);
		query.field("userAgent").equal(ua);
		/**
		 * remove the ip check cause it changes
		 * query.field("userIp").equal(muk.getUserIp());
		 */
		query.field("insertDate").greaterThanOrEq(startDate);
		query.field("insertDate").lessThanOrEq(endDate);

		UserMuksDAO dao = new UserMuksDAO(UserMuks.class, ManagerDB.getDatastore());
		List<UserMuks> list = dao.executeQuery(query, -1, -1);
		if (list != null && list.size() > 0) {
			muk = list.get(0);
			muk.setMuk(nMuk);
			muk.setUserIp(ip);
			muk.setTimes(muk.getTimes() + 1);
			muk.setModifyDate(addedCal.getTime());
			muk = dao.update(muk);
		} else {
			muk.setUser(user);
			muk.setMuk(nMuk);
			muk.setTimes(1);
			muk.setInsertDate(addedCal.getTime());
			muk.setModifyDate(addedCal.getTime());
			muk = dao.saveEntity(muk, new InsertOptions());
		}
		return muk;
	}

	protected UserEntities signinUSer(SignInData data, Calendar addedCal) {
		UserEntities entities = null;
		UsersDAO daoU = new UsersDAO(Users.class, ManagerDB.getDatastore());
		UserMuksDAO daoUM = new UserMuksDAO(UserMuks.class, ManagerDB.getDatastore());
		UserCredsDAO<UserCredsEmail> daoUC = new UserCredsDAO<UserCredsEmail>(UserCredsEmail.class,
				ManagerDB.getDatastore());
		Users user = new Users();
		UserMuks muk = new UserMuks();
		UserCredsEmail creds = new UserCredsEmail();
		user.setName(data.getName());
		user.setSurName(data.getMiddlename());
		user.setActif(Boolean.TRUE);
		user.setGender(data.getGender());
		user.setInsertDate(addedCal.getTime());
		user.setModifyDate(addedCal.getTime());
		user = daoU.saveEntity(user, new InsertOptions());
		System.out.println("user : "+user);
		MiscManager.prettyprint(user);
		if (user == null)
			return entities;

		muk.setUser(user);
		muk.setMuk(StringUtilities.generateFormToken(9) + addedCal.getTimeInMillis());
		muk.setTimes(1);
		muk.setUserAgent(data.getUserAgent());
		muk.setUserIp(data.getIpVisitor());
		muk.setInsertDate(addedCal.getTime());
		muk.setModifyDate(addedCal.getTime());
		muk = daoUM.saveEntity(muk, new InsertOptions());
		System.out.println("muk : "+muk);
		MiscManager.prettyprint(muk);
		if (muk == null) {
			daoU.remove(user);
			return entities;
		}

		creds.setUser(user);
		creds.setPassword(data.getPswrd());
		creds.setEmail(data.getEmail());

		creds.setInsertDate(addedCal.getTime());
		creds.setModifyDate(addedCal.getTime());
		creds = daoUC.saveEntity(creds, new InsertOptions());

		System.out.println("creds : "+creds);
		MiscManager.prettyprint(creds);
		if (creds == null) {
			daoU.remove(user);
			daoUM.remove(muk);
			return entities;
		}

		entities = new UserEntities();
		entities.setUser(user);
		entities.setCreds(creds);
		entities.setMuk(muk);
		return entities;
	}

}
