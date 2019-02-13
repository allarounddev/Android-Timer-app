package com.forixusa.scoretimer.android.services;

import java.util.ArrayList;
import com.forixusa.scoretimer.android.data.ScoreResultDao;
import com.forixusa.scoretimer.android.models.ScoreResult;
import android.content.Context;

public class ScoreResultService {

	public static ArrayList<ScoreResult> getScoreResults(Context context){
		ArrayList<ScoreResult> scoreResults = new ArrayList<ScoreResult>();
		ScoreResultDao scoreResultDao = new ScoreResultDao(context);
		try {
			scoreResults = scoreResultDao.selectAlls();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			scoreResultDao.close();
		}
		return scoreResults;
	}
	
	public static void deleteAlls(Context context) {
		ScoreResultDao ScoreResultDao = new ScoreResultDao(context);
		ScoreResultDao.deleteAlls();
		ScoreResultDao.close();
	}
	
	public static void save(Context context, ScoreResult scoreResult) {
		ScoreResultDao scoreResultDao = new ScoreResultDao(context);
		if(scoreResult.scoreResultId > 0) {
			scoreResultDao.update(scoreResult);
		} else {
			scoreResultDao.insert(scoreResult);
		}
		scoreResultDao.close();
	}
}
