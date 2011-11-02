package contasfaceis.main;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;


public class MainActivity extends Activity {
    
  //FB App id 
    public static final String APP_ID = "287456104617729";

    private LoginButton mLoginButton;
    private TextView mText;

    private ContasFaceis appState;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appState = (ContasFaceis) MainActivity.this.getApplication();
        
        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " +
                    "specified before running this example: see Example.java");
        }

        setContentView(R.layout.main);
        mLoginButton = (LoginButton) this.findViewById(R.id.login);
        mText = (TextView) this.findViewById(R.id.txt);

        Facebook mFacebook = new Facebook(APP_ID);
        appState.setFacebook(mFacebook);
        
        new AsyncFacebookRunner(appState.getFacebook());

        SessionStore.restore(appState.getFacebook(), this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        Context context = getApplicationContext();
        mLoginButton.init(this, appState.getFacebook(),context);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
    	appState.getFacebook().authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	String accesstoken = appState.getFacebook().getAccessToken();
        	User user = new User(accesstoken);
        	
        	appState.setcurrentUser(user);
        	JSONObject JSONUser = appState.getcurrentUser().getUserInformationFromServer(appState.getURL()); 
        	if(appState.getcurrentUser().setParameters(JSONUser)) {
        		Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
            	MainActivity.this.startActivity(intent);
          	} else
        		mText.setText("Usuario desconhecido.");
        }

        public void onAuthFail(String error) {
            mText.setText("Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            mText.setText("Logging out...");
        }

        public void onLogoutFinish() {
            mText.setText("You have logged out! ");
        }
    }
    
}