package com.supernaut.ingressscores;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

public class ScoreWidget extends AppWidgetProvider {
	public static Webservice webservice;
	public static String resistance = "null";
	DateFormat df = new SimpleDateFormat("hh:mm:ss");

	public ScoreWidget() {
		webservice = new Webservice();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteViews;
		ComponentName scoreWidget;
		Log.d("onUpdate", "onUpdate");
		
		new GetScoreAsync(null, null).execute();
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		scoreWidget = new ComponentName(context, ScoreWidget.class);
		// remoteViews.setTextViewText(R.id.score_tv, df.format(new Date()));
		remoteViews.setTextViewText(R.id.score_tv, resistance);

		appWidgetManager.updateAppWidget(scoreWidget, remoteViews);
	}

}
