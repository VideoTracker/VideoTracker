package com.udem.videotracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author damota
 */
public class InternetCheckActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_internet);
		new InternetCheck(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private class InternetCheck extends AsyncTask<String, Integer, Object> {

		public InternetCheckActivity activity;
		public Object resultat = null;

		public InternetCheck(InternetCheckActivity a) {
			this.activity = a;
		}

		protected Object doInBackground(String... params) {
			while (!RechercheActivity.isOnline(activity)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return activity;
		}

		protected void onPostExecute(Object resultat) {
			if (resultat != null) {
				Intent intent = new Intent(activity,
						RechercheActivity.class);
				activity.startActivity(intent);
			}

			return;
		}

	}

}
