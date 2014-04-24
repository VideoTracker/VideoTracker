<<<<<<< HEAD
package com.udem.videotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/*
 * Si on utilise l'émulateur Android (et seulement l'émulateur), il est simple
 * d'accéder et d'examiner la base de données directement depuis Eclipse. Les
 * bases de données Android sont SQLite3.
 * 
 * Dans la perspective DDMS, sous l'onglet File Explorer, vous pourrez naviguer
 * vers /data/data/iro.ift2905.bddmeteo/databases et sélectionner "meteo.db". En
 * cliquant sur "Pull a file from the device", vous obtiendrez une copie de la
 * base de données sur votre ordinateur que vous pourrez visualiser à votre guise.
 * 
 * Vous trouverez un exécutable sqlite3 dans le dossier du SDK (sous ADT, sdk/tools)
 * qui vous permet de facilement lire la base de données. Quelques commandes :
 * 
 *  sqlite3 meteo.db
 *  > .help  -> Donne la liste des commandes
 *  > .dump  -> Affiche la structure de la base de données et toutes les données
 *  > .schema -> Affiche la structure de la base de données
 *  > .quit
 * 
 * Si vous n'aimez pas la ligne de commande, il existe aussi un module Firefox
 * nommé SQLite Manager qui vous permet de naviguer avec une interface graphique.
 */

/**
 * 
 * Cette classe permet de facilier la connexion à la
 * base de donnée que nous utilisons. Elle s'occupe de
 * gérer non seulement l'insertion et la lecture de données,
 * mais aussi de créer et détruire la base de données au
 * besoin. Cette fonctionnalité est standardisée, ce qui
 * la rend réutilisable dans d'autres classes d'Android,
 * par exemple un ContentProvider.
 *
 */
public class DBHelperVT extends SQLiteOpenHelper {

	/*
	 * La version est un entier obligatoire qui indique
	 * la version de la *structure* de la base de données.
	 * À chaque fois que celle-ci est changée, on doit
	 * incrémenter la version.
	 * 
	 * Un changement structurel peut être, par exemple,
	 * ajouter ou enlever des colonnes, changer la définition
	 * ou les propriétés d'une colonne, etc.
	 */
	static final int VERSION=1;

	static final String TABLE_VIDEO = "Video";
	/*
	 * Énumération des noms de colonne de la table video
	 */
	static final String V_ID = "id_video";
	static final String V_TITLE = "titre";
	static final String V_DESC = "description";
	static final String V_LINK = "lien";
	static final String V_DATEPUB = "date_publication";
	static final String V_THUMBMAIL = "thumbmail";
	
	
	static final String TABLE_PLAYLIST = "Playlist";
	/*
	 * Énumération des noms de colonne de la table playlist
	 */
	static final String P_ID = "id_playlist";
	static final String P_NOM = "nom";
	static final String P_DATECREA = "date_creation";
	
	
	static final String TABLE_ASSOCIATION = "Association";
	/*
	 * Énumération des noms de colonne de la table association
	 */
	static final String A_IDVIDEO = "id_video";
	static final String A_IDPLAYLIST = "id_playlist";

	

	/*
	 * On utilisera ce contexte pour afficher des toasts.
	 */
	Context context;	

	public DBHelperVT(Context context) {
		// Une application conserve toutes ses données privées
		// dans /data/data/<package>/.
		// La base de donnée est donc dans
		// /data/data/iro.ift2905.bddmeteo/databases (Voir DDMS)
		super(context, "meteo.db", null, VERSION);
		this.context=context;
	}

