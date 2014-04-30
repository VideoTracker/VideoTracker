package com.udem.videotracker.recherche;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.udem.videotracker.R;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
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
	public static class VideoData implements Parcelable {
		public final String title;
		public final String description;
		public final String auteur;
		public final String url_video;
		public final String url_image;
		public boolean favori;
		public boolean suppr;
		public int nbVues;
		public int like_count;
		public Drawable picture;
		public Date datePublication = new Date();
		public Source sourceVideo;



		public VideoData(String title, String description, String auteur,
				String url_video, String url_image, boolean favori,
				boolean suppr, int nbVues, int like_count, Drawable picture,
				Date datePublication, Source sourceVideo) {
			this.title = title;
			this.description = description;
			this.auteur = auteur;
			this.url_video = url_video;
			this.url_image = url_image;
			this.nbVues = nbVues;
			this.like_count = like_count;
			this.picture = picture;
			this.datePublication = datePublication;
			this.sourceVideo = sourceVideo;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(title);
			dest.writeString(description);
			dest.writeString(auteur);
			dest.writeString(url_video);
			dest.writeString(url_image);
			dest.writeInt(like_count);
			dest.writeInt(nbVues);
			dest.writeLong(datePublication.getTime());
		}

		public static final Parcelable.Creator<VideoData> CREATOR = new Parcelable.Creator<VideoData>() {
			@Override
			public VideoData createFromParcel(Parcel source) {
				try {
					return new VideoData(source);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public VideoData[] newArray(int size) {
				return new VideoData[size];
			}
		};

		public VideoData(Parcel in) throws IllegalStateException, IOException {
			this.title = in.readString();
			this.description = in.readString();
			this.auteur = in.readString();
			this.url_video = in.readString();
			this.url_image = in.readString();
			this.like_count = in.readInt();
			this.nbVues = in.readInt();
			this.datePublication = new Date(in.readLong());
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

		/*
		 * ImageButton suppression = (ImageButton)
		 * view.findViewById(R.id.button_suppression); if (!data.suppr)
		 * suppression.setVisibility(0);
		 */

		title_text.setText(data.title);
		description_text.setText(data.description);
		icon.setImageDrawable(data.picture);

		if (position % 2 == 0)
			view.setBackgroundColor(Color.argb(255, 20, 20, 20));
		else
			view.setBackgroundColor(Color.BLACK);

		return view;
	}

	public VideoAdapter(Context context, List<VideoData> data) {
		_context = context;
		_data = data;
	}

}
