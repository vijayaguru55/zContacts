package dto;

public class Credentials {
	private String userId;
	private String password;
	private String username;
	public Credentials(String password,String userId,String username) {
		this.password = password;
		this.userId = userId;
		this.username =username;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
