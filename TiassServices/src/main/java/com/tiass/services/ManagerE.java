package com.tiass.services;

import java.util.List;

import com.tiass.models.MongoDoc;
import com.tiass.services.utils.SearchData; 
  
public interface ManagerE<T extends MongoDoc  >{
	  public <S extends SearchData >List<T>  searchList(S data) ; 
	 
	  
}
 