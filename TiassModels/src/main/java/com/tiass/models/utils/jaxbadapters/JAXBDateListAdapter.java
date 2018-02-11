package com.tiass.models.utils.jaxbadapters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter; 
public class JAXBDateListAdapter extends XmlAdapter<List<Long>, List<Date>> {
    @Override
    public List<Long> marshal(List<Date> dateList) throws Exception {
    	
    	List<Long> longs = new ArrayList<Long>();
    	dateList.forEach(d -> { if(d!=null) longs.add(d.getTime()); });
        return longs;
    }

    @Override
    public List<Date> unmarshal(List<Long> longList) throws Exception {
    	List<Date> dates = new ArrayList<Date>();
    	longList.forEach(f -> { if(f!=null) dates.add(new Date(f)); });

        return dates;
    }
}