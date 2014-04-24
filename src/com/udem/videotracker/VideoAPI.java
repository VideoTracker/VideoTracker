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
	private String url_video;
	private Drawable icone;

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

	VideoAPI(final VideoActivity activity, String ID, Source src)
			throws InterruptedException, java.text.ParseException {

		super("");
		Runnable threadNotification = new Runnable() {
			@Override
			public void run() {
				activity.video_titre.setText(video.title);
				activity.video_url.setText(video.url_video);
				activity.video_description.setText(video.description);
				activity.video_nbVues.setText("Nombre de vues : "
						+ video.nbVues);
				activity.video_like_count.setText("	Nombre de likes :"
						+ video.like_count);
				activity.favori.setChecked(video.favori);
				activity.image.setImageDrawable(video.picture);
				activity.button_play
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (v.getId() == R.id.button_play) {
									String path = activity.video_url.getText().toString();
									if (video.sourceVideo == Source.DAILYMOTION) {
										Intent i = new Intent(activity,
												DailymotionPlayerActivity.class);
										i.putExtra("url", path);
										activity.startActivity(i);
									} else {
										Intent i = new Intent(activity,
												YoutubePlayerActivity.class);
										String video_id = path.split("v=")[1];
										int andPosition = video_id.indexOf("&");
										if(andPosition != -1) {
										  video_id = video_id.substring(0, andPosition);
										}
										i.putExtra("url", video_id);
										activity.startActivity(i);
									}
								}
							}
						});
				notificationDone = true;
			}
		};

		try {
			switch (src) {
			case YOUTUBE:
				try {
					url = "https://www.googleapis.com/youtube/v3/videos?id="
							+ ID
							+ "&key="
							+ URLEncoder.encode(
									"AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU",
									"UTF-8")
							+ "&fields=items(id,snippet,statistics)&part=snippet,statistics";
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
				title = shorterString(snippet.getString("title"), LENGTH_TITLE);
				description = snippet.getString("description");
				icone = loadHttpImage(thumb);
				url_video = "https://m.youtube.com/watch?v="
						+ row_item.getString("id");
				datePublication = dateTmp.parse(snippet
						.getString("publishedAt"));
				nbVues = (int) stat.getLong("viewCount");
				like_count = (int) stat.getLong("likeCount");
				video = new VideoAdapter.VideoData(title, description, "",
						url_video, thumb, false, false, nbVues, like_count,
						icone, datePublication, Source.YOUTUBE);

				synchronized (threadNotification) {
					activity.runOnUiThread(threadNotification);
					while (!notificationDone)
						Thread.sleep(100);
					notificationDone = false;
				}
				break;
			case DAILYMOTION:

				url = "https://api.dailymotion.com/video/"+ID+"?fields=title,description,thumbnail_url,created_time,views_total,ratings_total,url";

				page = getHttp(url);

				js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));

				title = shorterString(js.getString("title"), LENGTH_TITLE);

				description = js.getString("description");
				thumb = js.getString("thumbnail_url");
				icone = loadHttpImage(thumb);
				url_video = js.getString("url");
				like_count = js.getInt("ratings_total");

				datePublication = new Date(js.getInt("created_time"));
				nbVues = js.getInt("views_total");

				video = new VideoAdapter.VideoData(title, description, "",
						url_video, thumb, false, false, nbVues, like_count,
						icone, datePublication, Source.DAILYMOTION);

				synchronized (threadNotification) {
					activity.runOnUiThread(threadNotification);

					while (!notificationDone)
						Thread.sleep(100); // unlocks myRunable while waiting
					notificationDone = false;
				}

				break;
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