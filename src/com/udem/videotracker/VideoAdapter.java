package com.udem.videotracker;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

	/**
	 * Classe qui represente le modele de données d'une video
	 * implémente parcelable pour pouvoir etre transmise entre
	 * deux activités.
	 */
	public static class VideoData implements Parcelable{
		public final String title;
		public final String description;
		public final String auteur;
		public final String url;
		public boolean favori;
		public boolean suppr;
		public int nbVues;
		public final Drawable picture;

		public VideoData(String _title, String _description, String _auteur, String _url, boolean _favori, boolean _suppr, int _nbVues, Drawable _picture) {
			title = _title;
			description = _description;
			auteur = _auteur;
			url = _url;
			favori = _favori;
			nbVues = _nbVues;
			picture = _picture;
			suppr= _suppr;
		}
		
		//TODO constructeur pour que antho fasse ses tests
		public VideoData(String _title, String _description, Drawable _picture) {
			title = _title;
			description = _description;
			auteur = null;
			url = null;
			favori = false;
			suppr = false;
			nbVues = 0;
			picture = _picture;
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
			dest.writeString(url);
			dest.writeByte((byte) (favori ? 1 : 0));     //si favori == true, byte == 1
			dest.writeInt(nbVues);
			Bitmap bitmap = (Bitmap)((BitmapDrawable) picture).getBitmap();
			dest.writeParcelable(bitmap, flags);
		}
		public static final Parcelable.Creator<VideoData> CREATOR = new Parcelable.Creator<VideoData>()
				{
			@Override
			public VideoData createFromParcel(Parcel source)
			{
				return new VideoData(source);
			}

			@Override
			public VideoData[] newArray(int size)
			{
				return new VideoData[size];
			}
				};

		@SuppressWarnings("deprecation")
		public VideoData(Parcel in) {
			this.title = in.readString();
			this.description = in.readString();
			this.auteur = in.readString();
			this.url = in.readString();
			this.favori = in.readByte() != 0;
			this.nbVues = in.readInt();
			Bitmap bitmap = (Bitmap)in.readParcelable(getClass().getClassLoader());
			this.picture = new BitmapDrawable(bitmap);

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
		TextView description_text = (TextView) view.findViewById(R.id.description);
		ImageView icon = (ImageView)view.findViewById(R.id.videoIcon);
		
		
		/*
		 * ImageButton suppression = (ImageButton) view.findViewById(R.id.button_suppression);
		 * if (!data.suppr)
		 * 		suppression.setVisibility(0);
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
