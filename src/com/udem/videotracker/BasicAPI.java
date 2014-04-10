package com.udem.videotracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public abstract class BasicAPI {
	
	protected int nombre_resultats;
	protected String url;

	// Sera null s'il n'y a pas d'erreur
	String erreur;

	BasicAPI(String keywords) {
		erreur = null;
		url = "";
		nombre_resultats = 0;
	}

	/*
	 * Méthode utilitaire qui permet de rapidement charger et obtenir une page
	 * web depuis l'internet.
	 */
	protected HttpEntity getHttp(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();
	}

	/*
	 * Méthode utilitaire qui permet d'obtenir une image depuis une URL.
	 */
	@SuppressWarnings("deprecation")
	protected Drawable loadHttpImage(String url) throws ClientProtocolException,
			IOException {
		InputStream is = getHttp(url).getContent();
		Drawable d = Drawable.createFromStream(is, "src");
		Bitmap b = ((BitmapDrawable)d).getBitmap();
	    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 350, 450, false);
	    d = new BitmapDrawable(bitmapResized);
		return d;
	}
	
	protected String shorterString(String description, int n)
	{
	    if (description.length() <= n){
	       return(description);
	       }
	    else{
	        return(description.substring(0, n)) + "...";
	    }
	}


}
