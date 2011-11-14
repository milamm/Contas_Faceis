package contasfaceis.main;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Account {
	
	private String METHOD;
	private String name;
	private String currency;
	private Integer id;
	private ArrayList<ParticipantAccount> participAccount;
	
	Account(String name, String currency) {
		this.name = name;
		this.currency = currency; 
		participAccount = new ArrayList<ParticipantAccount>();
	}
	
	Account(String name, String currency, Integer id) {
		this.name = name;
		this.currency = currency;
		this.id = id;
		participAccount = new ArrayList<ParticipantAccount>();
	}
	
	public Account(JSONObject JSONAcc) {
		METHOD = "constructorJSON";
		participAccount = new ArrayList<ParticipantAccount>();
		try {
			this.name = JSONAcc.getString("name");
			this.currency = JSONAcc.getString("currency");
			this.id = Integer.parseInt(JSONAcc.getString("id"));
		} catch (JSONException JSe) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,JSe.getMessage());
		}
	}

	public void setParameters(JSONObject JSONAcc) throws JSONException {
		METHOD = "setParameters";
		ParticipantAccount particAcc = null;
				
		this.id = Integer.parseInt(JSONAcc.getString("id"));
		JSONArray participantsList = JSONAcc.getJSONArray("listParticipants");
		for(int i=0;i<participantsList.length();i++) {
			JSONObject participant = participantsList.getJSONObject(i);
			//if(!participant.getString("role").equals("ADMIN")) {
				JSONObject user = participant.getJSONObject("user");
				User u = new User(user.getString("firstName"),user.getString("lastName"),user.getString("email"));
				particAcc = new ParticipantAccount(u, this, participant.getString("status"), participant.getString("role"), participant.getInt("id"));
				participAccount.add(particAcc);
			//}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public ArrayList<ParticipantAccount> getParticipants() {
		return participAccount;
	}
	
	public boolean getParticipantsFromServer(String url, String fBaccessToken)
			throws AccException {
		METHOD = "getParticipantsFromServer";

		JSONObject JSon = null;
		Http http = new Http();

		try {
			JSon = http.doGet(url + "/account/" + this.id + "/participant/all/"
					+ fBaccessToken);
			if (!JSon.isNull("listOfParticipants")) {
				JSONArray particsAcc = JSon.getJSONArray("listOfParticipants");
				for (int i = 0; i < particsAcc.length(); i++) {
					JSONObject JSparticAcc = particsAcc.getJSONObject(i);
					User u = new User();
					u.setParameters(JSparticAcc.getJSONObject("user"));
					String status = JSparticAcc.getString("status");
					String role = JSparticAcc.getString("role");
					int id = JSparticAcc.getInt("id");
					ParticipantAccount particAcc = new ParticipantAccount(u,
							this, status, role, id);
					participAccount.add(particAcc);
				}
				return true;
			}
			if (!JSon.isNull("error")) {
				throw new AccException(JSon.getString("error"));
			}
		} catch (JSONException JSe) {
			Log.e(this.getClass().getSimpleName() + "/" + METHOD,
					JSe.getMessage());
			return false;
		}
		return false;
	}
	
	public boolean addExpense(String URL, String description, Double amount, String access_token) throws AccException {
		METHOD = "addExpense";
		
		ArrayList<NameValuePair> expenseInfo = new ArrayList<NameValuePair>();
		JSONObject JSon = null;
		Http http = new Http();
	
		expenseInfo.add(0,new BasicNameValuePair("access_token", access_token));
		expenseInfo.add(1,new BasicNameValuePair("accountId", this.id.toString()));
		expenseInfo.add(2,new BasicNameValuePair("description", description));
		expenseInfo.add(3,new BasicNameValuePair("amount", amount.toString()));
		
		try {
			JSon = http.doPost(URL+"/expense/",expenseInfo);
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
	
	public boolean createAccountonServer(String URL, ArrayList<NameValuePair> accountInfo, User adminuser) {
		METHOD = "createAccountonServer";
		
		JSONObject JSon = null;
		Http http = new Http();
	
		try {
			JSon = http.doPost(URL+"/account/",accountInfo);
			setParameters(JSon);
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
