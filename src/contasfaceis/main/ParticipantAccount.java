package contasfaceis.main;

public class ParticipantAccount {
	
	User user;
	Account account;
	String status;
	String role;
	
	ParticipantAccount(User u, Account a, String s, String r) {
		user = u;
		account = a;
		status = s;
		role = r;
	}
}
