package com.forixusa.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDAOBase {
	private final Context context;
	protected SQLiteDatabase db;
	protected OpenHelper mOpenHelper;

	public DBDAOBase(final Context context) {
		this.context = context;
		mOpenHelper = new OpenHelper(this.context);
		this.db = mOpenHelper.getWritableDatabase();
	}

	public void open() {
		mOpenHelper = new OpenHelper(this.context);
		this.db = mOpenHelper.getWritableDatabase();
	}

	public SQLiteDatabase getDb() {
		return this.db;
	}

	public void close() {
		if (this.mOpenHelper != null) {
			this.mOpenHelper.close();
		}
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(final Context context) {
			super(context, DBConstraint.DATABASE_NAME, null, DBConstraint.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DBConstraint.CREATE_TABLE_SCORE_RESULTS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (final String table : DBConstraint.tables) {
				db.execSQL("DROP TABLE IF EXISTS " + table);
			}
			onCreate(db);
		}
	}
}
