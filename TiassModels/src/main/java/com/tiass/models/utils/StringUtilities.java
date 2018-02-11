package com.tiass.models.utils;

 
import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * customs utilities that deal with String chains
 * 
 * @author Jimmy Bahole
 *
 */
public class StringUtilities {

	public static boolean passwordValidation(String password) {
		boolean isValid = false;

		// (?=.*[0-9]) a digit must occur at least once - OK
		// (?=.*[a-z]) a lower case letter must occur at least once
		// (?=.*[A-Z]) an upper case letter must occur at least once
		// (?=.*[@#*=]) a special character must occur at least once
		// (?=[\\S]+$) no whitespace allowed in the entire string- OK
		// .{6,20} at least 6 to 20 characters- OK
		/*
		 * ^ # start-of-string (?=.*[0-9]) # a digit must occur at least once
		 * (?=.*[a-z]) # a lower case letter must occur at least once
		 * (?=.*[A-Z]) # an upper case letter must occur at least once
		 * (?=.*[@#$%^&+=]) # a special character must occur at least once
		 * (?=\S+$) # no whitespace allowed in the entire string .{8,} #
		 * anything, at least eight places though $ # end-of-string
		 */
		// String passwordRegexOLD = "((?=.*[0-9])(?=[\\S]+$).{6,20})";
		// String passwordRegex =
		// "(^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=[\\S]+$).{6,20})";
		/**
		 * validate : # start-of-string # a digit must occur at least once # no
		 * whitespace allowed in the entire string
		 */
		if (password == null) {
			return isValid;
		}
		String passwordRegex = "(^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=[\\S]+$).{6,20})";
		isValid = passwordV(password.toLowerCase(), passwordRegex);
		if (!isValid) {
			/**
			 * validate : # start-of-string # a digit must occur at least once #
			 * a special character must occur at least once # no whitespace
			 * allowed in the entire string
			 */
			passwordRegex = "(^(?=.*[0-9])(?=.*[a-z])(?=[\\S]+$).{6,20})";
			isValid = passwordV(password.toLowerCase(), passwordRegex);
		}
		isValid = password.matches(passwordRegex);
		return isValid;
	}

	private static boolean passwordV(String password, String passwordRegex) {
		boolean isValid = false;
		if (password == null) {
			return isValid;
		}
		isValid = password.matches(passwordRegex);
		return isValid;
	}

	public static boolean quizValidation(String quiz) {
		boolean isValid = false;
		if (quiz == null) {
			return isValid;
		}
		String quizClean = cleanString(quiz, true);

		if (quiz != null && quiz.trim().length() >= 3
				&& StringUtilities.createArrayFromString(quiz, ' ', true, 1).size() >= 3
				&& quiz.trim().length() == quizClean.trim().length()) {
			isValid = true;

		}
		return isValid;
	}

	public static boolean answerValidation(String answer) {
		boolean isValid = false;
		if (answer == null) {
			return isValid;
		}
		String answerClean = cleanString(answer, true);
		if (answer.trim().length() >= 5 && answer.trim().length() == answerClean.trim().length()
				&& !StringUtils.contains(answerClean, " ") && !StringUtils.contains(answer, " ")) {

			isValid = true;

		}
		return isValid;
	}

	public static boolean nameValidation(String name) {
		boolean isValid = false;

		// ^ # Start of the line
		// [a-z0-9_-] # Match characters and symbols in the list, a-z, 0-9,
		// underscore, hyphen
		// {6,15} # Length at least 6 characters and maximum length of 15
		// $ # End of the line
		/*
		 * String strWithNumber = "This string has a 1 number"; String
		 * strWithoutNumber = "This string has a number";
		 * 
		 * System.out.println(strWithNumber
		 * +" : "+strWithNumber.matches(".*\\d.*"));
		 * System.out.println(strWithoutNumber
		 * +" : "+strWithoutNumber.matches(".*\\d.*"));
		 */
		if (name == null) {
			return isValid;
		}
		name = name.toLowerCase();
		String name2 = name.replaceAll("[^a-z ]", "");

		if (name != null) {
			isValid = name.trim().length() > 3 && (name.length() == name2.length()) && (name.equals(name2));
		}
		return isValid;
	}

