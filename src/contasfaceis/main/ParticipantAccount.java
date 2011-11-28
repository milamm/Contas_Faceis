package contasfaceis.main;

import java.util.ArrayList;

public class ParticipantAccount {
	
	private User user;
	private Account account;
	private String status;
	private String role;
	private int id;
	private Double totalSpent;
	private Double balance;
	private ArrayList<String[]> debitcreditList = new ArrayList<String[]>();
	
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
	
	public Double getTotalSpent() {
		return totalSpent;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public ArrayList<String[]> getDebitCreditList() {
		return debitcreditList;
	}
	
	public void setTotalSpent(Double totalSpent) {
		this.totalSpent = totalSpent;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	 
	public void setDebitCreditList(ArrayList<String[]> debitcreditlist) {
		if(debitcreditlist==null)
			debitcreditList = new ArrayList<String[]>();
		else 
			debitcreditList = debitcreditlist;
	}
	
	public void addDebitCredit(String[] debitcredit) {
		debitcreditList.add(debitcredit);
	}

	public void setStatus(String status) {
		this.status = status; 
	}
}
