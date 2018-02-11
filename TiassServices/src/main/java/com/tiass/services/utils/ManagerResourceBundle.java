package com.tiass.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.commons.lang3.StringUtils;

public class ManagerResourceBundle {
	/**
	 * for fetching bundled resources in echo.properties file
	 */
	private final static Map<String, String>	prefixes	= new HashMap<String, String>();
	 
	 
	private Map<String, TiassResourceBundle>	mapBundles	= new HashMap<String, TiassResourceBundle>();

	private Locale								locale;

	public ManagerResourceBundle(Locale locale) {
		this.setLocale(locale);
	}

	public List<String> translateList(List<String>labels) {
		List<String> translates = new ArrayList<String>();
		for (String label : labels ) {
			translates.add(this.translateLabel(  label));
		}
		return translates;
	}

	public String translateLabel(  String label) {
		if (StringUtils.isBlank(StringUtils.trimToEmpty(label))) {
			return "";
		}
		String translate = label;
		TiassResourceBundle rb = this.getResourceBundled(  label);
		if (rb != null) {
			try {
				translate = rb.getString(label);
			} catch (MissingResourceException e) {
				translate = '!' + label + '!';
			}
		}
		return translate;
	}

	private TiassResourceBundle getResourceBundled(  String label) {
		TiassResourceBundle rb = null;
		 
		String resource = null;
		String pref = null;
		for (String prefix : prefixes.keySet()) {
			if (label.startsWith(prefix)) {
				pref = prefix;
				rb = this.mapBundles.get(pref); 
				resource = StringUtils.remove(prefix, ".");
				// resource = prefix.replace('.', ' ').trim();
				break;
			}
		} 
		if (resource == null || rb != null) {
			return rb;
		}
		try {
			rb = new TiassResourceBundle(prefixes.get(pref), resource, this.getLocale());
		} catch (NullPointerException ne) {
			ne.printStackTrace(System.out);
		} catch (MissingResourceException mre) {
			mre.printStackTrace(System.out);
		} finally {
			if (rb == null) {
				try {
					rb = new TiassResourceBundle(prefixes.get(pref), resource, null);
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
			}
		}
		if (rb != null) {
			mapBundles.put(pref, rb);
		} 
		return rb;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public static void addOwnResource( ) {
		ManagerResourceBundle.addResource("wp.impl.services.controllers.msgs", "validator.");

	}
	public static void addResource(String _package, String prefix) {
		if (!ManagerResourceBundle.prefixes.containsKey(prefix)) {
			ManagerResourceBundle.prefixes.put(prefix, _package);
		}
	}
}
