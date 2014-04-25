package com.udem.videotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



public class VTBDD {
	
	private static final String NOM_BDD = "TEST3.db";
	private static final int VERSION_BDD = 1;
	
	private static final String TABLE_VIDEO = "table_video";
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
	
	
	private static final String TABLE_PLAYLIST = "table_playlist";
	private static final String COL_ID = "id_playlist";
	private static final String COL_PNAME = "name";
	private static final String COL_DATE = "date";
 
	private static final String CREATE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PNAME + " TEXT NOT NULL, "
	+ COL_DATE + " TEXT NOT NULL);";
	
	private static final String TABLE_ASSOCIATION = "Association";
	/*
	 * Énumération des noms de colonne de la table association
	 */
	private static final String A_IDVIDEO = "id_video";
	private static final String A_IDPLAYLIST = "id_playlist";
	
	private static final String CREATE_ASSOCIATION = "CREATE TABLE " + TABLE_PLAYLIST + " ("+ A_IDVIDEO + " INTEGER NOT NULL, "
			+ A_IDPLAYLIST + " INTEGER NOT NULL);";
	
	private static final String TABLE_HISTORIQUE = "Historique";
	/*
	 * Énumération des noms de colonne de la table historique
	 */
	private static final String H_ID = "id_historique";
	private static final String H_KEYWORDS = "keywords";
	
	private static final String CREATE_HISTORIQUE = "CREATE TABLE " + TABLE_HISTORIQUE + " ("+H_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ H_KEYWORDS + " TEXT NOT NULL);";
	/*
	 * On utilisera ce contexte pour afficher des toasts.
	 */
	
	private SQLiteDatabase bdd;
	 
	private DBHelperVT maBaseSQLite;
 
	public VTBDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new DBHelperVT(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public void addPlaylist(String name, String date) {
		
		ContentValues values = new ContentValues();
	    values.put(COL_PNAME, name);
	    values.put(COL_DATE, date);
	 
	    // insert row
	    bdd.insert(TABLE_PLAYLIST, null, values);		
	}
	
	public void addToVideo(VideoAdapter.VideoData data, SQLiteDatabase db){
		ContentValues values = new ContentValues();
		values.put(V_TITLE, data.title);
		values.put(V_DESC, data.description);
		values.put(V_LINK, data.url_video);
		values.put(V_DATEPUB, data.datePublication.toString());
		values.put(V_THUMBMAIL, data.url_image);
		
		bdd.insertOrThrow(TABLE_VIDEO,null, values);
		
	}
	
	public boolean deletePlaylist(String name) {
		return bdd.delete(TABLE_PLAYLIST, COL_PNAME + "=" + name, null) > 0;
			
	}
	
	public void addToHistoric(String keywords) {
		
		ContentValues values = new ContentValues();
		values.put(H_KEYWORDS, keywords);
		
		bdd.insertOrThrow(TABLE_HISTORIQUE, null, values);
		
	}
	
}
