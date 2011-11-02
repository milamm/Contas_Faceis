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
	//private String METHOD;
	TextView nameTV;
	EditText nameET;
	TextView particTV;
	EditText participant;
	TextView currencyTV;
	EditText currencyET;
	Button addparticBT;
	Button createaccBT;
	//int npartic;
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
    	//String particEmail = participant.getText().toString();
    	//participant.addTextChangedListener(new AddParticWatcher());
    	
    	accountInfo = new ArrayList<NameValuePair>();
    	//npartic = 0;
	}
	
	private final class AddPartButtonOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
						
			String email = participant.getText().toString();
			participant.setText("");
			
			//Pattern p = Pattern.compile("\b[A-Z0-9._%+-]+@[A-Z0-9.-]\b");
			if(email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
				TextView particAddedtv = new TextView(v.getContext());
				particAddedtv.setText(email);
			
				ViewGroup parent = (ViewGroup) v.getParent();
				int i = parent.indexOfChild(participant);
				parent.addView(particAddedtv,i);
				
				accountInfo.add(new BasicNameValuePair("emails",email));
				//npartic++;
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
