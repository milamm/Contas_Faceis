package contasfaceis.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;

public class UserPageActivity extends Activity {

	private ContasFaceis appState;
	private User currentUser;

	private LoginButton logoutButton;
	private TextView greetingtv;
	private Button createAccountbt;
	private ListView accountListlt;
	
	private ArrayList<ParticipantAccount> particAccList;
	private MyCustomAdapter listAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appState = (ContasFaceis) this.getApplication();
		currentUser = appState.getcurrentUser();

		setContentView(R.layout.userpage);

		greetingtv = (TextView) this.findViewById(R.id.greeting);
		greetingtv.setText("Bem-vindo, " + currentUser.getFirstName());

		logoutButton = (LoginButton) this.findViewById(R.id.logout);
		SessionStore.restore(appState.getFacebook(), this);
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		logoutButton.init(this, appState.getFacebook(),this.getApplicationContext());

		createAccountbt = (Button) this.findViewById(R.id.createAccount);
		createAccountbt.setOnClickListener(new CreaAccOnClickListener());
		
		particAccList = currentUser.getUserAccountsParticipationsFromServer(appState.getURL());
		appState.setparticipantAccountRelations(particAccList);
		if(checkforPendingAccounts())
			Toast.makeText(UserPageActivity.this, "Você foi convidado para novas contas! ", 10).show();
		
		accountListlt = (ListView) this.findViewById(R.id.accountList);
		TextView listtitleTV = new TextView(this.getApplicationContext()); 
		listtitleTV.setText("Suas Contas");
		listtitleTV.setTextSize(20);
		accountListlt.addHeaderView(listtitleTV);
		listAdapter = new MyCustomAdapter();
		accountListlt.setAdapter(listAdapter);
		
		accountListlt.setOnItemClickListener(new ListAccOnItemClickListener());
		//List<String> accNamesList = appState.getcurrentUser().getUserAccNames(particAccList); 
		
		//constructListView(accountListlt,"Suas Contas");		
	}
	
	/*private ArrayList<ParticipantAccount> getPartcipantAccountList() {
		return particAccList;
	}
	
	private void constructListView(ListView lv, String title) {
		TextView titleTV = new TextView(this);
		String[] accName = new String[particAccList.size()];
		//TextView accNameTV[] = new TextView[particAccList.size()];
		boolean newaccinvit = false;
		for(int i=0; i<particAccList.size();i++) {
			ParticipantAccount particAcc = particAccList.get(i);
			accName[i] = particAcc.getAccount().getName(); 
		}
		titleTV.setText("Suas Contas");
		titleTV.setTextSize(20);
		lv.addHeaderView(titleTV);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.listuseraccounts_item, accName));
		ListAdapter k = lv.getAdapter(); 
		for(int i=0;i<lv.getChildCount();i++) {
			ParticipantAccount particAcc = particAccList.get(i);
			TextView TV = (TextView) lv.getChildAt(i); 
			TV.setId(particAcc.getID());
			if(particAcc.getRole().equals("PENDING")) {
				TV.setTypeface(Typeface.DEFAULT_BOLD);
				newaccinvit = true;
			}
		}
		if(newaccinvit)
			Toast.makeText(this, "Você foi convidado para novas contas!", Toast.LENGTH_LONG).show();
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    
		  });
	}*/
	
	private boolean checkforPendingAccounts() {
		for(int i=0;i<particAccList.size();i++) {
			if(particAccList.get(i).getStatus().equals("PENDING"))
				return true;
		}
		return false;
	}

	private class ListAccOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ParticipantAccount	pA = particAccList.get(position-1);
			appState.setcurrentAccount(pA.getAccount());
			appState.setcurrentParticipantAccount(pA);
			//Log.v("Id", view.getId()+"/partAcc "+pA.getID());
			
			Intent intent = new Intent(UserPageActivity.this, AccountPageActivity.class);
			//intent.putExtra("particAccId", pA.getID());
			//intent.putExtra("particRole", pA.get);
			UserPageActivity.this.startActivity(intent);
		}
	}
	
	private class MyCustomAdapter extends BaseAdapter {
		 
        private ArrayList<ParticipantAccount> mData = new ArrayList<ParticipantAccount>();
        //private LayoutInflater mInflater;
 
        public MyCustomAdapter() {
        	mData = particAccList;
           //mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
 
        /*public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }*/
 
        @Override
        public int getCount() {
            return mData.size();
        }
 
        @Override
        public ParticipantAccount getItem(int position) {
            return mData.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView + "Size " + this.getCount());
            //LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView TV = null;
            ParticipantAccount particAcc = mData.get(position);
            
            View row = convertView;
            if(row == null){
                LayoutInflater inflater= getLayoutInflater();
                row = inflater.inflate(R.layout.listuseraccounts_item, parent, false);
            }
            
            TV = (TextView) row;
            TV.setText(particAcc.getAccount().getName());
            //TV.setId(particAcc.getID());
            if(particAcc.getStatus().equals("CONFIRMED")) { 
            	TV.setTypeface(Typeface.DEFAULT);
            	TV.setTextColor(Color.WHITE);
        	} else {
            	TV.setTypeface(Typeface.DEFAULT_BOLD);
            	TV.setTextColor(Color.RED);
            	//pendingaccounts = true;
            }
            /*if (convertView == null) {
                if(particStatus.equals("CONFIRMED")) { 
                	convertView = mInflater.inflate(R.layout.listuseraccounts_item, null);
                	TV = (TextView)convertView.findViewById(R.id.useraccconfirmed);
            	} else {
                	convertView = mInflater.inflate(R.layout.listuseraccountspending_item, null);
                	TV = (TextView)convertView.findViewById(R.id.useraccpending);
            	}
                //holder = new ViewHolder();
                //holder.textView = (TextView)convertView.findViewById(R.id.text);
                //convertView.setTag(holder);
            } else {*/
                //holder = (ViewHolder)convertView.getTag();
            	
            //}
            
            //holder.textView.setText(mData.get(position));
            //return convertView;
            return row;
        }
	}

	private final class CreaAccOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(UserPageActivity.this,
					CreateAccountActivity.class);
			UserPageActivity.this.startActivity(intent);
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(UserPageActivity.this, "Logging out...",Toast.LENGTH_LONG).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(UserPageActivity.this, "You have logged out! ", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(UserPageActivity.this, MainActivity.class);
			UserPageActivity.this.startActivity(intent);
		}
	}

}