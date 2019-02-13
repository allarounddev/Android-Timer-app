package com.forixusa.scoretimersat.android.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.forixusa.android.database.DBConstraint;
import com.forixusa.android.database.DBDAOBase;
import com.forixusa.scoretimersat.android.models.ScoreResult;

public class ScoreResultDao extends DBDAOBase {
	private static final String TAG = ScoreResultDao.class.getSimpleName();

	private final String[] mColumns = new String[] { DBConstraint.SCORE_RESULT_ID, DBConstraint.DATE,
			DBConstraint.ACCURACY, DBConstraint.PACE, DBConstraint.ESTIMATION_SCORE, DBConstraint.TEST_OPTION

	};

	public ScoreResultDao(Context context) {
		super(context);

	}

	public void deleteAlls() {
		this.db.delete(DBConstraint.SCORE_RESULT_TABLE_NAME, null, null);
	}

	public void deleteByTestOption(String testOption) {
		this.db.delete(DBConstraint.SCORE_RESULT_TABLE_NAME, DBConstraint.TEST_OPTION + " MATCH ?",
				new String[] { testOption });
	}

	public void delete(final String scoreResultId) {
		this.db.delete(DBConstraint.SCORE_RESULT_TABLE_NAME, DBConstraint.SCORE_RESULT_ID + " MATCH ?",
				new String[] { scoreResultId });
	}

	public ArrayList<ScoreResult> selectAlls() {
		final ArrayList<ScoreResult> scoreResults = new ArrayList<ScoreResult>();
		Cursor cursor = null;
		try {
			cursor = this.db.query(DBConstraint.SCORE_RESULT_TABLE_NAME, mColumns, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				final SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
				do {
					final ScoreResult scoreResult = new ScoreResult();
					scoreResult.scoreResultId = cursor.getInt(cursor.getColumnIndex(DBConstraint.SCORE_RESULT_ID));
					scoreResult.accuracy = cursor.getDouble(cursor.getColumnIndex(DBConstraint.ACCURACY));
					scoreResult.pace = cursor.getDouble(cursor.getColumnIndex(DBConstraint.PACE));
					scoreResult.estimationScore = cursor
							.getDouble(cursor.getColumnIndex(DBConstraint.ESTIMATION_SCORE));
					scoreResult.date = parser.parse(cursor.getString(cursor.getColumnIndex(DBConstraint.DATE)));
					scoreResult.testOption = cursor.getString(cursor.getColumnIndex(DBConstraint.TEST_OPTION));
					scoreResults.add(scoreResult);
				} while (cursor.moveToNext());

			}
		} catch (final ParseException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return scoreResults;
	}

	public ArrayList<ScoreResult> selectByTestOption(String testOption) {
		final ArrayList<ScoreResult> scoreResults = new ArrayList<ScoreResult>();
		Cursor cursor = null;
		try {
			cursor = this.db.query(DBConstraint.SCORE_RESULT_TABLE_NAME, mColumns, DBConstraint.TEST_OPTION
					+ " MATCH ?", new String[] { testOption }, null, null, null);
			if (cursor.moveToFirst()) {
				final SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
				do {
					final ScoreResult scoreResult = new ScoreResult();
					scoreResult.scoreResultId = cursor.getInt(cursor.getColumnIndex(DBConstraint.SCORE_RESULT_ID));
					scoreResult.accuracy = cursor.getDouble(cursor.getColumnIndex(DBConstraint.ACCURACY));
					scoreResult.pace = cursor.getDouble(cursor.getColumnIndex(DBConstraint.PACE));
					scoreResult.estimationScore = cursor
							.getDouble(cursor.getColumnIndex(DBConstraint.ESTIMATION_SCORE));
					scoreResult.date = parser.parse(cursor.getString(cursor.getColumnIndex(DBConstraint.DATE)));
					scoreResult.testOption = cursor.getString(cursor.getColumnIndex(DBConstraint.TEST_OPTION));
					scoreResults.add(scoreResult);
				} while (cursor.moveToNext());

			}
		} catch (final ParseException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return scoreResults;
	}

	public ScoreResult select(final int id) {
		final ScoreResult scoreResult = new ScoreResult();
		Cursor cursor = null;
		try {
			cursor = this.db.query(DBConstraint.SCORE_RESULT_TABLE_NAME, mColumns, DBConstraint.SCORE_RESULT_ID
					+ " MATCH ?", new String[] { Integer.toString(id) }, null, null, null);

			if (cursor.moveToFirst()) {
				final SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				scoreResult.scoreResultId = cursor.getInt(cursor.getColumnIndex(DBConstraint.SCORE_RESULT_ID));
				scoreResult.accuracy = cursor.getDouble(cursor.getColumnIndex(DBConstraint.ACCURACY));
				scoreResult.pace = cursor.getDouble(cursor.getColumnIndex(DBConstraint.PACE));
				scoreResult.estimationScore = cursor.getDouble(cursor.getColumnIndex(DBConstraint.ESTIMATION_SCORE));
				scoreResult.date = parser.parse(cursor.getString(cursor.getColumnIndex(DBConstraint.DATE)));
				scoreResult.testOption = cursor.getString(cursor.getColumnIndex(DBConstraint.TEST_OPTION));
			}
		} catch (final ParseException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return scoreResult;
	}

	public long insert(ScoreResult scoreResult) {
		return db.insertOrThrow(DBConstraint.SCORE_RESULT_TABLE_NAME, null, createContent(scoreResult));
	}

	public long update(ScoreResult scoreResult) {
		return this.db.update(DBConstraint.SCORE_RESULT_TABLE_NAME, createContent(scoreResult),
				DBConstraint.SCORE_RESULT_ID + " MATCH ?", new String[] { String.valueOf(scoreResult.scoreResultId) });
	}

	private ContentValues createContent(final ScoreResult scoreResult) {
		final ContentValues values = new ContentValues();
		values.put(DBConstraint.ACCURACY, scoreResult.accuracy);
		values.put(DBConstraint.PACE, scoreResult.pace);
		values.put(DBConstraint.ESTIMATION_SCORE, scoreResult.estimationScore);
		values.put(DBConstraint.DATE, scoreResult.date.toString());
		values.put(DBConstraint.TEST_OPTION, scoreResult.testOption);
		return values;
	}

}
