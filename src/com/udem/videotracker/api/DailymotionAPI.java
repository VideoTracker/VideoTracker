package com.udem.videotracker.api;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

public class DailymotionAPI extends BasicAPI{

	DailymotionAPI(String keywords) {

		super(keywords);
		
		url = "https://api.dailymotion.com/videos?search=" + keywords;

		try {
			// Charge le fichier JSON à l'URL donné depuis le web
			HttpEntity page = getHttp(url);

			// Interprète la page retournée comme un fichier JSON encodé en
			// UTF-8
			JSONObject js = new JSONObject(EntityUtils.toString(page,
					HTTP.UTF_8));

			nombre_resultats = js.getInt("total");
			
			String title;
			String owner;
			JSONArray array = js.getJSONArray("list");
			for (int i = 0; i < array.length(); i++) {
			    JSONObject row = array.getJSONObject(i);
			    title = row.getString("title");
			    owner = row.getString("owner");
			    Log.i("DAILYMOTION", "video num " + i +": " + title + " " + owner);
			}
			
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

}
