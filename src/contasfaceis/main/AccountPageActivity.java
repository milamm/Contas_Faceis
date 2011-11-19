package contasfaceis.main;

import java.util.ArrayList;

import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionStore;
import com.facebook.android.SessionEvents.LogoutListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import contasfaceis.main.Account.AccException;
import contasfaceis.main.UserPageActivity.SampleLogoutListener;

public class AccountPageActivity extends Activity {
	private TextView username;
	private LoginButton logoutButton;
	private TextView accname;
	private TextView currencyTV;
	private TextView currency;
	private TableLayout participantList;
	private TextView unitCostTV;
	private TableLayout debitcreditTL;
	private Button BT1, BT2;

	private ContasFaceis appState;
	private Account currentAccount;
	private ArrayList<ParticipantAccount> particList;
	private Boolean newUser = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appState = (ContasFaceis) AccountPageActivity.this.getApplication();
		currentAccount = appState.getcurrentAccount();
		ParticipantAccount pA = appState.getcurrentParticipantAccount();

		setContentView(R.layout.accountpage);

		username = (TextView) this.findViewById(R.id.username);
		username.setText(appState.getcurrentUser().getFirstName());
		
		logoutButton = (LoginButton) this.findViewById(R.id.logout);
		SessionStore.restore(appState.getFacebook(), this);
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		logoutButton.init(this, appState.getFacebook());
		
		accname = (TextView) this.findViewById(R.id.accname);
		accname.setText(currentAccount.getName());
		currencyTV = (TextView) this.findViewById(R.id.currencytv);
		currency = (TextView) this.findViewById(R.id.currency);
		currency.setText(currentAccount.getCurrency());
		unitCostTV = (TextView) this.findViewById(R.id.unitcostTv);
		debitcreditTL = (TableLayout) this.findViewById(R.id.debitcreditTl);

		if (pA.getStatus().equals("PENDING")) {
			BT1 = (Button) this.findViewById(R.id.refusebt);
			BT1.setEnabled(true);
			BT1.setVisibility(View.VISIBLE);
			BT1.setOnClickListener(new ButtonOnClickListener());
			BT2 = (Button) this.findViewById(R.id.acceptbt);
			BT2.setEnabled(true);
			BT2.setVisibility(View.VISIBLE);
			BT2.setOnClickListener(new ButtonOnClickListener());
		}

		participantList = (TableLayout) this.findViewById(R.id.participantList);
				
		try {
			if(currentAccount.getParticipants().size()==0) {
				if (currentAccount.getParticipantsFromServer(appState.getURL(), appState.getcurrentUser().getFBaccessToken())) {
					particList = currentAccount.getParticipants();
			    } else
					Toast.makeText(this, "Conta desconhecida", Toast.LENGTH_LONG).show();
				}
			else 
				particList = currentAccount.getParticipants();
			
			populateParticipantTable();
		} catch (AccException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.accountmenu, menu);
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem DelItem = menu.findItem(R.id.deleteacc);
		MenuItem CalcItem = menu.findItem(R.id.calculate);
		MenuItem AddPayItem = menu.findItem(R.id.addexpense);
		MenuItem SeeHistItem = menu.findItem(R.id.seehist);

		if(appState.getcurrentParticipantAccount().getRole().equals("USER")) {
			DelItem.setEnabled(false);
			DelItem.setVisible(false);
		}
		
