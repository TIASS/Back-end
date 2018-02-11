package com.tiass.models.utils.jaxbadapters;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import com.tiass.models.utils.MiscManager;

public class JAXBObjectIdListAdapter extends XmlAdapter<List<String>, List<ObjectId>> {
    @Override
    public List<String> marshal(List<ObjectId> id) throws Exception {
    	List<String> list = new ArrayList<String>();
    	id.forEach(o -> { if(o!=null) list.add( MiscManager.serializeObjectId(o));});
        return list;
    }

    @Override
    public List<ObjectId> unmarshal(List<String> id) throws Exception {
    	List<ObjectId> list = new ArrayList<ObjectId>();
    	
    	id.forEach(o -> { if(StringUtils.isNotBlank( o) ) list.add( MiscManager.deserializeObjectId(o));});

        return   list;
    }
}