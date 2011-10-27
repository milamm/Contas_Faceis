package contasfaceis.main;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
			
			JSONObject JSonObject = null;
	    	Http http = new Http();
			String name = nameET.getText().toString();
			String currency = currencyET.getText().toString();
			
			accountInfo.add(0,new BasicNameValuePair("access_token", appState.getcurrentUser().getFBaccessToken()));
			accountInfo.add(1,new BasicNameValuePair("name", name.toLowerCase()));
			accountInfo.add(2,new BasicNameValuePair("currency", currency.toLowerCase()));

	    	try {
	    		JSonObject = http.doPost(appState.getURL()+"/account/",accountInfo);
	    	} catch(Exception e) {
	    		Log.e("HttpPost",e.getMessage());
	    	}
		}
	}
	/*private final class AddParticWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			String email =  arg0.toString();
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			String email =  s.toString();
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String email =  s.toString();
			
		}
		
	}*/

}
