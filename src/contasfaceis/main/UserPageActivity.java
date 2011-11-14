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
	private AccCustomAdapter listAdapter;

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
		logoutButton.init(this, appState.getFacebook());

		createAccountbt = (Button) this.findViewById(R.id.createAccount);
		createAccountbt.setOnClickListener(new CreaAccOnClickListener());

		particAccList = currentUser
				.getUserAccountsParticipationsFromServer(appState.getURL());
		appState.setparticipantAccountRelations(particAccList);
		if (checkforPendingAccounts())
			Toast.makeText(UserPageActivity.this,
					"Voc� foi convidado para novas contas! ", 10).show();

		accountListlt = (ListView) this.findViewById(R.id.accountList);
		TextView listtitleTV = new TextView(this.getApplicationContext());
		listtitleTV.setText("Suas Contas");
		listtitleTV.setTextSize(20);
		accountListlt.addHeaderView(listtitleTV);
		listAdapter = new AccCustomAdapter();
		accountListlt.setAdapter(listAdapter);

		accountListlt.setOnItemClickListener(new ListAccOnItemClickListener());
	}

	private boolean checkforPendingAccounts() {
		for (int i = 0; i < particAccList.size(); i++) {
			if (particAccList.get(i).getStatus().equals("PENDING"))
				return true;
		}
		return false;
	}

	private class ListAccOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ParticipantAccount pA = particAccList.get(position - 1);
			appState.setcurrentAccount(pA.getAccount());
			appState.setcurrentParticipantAccount(pA);

			Intent intent = new Intent(UserPageActivity.this,
					AccountPageActivity.class);
			UserPageActivity.this.startActivity(intent);
		}
	}

	private class AccCustomAdapter extends BaseAdapter {

		private ArrayList<ParticipantAccount> mData = new ArrayList<ParticipantAccount>();

		public AccCustomAdapter() {
			mData = particAccList;
		}

		public int getCount() {
			return mData.size();
		}

		public ParticipantAccount getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("getView " + position + " " + convertView
					+ "Size " + this.getCount());
			// LayoutInflater mInflater =
			// (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TextView TV = null;
			ParticipantAccount particAcc = mData.get(position);

			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.listuseraccounts_item, parent,
						false);
			}

			TV = (TextView) row;
			TV.setText(particAcc.getAccount().getName());
			// TV.setId(particAcc.getID());
			if (particAcc.getStatus().equals("CONFIRMED")) {
				TV.setTypeface(Typeface.DEFAULT);
				TV.setTextColor(Color.WHITE);
			} else {
				TV.setTypeface(Typeface.DEFAULT_BOLD);
				TV.setTextColor(Color.RED);
				// pendingaccounts = true;
			}
			/*
			 * if (convertView == null) { if(particStatus.equals("CONFIRMED")) {
			 * convertView = mInflater.inflate(R.layout.listuseraccounts_item,
			 * null); TV =
			 * (TextView)convertView.findViewById(R.id.useraccconfirmed); } else
			 * { convertView =
			 * mInflater.inflate(R.layout.listuseraccountspending_item, null);
			 * TV = (TextView)convertView.findViewById(R.id.useraccpending); }
			 * //holder = new ViewHolder(); //holder.textView =
			 * (TextView)convertView.findViewById(R.id.text);
			 * //convertView.setTag(holder); } else {
			 */
			// holder = (ViewHolder)convertView.getTag();

			// }

			// holder.textView.setText(mData.get(position));
			// return convertView;
			return row;
		}
	}

	private final class CreaAccOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(UserPageActivity.this,
					CreateAccountActivity.class);
			UserPageActivity.this.startActivity(intent);
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(UserPageActivity.this, "Logging out...",
					Toast.LENGTH_LONG).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(UserPageActivity.this, "You have logged out! ",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(UserPageActivity.this,
					MainActivity.class);
			UserPageActivity.this.startActivity(intent);
		}
	}

}