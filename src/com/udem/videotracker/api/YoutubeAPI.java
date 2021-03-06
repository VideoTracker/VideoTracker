package com.udem.videotracker.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.udem.videotracker.recherche.VideoAdapter;
import com.udem.videotracker.recherche.VideoAdapter.Source;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

@SuppressLint("SimpleDateFormat")
public class YoutubeAPI extends BasicAPI {

	public String erreur;
	private String title;
	private String description;
	private String thumb;
	private String id_video="";
	private Drawable icone;

	private JSONArray array;
	private JSONObject js;
	public static int LENGTH_TITLE = 40;
	public static int LENGTH_DESCRIPTION = 120;
	
	private int res_youtube;
	@SuppressWarnings("unused")
	private int res_daily;

	private ProgressBar progressBar;

	private int progressStep;
	private int progress;
	private int different_apis;
	private int length;
	private int nbVues;
	private Date datePublication = new Date();

	final String pattern = "yyyy-MM-dd'T'hh:mm:ss";
	final SimpleDateFormat dateTmp = new SimpleDateFormat(pattern);

	private HttpEntity page;

	private boolean notificationDone = false;

	public YoutubeAPI(Activity activity, String keywords, ProgressBar _progressBar,
			boolean searchDailymotion, boolean searchYoutube,
			ArrayList<VideoAdapter.VideoData> videoData,
			final VideoAdapter adapter) throws InterruptedException, java.text.ParseException {

		super(keywords);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		res_youtube=Integer.parseInt(prefs.getString("yt_display", "10"));

		different_apis = 0;
		if (searchDailymotion)
			different_apis++;
		if (searchYoutube)
			different_apis++;

		progressBar = _progressBar;

		Runnable threadNotification = new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				notificationDone = true;
			}
		};

		try {
			if (searchYoutube) {
				try {
					url = "https://www.googleapis.com/youtube/v3/search?part="
							+ URLEncoder.encode("snippet", "UTF-8")
							+ "&maxResults="
							+ res_youtube
							+ "&order=relevance"
							+ "&type=video"
							+ "&q="
							+ keywords
							+ "&key="
							+ URLEncoder.encode(
									"AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU",
									"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				// Charge le fichier JSON � l'URL donn� depuis le web
				page = getHttp(url);

				// Interpr�te la page retourn�e comme un fichier JSON encod� en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));

				JSONArray items = js.getJSONArray("items");

				length = items.length();

				progressStep = (100 / different_apis)
						/ (length == 0 ? 1 : length);
				progress = progressStep;

				for (int i = 0; i < length; i++) {

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
					id_video = row_item.getJSONObject("id").getString("videoId");

					datePublication = dateTmp.parse(video.getString("publishedAt"));

					videoData.add(new VideoAdapter.VideoData(title, description, "", thumb, 0, 0, icone, datePublication, Source.YOUTUBE,id_video));

					synchronized( threadNotification ) {
						activity.runOnUiThread(threadNotification) ;
						while(!notificationDone)
							Thread.sleep(100) ; // unlocks myRunable while waiting
						notificationDone = false;
					}

					progress += progressStep;
				}
			}

			// DAILYMOTION

			if (searchDailymotion) {

				url = "https://api.dailymotion.com/videos?fields=id,title,description,thumbnail_url,created_time,views_total&search="
						+ keywords;

				// Charge le fichier JSON � l'URL donn� depuis le web
				page = getHttp(url);

				// Interpr�te la page retourn�e comme un fichier JSON encod� en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
				array = js.getJSONArray("list");

				length = array.length();
				progressStep = (100 / different_apis)
						/ (length == 0 ? 1 : length);
				;
				progress += progressStep;

				for (int i = 0; i < length; i++) {
					progressBar.setProgress(progress);
					nombre_resultats++;
					JSONObject row = array.getJSONObject(i);
					id_video = row.getString("id");
					title = shorterString(row.getString("title"), LENGTH_TITLE);

					description = shorterString(row.getString("description"),
							LENGTH_DESCRIPTION);
					thumb = row.getString("thumbnail_url");
					icone = loadHttpImage(thumb);

					datePublication = new Date(row.getInt("created_time"));
					nbVues = row.getInt("views_total");

					videoData.add(new VideoAdapter.VideoData(title, description, "", thumb, nbVues, 0, icone, datePublication, Source.DAILYMOTION,id_video));


					synchronized( threadNotification ) {
						activity.runOnUiThread(threadNotification) ;

						while(!notificationDone)
							Thread.sleep(100); // unlocks myRunable while waiting
						notificationDone = false;
					}

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