	/*
	 * onCreate est appelée lorsqu'on crée la base de données.
	 * Tous les appels d'initialisation, par exemple la création
	 * de tables, doivent être effectués ici.
	 * 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Toast.makeText(context, "Création BDD", Toast.LENGTH_LONG).show();
		Log.d("DBHelper", "Création BDD");
		
		// Appel standard pour créer une table dans la base de données.
		String sql = "create table " + TABLE_VIDEO + " ("
				+ V_ID +" integer primary key, "
				+ V_TITLE + " text,"
				+ V_DESC + " text,"
				+ V_LINK + " text,"
				+ V_THUMBMAIL + " text,"
				+ V_DATEPUB + " date );";
		
		sql += "create table " + TABLE_PLAYLIST + " ("
				+ P_ID +" integer primary key, "
				+ P_NOM + " text,"
				+ P_DATECREA + " date );";
		
		sql += "create table " + TABLE_ASSOCIATION + " ("
				+ A_IDVIDEO +" integer, "
				+ A_IDPLAYLIST + " integer);";

		// ExecSQL prend en entrée une commande SQL et l'exécute
		// directement sur la base de données.
		db.execSQL(sql);
	}

	/*
	 * onUpgrade est appelée lorsque l'application détecte une
	 * base de données avec une version plus ancienne que celle
	 * actuellement utilisée. Ceci peut arriver si, par exemple,
	 * on crée une mise à jour pour l'application qui change la
	 * structure de la base de données et donc la version; tous
	 * les usagers ayant déjà utilisé l'application avant cette
	 * mise à jour auront l'ancienne version de la structure et
	 * devront être mis à jour.
	 * 
	 * Notez qu'en général, on souhaitera conserver les données
	 * dans la base de données en effectuant cette mise à jour.
	 * L'exemple ici se contente de tout effacer, mais ce comportement
	 * n'est désirable que si la base de données ne sert que de cache
	 * et donc ne contient que des valeurs temporaires.
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int ancienneVersion, int nouvelleVersion) {
		Toast.makeText(context, "Mise à jour BDD", Toast.LENGTH_LONG).show();
		Log.d("DBHelper", "Mise à jour BDD");
	}

	/*
	 * Cette méthode a été créée spécifiquement pour la base de donnée
	 * présente et permet d'ajouter un élément à celle-ci.
	 * 
	 */
	public void addToFav(VideoAdapter.VideoData data, int idplaylist) {
		// Malgré le nom "get", la méthode getWritableDatabase fait plus
		// que simplement retourner une référence; elle ouvre une
		// connexion à la base de données. Il est donc impératif d'appeler
		// db.close() à la fin du processus.
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cur = db.rawQuery("SELECT * FROM Video V WHERE V.lien='"+ data.url+"'", new String [] {});
		int id =0;
		if(!cur.moveToFirst()){
			addToVideo(data, db);
		}else{
			id = cur.getInt(1);
		}
		
		String sql =
	            "INSERT or replace INTO Association (id_video, id_playlist) VALUES('"+id +"','"+idplaylist+"')" ;       
	                db.execSQL(sql);
		


		
		// N'oubliez pas ceci!
		db.close();		
	}
	
	public void addToVideo(VideoAdapter.VideoData data, SQLiteDatabase db){
		String sql = "INSERT or replace INTO Video ( titre, description, lien,date_publication, thumbmail) VALUES('"+data.title+"','"+data.description+"','"+ data.url+"','2012-04-08','lol/src')" ;       
		db.execSQL(sql);
	}

	
	/*
	 * Méthode utilitaire qui retourne le nombre
	 * d'éléments dans la base de données.
	 * 
	 */
	public int querySize() {
		// Notez l'usage de getReadableDatabase; lorsqu'on ne
		// cherche pas à modifier la base de données, il
		// est recommandé d'utiliser une connexion lecture seule.
		SQLiteDatabase db = this.getReadableDatabase();

		// Cursor est un objet très utilisé qui permet de stocker
		// le résultat d'une requête SQL. Ce résultat peut être,
		// dans le cas présent, la colonne ID de toutes les lignes
		// de la base de donnée, ce qui nous permet de les compter.
		Cursor c = db.query(TABLE_VIDEO, new String[] {V_ID}, null, null, null, null, null);
		int size = c.getCount();
		db.close();
		return size;
	}


}
