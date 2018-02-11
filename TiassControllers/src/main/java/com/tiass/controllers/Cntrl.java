package com.tiass.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.tiass.models.MongoDoc;
import com.tiass.models.utils.DateUtilities;
import com.tiass.models.utils.StringUtilities;
import com.tiass.services.ManagerDB;
import com.tiass.services.data.PostData;
import com.tiass.services.data.UserEnvironment;
import com.tiass.services.utils.ManagerResourceBundle;
import com.tiass.services.utils.PropertiesHolder;

public class Cntrl {
	private final static String TEMP_FOLDER = "temps_files";
	public final static String ADMIN_FOLDER = "admin_folder";
	public final static String CLIENT_FOLDER = "client_folder";
	public final static String ALBUM_FOLDER = "album_folder";
	public final static String PAGE_FOLDER = "page_folder";
	private UserEnvironment UE;
	private Calendar addedCal;
	private String realServletContextPath = null;
	private Logger logger = Logger.getLogger(Cntrl.class);

	private Map<String, Object> mapResults = null;

	public Cntrl() {
		super();
	}

	public Cntrl(Cntrl controller) {
		this.setUE(controller.getUE());
		this.setRealServletContextPath(controller.getRealServletContextPath());
	}

	protected void initMapResults() {
		if (this.getMapResults() != null) {
			return;
		}
		this.setMapResults(new HashMap<String, Object>());
		this.getMapResults().put(PropertiesHolder.jsonkeyerror, new ArrayList<String>());
		this.getMapResults().put(PropertiesHolder.jsonkeymessage, new ArrayList<String>());
		this.getMapResults().put(PropertiesHolder.jsonkeyobject, null);

	}

	@SuppressWarnings("unchecked")
	protected void addToMapResults(String key, Object value) {
		initMapResults();
		switch (key) {
		case PropertiesHolder.jsonkeyprocess:
			if (value instanceof Boolean) {
				this.getMapResults().put(PropertiesHolder.jsonkeyprocess, Boolean.class.cast(value));
			}
			break;
		case PropertiesHolder.jsonkeyerror:
			if (value instanceof String) {
				((ArrayList<String>) this.getMapResults().get(PropertiesHolder.jsonkeyerror))
						.add(String.valueOf(value));
			}
			break;
		case PropertiesHolder.jsonkeymessage:
			if (value instanceof String) {
				((ArrayList<String>) this.getMapResults().get(PropertiesHolder.jsonkeymessage))
						.add(String.valueOf(value));

			}
			break;
		case PropertiesHolder.jsonkeyobject:
			this.getMapResults().put(PropertiesHolder.jsonkeyobject, value);

			break;
		default:
			break;
		}

	}