	public static boolean userNameValidation(String userName) {
		boolean isValid = false;

		// ^ # Start of the line
		// [a-z0-9_-] # Match characters and symbols in the list, a-z, 0-9,
		// underscore, hyphen
		// {6,15} # Length at least 6 characters and maximum length of 15
		// $ # End of the line
		if (userName == null) {
			return isValid;
		}
		String userNameRegex = "^[a-z0-9_-]{5,15}$";
		isValid = userName.toLowerCase().matches(userNameRegex);
		return isValid;
	}

	/**
	 * validates for email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean emailValidation(String email) {
		boolean isValid = false;

		// ^ #start of the line
		// [_A-Za-z0-9-]+ # must start with string in the bracket [ ], must
		// contains one or more (+)
		// ( # start of group #1
		// \\.[_A-Za-z0-9-]+ # follow by a dot "." and string in the bracket [
		// ], must contains one or more (+)
		// )* # end of group #1, this group is optional (*)
		// @ # must contains a "@" symbol
		// [A-Za-z0-9]+ # follow by string in the bracket [ ], must contains one
		// or more (+)
		// ( # start of group #2 - first level TLD checking
		// \\.[A-Za-z0-9]+ # follow by a dot "." and string in the bracket [ ],
		// must contains one or more (+)
		// )* # end of group #2, this group is optional (*)
		// ( # start of group #3 - second level TLD checking
		// \\.[A-Za-z]{2,} # follow by a dot "." and string in the bracket [ ],
		// with minimum length of 2
		// ) # end of group #3
		// $ #end of the line
		/*
		 * Pattern rfc2822 =
		 * Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[
		 * a-z0-9!#$%&'*+/=?^
		 * _`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a
		 * -z0-9](?:[a-z0-9-]*[a-z0-9])?$");
		 * if(rfc2822.matcher(email).matches()) { // Well formed email }
		 */

