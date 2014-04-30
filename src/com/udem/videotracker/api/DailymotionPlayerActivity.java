package com.udem.videotracker.api;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class DailymotionPlayerActivity extends Activity {

	HTML5WebView mWebView;
	private int width = 0;
	private int height = 0;
	private Bundle savedInstanceState;
	private String url = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        url = getIntent().getStringExtra("url");
        mWebView = new HTML5WebView(this);

        setContentView(mWebView.getLayout());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }
    
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
		width = mWebView.getWidth();
		height = mWebView.getHeight();
		
		if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else { 
        	String videoHtml = makeUrl(url);
            mWebView.loadData(videoHtml, "text/html", "UTF-8");
        }
	}

    @Override
    public void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
                mWebView.hideCustomView();
            //  mWebView.goBack();
                //mWebView.goBack();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    
    public String makeUrl(String url){
    	String resultat = "";
    	resultat+= "<iframe src=\"";
    	resultat+= url + "\" ";
    	resultat+="width=\""+width+"\" height=\""+height+"\" frameborder=\"0\"></iframe>";
    	return resultat;
    }
}