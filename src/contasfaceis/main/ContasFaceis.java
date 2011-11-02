package contasfaceis.main;

import java.util.ArrayList;

import com.facebook.android.Facebook;

import android.app.Application;

public class ContasFaceis extends Application {
	
	private String URL = "http://cold-frost-3484.herokuapp.com";
	private User currentUser;
	private Account currentAccount;
	private ParticipantAccount currentparticipantAccount;
	private ArrayList<ParticipantAccount> participantAccountRelations;
	private Facebook mFacebook;
	
	public void setcurrentUser(User u) {
		currentUser = u;
	}
	
	public void setcurrentAccount(Account c) {
		currentAccount = c;
	}
	
	public void setcurrentParticipantAccount(ParticipantAccount participantAccount) {
		currentparticipantAccount = participantAccount;
	}
	
	public void setparticipantAccountRelations(ArrayList<ParticipantAccount> participantAccountRels) {
		participantAccountRelations = participantAccountRels;
	}
	
	public void setFacebook(Facebook fb) {
		mFacebook = fb;
	}
	
	public User getcurrentUser() {
		return currentUser;
	}
	
	public Account getcurrentAccount() {
		return currentAccount;
	}
	
	public ParticipantAccount getcurrentParticipantAccount() {
		return currentparticipantAccount;
	}
	
	public ArrayList<ParticipantAccount> getparticipantAccountRelations() {
		return participantAccountRelations;
	}
	
	public String getURL() {
		return URL;
	}
	
	public Facebook getFacebook() {
		return mFacebook;
	}
	
}
