package com.udem.videotracker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.udem.videotracker.VideoAdapter.Source;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Log;
import android.view.View;


public class VideoAPI extends BasicAPI {

	String erreur;
	private String title;
	private String description;
	private String thumb;
	private String id="";
	private String url_video_youtube="https://m.youtube.com/watch?v=";
	private String url_video_daily="https://m.youtube.com/watch?v=";
	private Drawable icone;

	private JSONArray array;
	private JSONObject js;
	public static int LENGTH_TITLE = 40;
	public static int LENGTH_DESCRIPTION = 120;

	private int nbVues;
	private int like_count;
	private Date datePublication = new Date();
	private boolean notificationDone = false;
	private VideoAdapter.VideoData video;

	final String pattern = "yyyy-MM-dd'T'hh:mm:ss";
	final SimpleDateFormat dateTmp = new SimpleDateFormat(pattern);


	private HttpEntity page;


	VideoAPI(final VideoActivity activity, String ID, boolean searchDailymotion, boolean searchYoutube) throws InterruptedException, java.text.ParseException {

		super("");
		Runnable threadNotification = new Runnable() {
			@Override
			public void run() {
				activity.video_titre.setText(video.title);
				activity.video_url.setText(video.url_video);
				activity.video_description.setText(video.description);
				activity.video_nbVues.setText(""+video.nbVues);
				activity.favori.setChecked(video.favori);
				activity.image.setImageDrawable(video.picture);
				activity.button_play.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						if (v.getId() == R.id.button_play){
							Log.i("ANTHO","teeest");
							Intent i = new Intent(activity, DailymotionPlayerActivity.class);
							String path = video.url_video;
							i.putExtra("url", path);
							activity.startActivity(i);
						}
					}
				});
				notificationDone = true;
			}
		};

		try {
			if (searchYoutube) {
				try {
					url = "https://www.googleapis.com/youtube/v3/videos?id="
							+ "7lCDEYXw3mM"
							+ "&key="
							+ URLEncoder.encode(
									"AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU",
									"UTF-8")
							+"&fields=items(id,snippet,statistics)&part=snippet,statistics";
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				// Charge le fichier JSON à l'URL donné depuis le web
				page = getHttp(url);

				// Interprète la page retournée comme un fichier JSON encodé en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));

				JSONArray items = js.getJSONArray("items");
				Log.i("toto", js.toString());
				

				JSONObject row_item = items.getJSONObject(0);
				JSONObject snippet = row_item.getJSONObject("snippet");
				JSONObject stat = row_item.getJSONObject("statistics");
				thumb = snippet.getJSONObject("thumbnails")
						.getJSONObject("default").getString("url");
				title = shorterString(snippet.getString("title"),
						LENGTH_TITLE);
				description = shorterString(snippet.getString("description"),
						LENGTH_DESCRIPTION);
				icone = loadHttpImage(thumb);
				url_video_youtube += row_item.getString("id");
				datePublication = dateTmp.parse(snippet.getString("publishedAt"));
				if(icone==null)
					Log.i("TAGA","nuuehfjefheifjhehf");
				video = new VideoAdapter.VideoData(title, description, "", url_video_youtube, thumb, false, false, 0, 0, icone, datePublication, Source.YOUTUBE);

				synchronized( threadNotification ) {
					activity.runOnUiThread(threadNotification) ;
					while(!notificationDone)
						Thread.sleep(100) ;
					notificationDone = false;
				}


			}

			// DAILYMOTION

			/*if (searchDailymotion) {

				url = "https://api.dailymotion.com/videos?fields=id,title,description,thumbnail_url,created_time,views_total&search=";

				// Charge le fichier JSON à l'URL donné depuis le web
				page = getHttp(url);

				// Interprète la page retournée comme un fichier JSON encodé en
				// UTF-8
				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
				array = js.getJSONArray("list");



				JSONObject row = array.getJSONObject(0);
				id = row.getString("id");
				title = shorterString(row.getString("title"), LENGTH_TITLE);

				description = shorterString(row.getString("description"),
						LENGTH_DESCRIPTION);
				thumb = row.getString("thumbnail_url");
				icone = loadHttpImage(thumb);

				datePublication = new Date(row.getInt("created_time"));
				nbVues = row.getInt("views_total");

				video = new VideoAdapter.VideoData(title, description, "", url_video_daily, thumb, false, false, nbVues, 0, icone, datePublication, Source.DAILYMOTION);


				synchronized( threadNotification ) {
					activity.runOnUiThread(threadNotification) ;

					while(!notificationDone)
						Thread.sleep(100); // unlocks myRunable while waiting
					notificationDone = false;
				}

			}*/

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