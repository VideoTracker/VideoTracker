package com.udem.videotracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
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

	 static final String TABLE_VIDEO = "table_video";
	/*
	 * Énumération des noms de colonne de la table video
	 */
	private static final String V_ID = "id_video";
	private static final String V_TITLE = "titre";
	private static final String V_DESC = "description";
	private static final String V_LINK = "lien";
	private static final String V_DATEPUB = "date_publication";
	private static final String V_THUMBMAIL = "thumbmail";
	
	private static final String CREATE_VIDEO = "CREATE TABLE " + TABLE_VIDEO + " ("
			+ V_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + V_TITLE + " TEXT NOT NULL, "
			+ V_DESC + " TEXT NOT NULL, " + V_LINK + " TEXT NOT NULL, " + V_DATEPUB + " TEXT NOT NULL, "
			+ V_THUMBMAIL + " TEXT NOT NULL);";
	
	
	static final String TABLE_PLAYLIST = "table_playlist";
	 static final String COL_ID = "id_playlist";
	 static final String COL_PNAME = "name";
	 static final String COL_DATE = "date";
 
	private static final String CREATE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PNAME + " TEXT NOT NULL, "
	+ COL_DATE + " TEXT NOT NULL);";
	
	 static final String TABLE_ASSOCIATION = "Association";
	/*
	 * Énumération des noms de colonne de la table association
	 */
	private static final String A_IDVIDEO = "id_video";
	private static final String A_IDPLAYLIST = "id_playlist";
	
	private static final String CREATE_ASSOCIATION = "CREATE TABLE " + TABLE_ASSOCIATION + " ("+ A_IDVIDEO + " INTEGER NOT NULL, "
			+ A_IDPLAYLIST + " INTEGER NOT NULL);";
	
	private static final String TABLE_HISTORIQUE = "Historique";
	/*
	 * Énumération des noms de colonne de la table historique
	 */
	private static final String H_ID = "id_historique";
	private static final String H_KEYWORDS = "keywords";
	
	static final String CREATE_HISTORIQUE = "CREATE TABLE " + TABLE_HISTORIQUE + " ("+H_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+ H_KEYWORDS + " TEXT NOT NULL);";
	/*
	 * On utilisera ce contexte pour afficher des toasts.
	 */
	Context context;	
	

	
	public DBHelperVT(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_VIDEO);
		db.execSQL(CREATE_PLAYLIST);
		db.execSQL(CREATE_HISTORIQUE);
		db.execSQL(CREATE_ASSOCIATION);
	}
 
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int ancienneVersion, int nouvelleVersion) {
		Toast.makeText(context, "Mise à jour BDD", Toast.LENGTH_LONG).show();
		Log.d("DBHelper", "Mise à jour BDD");
		
		// Efface l'ancienne base de données
		db.execSQL("drop table if exists "+TABLE_VIDEO);
		db.execSQL("drop table if exists "+TABLE_ASSOCIATION);
		db.execSQL("drop table if exists "+TABLE_HISTORIQUE);
		db.execSQL("drop table if exists "+TABLE_PLAYLIST);
		
		// Appelle onCreate, qui recrée la base de données
		onCreate(db);
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
