package contasfaceis.main;

import java.util.ArrayList;

public class Account {
	
	String name;
	ArrayList<ParticipantAccount> participAccount;
	
	Account(String name, ArrayList particAccount) {
		this.name = name;
		participAccount = particAccount; 
	}

}
