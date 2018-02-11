package com.tiass.services.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class TiassResourceBundle {
 
	private ResourceBundle		RESOURCE_BUNDLE	= null;
	private String				resource;
	private Locale				local;

	public TiassResourceBundle(String _package, String resource, Locale local) {
		this.setLocal(local);
		this.setResource(resource);
		this.setResourceBundle(_package, resource, local);
	}

	public void setResourceBundle(String _package, String resource, Locale local) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(_package + "." + resource, local);
	}

	public String getString(String key) {
		try {
			if (StringUtils.contains(key, ':')) {
				String[] k = StringUtils.split(key, ':');
				List<String> p = new ArrayList<String>();
				for (String s : k) {
					if (k[0].equals(s)) {
						continue;
					}
					p.add(s);
				}
				return this.getString(k[0], p);
			} else {
				return RESOURCE_BUNDLE.getString(key);
			}

		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private String getString(String key, Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Locale getLocal() {
		return local;
	}

	public void setLocal(Locale local) {
		this.local = local;
	}
}