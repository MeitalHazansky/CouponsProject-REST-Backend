package com.coupons.business_delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Scanner;

import com.MainCoupons.UserType;
import com.coupons.rest_exceptions.BusinessDelegateException;


public enum BusinessDelegate {

	BusinessDelegate;

	private BusinessDelegate() {
	}

	public synchronized String storeIncome(String name, String description, double amount, UserType type) throws BusinessDelegateException  {
		HttpURLConnection con = null;
		try {
			URL url = new URL("http://localhost:8888/IncomeService/income");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			String json = "{\"id\":\"0\"," 
			             + "\"name\":\"" + name + "\"," 
					     + "\"date\":\"" + new Date(System.currentTimeMillis()) + "\"," 
			             + "\"description\":\"" + description + "\","
					     + "\"amount\":\"" + amount + "\","
					     + "\"userType\":\"" + type + "\"}";
			byte[] out = json.getBytes(StandardCharsets.UTF_8);
			int length = out.length;

			con.setFixedLengthStreamingMode(length);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.connect();
			OutputStream os = con.getOutputStream();
			os.write(out);
			os.close();
			
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			Scanner s = new Scanner(in);
			s.useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			s.close();
			return result;
		} catch (IOException e) {
			throw new BusinessDelegateException();
		}
	}
	
	public synchronized String viewAllIncome() throws BusinessDelegateException {
		URL url;
		try {
		url = new URL("http://localhost:8888/IncomeService/income");		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
	    InputStream in = con.getInputStream();
	    String encoding = con.getContentEncoding();
	    encoding = encoding == null ? "UTF-8" : encoding;
	    Scanner s = new Scanner(in);
	    s.useDelimiter("\\A");
	    String result = s.hasNext() ? s.next() : "";
	    s.close();
	    return result;
		} catch (IOException e) {
			throw new BusinessDelegateException();
		} 
	}
	public synchronized String viewIncomeByCompany(String name) throws BusinessDelegateException {
		URL url;
		try {
		url = new URL("http://localhost:8888/IncomeService/income/company?name=" + name);		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
	    InputStream in = con.getInputStream();
	    String encoding = con.getContentEncoding();
	    encoding = encoding == null ? "UTF-8" : encoding;
	    Scanner s = new Scanner(in);
	    s.useDelimiter("\\A");
	    String result = s.hasNext() ? s.next() : "";
	    s.close();
	    return result;
		} catch (IOException e) {
			throw new BusinessDelegateException();
		} 
	}
	public synchronized String viewIncomeByCustomer(String name) throws BusinessDelegateException {
		URL url;
		try {
		url = new URL("http://localhost:8888/IncomeService/income/customer?name=" + name);		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
	    InputStream in = con.getInputStream();
	    String encoding = con.getContentEncoding();
	    encoding = encoding == null ? "UTF-8" : encoding;
	    Scanner s = new Scanner(in);
	    s.useDelimiter("\\A");
	    String result = s.hasNext() ? s.next() : "";
	    s.close();
	    return result;
		} catch (IOException e) {
			throw new BusinessDelegateException();
		} 
	}
	

}
