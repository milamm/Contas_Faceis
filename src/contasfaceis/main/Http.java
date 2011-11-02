package contasfaceis.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class Http {
	
	private String METHOD;

	public JSONObject doGet(String url) {
	
		METHOD = "doGet";
		
		JSONObject JSon = new JSONObject();
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(request);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream contentIS = entity.getContent();
				String content = convertStreamtoString(contentIS);
			
				//if(content.startsWith("[")) {
					JSon = new JSONObject(content);
				//}
				//else {
					//JSONObject JSonObj = new JSONObject(content);
					//JSon.put(JSonObj);
				//}
					
			} else {
				Log.e(this.getClass().getSimpleName()+"/"+METHOD, "Failed to connect to server");
			}
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,e.getMessage());
		}
		return JSon;
	}
	
	public JSONObject doPost(String url, List<NameValuePair> pairs) {
		
		METHOD = "doPost";
		
		JSONObject JSon = new JSONObject();
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		
		try {
			request.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(request);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream contentIS = entity.getContent();
				String content = convertStreamtoString(contentIS);
								
				//if(content.startsWith("[")) {
					JSon = new JSONObject(content);
				/*}
				else {
					JSONObject JSonObj = new JSONObject(content);
					JSon.put(JSonObj);
				}*/
			} else {
				Log.e(this.getClass().getSimpleName()+"/"+METHOD, "Failed to connect to server");
			}
        } catch (Exception e) {
			Log.e(this.getClass().getSimpleName()+"/"+METHOD,e.getMessage());
		}
		return JSon;
	}
	
	public String convertStreamtoString(InputStream is) {
		BufferedReader reader = new BufferedReader( new InputStreamReader(is));
		String line = null;
		StringBuffer sb = new StringBuffer("");
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName()+"/"+METHOD,e.getMessage());
			}
		}
		return sb.toString();
	}
	
}