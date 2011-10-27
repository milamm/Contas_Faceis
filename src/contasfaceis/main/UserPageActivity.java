package contasfaceis.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UserPageActivity extends Activity {
	
	private User currentUser;
	
	private TextView greetingtv;
	private Button createAccountbt;
	private ListView accountListlt;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ContasFaceis appState = (ContasFaceis) this.getApplication();
        currentUser = appState.getcurrentUser();
    	
    	setContentView(R.layout.userpage);
    	greetingtv = (TextView) this.findViewById(R.id.greeting);
    	greetingtv.setText("Bem-vindo, " + currentUser.getFirstName());
    	createAccountbt = (Button) this.findViewById(R.id.createAccount);
    	createAccountbt.setOnClickListener(new CreaAccOnClickListener());
    	accountListlt = (ListView) this.findViewById(R.id.accountList);
	}
	
	private final class CreaAccOnClickListener implements OnClickListener {
			
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(UserPageActivity.this, CreateAccountActivity.class);
       		UserPageActivity.this.startActivity(intent);
		}
	}
	
}