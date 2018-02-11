package com.tiass.services.data;

public class SignInData extends LoginData {

	private String name;
	private String middlename;
	private String pswrdconf;
	private int gender = -1;
	private Boolean business;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	 
	public String getPswrdconf() {
		return pswrdconf;
	}
	public void setPswrdconf(String pswrdconf) {
		this.pswrdconf = pswrdconf;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public Boolean getBusiness() {
		return business;
	}
	public void setBusiness(Boolean business) {
		this.business = business;
	}

}
