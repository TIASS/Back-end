package com.tiass.models.utils.jaxbadapters;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter; 
public class JAXBDateAdapter extends XmlAdapter<Long, Date> {
    @Override
    public Long marshal(Date date) throws Exception {
    	 
        return date.getTime();
    }

    @Override
    public Date unmarshal(Long date) throws Exception {
    	 
        return  new Date(date);
    }
}