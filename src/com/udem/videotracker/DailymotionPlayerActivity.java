package com.udem.videotracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class DailymotionPlayerActivity extends Activity {

	private WebView webView;
	private String videoHtml;
	private int width = 0;
	private int height = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_dailymotion);
		webView = (WebView) findViewById(R.id.dailymotion_player);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED); 
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
		width = webView.getWidth();
		height = webView.getHeight();
		Log.i("ANTHO", "test"+width);
		Log.i("ANTHO", "test"+height);
		
		videoHtml = "<iframe src=\"http://www.dailymotion.com/embed/video/x1escia_fumer-100-cigarettes-en-1-fois_creation\" width=\""+width+"\" height=\""+height+"\" frameborder=\"0\"></iframe>";
		//webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//webView.getSettings().setJavaScriptEnabled(true);
		//webView.getSettings().setPluginState(PluginState.ON);
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setPluginState(PluginState.ON);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setLoadWithOverviewMode(false); // zoom y/n
		webSettings.setUseWideViewPort(false); // true = like desktop
		//webSettings.setPluginsEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setDatabaseEnabled(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setDomStorageEnabled(true);
		
		webView.loadData(videoHtml, "text/html", "UTF-8");
	}
}