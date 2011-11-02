package contasfaceis.main;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Account {
	
	private String METHOD;
	private String name;
	private String currency;
	private Integer id;
	private ArrayList<ParticipantAccount> participAccount = new ArrayList<ParticipantAccount>();
	
	Account(String name, String currency) {
		this.name = name;
		this.currency = currency; 
	}
	
	Account(String name, String currency, Integer id) {
		this.name = name;
		this.currency = currency; 
		this.id = id;
	}
	
	Account(JSONObject JSONAcc) {
		METHOD = "constructorJSON";
		try {
			this.name = JSONAcc.getString("name");
			this.currency = JSONAcc.getString("currency");
			this.id = Integer.parseInt(JSONAcc.getString("id"));
		} catch (JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
		}
	}

	public String getName() {
		return name;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public ArrayList<ParticipantAccount> getParticipants() {
		ArrayList<ParticipantAccount> participants = new ArrayList<ParticipantAccount>();
		for(int i=0;i<participAccount.size();i++) {
			participants.add(participAccount.get(i));
		}
		return participants;
	}
	
	public boolean createAccountonServer(String URL, ArrayList<NameValuePair> accountInfo, User adminuser) {
		METHOD = "createAccountonServer";
		
		ParticipantAccount particAcc = new ParticipantAccount(adminuser, this, "CONFIRMED", "ADMIN");
		participAccount.add(particAcc);
		
		JSONObject JSon = null;
		Http http = new Http();
	
		try {
			JSon = http.doPost(URL+"/account/",accountInfo);
			this.id = Integer.parseInt(JSon.getString("id"));
			JSONArray participantsList = JSon.getJSONArray("listParticipants");
			for(int i=0;i<participantsList.length();i++) {
				JSONObject participant = participantsList.getJSONObject(i);
				if(!participant.getString("role").equals("ADMIN")) {
					JSONObject user = participant.getJSONObject("user");
					User u = new User(user.getString("firstName"),user.getString("lastName"),user.getString("email"));
					particAcc = new ParticipantAccount(u, this, participant.getString("status"), participant.getString("role"));
					participAccount.add(particAcc);
				}
			}
			return true;
		} catch(JSONException e) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,e.getMessage());
			return false;
		}
	}
	
	public boolean confirmAccountParticipation(String url, Integer ParticAccId, String fBaccessToken) throws AccException {
		METHOD = "confirmAccountParticipation";
		
		JSONObject JSon = null;
		Http http = new Http();
	
		try {
			JSon = http.doGet(url+"/participationaccount/"+ParticAccId.toString()+"/confirm/"+fBaccessToken);
			if(!JSon.isNull("success")) 
				return true;
			if(!JSon.isNull("error")) {
				throw new AccException(JSon.getString("error"));
			}
		} catch(JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
			return false;
		}
		return false;
	}

	public boolean deleteAccount(String URL, String accesstoken) throws AccException {
		METHOD = "deleteAccount";
		
		JSONObject JSon = null;
		Http http = new Http();
	
		try {
			JSon = http.doGet(URL+"/account/"+id.toString()+"/delete/"+accesstoken);
			if(!JSon.isNull("success")) 
				return true;
			if(!JSon.isNull("error")) {
				throw new AccException(JSon.getString("error"));
			}
		} catch(JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
			return false;
		}
			/*for(int i=0;i<participantsList.length();i++) {
				JSONObject participant = participantsList.getJSONObject(i);
				if(!participant.getString("role").equals("ADMIN")) {
					JSONObject user = participant.getJSONObject("user");
					User u = new User(user.getString("firstName"),user.getString("lastName"),user.getString("email"));
					particAcc = new ParticipantAccount(u, this, participant.getString("status"), participant.getString("role"));
					participAccount.add(particAcc);
				}
			}*/
		return false;
	}
	
	public class AccException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public AccException(String message) {
			super(message);
		}
	}
}
