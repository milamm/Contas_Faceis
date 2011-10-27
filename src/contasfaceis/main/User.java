package contasfaceis.main;

public class User {
	
	private String firstName;
	private String lastName;
	private String email;
	private String FBaccessToken;
	
/*	User(int id, int FBid, String firstName, String lastName, String email) {
		this.id = id;
		this.FBid = FBid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}*/
	
	public User(String accessToken) {
		FBaccessToken = accessToken;
	}
	
	public void setParameters(String fname, String lname, String email) {
		firstName = fname;
		lastName = lname;
		this.email = email;
	}
	
	public void setFirstName(String fname) {
		firstName = fname;
	}
	
	public void setLastName(String lname) {
		lastName = lname;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFBaccessToken() {
		return FBaccessToken;
	}
}