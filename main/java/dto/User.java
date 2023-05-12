package dto;

public class User {
	private String name;
	private String userId;
	private long mobileNumber;

	public User(String name, long mobileNumber) {
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.userId = name.substring(0,4).toUpperCase()+ String.valueOf(mobileNumber).substring(0, 5);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	

}
