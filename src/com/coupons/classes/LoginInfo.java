package com.coupons.classes;

import com.MainCoupons.UserType;

public class LoginInfo {

	private String userName;
	private String password;
	private UserType userType;
	
	
	
	public LoginInfo(String userName, String password, UserType userType) {
		this.userName = userName;
		this.password = password;
		this.userType = userType;
	}


	public LoginInfo() {
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public UserType getUserType() {
		return userType;
	}


	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	
	
}
