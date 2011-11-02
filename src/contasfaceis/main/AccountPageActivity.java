package contasfaceis.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import contasfaceis.main.Account.AccException;

public class AccountPageActivity extends Activity {
	TextView nameTV;
	TextView name;
	TextView currencyTV;
	TextView currency;
	ListView participantList;
	Button BT1=null, BT2=null;
	
	ContasFaceis appState;
	Account currentAccount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appState = (ContasFaceis) AccountPageActivity.this.getApplication();
		currentAccount = appState.getcurrentAccount();
		ParticipantAccount pA = appState.getcurrentParticipantAccount();
		
	//	Bundle bundle = this.getIntent().getExtras();
	//	Integer particAccId = bundle.getInt("particAccId");
				
    	setContentView(R.layout.accountpage);
    	
    	nameTV = (TextView) this.findViewById(R.id.nametv);
    	name = (TextView) this.findViewById(R.id.name);
    	name.setText(currentAccount.getName());
    	currencyTV = (TextView) this.findViewById(R.id.currencytv);
    	currency = (TextView) this.findViewById(R.id.currency);
    	currency.setText(currentAccount.getCurrency());
    	
    	if(pA.getStatus().equals("PENDING")) {
    		BT1 = (Button) this.findViewById(R.id.refusebt);
    		BT1.setVisibility(View.VISIBLE);
    		BT1.setOnClickListener(new ButtonOnClickListener());
    		BT2 = (Button) this.findViewById(R.id.acceptbt);
    		BT2.setVisibility(View.VISIBLE);
    		BT2.setOnClickListener(new ButtonOnClickListener());
       	} else if(pA.getRole().equals("ADMIN")) {
       		BT2 = (Button) this.findViewById(R.id.deletebt);
       		BT2.setVisibility(View.VISIBLE);
       		BT2.setOnClickListener(new ButtonOnClickListener());
       	}
    		
       	participantList = (ListView) this.findViewById(R.id.participantList);
    	//ArrayList<ParticipantAccount> particList = currentAccount.getParticipants();
		TextView particListTitle = new TextView(this); 
		particListTitle.setText("Participantes");
		participantList.addHeaderView(particListTitle);
		//participantList.setAdapter(new myCustomAdapter());
		//participantList.setAdapter(new ArrayAdapter<String>(this, R.layout.listparticaccount_item, particList));
	}
	
	private class ButtonOnClickListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			boolean resultOK = false;
			
			try {
				if(v.getId() == R.id.deletebt) {
					if(currentAccount.deleteAccount(appState.getURL(),appState.getcurrentUser().getFBaccessToken())) {
						Toast.makeText(AccountPageActivity.this, "Account deleted.", Toast.LENGTH_LONG);
						resultOK = true;
					} else
						Toast.makeText(AccountPageActivity.this, "Could not delete account.", Toast.LENGTH_LONG);
				} else if(v.getId() == R.id.acceptbt) {
					if(currentAccount.confirmAccountParticipation(appState.getURL(),appState.getcurrentParticipantAccount().getID(),appState.getcurrentUser().getFBaccessToken())) {
						Toast.makeText(AccountPageActivity.this, "Participation confirmed.", Toast.LENGTH_LONG);
						resultOK = true;
					} else
						Toast.makeText(AccountPageActivity.this, "Could not confirm your participation to this account.", Toast.LENGTH_LONG);
				}
			
			} catch(AccException dAE) {
					Toast.makeText(AccountPageActivity.this, dAE.getMessage(), Toast.LENGTH_LONG);
			} finally {
				if(resultOK) {
					Intent intent = new Intent(AccountPageActivity.this, UserPageActivity.class);
					AccountPageActivity.this.startActivity(intent);
				}
			}
		}
	}
	
	/*private class MyCustomAdapter extends BaseAdapter {
		 
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
 
        /*@Override
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
            	pendingaccounts = true;
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
          /*  return row;
        }
	}*/
}
