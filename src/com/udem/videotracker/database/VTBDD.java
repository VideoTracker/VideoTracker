package com.udem.videotracker.database;

import java.util.ArrayList;
import java.util.Date;


import com.udem.videotracker.playlist.PlaylistAdapter;
import com.udem.videotracker.recherche.VideoAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class VTBDD {
	
	private static final String NOM_BDD = "TEST3.db";
	private static final int VERSION_BDD = 1;
	
	private static final String TABLE_VIDEO = "table_video";
	/*
	 * �num�ration des noms de colonne de la table video
	 */
	private static final String V_ID = "id_video";
	private static final String V_TITLE = "titre";
	private static final String V_DESC = "description";
	private static final String V_LINK = "lien";
	private static final String V_DATEPUB = "date_publication";
	private static final String V_THUMBMAIL = "thumbmail";
	private static final String V_SRC = "source";

	
	private static final String TABLE_PLAYLIST = "table_playlist";
	private static final String COL_PNAME = "name";
	private static final String COL_DATE = "date";
 
	
	private static final String TABLE_ASSOCIATION = "Association";
	/*
	 * �num�ration des noms de colonne de la table association
	 */
	private static final String A_IDVIDEO = "id_video";
	private static final String A_IDPLAYLIST = "id_playlist";

	
	private static final String TABLE_HISTORIQUE = "Historique";
	/*
	 * �num�ration des noms de colonne de la table historique
	 */
	private static final String H_KEYWORDS = "keywords";
	
	/*
	 * On utilisera ce contexte pour afficher des toasts.
	 */
	
	private SQLiteDatabase bdd;
	 
	private DBHelperVT maBaseSQLite;
 
	public VTBDD(Context context){
		//On cr�er la BDD et sa table
		maBaseSQLite = new DBHelperVT(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en �criture
		
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public void addPlaylist(String name, String date) {
		
		ContentValues values = new ContentValues();
	    values.put(COL_PNAME, name);
	    values.put(COL_DATE, date);
	 
	    bdd.insert(TABLE_PLAYLIST, null, values);		
	}
	
	public long addToVideo(VideoAdapter.VideoData data){
		ContentValues values = new ContentValues();
		values.put(V_TITLE, data.title);
		values.put(V_DESC, data.description);
		values.put(V_LINK, data.id_url);
		values.put(V_DATEPUB, data.datePublication.toString());
		values.put(V_THUMBMAIL, data.url_image);
		values.put(V_SRC, data.sourceVideo.toString());

		
		return bdd.insertOrThrow(TABLE_VIDEO,null, values);
		
	}
	
	public ArrayList<VideoAdapter.VideoData> getPlaylistContent(int id){
		
		ArrayList<VideoAdapter.VideoData> ret = new ArrayList<VideoAdapter.VideoData>();
		
	    Cursor cursor = 
	            bdd.query(TABLE_ASSOCIATION, 
	           new String[]{ A_IDVIDEO,A_IDPLAYLIST}, 
	            A_IDPLAYLIST+"= ?", 
	            new String[] { String.valueOf(id) }, 
	            null,
	            null,
	            null,
	            null);
	 
	    if (cursor != null){
	    	while(cursor.moveToNext()){
	    		ret.add(getVideo(cursor.getInt(0)));
	    	}
	    }
	    return ret;
	}
		
	public VideoAdapter.VideoData getVideo(int id){
		
	    Cursor cursor = 
	            bdd.query(TABLE_VIDEO, 
	           new String[]{ V_ID,V_TITLE, V_DESC, V_LINK, V_DATEPUB, V_THUMBMAIL,V_SRC}, 
	            V_ID+"= ?",  
	            new String[] { String.valueOf(id) }, 
	            null, 
	            null, 
	            null, 
	            null);
	    
	    if (cursor != null){
	    	cursor.moveToNext();
	    }
	    
	    @SuppressWarnings("deprecation")
		VideoAdapter.VideoData ret = new VideoAdapter.VideoData(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(5), 0, 0, null, new Date(cursor.getString(4)), cursor.getString(6),cursor.getInt(0),cursor.getString(3));
		return ret;
		
	}
	
	public boolean deletePlaylist(String name) {
		return bdd.delete(TABLE_PLAYLIST, COL_PNAME + "=\"" + name+"\"", null) > 0;
			
	}
	
	public boolean deleteVideoFromPlaylist(int id_video, int id_playlist){
		return bdd.delete(TABLE_ASSOCIATION, A_IDPLAYLIST + "=" + id_playlist + " AND " + A_IDVIDEO + "=" + id_video  , null) > 0;
	}
	
	public boolean addVideoToPlaylist(VideoAdapter.VideoData vid, int id_playlist){
		int id_video = (int) addToVideo(vid);
		ContentValues values = new ContentValues();
		values.put(A_IDVIDEO, id_video);
		values.put(A_IDPLAYLIST, id_playlist);
		bdd.insertOrThrow(TABLE_ASSOCIATION, null, values);
		return true;
	}
	public void addToHistoric(String keywords) {
		
		ContentValues values = new ContentValues();
		values.put(H_KEYWORDS, keywords);
		
		bdd.insertOrThrow(TABLE_HISTORIQUE, null, values);
		
	}
	
	public ArrayList<String> getHistoric(){
		int count = 0;
		ArrayList<String> ret = new ArrayList<String>();
		String query = "Select * FROM " + TABLE_HISTORIQUE;
		Cursor curs = bdd.rawQuery(query, null);
		
		if(curs.moveToFirst()){
			do{
				ret.add(curs.getString(1));
				count++;
			}while(curs.moveToNext() && count <5);
			
		}
		
		return ret;
		
	}
	
	public ArrayList<PlaylistAdapter.PlaylistData> getPlaylists(){
		ArrayList<PlaylistAdapter.PlaylistData> ret = new ArrayList<PlaylistAdapter.PlaylistData>();
		String query = "Select * FROM " + TABLE_PLAYLIST;
		Cursor curs = bdd.rawQuery(query, null);
		
		if(curs.moveToFirst()){
			do{
				PlaylistAdapter.PlaylistData playlist = new PlaylistAdapter.PlaylistData(curs.getString(1), curs.getInt(0));
				ret.add(playlist);
				
			}while(curs.moveToNext());
			
		}
		return ret;
	}
}
