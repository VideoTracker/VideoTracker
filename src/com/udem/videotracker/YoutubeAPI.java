package com.udem.videotracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Log;

public class YoutubeAPI {

	String temperature;
	String conditions;
	String ville;
	CharSequence depuis;
	Drawable icone;
	String nombre_resultats;

	// Sera null s'il n'y a pas d'erreur
	String erreur;

	YoutubeAPI(String keywords) {
		erreur = null;

		String url = null;
			try {
				url = "https://www.googleapis.com/youtube/v3/search?part="
						+ URLEncoder.encode("snippet", "UTF-8")
						+ "&q="
						+ URLEncoder.encode(keywords, "UTF-8") 
						+ "&key=" 
						+ URLEncoder.encode("AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		Log.i("DAILYMOTION", url);

		try {
			// Charge le fichier JSON à l'URL donné depuis le web
			HttpEntity page = getHttp(url);

			// Interprète la page retournée comme un fichier JSON encodé en
			// UTF-8
			JSONObject js = new JSONObject(EntityUtils.toString(page,
					HTTP.UTF_8));

			// Le format de ce JSON stocke les informations actuelles dans un
			// sous-objet "current_observation"
			// JSONObject obs = js.getJSONObject("current_observation");

			// nombre_resultats = js.getString("total");

			String title;
			String description;
			Log.i("DAILYMOTION",js.toString(1));
			JSONArray items = js.getJSONArray("items");
			for (int i = 0; i < items.length(); i++) {
				
				JSONObject row_item = items.getJSONObject(i);
				JSONArray video = row_item.getJSONArray("items");
				
				for (int j = 0; j < video.length(); j++) {
					
					JSONObject row_video = items.getJSONObject(i);
					title = row_video.getString("title");
					description = row_video.getString("description");
					Log.i("YOUTUBE", "video num " + i + ": " + title + " "
							+ description);
				}
			}

			// conditions = obs.getString("weather");
			// ville = obs.getJSONObject("display_location").getString("full");

			// Le temps d'observation est donné sous forme d'une "époque UNIX",
			// le nombre de secondes depuis le 1er janvier 1970
			// long epoch = Long.parseLong(obs.getString("observation_epoch"));
			// getRelativeTimeSpanString transforme un temps en milisecondes en
			// un temps relatif, par exemple "il y a une heure"
			// depuis =
			// android.text.format.DateUtils.getRelativeTimeSpanString(epoch*1000);

			// String iconName = obs.getString("icon");
			// if( iconName!=null ) {
			// Les icônes ont tous une variante nt_<nom>.gif pour la nuit, mais
			// nous les ignorons
			// icone = loadHttpImage("http://icons.wxug.com/i/c/j/" + iconName +
			// ".gif");
			// }
		} catch (ClientProtocolException e) {
			erreur = "Erreur HTTP (protocole) :" + e.getMessage();
		} catch (IOException e) {
			erreur = "Erreur HTTP (IO) :" + e.getMessage();
		} catch (ParseException e) {
			erreur = "Erreur JSON (parse) :" + e.getMessage();
		} catch (JSONException e) {
			erreur = "Erreur JSON :" + e.getMessage();
		}
	}

	/*
	 * Méthode utilitaire qui permet de rapidement charger et obtenir une page
	 * web depuis l'internet.
	 */
	private HttpEntity getHttp(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();
	}

	/*
	 * Méthode utilitaire qui permet d'obtenir une image depuis une URL.
	 */
	private Drawable loadHttpImage(String url) throws ClientProtocolException,
			IOException {
		InputStream is = getHttp(url).getContent();
		Drawable d = Drawable.createFromStream(is, "src");
		return d;
	}

}