		if(newUser) {
			CalcItem.setEnabled(false);
			CalcItem.setVisible(false);
			AddPayItem.setEnabled(false);
			AddPayItem.setVisible(false);
			SeeHistItem.setEnabled(false);
			SeeHistItem.setVisible(false);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		boolean resultOK = false;
	
	    switch (item.getItemId()) {
	    case R.id.addexpense:
	    	Intent intentExp = new Intent(AccountPageActivity.this,
					AddExpenseActivity.class);
			AccountPageActivity.this.startActivity(intentExp);
	        return true;
	    case R.id.calculate:
	    	try {
	    		if(currentAccount.calculate(appState.getURL(), appState.getcurrentUser().getFBaccessToken())) {
	    			updateParticipantTable();
	    			showTransactionstoResolve();
	    		} else
	    			Toast.makeText(this, "Erro ao fazer os cálculos", Toast.LENGTH_LONG);
	    	} catch(AccException accE) {
	    		Toast.makeText(AccountPageActivity.this, accE.getMessage(),
						Toast.LENGTH_LONG);
	    	}
	        return true;
	    case R.id.seehist:
	        return true;
	    case R.id.deleteacc:
	    	try {
	    		if(appState.getcurrentParticipantAccount().getRole().equals("ADMIN")) {
	    			if (currentAccount.deleteAccount(appState.getURL(),
	    					appState.getcurrentUser().getFBaccessToken())) {
	    				Toast.makeText(AccountPageActivity.this,
	    						"Conta deletada.", Toast.LENGTH_LONG);
	    				resultOK = true;
	    			} else
	    				Toast.makeText(AccountPageActivity.this,
	    						"Não foi possível deletar conta.", Toast.LENGTH_LONG);
	    			if (resultOK) {
	    				Intent intentDel = new Intent(AccountPageActivity.this,
	    						UserPageActivity.class);
	    				AccountPageActivity.this.startActivity(intentDel);
	    				return true;
	    			}
	    		} else 
	    			Toast.makeText(AccountPageActivity.this,
    						"Você não tem permissão para deletar esta conta.", Toast.LENGTH_LONG);
	    	} catch (AccException dAE) {
				Toast.makeText(AccountPageActivity.this, dAE.getMessage(),
						Toast.LENGTH_LONG);
				return false;
			}
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void populateParticipantTable() {
		for(int i=0; i<particList.size();i++) {
			TableRow row = new TableRow(this);
			
			TextView name = new TextView(this);
			name.setText(particList.get(i).getUser().getName());
			if(particList.get(i).getStatus().equals("PENDING")) {
				name.setTypeface(Typeface.DEFAULT_BOLD);
				name.setTextColor(Color.RED);
				newUser = true;
			}
			row.addView(name);
			
			TextView total = new TextView(this);
			total.setText("0");
			row.addView(total);
			
			TextView balance = new TextView(this);
			balance.setText("0");
			row.addView(balance);
			
			participantList.addView(row);
		}
	}
	
	private void updateParticipantTable() {
		particList = currentAccount.getParticipants();
		
		for(int i=0; i<particList.size();i++) {
	
			ParticipantAccount participant = particList.get(i);
			
			TableRow row = (TableRow) participantList.getChildAt(i+1);
			TextView totalTV = (TextView) row.getChildAt(1);
			TextView balanceTV = (TextView) row.getChildAt(2);
			
			totalTV.setText(participant.getTotalSpent().toString());
			balanceTV.setText(participant.getBalance().toString());
		}
	}
	
	private void showTransactionstoResolve() {
		String[] receiveCurUser = {"Você deve receber", "de"};
		String[] payCurUser = {"Você deve", "à"};
		String[] pay = {"deve", "à"};
		
		unitCostTV.setEnabled(true);
		debitcreditTL.setEnabled(true);
	    debitcreditTL.removeAllViews();
		
		unitCostTV.setText("Custo por pessoa = "+currentAccount.getUnitCost().toString());
		
		for(int i=0; i<particList.size(); i++) {
			ParticipantAccount participant = particList.get(i); 
			String email = participant.getUser().getEmail();
			TextView debitcreditTV = new TextView(this);
			ArrayList<String[]> debitcreditList = participant.getDebitCreditList();
			User currentUser = appState.getcurrentUser(); 
			
			if(currentUser.getEmail().equals(email)) {
						
				for(int j=0; j<debitcreditList.size(); j++) {
					String[] debitcredit = debitcreditList.get(j);
					
					if(debitcredit[0].equals("receive"))
						debitcreditTV.setText(receiveCurUser[0]+" "+debitcredit[1]+" "+receiveCurUser[1]+" "+debitcredit[2]);
					else
						debitcreditTV.setText(payCurUser[0]+" "+debitcredit[1]+" "+payCurUser[1]+" "+debitcredit[2]);
					debitcreditTL.addView(debitcreditTV, 0);
				}
				
			} else {
				
				for(int j=0; j<debitcreditList.size(); j++) {
					String[] debitcredit = debitcreditList.get(j);
					
					if(debitcredit[0].equals("owes") & !debitcredit[2].equals(currentUser.getName()))
						debitcreditTV.setText(participant.getUser().getName()+" "+pay[0]+" "+debitcredit[1]+" "+pay[1]+" "+debitcredit[2]);
					debitcreditTL.addView(debitcreditTV);
				}
				
			}
		}
		
	}
	
	private class ButtonOnClickListener implements OnClickListener {

		public void onClick(View v) {
			boolean resultOK = false;

			try {
				switch(v.getId()) {
				case R.id.acceptbt:
					if (currentAccount.confirmAccountParticipation(appState
							.getURL(), appState.getcurrentParticipantAccount()
							.getID(), appState.getcurrentUser()
							.getFBaccessToken())) {
						Toast.makeText(AccountPageActivity.this,
								"Participation confirmed.", Toast.LENGTH_LONG);
						resultOK = true;
					} else
						Toast.makeText(
								AccountPageActivity.this,
								"Could not confirm your participation to this account.",
								Toast.LENGTH_LONG);
				}

			} catch (AccException dAE) {
				Toast.makeText(AccountPageActivity.this, dAE.getMessage(),
						Toast.LENGTH_LONG);
			} finally {
				if (resultOK) {
					Intent intent = new Intent(AccountPageActivity.this,
							UserPageActivity.class);
					AccountPageActivity.this.startActivity(intent);
				}
			}
		}
	}
	
	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(AccountPageActivity.this, "Logging out...",
					Toast.LENGTH_LONG).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(AccountPageActivity.this, "You have logged out! ",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(AccountPageActivity.this,
					MainActivity.class);
			AccountPageActivity.this.startActivity(intent);
		}
	}

}
