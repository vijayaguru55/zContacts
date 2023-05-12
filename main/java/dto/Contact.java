package dto;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.Base64;

import com.mysql.cj.jdbc.Blob;

public class Contact {
	private String name;
	private long mobileNumber;
	private String contactID;
	private boolean isFavourite;
	private String image;
	public Contact(String name, long mobileNumber,String contactID) {
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.contactID = contactID;
		this.image="";
	}
//	
	public String getImage() {
		return this.image;
	}
	public void setimage(String image2) {
		this.image=image2;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getcontactID() {
		return this.contactID;
	}

	public void setcontactID(String contactID) {
		this.contactID = contactID;
	}

	public void setIsFavourite(boolean value){
		this.isFavourite = value;
	}

	public boolean getIsFavourite(){
		return this.isFavourite;
	}
	
}
