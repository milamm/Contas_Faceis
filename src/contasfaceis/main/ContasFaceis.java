package contasfaceis.main;

import android.app.Application;

public class ContasFaceis extends Application {
	
	public String URL = "http://cold-frost-3484.herokuapp.com";
	public User currentUser;
	public Account currentAccount;
	
	public void setcurrentUser(User u) {
		currentUser = u;
	}
	
	public void setcurrentAccount(Account c) {
		currentAccount = c;
	}
	
	public User getcurrentUser() {
		return currentUser;
	}
	
	public Account getcurrentAccount() {
		return currentAccount;
	}
	
	public String getURL() {
		return URL;
	}
}
