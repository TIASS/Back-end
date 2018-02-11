package com.tiass.services;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.tiass.models.entities.UserCreds;
import com.tiass.services.utils.SearchData;
public class ManagerEUserCredsImpl<C extends UserCreds> implements ManagerE<C> {
	private Class<C> clazz;

    @SuppressWarnings("unchecked")
	public ManagerEUserCredsImpl() { 
    	   this.setClazz((Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()) .getActualTypeArguments()[0]);
     } 
	@Override
	public <S extends SearchData> List<C> searchList(S data) {
		// TODO Auto-generated method stub
		return null;
	}

 
	public Class<C> getClazz() {
		return clazz;
	}
	public void setClazz(Class<C> clazz) {
		this.clazz = clazz;
	}


	public class SearchDataUserCreds extends SearchData {

	}

}
