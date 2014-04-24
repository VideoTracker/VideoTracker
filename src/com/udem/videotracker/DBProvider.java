package com.udem.videotracker;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * 
 * DBProvider est la classe maîtresse de l'implantation
 * par ContentProvider. Un ContentProvider permet d'enrober une
 * source de données (en l'occurrence, une base de données) dans
 * un système basé sur des URIs. Ceci permet, en gros, d'accéder
 * à n'importe quoi à l'aide de simples adresses, comme sur le web.
 * 
 * Un avantage important est que tout accès à des données, même si
 * celles-ci utilisent des formats complètement différents pour
 * stocker celles-ci (par exemple une base de données, des tableaux
 * en mémoire, des fichiers, un format propriétaire particulier,
 * un conteneur encrypté, etc.) sont accédées de la même manière.
 * 
 * Les ContentProviders ont aussi une infrastructure permettant
 * de charger les données en parallèle sur un thread séparé,
 * évitant de ralentir le thread d'interface.
 * 
 * Finalement, il est possible d'accéder à tous les URIs
 * depuis une autre app si le ContentProvider est exporté
 * (voic AndroidManifest.xml).
 * 
 */

public class DBProvider extends ContentProvider {
	DBHelperVT dbh;
	
	/*
	 * L'autorité est la base de tous les URIs définis par
	 * le ContentProvider. L'entier VIDEO est utilisé pour
	 * associer les URI à un code spécial nécessaire pour
	 * le ContentProvider (voir addURI ci-bas).
	 */
	private static final String AUTHORITY = "com.udem.videotracker";
	public static final int VIDEO = 10;
	
	/*
	 * Le BASE_PATH est l'équivalent d'une page dans un URI web conventionnel.
	 * En l'occurrence, nous n'utilisons qu'une page, la liste des éléments météo.
	 */
	private static final String BASE_PATH = "video";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	
	/*
	 * L'UriMatcher est ce qui associe le ContentProvider avec des URIs.
	 */
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, VIDEO);
	}

	/*
	 * onCreate est appelé pour créer le ContentProvider. Il faut
	 * éviter les opérations lentes et il est fortement recommandé
	 * de ne PAS ouvrir de connexion avec la base de données ici.
	 * 
	 * On retourne true si le ContentProvider s'est initialisé correctement.
	 */
	@Override
	public boolean onCreate() {
		dbh = new DBHelperVT(getContext());
		Log.d("contentprovider","created.");
		return true;
	}

	/*
	 * Appelé pour supprimer une entrée. On détermine l'URI
	 * utilisé pour savoir quelle entrée supprimer et comment.
	 * Dans ce cas, il n'y a qu'un URI, donc c'est relativement
	 * simple.
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbh.getWritableDatabase();
		int rowsDeleted = 0;
		Log.d("contentprovider","delete "+uri);

		switch (uriType) {
		case VIDEO:
			rowsDeleted = sqlDB.delete(DBHelperVT.TABLE_PLAYLIST, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		// La méthode notifyChange permet d'indiquer que le contenu relié à cet URI a changé
		// Le CursorAdapter utilise ces notifications pour mettre à jour ses ListViews automatiquement
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	/*
	 * Retourne le MIME-type des données. Comme nous restons simple,
	 * nous ne supportons pas cette fonction et retournons null pour
	 * ne pas indiquer de MIME-type. En principe, on analyserait l'URI
	 * pour déterminer la requête, puis retournerait le bon MIME-type
	 * pour celle-ci.
	 */
	@Override
	public String getType(Uri uri) {
		return null;
	}

	/*
	 * Appelé pour insérer une entrée. Le principe
	 * est semblabe à la suppression.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d("contentprovider","insert "+uri);

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbh.getWritableDatabase();
		switch (uriType) {
		case VIDEO:
			try {
				sqlDB.insertOrThrow(DBHelperVT.TABLE_PLAYLIST, null, values);
			} catch ( SQLException e ) { return null; }
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + values.get(DBHelperVT.P_ID) );
	}

	/*
	 * Appelé pour lire une ou des entrées. Le format est semblable
	 * à celui d'un appel direct à une base de données.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Log.d("contentprovider","query "+uri);
		
		// SQLiteQueryBuilder s'occupe d'automatiser certains éléments de la création d'une requête SQL
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Vérifie si toutes les colonnes demandées dans le tableau projection existent bien dans la base de données
		checkColumns(projection);

		queryBuilder.setTables(DBHelperVT.TABLE_PLAYLIST);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case VIDEO:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbh.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	/*
	 * Appelé pour modifier des entrées existantes, mais sans les supprimer.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d("contentprovider","update "+uri);

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbh.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case VIDEO:
			rowsUpdated = sqlDB.update(DBHelperVT.TABLE_PLAYLIST,values,selection,selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	/*
	 * Vérifie si les colonnes demandées dans une requête de type query
	 * sont bien définies dans la table de la base de données, et lance
	 * une exception si ce n'est pas le cas.
	 */
	private void checkColumns(String[] projection) {
		String[] available = {
				DBHelperVT.V_DATEPUB,
				DBHelperVT.V_DESC,
				DBHelperVT.V_ID,
				DBHelperVT.V_LINK,
				DBHelperVT.V_THUMBMAIL,
				DBHelperVT.V_TITLE,
				DBHelperVT.P_ID,
				DBHelperVT.P_NOM,
				DBHelperVT.P_DATECREA,
				DBHelperVT.H_KEYWORDS,
				DBHelperVT.A_IDPLAYLIST,
				DBHelperVT.A_IDVIDEO
				};
				
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