		String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (email != null) {
			isValid = email.matches(emailRegex);
		}
		return isValid;
	}

	public static String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			if (password != null && password.length() > 0) {
				md5.update(password.getBytes());
				BigInteger hash = new BigInteger(1, md5.digest());
				hashword = hash.toString(16);
			}

		} catch (NoSuchAlgorithmException nsae) {
			hashword = null;
			nsae.printStackTrace(System.out);
		}
		return hashword;
	}

	public static String generateUniqFolderName(File folder, String prefix) throws Exception {
		String UF = generateUniqFileName(folder) + "_" + prefix;
		return UF;
	}

	public static String generateUniqFileImageName(File folder, String fileType) throws Exception {
		String UF = "";
		String ext = getFileExtension(fileType);
		if (ext.trim().length() == 0) {
			UF = null;
			return UF;
		}
		if (!folder.isDirectory()) {
			folder.mkdir();
		}

		UF = StringUtilities.generateUniqFileName(folder);
		if (UF != null && UF.length() > 0) {
			UF += ext;
		}
		return UF;
	}

	public static String getFileExtension(String fileType) throws Exception {
		String ext = "";
		if ("image/gif".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".gif";
		} else if ("image/jpeg".equalsIgnoreCase(fileType.toString().trim())
				|| "image/jpg".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".jpeg";
		} else if ("image/png".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".png";
		} else if ("video/mpeg".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".mpeg";
		} else if ("video/mpeg4".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".mpeg4";
		} else if ("video/mp4".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".mp4";
		} else if ("video/webm".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".webm";
		} else if ("video/ogg".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".OGV";
		} else if ("application/x-shockwave-flash".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".SWF";
		} else if ("video/quicktime".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".quicktime";
		} else if ("video/x-ms-wmv".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".x-ms-wmv";
		} else if ("video/flv".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".flv";
		} else if ("video/x-flv".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".x-flv";
		} else if ("video/avi".equalsIgnoreCase(fileType.toString().trim())) {
			ext += ".avi";
		}

		return ext;
	}

	public static String getFileExtension2(String file) throws Exception {
		String ext = "";
		if (file == null) {
			return ext;
		}
		if (file.toLowerCase().trim().endsWith(".gif")) {
			ext += ".gif";
		} else if (file.toLowerCase().trim().endsWith(".jpeg") || file.toLowerCase().trim().endsWith(".jpg")) {
			ext += ".jpeg";
		} else if (file.toLowerCase().trim().endsWith(".png")) {
			ext += ".png";
		}
		return ext;
	}

	private static String generateUniqFileName(File folder) throws Exception {
		String UF = null;
		if (folder.isDirectory()) {
			String[] fileNames = folder.list();
			boolean continuer = true;
			while (continuer) {
				// UUID uniqueKey = UUID.randomUUID(4);
				UF = Calendar.getInstance().getTimeInMillis() + RandomStringUtils.randomAlphanumeric(5);
				continuer = false;
				for (String file : fileNames) {
					if (file.contains(".")) {
						file = file.substring(0, file.lastIndexOf('.'));
					}
					if (UF.equalsIgnoreCase(file)) {
						continuer = true;
						break;
					}
				}
			}
		}

		return UF;
	}

	public static String generateUniqDevisCode(Calendar calendar) throws Exception {

		// note a single Random object is reused here
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		if (year.trim().length() == 4) {
			year = year.substring(2);
		}
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.trim().length() == 1) {
			month = "0" + month;
		}
		String devisCode = RandomStringUtils.randomAlphanumeric(5) + month + year;
		return devisCode;
	}

	public static String generateUniqBusinessDevisCode(Calendar calendar) throws Exception {
		// note a single Random object is reused here
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		if (year.trim().length() == 4) {
			year = year.substring(2);
		}
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.trim().length() == 1) {
			month = "0" + month;
		}
		String devisBusinessCode = RandomStringUtils.randomAlphanumeric(9) + month + year;

		return devisBusinessCode;
	}

	public static String generateUniqTicket(Calendar calendar) throws Exception {

		// note a single Random object is reused here
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		if (year.trim().length() == 4) {
			year = year.substring(2);
		}
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.trim().length() == 1) {
			month = "0" + month;
		}
		String ticket = RandomStringUtils.randomAlphanumeric(4) + month + year;

		return ticket;
	}

	public static String generateFormToken() {
		return RandomStringUtils.randomAlphanumeric(9);
	}

	public static String generateFormToken(int l) {
		return RandomStringUtils.randomAlphanumeric(l);
	}

	public static String generateFormTokenAlphabetic(int l) {
		return RandomStringUtils.randomAlphabetic(l);
	}

	public static boolean URLValidation(String url) {
		boolean isValid = false;
		// String regex =
		// "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		// String regex =
		// "<\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
		// // matches <http://google.com>

		// String regex =
		// "<^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
		// // does not match <http://google.com>z

		/*
		 * try { String url2 = url; if(url2 !=null ){ url2 = url2.toLowerCase();
		 * url2 = url2.replaceFirst("www.", "");
		 * 
		 * if(!url2.startsWith("http")){ url2 = "http://"+url2; } String regex1
		 * =
		 * "<\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>"
		 * ; // matches <http://google.com>
		 * 
		 * String regex2 =
		 * "<^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
		 * // does not match <http://google.com>
		 * 
		 * isValid = url2.matches(regex1) || url2.matches(regex2); } }catch (
		 * Exception e) { isValid = false; }
		 */
		/*
		 * String regex1 =
		 * "<\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
		 * // matches <http://google.com>
		 * 
		 * String regex2 =
		 * "<^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
		 * // does not match <http://google.com> isValid = url.matches(regex1)
		 * || url .matches(regex2);
		 */
		try {
			String url2 = url;
			if (url2 != null) {
				if (url2.trim().length() > 80) {
					return isValid;
				}
				url2 = url2.toLowerCase();
				url2 = url2.replaceFirst("www.", "");

				if (!url2.startsWith("http")) {
					url2 = "http://" + url2;
				}
			}
			new URL(url2);
			isValid = true;
		} catch (MalformedURLException e) {
			isValid = false;
		}

		return isValid;
	}

	public static String cleanString(String st, boolean withspace) {

		if (st == null) {
			return st;
		}

		if (withspace) {
			st = cleanString(st);
		} else {
			st = st.replaceAll(".", "");
			st = st.replaceAll(",", "");
			st = st.replaceAll(";", "");
			st = st.replaceAll("*", "");
			st = st.replaceAll("#", "");
			st = st.replaceAll("@", "");
			st = st.replaceAll("&", "");
			st = st.replaceAll("^", "");
			st = st.replaceAll("!", "");
			st = st.replaceAll("%", "");
			st = st.replaceAll("/", "");
			st = st.replaceAll("|", "");
			st = st.replaceAll("\\", "");
			st = st.replaceAll("<", "");
			st = st.replaceAll(">", "");
			st = st.replaceAll("(", "");
			st = st.replaceAll(")", "");
			st = st.replaceAll("{", "");
			st = st.replaceAll("}", "");
			st = st.replaceAll(":", "");
			st = st.replaceAll("'", "");
			st = st.replaceAll("\"", "");
			st = st.replaceAll("=", "");
		}

		return st;
	}

	public static String cleanString(String st) {
		if (st == null) {
			return st;
		}
		st = st.replace('.', ' ');
		st = st.replace(',', ' ');
		st = st.replace(';', ' ');
		st = st.replace('*', ' ');
		st = st.replace('#', ' ');
		st = st.replace('@', ' ');
		st = st.replace('&', ' ');
		st = st.replace('^', ' ');
		st = st.replace('!', ' ');
		st = st.replace('%', ' ');
		st = st.replace('/', ' ');
		st = st.replace('|', ' ');
		st = st.replace('\\', ' ');
		st = st.replace('<', ' ');
		st = st.replace('>', ' ');
		st = st.replace('(', ' ');
		st = st.replace(')', ' ');
		st = st.replace('{', ' ');
		st = st.replace('}', ' ');
		st = st.replace(':', ' ');
		st = st.replace('\'', ' ');
		st = st.replace('"', ' ');
		st = st.replace('=', ' ');
		return st;
	}

	/**
	 * cleans the chain for characters who does not belong to email
	 * 
	 * @param st
	 *            the chain to clean
	 * @return
	 * @throws Exception
	 */
	public static String cleanStringEmail(String st) throws Exception {
		if (st == null) {
			return st;
		}
		st = st.replace(',', ' ');
		st = st.replace(';', ' ');
		st = st.replace('*', ' ');
		st = st.replace('#', ' ');
		st = st.replace('&', ' ');
		st = st.replace('^', ' ');
		st = st.replace('!', ' ');
		st = st.replace('%', ' ');
		st = st.replace('/', ' ');
		st = st.replace('|', ' ');
		st = st.replace('\\', ' ');
		st = st.replace('<', ' ');
		st = st.replace('>', ' ');
		st = st.replace('(', ' ');
		st = st.replace(')', ' ');
		st = st.replace('{', ' ');
		st = st.replace('}', ' ');
		st = st.replace(':', ' ');
		st = st.replace('\'', ' ');
		st = st.replace('"', ' ');
		st = st.replace('=', ' ');
		return st;
	}

	public static String cleanString2(String st) throws Exception {
		if (st == null) {
			return st;
		}
		st = st.replace('\'', ' ');
		return st;
	}

	public static String checkBadWords(List<String> bwl, String text) {
		String contentBadWord = null;
		if (text != null) {
			for (String bw : bwl) {
				if (text.toLowerCase().contains(bw.toLowerCase()) || text.toUpperCase().contains(bw.toUpperCase())) {
					contentBadWord = bw;
				}
			}
		}
		return contentBadWord;
	}

	public static String getBadWord(List<String> bwl, String text) {
		String contentBadWord = "";
		if (text != null) {
			for (String bw : bwl) {
				if (text.toLowerCase().contains(bw.toLowerCase()) || text.toUpperCase().contains(bw.toUpperCase())) {
					contentBadWord = bw;
				}
			}
		}
		return contentBadWord;

	}

	public static List<String> createArrayFromString(String string, char delimiter, boolean trim, int minlength) {
		List<String> array = new ArrayList<String>();
		try {
			if (string != null && !"".equals(string.trim())) {
				String ar[] = string.split(String.valueOf(delimiter));
				for (String s : ar) {
					if (trim)
						s = s.trim();
					if (s.length() >= (1 + minlength))
						array.add(s);
				}
			}
		} catch (Exception e) {

		}

		return array;
	}

	public static String trimString(String s) {

		if (s != null) {
			s = StringUtils.trim(s);
		}
		return s;
	}

	public static String trimStringInner(String s) {

		if (s != null) {
			s = s.trim().replaceAll("\\s+", "");
		}
		return s;
	}

	public static String capitalizeParagraphe(String text) {

		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(text);
		while (pos < sb.length()) {
			if (sb.charAt(pos) == '.') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
				sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
				capitalize = false;
			}
			pos++;
		}

		return text;

	}

	public static boolean isBlank(String s, char[] ignored) {
		if (s == null) {
			return true;
		}
		if (ignored != null) {
			for (char c : ignored) {
				if (s.contains(String.valueOf(c))) {
					s = s.replace(c, ' ');
				}
			}
		}
		return (s == null || "null".equals(s.trim().toLowerCase()) || s.trim().length() == 0);
	}

	public static String cleanStringForSave1(String value) {

		/*
		 * if(value!=null && "".equalsIgnoreCase(value.toLowerCase().trim())){
		 * return ""; }if (value != null && value.trim().length() > 2) { try {
		 * value = replaceAll("<script type=\"text/javascript\">", "", value,
		 * true); } catch (Exception e) { } try { value =
		 * replaceAll("<script type='text/javascript'>", "", value, true); }
		 * catch (Exception e) { } try { value = replaceAll("text/javascript",
		 * "t.e.x.t. / j.a.v.a.s.c.r.i.p.t.", value, true); } catch (Exception
		 * e) { } try { value = replaceAll("j.a.v.a.s.c.r.i.p.t.", "", value,
		 * true); } catch (Exception e) { } try { value = replaceAll("<script",
		 * "", value, true); } catch (Exception e) { } try { value =
		 * replaceAll("<script>", "", value, true); } catch (Exception e) { }
		 * try { value = replaceAll("</script>", "", value, true); } catch
		 * (Exception e) { } try { value = replaceAll("bizzPark", "bizz Park",
		 * value, true); } catch (Exception e) { } try { value =
		 * replaceAll("Bizz Park", "bizz Park", value, true); } catch (Exception
		 * e) { } try { value = replaceAll("<code>", "", value, true); } catch
		 * (Exception e) { } try { value = replaceAll("<code", "", value, true);
		 * } catch (Exception e) { } try { value = replaceAll("</code>", "",
		 * value, true); } catch (Exception e) { } try { value =
		 * replaceAll("<style>", "", value, true); } catch (Exception e) { } try
		 * { value = replaceAll("<style", "", value, true); } catch (Exception
		 * e) { } try { value = replaceAll("</style>", "", value, true); } catch
		 * (Exception e) { } try { value = replaceAll("<?php", "", value, true);
		 * } catch (Exception e) { } try { value = replaceAll("<%", "", value,
		 * true); } catch (Exception e) { } try { value = replaceAll("%>", "",
		 * value, true); } catch (Exception e) { } try { value =
		 * replaceAll("<?", "", value, true); } catch (Exception e) { } try {
		 * value = replaceAll("?>", "", value, true); } catch (Exception e) { }
		 * try { value = replaceAll("</", "", value, true); } catch (Exception
		 * e) { } }
		 */

		return value;
	}

	public static boolean hasDangerousScript(String value) {
		if (value != null && value.trim().length() > 2) {
			try {

				if (value.toLowerCase().contains("<script")) {
					return true;
				}
				if (value.toLowerCase().contains("<script>")) {
					return true;
				}
				if (value.toLowerCase().contains("</script>")) {
					return true;
				}
				if (value.toLowerCase().contains("<code")) {
					return true;
				}
				if (value.toLowerCase().contains("<code>")) {
					return true;
				}
				if (value.toLowerCase().contains("</code>")) {
					return true;
				}
				if (value.toLowerCase().contains("<style")) {
					return true;
				}
				if (value.toLowerCase().contains("<style>")) {
					return true;
				}
				if (value.toLowerCase().contains("</style>")) {
					return true;
				}
				if (value.toLowerCase().contains("<?php")) {
					return true;
				}
				if (value.toLowerCase().contains("<?")) {
					return true;
				}
				if (value.toLowerCase().contains("?>")) {
					return true;
				}
				if (value.toLowerCase().contains("<%")) {
					return true;
				}
				if (value.toLowerCase().contains("%>")) {
					return true;
				}
				if (value.toLowerCase().contains("</")) {
					return true;
				}
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**
	 * Filter the specified message string for characters that are sensitive in
	 * HTML. This avoids potential attacks caused by including JavaScript codes
	 * in the request URL that is often reported in error messages.
	 * 
	 * @param message
	 *            The message string to be filtered
	 */
	public static String filterin(String message) {
		if (message == null) {
			return message;
		}

		String br = "<br />";
		String inline = "(\r\n|\n)";
		message = message.replaceAll(br, "\n");

		/*
		 * char content[] = new char[message.length()]; message.getChars(0,
		 * message.length(), content, 0); StringBuilder result = new
		 * StringBuilder(content.length + 50); for (int i = 0; i <
		 * content.length; i++) { switch (content[i]) {
		 * 
		 * case '<': result.append("&lt;"); break; case '>':
		 * result.append("&gt;"); break; case '"': result.append("&quot;");
		 * break;
		 * 
		 * default: result.append(content[i]); /* case '\n':
		 * result.append("<br/>"); break; case '&': result.append("&amp;");
		 * break;
		 * 
		 * 
		 * } }
		 */
		message = StringEscapeUtils.escapeHtml4(message);

		// message = String.valueOf(result);
		message = message.replaceAll(inline, br);
		try {
			while (message.contains(br + br + br)) {
				message = message.replaceAll(br + br + br, br + br);
			}
			message = StringUtils.trim(message);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return message;

	}

	public static String filterout(String message) {

		if (message == null) {
			message = "";
			return message;
		}
		try {
			message = StringUtils.replace(message, "<br />", "\n");
			/*
			 * message = StringUtils.replace(message, "&lt;", "<"); message =
			 * StringUtils.replace(message, "&gt;", ">");
			 */
		} catch (Exception e) {
		}
		message = StringEscapeUtils.unescapeHtml4(message);
		return message;

	}

	/**
	 * encode a key to Base64 (URL Safe String)
	 * 
	 * @param key
	 * @return
	 */
	public static String convertKeyOut(String key) {
		if (key != null) {
			try {
				return Base64.encodeBase64URLSafeString(key.getBytes());
			} catch (java.lang.Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * decode a key from Base64 The key must have been encoded with URL Safe
	 * String for the match
	 * 
	 * @param key
	 * @return
	 */
	public static String convertKeyIn(String key) {
		String code = null;
		try {
			if (key != null && key.trim().length() > 0) {
				code = new String(Base64.decodeBase64(key));
			}
		} catch (java.lang.NumberFormatException nmfe) {
			code = null;
		} catch (java.lang.Exception e) {
			e.printStackTrace(System.out);
			code = null;
		}
		return code;
	}

	public static String replaceAll(String findtxt, String replacetxt, String str, boolean isCaseInsensitive) {
		if (str == null) {
			return null;
		}
		if (findtxt == null || findtxt.length() == 0) {
			return str;
		}
		if (findtxt.length() > str.length()) {
			return str;
		}
		int counter = 0;
		String thesubstr = "";
		while ((counter < str.length()) && (str.substring(counter).length() >= findtxt.length())) {
			thesubstr = str.substring(counter, counter + findtxt.length());
			if (isCaseInsensitive) {
				if (thesubstr.equalsIgnoreCase(findtxt)) {
					str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
					// Failing to increment counter by replacetxt.length()
					// leaves you open
					// to an infinite-replacement loop scenario: Go to replace
					// "a" with "aa" but
					// increment counter by only 1 and you'll be replacing 'a's
					// forever.
					counter += replacetxt.length();
				} else {
					counter++; // No match so move on to the next character from
								// which to check for a findtxt string match.
				}
			} else {
				if (thesubstr.equals(findtxt)) {
					str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
					counter += replacetxt.length();
				} else {
					counter++;
				}
			}
		}
		return str;
	}
}
