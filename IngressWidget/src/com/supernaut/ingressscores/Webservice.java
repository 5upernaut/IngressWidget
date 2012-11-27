package com.supernaut.ingressscores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.util.Log;

public class Webservice {
	private DefaultHttpClient httpClient;
	private BasicHttpParams httpParameters;
	
	public Webservice() {
		httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10 * 1000);
		
		httpClient = new DefaultHttpClient(httpParameters);
		System.setProperty("http.keepAlive", "true");
	}
	
	public int getScore() {
		try {
			String url = "http://ingressapi.herokuapp.com/score.json";
			
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(url));
			// httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			
			int code = response.getStatusLine().getStatusCode();
			// String responseBody = EntityUtils.toString(response.getEntity());
			// Log.d("responseBody: ", responseBody);
			JSONObject json = responseToJSON(response);
			Log.d("getScore", json.toString());
			
			String lastUpdated = json.getString("LastUpdated");
			JSONObject result = json.getJSONObject("Result");
			ScoreWidget.resistance = result.getString("Resistance");
			String enlightened = result.getString("Enlightened");
			
			Log.d("getScore", "lastUpdated " + lastUpdated + " resistance: " + ScoreWidget.resistance + " enlightened: " + enlightened);
			
			return code;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("getScore ", e.toString());
			return 404;
		}
	}
	
	private JSONObject responseToJSON(HttpResponse response) throws IOException {
		InputStream instream = null;
		StringBuilder s = new StringBuilder();
		
		try {
			instream = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			BufferedReader reader;
			
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
				reader = new BufferedReader(new InputStreamReader(instream));
			} else {
				reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
			}
			String sResponse = "";
			
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			
			JSONObject json = new JSONObject(s.toString());
			instream.close();
			return json;
		} catch (Exception e) {
			Log.e("responseToJSON error", e.toString());
			Log.e("response", s.toString());
			if (instream != null)
				instream.close();
			return null;
		}
	}

}
