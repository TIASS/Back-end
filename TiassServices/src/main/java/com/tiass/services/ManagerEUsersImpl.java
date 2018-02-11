package com.tiass.services;

import java.util.List;

import com.tiass.models.entities.Users;
import com.tiass.services.utils.SearchData;

public class ManagerEUsersImpl implements ManagerE<Users> {
	 
	@Override
	public <S extends SearchData> List<Users> searchList(S data) {
		// TODO Auto-generated method stub
		return null;
	} 

	public class SearchDataUsers extends SearchData {

	}

}
 