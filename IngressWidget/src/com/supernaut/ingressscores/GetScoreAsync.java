package com.supernaut.ingressscores;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class GetScoreAsync extends AsyncTask<String, Void, Boolean> {

	private Activity activity;
	private Handler handler;

	public GetScoreAsync(Activity activity, Handler handler){
		this.activity = activity;
		this.handler = handler;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		int code = ScoreWidget.webservice.getScore();

		if (code == 200)
			return true;
		else
			return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		Log.d("getScoreAsync", "result: " + result);
		// if(result)
		// handler.sendEmptyMessage(1);
		// else
		// handler.sendEmptyMessage(0);
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}
}   