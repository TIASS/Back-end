package com.tiass.models.utils.jaxbadapters;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.bson.types.ObjectId;

import com.tiass.models.utils.MiscManager; 
public class JAXBObjectIdAdapter extends XmlAdapter<String, ObjectId> {
    @Override
    public String marshal(ObjectId id) throws Exception {
		System.out.println("marshal ObjectId id : "+MiscManager.serializeObjectId(id));
        return MiscManager.serializeObjectId(id);
    }

    @Override
    public ObjectId unmarshal(String id) throws Exception {
		System.out.println("un-marshal ObjectId id : "+MiscManager.deserializeObjectId(id));
        return   MiscManager.deserializeObjectId(id);
    }
}