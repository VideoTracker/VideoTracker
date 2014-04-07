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
		public final Drawable picture;

		public VideoData(String _title, String _description, Drawable _picture) {
			title = _title;
			description = _description;
			picture = _picture;
		}

	}

	private Context _context;
	private List<VideoData> _data;

	/*
	 * Cette méthode est utilisée pour déterminer la taille actuelle de la
	 * liste. N'oubliez pas de vérifier que la liste est bien définie avant de
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
	 * Cette méthode retourne l'élément stocké à l'indice donné. Il n'est pas
	 * absolument nécessaire de vérifier si l'indice est bien dans la liste (les
	 * appels sont généralement bien formulés), mais cela reste une bonne
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
	 * Cette méthode retourne un numéro d'identification (ID) pour l'élément à
	 * l'indice donné. Cet ID devrait généralement être unique pour l'élément
	 * donné, en plus d'être relié à celui-ci. Dans notre exemple, on se
	 * contente de retourner l'indice, mais on pourrait par exemple imaginer une
	 * liste triable où l'on voudrait pouvoir retracer un élément particulier
	 * une fois le tri effectué et où la position n'est plus un ID approprié. On
	 * va souvent utiliser une forme de hachage des données pour générer cet ID.
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
	 * générique alors que ArrayList est une implantation de cette interface.
	 * L'adapteur n'a pas besoin d'une implantation particulière, tout ce qu'il
	 * lui faut, c'est une liste. Ceci permet donc au programme utilisant
	 * l'adapteur d'utiliser n'importe quelle implantation de List.
	 * 
	 * De plus, il faut toujours demander un Context, car ceci est utilisé entre
	 * autres pour obtenir le LayoutInflater nécessaire à l'initialisation des
	 * Views dans getView.
	 * 
	 */

	public VideoAdapter(Context context, List<VideoData> data) {
		_context = context;
		_data = data;
	}

}
