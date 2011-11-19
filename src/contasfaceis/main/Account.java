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
	private Double unitCost;
	
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
	
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public Double getUnitCost() {
		return unitCost;
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
	
	public boolean calculate(String URL, String accesstoken) throws AccException{
		METHOD = "calculate";
		
		JSONObject JSon = null;
		Http http = new Http();
	
		try {
			JSon = http.doGet(URL+"/account/"+id.toString()+"/result/"+accesstoken);
			if(!JSon.isNull("resultParticipants")) {
				JSONArray participantList = JSon.getJSONArray("resultParticipants");
				JSONArray transactionstoResolve = JSon.getJSONArray("transactionsToResolve");
				
				for(int i=0; i<participantList.length(); i++) {
					JSONObject participant = participantList.getJSONObject(i);
					String email = participant.getString("email");
					
					//Update participant account relation of user with email = 'email'
					for(int j=0; j<this.participAccount.size(); j++) {
						ParticipantAccount particAcc = this.participAccount.get(j); 
						if(particAcc.getUser().getEmail().equals(email)) {
							Double balance = participant.getDouble("balance");
							//String balanceStr = participant.getString("balance");
							Double amountSpent = participant.getDouble("amountSpent");
							
							/*if(balanceStr.startsWith("-"))
								balance = Double.valueOf(balanceStr.substring(1));
							else
								balance = Double.valueOf(balanceStr);*/
							
							particAcc.setBalance(balance);
							particAcc.setTotalSpent(amountSpent);
							
							//Search for credit or debit transaction for this user
							for(int k=0; k<transactionstoResolve.length(); k++) {
								JSONObject beneficiary_payor = transactionstoResolve.getJSONObject(k);
								JSONObject beneficiary = beneficiary_payor.getJSONObject("beneficiary");
								JSONObject payor = beneficiary_payor.getJSONObject("payor");
								Double amount = beneficiary_payor.getDouble("amount");
								
								if(beneficiary.getString("email").equals(email)) { 								   //User should receive 
									String[] creditStr = { "receive", amount.toString(), payor.getString("name")}; 
									particAcc.addDebitCredit(creditStr);
								} else if (payor.getString("email").equals(email)) {   							   //User should pay
									String[] debitStr = { "owes", amount.toString(), beneficiary.getString("name")}; 
									particAcc.addDebitCredit(debitStr);
								}
							}
						}
					}
				}
				Double unitCost = JSon.getDouble("unitCost");
				setUnitCost(unitCost);
				return true;
			}
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
		return false;
	}
	
	public class AccException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public AccException(String message) {
			super(message);
		}
	}
}
