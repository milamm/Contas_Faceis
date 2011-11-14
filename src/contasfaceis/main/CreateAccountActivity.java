package contasfaceis.main;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends Activity {
	
	private ContasFaceis appState;
	
	TextView nameTV;
	EditText nameET;
	TextView particTV;
	EditText participant;
	TextView currencyTV;
	EditText currencyET;
	Button addparticBT;
	Button createaccBT;
	
	ArrayList<NameValuePair> accountInfo;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appState = (ContasFaceis) CreateAccountActivity.this.getApplication();
		
    	setContentView(R.layout.createaccount);
    	
    	nameTV = (TextView) this.findViewById(R.id.nametv);
    	nameET = (EditText) this.findViewById(R.id.name);
    	particTV = (TextView) this.findViewById(R.id.particiTv);
    	participant = (EditText) this.findViewById(R.id.partic);
    	currencyTV = (TextView) this.findViewById(R.id.currencytv);
    	currencyET = (EditText) this.findViewById(R.id.currency);
    	addparticBT = (Button) this.findViewById(R.id.addpartic);
    	addparticBT.setOnClickListener(new AddPartButtonOnClickListener());
    	createaccBT = (Button) this.findViewById(R.id.createacc);
    	createaccBT.setOnClickListener(new CreateAccButtonOnClickListener());
    	
    	accountInfo = new ArrayList<NameValuePair>();
	}
	
	private final class AddPartButtonOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
						
			String email = participant.getText().toString();
			participant.setText("");
			
			if(email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
				TextView particAddedtv = new TextView(v.getContext());
				particAddedtv.setText(email);
			
				ViewGroup parent = (ViewGroup) v.getParent();
				int i = parent.indexOfChild(participant);
				parent.addView(particAddedtv,i);
				
				accountInfo.add(new BasicNameValuePair("emails",email));
			}
		}
	}
	
	private final class CreateAccButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			String name = nameET.getText().toString();
			String currency = currencyET.getText().toString();
			
			Account currentAccount = new Account(name, currency); 
			appState.setcurrentAccount(currentAccount);
			
			accountInfo.add(0,new BasicNameValuePair("access_token", appState.getcurrentUser().getFBaccessToken()));
			accountInfo.add(1,new BasicNameValuePair("name", name.toLowerCase()));
			accountInfo.add(2,new BasicNameValuePair("currency", currency.toLowerCase()));

	    	if(appState.getcurrentAccount().createAccountonServer(appState.getURL(),accountInfo, appState.getcurrentUser())) { 
	    		Toast.makeText(CreateAccountActivity.this, "Conta criada com sucesso.", Toast.LENGTH_LONG).show();
	    		Intent intent = new Intent(CreateAccountActivity.this, AccountPageActivity.class);
	    		CreateAccountActivity.this.startActivity(intent);
	    	} else 
	    		Toast.makeText(CreateAccountActivity.this, "Impossível criar conta.", Toast.LENGTH_LONG).show();
		}
	}
	
}
