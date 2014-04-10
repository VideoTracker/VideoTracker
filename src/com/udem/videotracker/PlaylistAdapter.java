package com.udem.videotracker;

import java.sql.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter permettant la gestion de l'affichage des playlist
 * @author rpiche
 *
 */
public class PlaylistAdapter extends BaseAdapter {

	/**
	 * Classe statique représentant les données des playlists
	 * @author rpiche
	 *
	 */
	public static class PlaylistData {
		public final String title;
		public int nbVideos;
		public Date dateCreation;
		
		public int id;

		public PlaylistData(String _title, int _nbVideos, Date _dateCreation, int _id){
			title = _title;
			nbVideos = _nbVideos;
			dateCreation = _dateCreation;
			id = _id;
		}
	}

	private Context _context;
	private List<PlaylistData> _data;

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
		PlaylistData data = _data.get(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_item_playlist, parent, false);
		}

		TextView title_text = (TextView) view.findViewById(R.id.playlist_titre);
		TextView nbVideos = (TextView) view.findViewById(R.id.playlist_nbVideos);
		title_text.setText(data.title);
		nbVideos.setText("Nombre de vidéos :"+data.nbVideos);


		if (position % 2 == 0)
			view.setBackgroundColor(Color.argb(255, 20, 20, 20));
		else
			view.setBackgroundColor(Color.BLACK);

		return view;
	}

	public PlaylistAdapter(Context context, List<PlaylistData> data) {
		_context = context;
		_data = data;
	}

}
