package contasfaceis.main;

import contasfaceis.main.Account.AccException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {
	
	TextView titleTV;
	TextView descriptionTV;
	EditText descriptionET;
	TextView amountTV;
	EditText amountET;
	Button addBT;
	
	ContasFaceis appState;
	Account currentAccount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appState = (ContasFaceis) AddExpenseActivity.this.getApplication();
		currentAccount = appState.getcurrentAccount();
		
		setContentView(R.layout.addexpensepage);

		titleTV = (TextView) findViewById(R.id.title);
		descriptionTV = (TextView) findViewById(R.id.descriptiontv);
		descriptionET = (EditText) findViewById(R.id.description);
		amountTV = (TextView) findViewById(R.id.amounttv);
		amountET = (EditText) findViewById(R.id.amount);
		addBT = (Button) findViewById(R.id.addexpbt);
		addBT.setOnClickListener(new AddExpOnClickListener());
	}
	
	private class AddExpOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			String description = descriptionET.getText().toString();
			Double amount = Double.valueOf(amountET.getText().toString());
			
			try {
	    		if (currentAccount.addExpense(appState.getURL(), description, amount, appState.getcurrentUser().getFBaccessToken())) {
	    			Toast.makeText(AddExpenseActivity.this,
	    					"Despesa adicionada.", Toast.LENGTH_LONG);
	    		} else
	    			Toast.makeText(AddExpenseActivity.this,
	    					"Não foi possível adicionar a despesa.", Toast.LENGTH_LONG);
	    			
	    		Intent intent = new Intent(AddExpenseActivity.this,
	    				AccountPageActivity.class);
	    		AddExpenseActivity.this.startActivity(intent);
	    	} catch (AccException dAE) {
				Toast.makeText(AddExpenseActivity.this, dAE.getMessage(),
						Toast.LENGTH_LONG);
			}
		}
		
	}
}

