package com.tiass.services.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConcernsData extends MobileData {

	private String concernsId;
	@XmlElement
	public String getConcernsId() {
		return concernsId;
	}

	public void setConcernsId(String concernsId) {
		this.concernsId = concernsId;
	}
}
