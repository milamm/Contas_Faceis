package contasfaceis.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User {
	
	private String METHOD;
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
	public User() {
	}
	
	public User(String accessToken) {
		FBaccessToken = accessToken;
	}
	
	public User(String fname, String lname, String email) {
		firstName = fname;
		lastName = lname;
		this.email = email;
	}
	
	public void setParameters(String fname, String lname, String email) {
		firstName = fname;
		lastName = lname;
		this.email = email;
	}
	
	public boolean setParameters(JSONObject userInfo) {
		METHOD = "setParametersJSON";
		try {
			firstName = userInfo.getString("firstName");
			lastName = userInfo.getString("lastName");
			email = userInfo.getString("email");
			return true;
		} catch (JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
			return false;
		}
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
	
	public String getName() {
		return firstName+" "+lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFBaccessToken() {
		return FBaccessToken;
	}
	
	public JSONObject getUserInformationFromServer(String URL) {
		METHOD = "getUserInformationFromServer";
    	JSONObject JSon = null;
    	Http http = new Http();
    	try {
    		JSon = http.doGet(URL+"/user/"+FBaccessToken);
    		return JSon;
    	} catch(Exception e) {
    		Log.e(this.getClass().getSimpleName()+"/"+METHOD,e.getMessage());
    		return JSon;
    	}
    }

	public ArrayList<ParticipantAccount> getUserAccountsParticipationsFromServer(String URL) {
		METHOD = "getUserAccountsParticipationsFromServer";
		ArrayList<ParticipantAccount> accounts = new ArrayList<ParticipantAccount>();
		JSONObject JSon = null;
		Http http = new Http();
		try {
			JSon = http.doGet(URL+"/user/account/all/"+FBaccessToken);
			JSONArray particAcc = JSon.getJSONArray("listParticipationsToAccounts");
			for(int i=0;i<particAcc.length();i++) {
				JSONObject particAccInfo = particAcc.getJSONObject(i);
				JSONObject accountInfo = particAccInfo.getJSONObject("account");
				Account account = new Account(accountInfo);
				ParticipantAccount particAccount = new ParticipantAccount(this, account, particAccInfo.getString("status"), 
						particAccInfo.getString("role"), particAccInfo.getInt("id"));
				accounts.add(i,particAccount);
			}
			return accounts;
		} catch(JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
			return accounts;
		}
		
	}
		
	public List<String> getUserAccNames(ArrayList<ParticipantAccount> particAccList) {
		List<String> accNamesList = new ArrayList<String>(); 
		
		for(int i=0;i<particAccList.size();i++) {
			ParticipantAccount particAcc = particAccList.get(i);
			Account account = particAcc.getAccount();
			accNamesList.add(account.getName());
		}
		return accNamesList;
	}
	
}