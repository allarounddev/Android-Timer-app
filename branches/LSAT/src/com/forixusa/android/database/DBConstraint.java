package com.forixusa.android.database;

public class DBConstraint {
	public static final String DATABASE_NAME = "scoretimer.db";
	public static final int DATABASE_VERSION = 2;

	public static final String SCORE_RESULT_TABLE_NAME = "ScoreResults";

	public static final String[] tables = new String[] { SCORE_RESULT_TABLE_NAME };

	// Fields of ScoreResults table
	public static final String SCORE_RESULT_ID = "score_result_id";
	public static final String DATE = "date";
	public static final String ACCURACY = "accuracy";
	public static final String PACE = "pace";
	public static final String ESTIMATION_SCORE = "estimation_score";
	public static final String TEST_OPTION = "test_option";

	public static final String CREATE_TABLE_SCORE_RESULTS = "CREATE VIRTUAL TABLE " + SCORE_RESULT_TABLE_NAME
			+ " USING FTS3(" + SCORE_RESULT_ID + " INTEGER PRIMARY KEY, " + DATE + " TEXT, " + ACCURACY + " DOUBLE, "
			+ PACE + " DOUBLE, " + TEST_OPTION + " TEXT, " + ESTIMATION_SCORE + "  DOUBLE)";

}
