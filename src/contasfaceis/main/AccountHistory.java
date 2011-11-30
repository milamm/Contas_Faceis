package contasfaceis.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import contasfaceis.main.Account.AccException;

public class AccountHistory extends ListActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        ContasFaceis appState = (ContasFaceis) AccountHistory.this.getApplication();
        Account currentAccount = appState.getcurrentAccount();
        
        setContentView(R.layout.accounthistory);
        ListView lv = getListView(); 
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.acchistoryheader, (ViewGroup) findViewById(R.id.acchistoryHeader));

        //Get history
        try {
        	ArrayList<HashMap<String, String>> accountHistory = currentAccount.getAccountHistory(appState.getURL(),appState.getcurrentUser().getFBaccessToken());
        	if(accountHistory==null)
        		Toast.makeText(this, "Erro ao tentar acessar histórico da conta.", Toast.LENGTH_LONG).show();
        	else {
        		lv.addHeaderView( header );
        		SimpleAdapter adapter = new SimpleAdapter(this, accountHistory, R.layout.accounthistory_row,
        	            new String[] {"user", "description", "amount"}, new int[] {R.id.acchistory_user, R.id.acchistory_desc, R.id.acchistory_amount});
        		lv.setAdapter(adapter);
        	}
        } catch(AccException accE) {
        	Toast.makeText(this, accE.getMessage(), Toast.LENGTH_LONG).show();
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(AccountHistory.this, AccountPageActivity.class);
			AccountHistory.this.startActivity(intent);
			return true;
		}
		return true;
	}
	
}
