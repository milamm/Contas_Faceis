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
	private Button BT1, BT2;

	private ContasFaceis appState;
	private Account currentAccount;
	private ArrayList<ParticipantAccount> particList;
	
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
			//listAdapter = new ParticCustomAdapter();
			//participantList.setAdapter(listAdapter);
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
		MenuItem item = menu.findItem(R.id.deleteacc);

		if(appState.getcurrentParticipantAccount().getRole().equals("USER")) {
			item.setEnabled(false);
			item.setVisible(false);
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
			}
			row.addView(name);
			
			TextView total = new TextView(this);
			total.setText("teste");
			row.addView(total);
			
			TextView balance = new TextView(this);
			balance.setText("teste");
			row.addView(balance);
			
			participantList.addView(row);
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
