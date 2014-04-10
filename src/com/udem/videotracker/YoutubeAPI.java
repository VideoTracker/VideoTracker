package com.udem.videotracker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.widget.ProgressBar;

public class YoutubeAPI extends BasicAPI {

	String erreur;
	private String title;
	private String description;
	private String thumb;
	private String id = "";
	private Drawable icone;

	private JSONArray array;
	private JSONObject js;
	public static int LENGTH_TITLE = 40;
	public static int LENGTH_DESCRIPTION = 120;

	private ProgressBar progressBar;

	private int progressStep;
	private int progress;
	private int different_apis;

	private HttpEntity page;

	YoutubeAPI(Activity activity, String keywords, ProgressBar _progressBar,
			boolean searchDailymotion, boolean searchYoutube, ArrayList<VideoAdapter.VideoData> videoData, final VideoAdapter adapter) {

		super(keywords);

		different_apis = 0;
		if (searchDailymotion)
			different_apis++;
		if (searchYoutube)
			different_apis++;

		progressBar = _progressBar;

		try {
			if (searchYoutube) {
				try {
					url = "https://www.googleapis.com/youtube/v3/search?part="
							+ URLEncoder.encode("snippet", "UTF-8")
							+ "&q="
							+ keywords
							+ "&key="
							+ URLEncoder.encode(
									"AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU",
									"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Charge le fichier JSON à l'URL donné depuis le web
				page = getHttp(url);

				// Interprète la page retournée comme un fichier JSON encodé en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));

				JSONArray items = js.getJSONArray("items");

				progressStep = (100 / different_apis) / items.length();
				progress = progressStep;

				for (int i = 0; i < items.length(); i++) {

					progressBar.setProgress(progress);

					nombre_resultats++;

					JSONObject row_item = items.getJSONObject(i);
					JSONObject video = row_item.getJSONObject("snippet");
					thumb = video.getJSONObject("thumbnails")
							.getJSONObject("default").getString("url");

					title = shorterString(video.getString("title"),
							LENGTH_TITLE);
					description = shorterString(video.getString("description"),
							LENGTH_DESCRIPTION);
					icone = loadHttpImage(thumb);

					videoData.add(new VideoAdapter.VideoData(title,
							description, icone));
					
					activity.runOnUiThread(new Runnable() {
					     @Override
					     public void run() {

					    	 adapter.notifyDataSetChanged();

					    }
					});

					progress += progressStep;
				}
			}

			// DAILYMOTION

			if (searchDailymotion) {

				url = "https://api.dailymotion.com/videos?fields=id,title,description,thumbnail_url&search=" 
				+ keywords;

				// Charge le fichier JSON à l'URL donné depuis le web
				page = getHttp(url);

				// Interprète la page retournée comme un fichier JSON encodé en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
				array = js.getJSONArray("list");

				progressStep = (100 / different_apis) / array.length();
				progress += progressStep;

				for (int i = 0; i < array.length(); i++) {
					progressBar.setProgress(progress);
					nombre_resultats++;
					JSONObject row = array.getJSONObject(i);
					id = row.getString("id");
					title = shorterString(row.getString("title"), LENGTH_TITLE);

					description = shorterString(
							row.getString("description"),
							LENGTH_DESCRIPTION);
					thumb = row.getString("thumbnail_url");
					icone = loadHttpImage(thumb);

					videoData.add(new VideoAdapter.VideoData(title,
							description, icone));
					
					activity.runOnUiThread(new Runnable() {
					     @Override
					     public void run() {

					    	 adapter.notifyDataSetChanged();

					    }
					});

					progress += progressStep;

				}
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
