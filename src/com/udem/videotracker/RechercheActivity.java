package com.udem.videotracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Activité principale de l'application, elle est lancée
 * au démarrage. Elle permet de faire une recherche selon
 * les checkbox cochées.
 * @author rpiche
 *
 */
public class RechercheActivity extends Activity implements OnClickListener {
	private EditText text_search;
	private ImageButton button_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recherche);
		text_search = (EditText) findViewById(R.id.text_search);
		button_search = (ImageButton) findViewById(R.id.button_search);
		button_search.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_recherche, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_playlist:
			intent = new Intent(RechercheActivity.this, PlaylistActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_a_propos:
			intent = new Intent(RechercheActivity.this, ProposActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_aide:
			intent = new Intent(RechercheActivity.this, VideoActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_preferences:
			intent = new Intent(RechercheActivity.this,
					PreferencesActivity.class);
			startActivity(intent);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if (v == button_search) {
			String search = "";
			CheckBox checkD = (CheckBox) findViewById(R.id.checkbox_dailymotion);
			CheckBox checkY = (CheckBox) findViewById(R.id.checkbox_youtube);
			if ((!checkD.isChecked()) && (!checkY.isChecked())) {
				Toast.makeText(
						getApplicationContext(),
						"Veuillez s�lectionner au moins un moteur de recherche",
						Toast.LENGTH_SHORT).show();
			}
			search += text_search.getText().toString();
			Intent intent = new Intent(RechercheActivity.this,
					ResultActivity.class);
			intent.putExtra("SEARCH", search);
			intent.putExtra("SEARCH_DAILYMOTION", checkD.isChecked());
			intent.putExtra("SEARCH_YOUTUBE", checkY.isChecked());
			startActivity(intent);
		}
	}

}
