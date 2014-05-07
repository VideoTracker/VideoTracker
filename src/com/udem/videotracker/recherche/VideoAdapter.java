package com.udem.videotracker.recherche;

import java.util.Date;
import java.util.List;

import com.udem.videotracker.R;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {

	public enum Source {
		YOUTUBE, DAILYMOTION
	}

	/**
	 * Classe qui represente le modele de données d'une video implémente
	 * parcelable pour pouvoir etre transmise entre deux activités.
	 */
	public static class VideoData {
		public final String title;
		public final String description;
		public final String url_video;
		public final String url_image;
		public int nbVues;
		public int like_count;
		public Drawable picture;
		public Date datePublication = new Date();
		public Source sourceVideo;
		public int id_video;
		public String id_url;



		public VideoData(String title, String description,String url_video, String url_image, int nbVues, int like_count, Drawable picture,
				Date datePublication, Source sourceVideo, String id_url) {
			this.title = title;
			this.description = description;
			this.url_video = url_video;
			this.url_image = url_image;
			this.nbVues = nbVues;
			this.like_count = like_count;
			this.picture = picture;
			this.datePublication = datePublication;
			this.sourceVideo = sourceVideo;
			this.id_url = id_url;
		}
		
		public VideoData(String title, String description,String url_video, String url_image, int nbVues, int like_count, Drawable picture,
				Date datePublication, String sourceVideo, int id_video,String id_url){
			this(title, description, url_video, url_image, nbVues, like_count, picture,
					datePublication, Source.DAILYMOTION, id_url);	
			if(sourceVideo.equals("YOUTUBE")){
				this.sourceVideo=Source.YOUTUBE;
			}

			this.id_video=id_video;
			
		}
	}

	private Context _context;
	private List<VideoData> _data;

	@Override
	public int getCount() {
		if (_data != null)
			return _data.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int at) {
		if (_data != null && at >= 0 && at < _data.size())
			return _data.get(at);
		else
			return null;
	}

	@Override
	public long getItemId(int at) {
		return at;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		VideoData data = _data.get(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_item, parent, false);
		}

		TextView title_text = (TextView) view.findViewById(R.id.title);
		TextView description_text = (TextView) view
				.findViewById(R.id.description);
		ImageView icon = (ImageView) view.findViewById(R.id.videoIcon);
		ImageView source = (ImageView) view.findViewById(R.id.website);
		if (data.sourceVideo == Source.DAILYMOTION) {
			source.setImageResource(R.drawable.dailymotion);
		} else {
			source.setImageResource(R.drawable.youtube);
		}
		title_text.setText(data.title);
		description_text.setText(data.description);
		icon.setImageDrawable(data.picture);

		if (position % 2 == 0)
			view.setBackgroundColor(Color.LTGRAY);
		else
			view.setBackgroundColor(Color.WHITE);

		return view;
	}

	public VideoAdapter(Context context, List<VideoData> data) {
		_context = context;
		_data = data;
	}

}
