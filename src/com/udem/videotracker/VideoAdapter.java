package com.udem.videotracker;

import java.util.List;

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

	public static class VideoData {
		public final String title;
		public final String description;
		public final String auteur;
		public final String url;
		public boolean favori;
		public int nbVues;
		public final Drawable picture;

		public VideoData(String _title, String _description, String _auteur, String _url, boolean _favori, int _nbVues, Drawable _picture) {
			title = _title;
			description = _description;
			auteur = _auteur;
			url = _url;
			favori = _favori;
			nbVues = _nbVues;
			picture = _picture;
		}
		
		//TODO constructeur pour que antho fasse ses tests
		public VideoData(String _title, String _description, Drawable _picture) {
			title = _title;
			description = _description;
			auteur = null;
			url = null;
			favori = false;
			nbVues = 0;
			picture = _picture;
		}

	}

	private Context _context;
	private List<VideoData> _data;

	/*
	 * Cette m�thode est utilis�e pour d�terminer la taille actuelle de la
	 * liste. N'oubliez pas de v�rifier que la liste est bien d�finie avant de
	 * retourner sa taille!
	 */
	@Override
	public int getCount() {
		if (_data != null)
			return _data.size();
		else
			return 0;
	}

	/*
	 * Cette m�thode retourne l'�l�ment stock� � l'indice donn�. Il n'est pas
	 * absolument n�cessaire de v�rifier si l'indice est bien dans la liste (les
	 * appels sont g�n�ralement bien formul�s), mais cela reste une bonne
	 * pratique.
	 */
	@Override
	public Object getItem(int at) {
		if (_data != null && at >= 0 && at < _data.size())
			return _data.get(at);
		else
			return null;
	}

	/*
	 * Cette m�thode retourne un num�ro d'identification (ID) pour l'�l�ment �
	 * l'indice donn�. Cet ID devrait g�n�ralement �tre unique pour l'�l�ment
	 * donn�, en plus d'�tre reli� � celui-ci. Dans notre exemple, on se
	 * contente de retourner l'indice, mais on pourrait par exemple imaginer une
	 * liste triable o� l'on voudrait pouvoir retracer un �l�ment particulier
	 * une fois le tri effectu� et o� la position n'est plus un ID appropri�. On
	 * va souvent utiliser une forme de hachage des donn�es pour g�n�rer cet ID.
	 */
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

		title_text.setText(data.title);
		description_text.setText(data.description);
		icon.setImageDrawable(data.picture);

		if (position % 2 == 0)
			view.setBackgroundColor(Color.argb(255, 20, 20, 20));
		else
			view.setBackgroundColor(Color.BLACK);

		return view;
	}

	/**
	 * 
	 * Constructeur de l'adapteur. On notera l'utilisation de List au lieu de
	 * ArrayList; ceci est une bonne pratique, car List est une interface
	 * g�n�rique alors que ArrayList est une implantation de cette interface.
	 * L'adapteur n'a pas besoin d'une implantation particuli�re, tout ce qu'il
	 * lui faut, c'est une liste. Ceci permet donc au programme utilisant
	 * l'adapteur d'utiliser n'importe quelle implantation de List.
	 * 
	 * De plus, il faut toujours demander un Context, car ceci est utilis� entre
	 * autres pour obtenir le LayoutInflater n�cessaire � l'initialisation des
	 * Views dans getView.
	 * 
	 */

	public VideoAdapter(Context context, List<VideoData> data) {
		_context = context;
		_data = data;
	}

}
