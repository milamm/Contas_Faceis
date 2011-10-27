package contasfaceis.main;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private Facebook mFacebook;
    
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

        mFacebook = new Facebook(APP_ID);
        new AsyncFacebookRunner(mFacebook);

        SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton.init(this, mFacebook);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	String accesstoken = mFacebook.getAccessToken();
        	User user = new User(accesstoken);
        	
        	appState.setcurrentUser(user);
        	
        	JSONObject JSonObject = getUserInformation(appState.currentUser.getFBaccessToken());
        	
        	try {
        		String fname = JSonObject.getString("firstName");
            	String lname = JSonObject.getString("lastName");
            	String email = JSonObject.getString("email");
            	appState.currentUser.setParameters(fname, lname, email);
            	
        		if(fname != null) {
        			Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
            		MainActivity.this.startActivity(intent);
        			//mText.setText("Bem-vindo, " + currentUser.firstName);
        		} else
        			mText.setText("Falha na conexao com o servidor.");
        	} catch(JSONException JSe) {
        		Log.e("JSON",JSe.getMessage());
        	}
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
    
    private JSONObject getUserInformation(String FBaccessToken) {
    	JSONObject JSonObject = null;
    	Http http = new Http();
    	try {
    		JSonObject = http.doGet(appState.getURL()+"/user/"+FBaccessToken);
    		return JSonObject;
    	} catch(Exception e) {
    		Log.e("HttpGet",e.getMessage());
    		return JSonObject;
    	}
    }
}