package com.forixusa.scoretimeract.android.services;

import java.util.ArrayList;

import android.content.Context;

import com.forixusa.scoretimeract.android.data.ScoreResultDao;
import com.forixusa.scoretimeract.android.models.ScoreResult;

public class ScoreResultService {

	public static ArrayList<ScoreResult> getScoreResults(Context context, String testOption) {
		ArrayList<ScoreResult> scoreResults = new ArrayList<ScoreResult>();
		final ScoreResultDao scoreResultDao = new ScoreResultDao(context);
		try {
			scoreResults = scoreResultDao.selectByTestOption(testOption);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			scoreResultDao.close();
		}
		return scoreResults;
	}

	public static ArrayList<ScoreResult> getAllScoreResults(Context context) {
		ArrayList<ScoreResult> scoreResults = new ArrayList<ScoreResult>();
		final ScoreResultDao scoreResultDao = new ScoreResultDao(context);
		try {
			scoreResults = scoreResultDao.selectAlls();
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			scoreResultDao.close();
		}
		return scoreResults;
	}

	public static void deleteAlls(Context context) {
		final ScoreResultDao ScoreResultDao = new ScoreResultDao(context);
		ScoreResultDao.deleteAlls();
		ScoreResultDao.close();
	}

	public static void deleteByTestOption(Context context, String testOption) {
		final ScoreResultDao ScoreResultDao = new ScoreResultDao(context);
		ScoreResultDao.deleteByTestOption(testOption);
		ScoreResultDao.close();
	}

	public static void save(Context context, ScoreResult scoreResult) {
		final ScoreResultDao scoreResultDao = new ScoreResultDao(context);
		if (scoreResult.scoreResultId > 0) {
			scoreResultDao.update(scoreResult);
		} else {
			scoreResultDao.insert(scoreResult);
		}
		scoreResultDao.close();
	}
}