	public final static boolean instanciateCmmnApplication() {
		try {
			ManagerResourceBundle.addOwnResource();
			ManagerResourceBundle.addResource("wp.controllers.commons.msgs", "validators.");
			return true;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return false;
	}

	public final static boolean instanciateCmmnMongoDB(ServletContext servletContext) {
		boolean ok = false;
		try {
			String prop_db_packages = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_packages));
			final List<String> DATASTORE_PACKAGES = new ArrayList<String>();
			if (StringUtils.isNotBlank(prop_db_packages))
				for (String pack : Arrays.asList(prop_db_packages.split(",")))
					if (StringUtils.isNotBlank(pack))
						DATASTORE_PACKAGES.add(StringUtils.trim(pack));

			String prop_db_name = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_name));
			final String DATABASENAME = StringUtils.isNotBlank(prop_db_name) ? StringUtils.trim(prop_db_name) : null;

			String prop_db_host = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_host));
			final String HOST = StringUtils.isNotBlank(prop_db_host) ? StringUtils.trim(prop_db_host) : null;

			String prop_db_port = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_port));
			final int PORT = StringUtils.isNotBlank(prop_db_port)
					&& StringUtils.isNumeric(StringUtils.trim(prop_db_port))
					&& Integer.parseInt(StringUtils.trim(prop_db_port)) > 0
							? Integer.parseInt(StringUtils.trim(prop_db_port)) : -1;

			String prop_db_username = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_username));
			final String USER = StringUtils.isNotBlank(prop_db_username) ? StringUtils.trim(prop_db_username) : null;

			String prop_db_pass = String.valueOf(servletContext.getAttribute(PropertiesHolder.prop_db_pass));
			final String PASSWORD = StringUtils.isNotBlank(prop_db_pass) ? StringUtils.trim(prop_db_pass) : null;
			/*
			 * System.out.println("DATASTORE_PACKAGES : " + DATASTORE_PACKAGES);
			 * System.out.println("DATABASENAME : " + DATABASENAME);
			 * System.out.println("HOST : " + HOST);
			 * System.out.println("PORT : " + PORT);
			 * System.out.println("USER : " + USER);
			 * System.out.println("PASSWORD : " + PASSWORD);
			 */
			ok = ManagerDB.setMongoDatabase(HOST, PORT, USER, PASSWORD, DATABASENAME, DATASTORE_PACKAGES);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return ok;
	}

	public void setPermissionException(String messageException[]) throws Exception {
		this.setControllerException(messageException);
	}

	protected File getPlateformFolder() {
		File plateformFolder = new File(this.getRealServletContextPath());
		try {
			if (plateformFolder == null || !plateformFolder.exists() || !plateformFolder.isDirectory())
				plateformFolder.mkdir();

			plateformFolder = new File(this.getRealServletContextPath());
		} catch (SecurityException e) {
			e.printStackTrace(System.out);
		}

		return plateformFolder;
	}

	public static File createNewFolder(File plateformFolder, String folderName) {
		if (plateformFolder == null || !plateformFolder.exists() || !plateformFolder.isDirectory()
				|| StringUtils.isBlank(folderName))
			return null;

		File file = new File(plateformFolder, folderName);
		if (file.exists() && file.isDirectory())
			return file;

		try {
			file.mkdir();
		} catch (SecurityException e) {
			e.printStackTrace(System.out);
		}
		if (file != null && file.exists() && file.isDirectory())
			return file;
		else
			return null;
	}

	// @Context UriInfo uriInfo,
	public static boolean checkFileSize(File pfFolder, String fileFormat, BufferedImage bi, long limit) {
		boolean isOK = false;
		try {
			File tempFolder = new File(pfFolder, TEMP_FOLDER);
			if (tempFolder == null || !tempFolder.exists() || !tempFolder.isDirectory()) {
				tempFolder = Cntrl.createNewFolder(pfFolder, TEMP_FOLDER);
				if (tempFolder == null)
					return isOK;
			}
			String tempName = StringUtilities.generateFormToken(9) + Calendar.getInstance().getTimeInMillis();
			final File file = writeToFile(bi, tempFolder, tempName + "." + fileFormat, fileFormat);

			/*
			 * try { Path path = Paths.get(file.getAbsolutePath()); byte[] data
			 * = Files.readAllBytes(path); MagicMatch match =
			 * Magic.getMagicMatch(data); String mimeType = match.getMimeType();
			 * System.out.println("mimeType :" + file.getName() + " is " +
			 * mimeType);
			 * 
			 * } catch (MagicParseException | MagicMatchNotFoundException |
			 * MagicException | IOException e) { e.printStackTrace(); }
			 */
			// System.out.println(" *** checkFileSize : " + file.length() + " #
			// " + limit);
			if (file.length() <= limit) {
				isOK = true;
			}
			// System.out.println("Mime Type of " + file.getName() + " is " +
			// new MimetypesFileTypeMap().getContentType(file));
			Thread t = new Thread() {
				@Override
				public void run() {
					file.delete();
				}
			};
			t.start();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return isOK;
	}

	// @Context UriInfo uriInfo,
	public static Map.Entry<File, Boolean> checkFileSize(File pfFolder, String fileFormat, final InputStream is,
			long limit) {
		Map.Entry<File, Boolean> entry = null;
		boolean isOK = false;
		try {
			File tempFolder = new File(pfFolder, TEMP_FOLDER);
			if (tempFolder == null || !tempFolder.exists() || !tempFolder.isDirectory()) {
				tempFolder = Cntrl.createNewFolder(pfFolder, TEMP_FOLDER);
				if (tempFolder == null) {
					entry = new AbstractMap.SimpleEntry<File, Boolean>(null, Boolean.FALSE);
					return entry;
				}
			}
			String tempName = StringUtilities.generateFormToken(9) + Calendar.getInstance().getTimeInMillis();
			final File file = writeToFile(is, tempFolder, tempName + "." + fileFormat);

			// System.out.println(" *** checkFileSize : " + file.length() + " #
			// " + limit);
			if (file.length() <= limit) {
				isOK = true;
			}
			entry = new AbstractMap.SimpleEntry<File, Boolean>(file, isOK);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return entry;
	}

	public static File writeToFile(BufferedImage bi, File folder, String fileName, String fileFormat)
			throws IOException {
		if (folder != null && folder.exists() && folder.isDirectory()) {
			File f = new File(folder, fileName);

			if (ImageIO.write(bi, fileFormat, f))
				return f;

		}
		return null;
	}

	public static File writeToFile(final InputStream is, File folder, String fileName) throws IOException {
		if (folder != null && folder.exists() && folder.isDirectory()) {
			// byte[] buffer = new byte[is.available()]; is.read(buffer);

			/*
			 * System.out.println("InputStream available fi : " +
			 * fi.available()); System.out.println("InputStream available is : "
			 * + is.available()); System.out.println("Files.copy : " +
			 * f.toPath()); long written = Files.copy(is, f.toPath());
			 * System.out.println("written Files.copy : " + written); if
			 * (written < 1) { OutputStream outStream = new FileOutputStream(f);
			 * written = IOUtils.copy(is, outStream);
			 * System.out.println("written IOUtils.copy : " + written);
			 * IOUtils.closeQuietly(outStream);
			 * 
			 * }
			 */
			final File f = new File(folder, fileName);
			try {

				// long written = Files.copy(is, f.toPath());
				// System.out.println("written Files.copy : " + written);
				// System.out.println("Done!");
				return f;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
			/*
			 * try{ final File f = new File(folder, "BBB"+fileName);
			 * OutputStream outStream = new FileOutputStream(f); long written =
			 * IOUtils.copy(is, outStream);
			 * System.out.println("written IOUtils.copy : " + written);
			 * IOUtils.closeQuietly(outStream); System.out.println("Done! BBB");
			 * 
			 * }catch(Exception e){ e.printStackTrace(System.out); return null;
			 * }
			 */
			/*
			 * File f = new File(folder, fileName); try{
			 * 
			 * OutputStream outStream = new FileOutputStream(f); int read = 0;
			 * byte[] bytes = new byte[1024];
			 * 
			 * while ((read = is.read(bytes)) != -1) {
			 * System.out.println("read : "+read); outStream.write(bytes, 0,
			 * read); } IOUtils.closeQuietly(outStream);
			 * System.out.println("Done!"); return f; }catch(Exception e){
			 * e.printStackTrace(System.out); return null; }
			 */

		}
		return null;
	}

	public static File moveToFile(Path source, File folder, String fileName) throws IOException {
		File fdest = null;
		if (folder != null && folder.exists() && folder.isDirectory()) {
			File f = new File(folder, fileName);
			Path destination = Files.move(source, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
			fdest = destination.toFile();
		}
		return fdest;
	}

	public static String getFileFormat(final InputStream is, FormDataContentDisposition fileDetail) {

		String format = null;

		try {

			format = URLConnection.guessContentTypeFromStream(is);
			// System.out.println("guess Content Type From Stream : " + format);

			if (format == null)
				format = URLConnection.guessContentTypeFromName(fileDetail.getFileName());
			// System.out.println("guess Content Type From Name : " + format);

			if (format == null) {
			} else if (format.equals("image/png"))
				format = "png";
			else if (format.equals("image/jpeg"))
				format = "jpeg";
			else if (format.equals("image/gif"))
				format = "gif";
			else if (format.equals("image/x-icon"))
				format = "ico";
			else if (format.equals("application/pdf"))
				format = "pdf";
			else if (format.equals("text/html"))
				format = "html";
			else if (format.equals("text/plain"))
				format = "txt";
			else if (format.equals("text/css"))
				format = "css";
			else if (format.equals("text/csv"))
				format = "csv";
			else if (format.equals("application/msword"))
				format = "doc";
			else if (format.equals("application/java-archive"))
				format = "jar";
			else if (format.equals("application/xml"))
				format = "xml";
			else if (format.equals("application/zip"))
				format = "zip";
			else if (format.equals("video/mpeg"))
				format = "mpeg";
			else if (format.equals("video/mp4"))
				format = "mp4";
			else if (format.equals("video/flv"))
				format = "flv";

		} catch (IOException e) {
			format = null;
			e.printStackTrace(System.out);
		}

		return format;
	}

	public static <E extends MongoDoc> void docCalendarFromGMT(PostData pd, E doc) {
		/*
		 * Field[] fields = doc.getClass().getDeclaredFields();
		 * System.out.println("fields : " + fields); try { if (fields != null)
		 * for (Field f : fields) System.out.println("Declaring Class : " +
		 * f.getDeclaringClass().getName() + " - " + f.getType().getName() +
		 * " - " + f.getClass().getName());
		 * 
		 * } catch (Exception e) { e.printStackTrace(System.out); } try {
		 * System.out.println("****Tzoffset : "+pd.getTzoffset());
		 * System.out.println("getTime 1 : "+doc.getInsertDate().getTime());
		 * SimpleDateFormat formatter = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		 * 
		 * formatter.setTimeZone(TimeZone.getTimeZone("GMT" +
		 * pd.getTzoffset()));
		 * 
		 * System.out.println("getTime - : "+LocalDateTime.parse(
		 * formatter.format(doc.getInsertDate()) ) .atOffset( ZoneOffset.of (
		 * pd.getTzoffset() ) ) .toInstant().toEpochMilli());
		 * 
		 * Date date = new Date(LocalDateTime.parse(
		 * formatter.format(doc.getInsertDate()) ) .atOffset( ZoneOffset.of (
		 * pd.getTzoffset() ) ) .toInstant().toEpochMilli()) ; // Create a new
		 * Date object doc.setInsertDate(date); String sDate =
		 * formatter.format(doc.getInsertDate()); // Convert to String first
		 * System.out.println("sDate : "+sDate);
		 * System.out.println("getTime 2 : "+doc.getInsertDate().getTime());
		 * 
		 * //doc.setInsertDate( convertDateTimeZone(doc.getInsertDate(), "GMT",
		 * "GMT"+pd.getTzoffset()));
		 * 
		 * } catch (Exception e) { e.printStackTrace(System.out); }
		 */
		try {
			Calendar insertDate = Calendar.getInstance();
			insertDate.setTime(doc.getInsertDate());
			insertDate = DateUtilities.calendarFromGMT(insertDate, pd.getTzoffset());
			if (insertDate != null)
				doc.setInsertDate(insertDate.getTime());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		try {
			Calendar modifyDate = Calendar.getInstance();
			modifyDate.setTime(doc.getModifyDate());
			modifyDate = DateUtilities.calendarFromGMT(modifyDate, pd.getTzoffset());
			if (modifyDate != null)
				doc.setModifyDate(modifyDate.getTime());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static Date convertDateTimeZone(Date date, String fromTimeZone, String toTimeZone) {
		TimeZone toTZ = TimeZone.getTimeZone(toTimeZone);
		Calendar toCal = Calendar.getInstance(toTZ);

		TimeZone fromTZ = TimeZone.getTimeZone(fromTimeZone);
		Calendar fromCal = Calendar.getInstance(fromTZ);
		fromCal.setTime(date);
		toCal.setTimeInMillis(fromCal.getTimeInMillis() + toTZ.getOffset(fromCal.getTimeInMillis())
				- TimeZone.getDefault().getOffset(fromCal.getTimeInMillis()));
		return toCal.getTime();
	}

	public UserEnvironment getUE() {
		return UE;
	}

	public void setUE(UserEnvironment uE) {
		UE = uE;
	}

	/**
	 * 
	 * get the current GMT Calendar Data. Created if not set.
	 * 
	 * @return GMT Calendar
	 * @throws Exception
	 */
	public Calendar getAddedCal() throws Exception {
		if (addedCal == null)
			this.setAddedCal(this.calendarToGMT(Calendar.getInstance()));

		return addedCal;
	}

	public void setAddedCal(Calendar addedCal) {
		this.addedCal = addedCal;
	}

	public String getRealServletContextPath() {
		return realServletContextPath;
	}

	public void setRealServletContextPath(String realServletContextPath) {
		if (realServletContextPath == null) {
			realServletContextPath = "";
		}
		if (!realServletContextPath.endsWith("/")) {
			realServletContextPath += "/";
		}
		this.realServletContextPath = realServletContextPath;
	}

	/**
	 * create GMT Calendar
	 * 
	 * @param date
	 * @return GMT Calendar
	 * @throws Exception
	 */
	public Calendar calendarToGMT(Calendar date) throws Exception {
		return  calendarToGMTStatic(date);
	}

	public Calendar calendarFromGMT(Calendar cal, String offset) {
		return  calendarFromGMTStatic(cal, offset);
	}

	/**
	 * create GMT Calendar
	 * 
	 * @param date
	 * @return GMT Calendar
	 * @throws Exception
	 */
	public static Calendar calendarToGMTStatic(Calendar date) throws Exception {
		if (date == null) {
			return null;
		}
		TimeZone tz = TimeZone.getDefault();
		Date ret = new Date(date.getTimeInMillis() - tz.getRawOffset());
		// if we are now in DST, back off by the delta. Note that we are
		// checking the GMT date, this is the KEY.
		if (tz.inDaylightTime(ret)) {
			Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
			// check to make sure we have not crossed back into standard time
			// this happens when we are on the cusp of DST (7pm the day before
			// the change for PDT)
			if (tz.inDaylightTime(dstDate)) {
				ret = dstDate;
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ret);
		return calendar;
	}

	public static Calendar calendarFromGMTStatic(Calendar cal, String offset) {
		Calendar calendar = Calendar.getInstance();
		/*
		 * yyyy-MMM-dd HH:mm:ss
		 */
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT" + offset));
		try {
			calendar.setTime(dateFormatGmt.parse(dateFormatGmt.format(cal.getTime())));
		} catch (ParseException e) {
			calendar = null;
			e.printStackTrace(System.out);
		}
		return calendar;

	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Map<String, Object> getMapResults() {
		return mapResults;
	}

	public void setMapResults(Map<String, Object> mapResults) {
		this.mapResults = mapResults;
	}

	private String controllerException[] = null;

	protected void catchControllerException(Exception e) {
		if (this.getControllerException() != null) {
			this.addToMapResults(PropertiesHolder.jsonkeyerror, "validators.error.small.concern");
			this.addToMapResults(PropertiesHolder.jsonkeyerror, "validators.error.something.wrong");
			for (String s : this.getControllerException())
				this.addToMapResults(PropertiesHolder.jsonkeyerror, s);
			return;
		}
		e.printStackTrace(System.out);
	}

	public String[] getControllerException() {
		return controllerException;
	}

	public void setControllerException(String controllerException[]) throws Exception {
		this.controllerException = controllerException;
		throw new Exception();
	}
}
