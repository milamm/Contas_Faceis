package contasfaceis.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
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

	public JSONObject doGet(String url) {
	
		//BufferedReader in = null;
		JSONObject JSonObject = null;
		BufferedReader reader = null;
		try {		
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				StringBuffer sb = new StringBuffer("");
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				JSonObject = new JSONObject(sb.toString());
			} else {
				Log.e("HttpGet", "Failed to connect to server");
			}
			return JSonObject;
        } catch(Exception e) {
        	Log.e("HttpGet",e.getMessage());
        	JSonObject = null;
        	return JSonObject;
        } finally {
        	if (reader != null) {
        		try {
        			reader.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}       
        }
	}
	
	public JSONObject doPost(String url, List<NameValuePair> pairs) {
		
		//BufferedReader in = null;
		JSONObject JSonObject = null;
		BufferedReader reader = null;
		try {		
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			request.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(request);
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				StringBuffer sb = new StringBuffer("");
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				JSonObject = new JSONObject(sb.toString());
			} else {
				Log.e("HttpPost", "Failed to connect to server");
			}
			return JSonObject;
        } catch(Exception e) {
        	Log.e("HttpPost",e.getMessage());
        	JSonObject = null;
        	return JSonObject;
        } finally {
        	if (reader != null) {
        		try {
        			reader.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}       
        }
	}
	
}