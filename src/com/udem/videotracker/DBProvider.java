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
 * DBProvider est la classe ma�tresse de l'implantation
 * par ContentProvider. Un ContentProvider permet d'enrober une
 * source de donn�es (en l'occurrence, une base de donn�es) dans
 * un syst�me bas� sur des URIs. Ceci permet, en gros, d'acc�der
 * � n'importe quoi � l'aide de simples adresses, comme sur le web.
 * 
 * Un avantage important est que tout acc�s � des donn�es, m�me si
 * celles-ci utilisent des formats compl�tement diff�rents pour
 * stocker celles-ci (par exemple une base de donn�es, des tableaux
 * en m�moire, des fichiers, un format propri�taire particulier,
 * un conteneur encrypt�, etc.) sont acc�d�es de la m�me mani�re.
 * 
 * Les ContentProviders ont aussi une infrastructure permettant
 * de charger les donn�es en parall�le sur un thread s�par�,
 * �vitant de ralentir le thread d'interface.
 * 
 * Finalement, il est possible d'acc�der � tous les URIs
 * depuis une autre app si le ContentProvider est export�
 * (voic AndroidManifest.xml).
 * 
 */

public class DBProvider extends ContentProvider {
	DBHelperVT dbh;
	
	/*
	 * L'autorit� est la base de tous les URIs d�finis par
	 * le ContentProvider. L'entier VIDEO est utilis� pour
	 * associer les URI � un code sp�cial n�cessaire pour
	 * le ContentProvider (voir addURI ci-bas).
	 */
	private static final String AUTHORITY = "com.udem.videotracker";
	public static final int VIDEO = 10;
	
	/*
	 * Le BASE_PATH est l'�quivalent d'une page dans un URI web conventionnel.
	 * En l'occurrence, nous n'utilisons qu'une page, la liste des �l�ments m�t�o.
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
	 * onCreate est appel� pour cr�er le ContentProvider. Il faut
	 * �viter les op�rations lentes et il est fortement recommand�
	 * de ne PAS ouvrir de connexion avec la base de donn�es ici.
	 * 
	 * On retourne true si le ContentProvider s'est initialis� correctement.
	 */
	@Override
	public boolean onCreate() {
		dbh = new DBHelperVT(getContext());
		Log.d("contentprovider","created.");
		return true;
	}

	/*
	 * Appel� pour supprimer une entr�e. On d�termine l'URI
	 * utilis� pour savoir quelle entr�e supprimer et comment.
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
		
		// La m�thode notifyChange permet d'indiquer que le contenu reli� � cet URI a chang�
		// Le CursorAdapter utilise ces notifications pour mettre � jour ses ListViews automatiquement
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	/*
	 * Retourne le MIME-type des donn�es. Comme nous restons simple,
	 * nous ne supportons pas cette fonction et retournons null pour
	 * ne pas indiquer de MIME-type. En principe, on analyserait l'URI
	 * pour d�terminer la requ�te, puis retournerait le bon MIME-type
	 * pour celle-ci.
	 */
	@Override
	public String getType(Uri uri) {
		return null;
	}

	/*
	 * Appel� pour ins�rer une entr�e. Le principe
	 * est semblabe � la suppression.
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
	 * Appel� pour lire une ou des entr�es. Le format est semblable
	 * � celui d'un appel direct � une base de donn�es.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Log.d("contentprovider","query "+uri);
		
		// SQLiteQueryBuilder s'occupe d'automatiser certains �l�ments de la cr�ation d'une requ�te SQL
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// V�rifie si toutes les colonnes demand�es dans le tableau projection existent bien dans la base de donn�es
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
	 * Appel� pour modifier des entr�es existantes, mais sans les supprimer.
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
	 * V�rifie si les colonnes demand�es dans une requ�te de type query
	 * sont bien d�finies dans la table de la base de donn�es, et lance
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
