package contasfaceis.main;

public class ParticipantAccount {
	
	private User user;
	private Account account;
	private String status;
	private String role;
	private int id;
	
	ParticipantAccount(User u, Account a, String s, String r, int id) {
		user = u;
		account = a;
		status = s;
		role = r;
		this.id = id;
	}
	
	ParticipantAccount(User u, Account a, String s, String r) {
		user = u;
		account = a;
		status = s;
		role = r;
	}
	
	public User getUser() {
		return user;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public String getStatus() {
		return status;
	}

	public String getRole() {
		return role;
	}
	
	public int getID() {
		return id;
	}
}
